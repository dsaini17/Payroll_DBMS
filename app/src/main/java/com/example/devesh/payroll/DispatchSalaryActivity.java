package com.example.devesh.payroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.devesh.payroll.Database.MyDatabase;
import com.example.devesh.payroll.ExportClass.ExportDatabase;
import com.example.devesh.payroll.Models.Salary;
import com.example.devesh.payroll.Tables.AttendanceTable;
import com.example.devesh.payroll.Tables.EmployeeTable;
import com.example.devesh.payroll.Tables.LoanTable;
import com.example.devesh.payroll.Tables.SalaryTable;
import com.example.devesh.payroll.Tables.TaxTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DispatchSalaryActivity extends AppCompatActivity {

    public static final String TAG = " dispatch salaries ";
    ArrayList<String> queryList;
    ArrayList<Salary> dataList;
    SQLiteDatabase database;
    SalaryAdapter salaryAdapter;
    ListView salaryListView;
    String exportString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_salary);


        init();

        fetchData();


    }

    public void fetchData() {

        queryList.clear();

        database = MyDatabase.getReadable(getApplicationContext());

        String salaryQuery = " SELECT " + EmployeeTable.TABLE_NAME + "." + EmployeeTable.Columns.NAME + " , "
                + SalaryTable.TABLE_NAME + ".* , " + AttendanceTable.TABLE_NAME + "." + AttendanceTable.Columns.PRESENT
                + " , " + AttendanceTable.TABLE_NAME + "." + AttendanceTable.Columns.WORKING_DAYS
                + " FROM " + SalaryTable.TABLE_NAME + " JOIN " + EmployeeTable.TABLE_NAME + " ON "
                + SalaryTable.TABLE_NAME + "." + SalaryTable.Columns.EMPLOYEE_ID
                + " = " + EmployeeTable.TABLE_NAME + "." + EmployeeTable.Columns.ID + " JOIN "
                + AttendanceTable.TABLE_NAME + " ON " + SalaryTable.TABLE_NAME + "." + SalaryTable.Columns.EMPLOYEE_ID
                + " = " + AttendanceTable.TABLE_NAME + "." + AttendanceTable.Columns.EMPLOYEE_ID
                + " ; ";

        queryList.add(salaryQuery);
        Cursor cursor = database.rawQuery(salaryQuery, null);

        Log.d("count = ", String.valueOf(cursor.getCount()));

        String updateTax1 = "UPDATE " + TaxTable.TABLE_NAME + " SET "
                + TaxTable.Columns.TAX_COLLECTED + " = " + " (101*( " + TaxTable.Columns.TAX_COLLECTED + "/100)) ;";

        Log.d(TAG, updateTax1);

        queryList.add(updateTax1);
        database.execSQL(updateTax1);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.NAME));
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(SalaryTable.Columns.EMPLOYEE_ID));
                Integer salary = cursor.getInt(cursor.getColumnIndexOrThrow(SalaryTable.Columns.SALARY));
                Integer bonus = cursor.getInt(cursor.getColumnIndexOrThrow(SalaryTable.Columns.BONUS));
                Integer present = cursor.getInt(cursor.getColumnIndexOrThrow(AttendanceTable.Columns.PRESENT));
                Integer total = cursor.getInt(cursor.getColumnIndexOrThrow(AttendanceTable.Columns.WORKING_DAYS));
                Integer principal = 0;
                float rate = 0;

                String loanQuery = " SELECT * FROM " + LoanTable.TABLE_NAME + " WHERE "
                        + LoanTable.Columns.EMPLOYEE_ID + " =  " + id + " ; ";

                queryList.add(loanQuery);

                database = MyDatabase.getReadable(getApplicationContext());
                Cursor myCursor = database.rawQuery(loanQuery, null);

                if (myCursor.getCount() != 0) {
                    if (myCursor != null && myCursor.moveToFirst()) {
                        principal = myCursor.getInt(myCursor.getColumnIndexOrThrow(LoanTable.Columns.PRINCIPAL));
                        rate = myCursor.getFloat(myCursor.getColumnIndexOrThrow(LoanTable.Columns.INTEREST));
                    }
                }

                Log.d(TAG, "basic salary = " + salary);

                Integer alpha = (salary / 100) * 84;

                Integer beta = (salary / 100) * 6;

                float interest = (principal * rate) / 1200;

                Log.d(TAG, "after tax = " + alpha);

                Integer Salary_now = alpha - (int) interest;

                Log.d(TAG, "interest = " + interest + " after interet = " + Salary_now);

                Float val = Float.valueOf(1);


                Log.d(TAG, "attendance val= " + val);

                Integer Salary_new = (int) (Salary_now * val);

                float sal = Float.valueOf(Salary_new);

                if (total != 0) {
                    sal = sal / total;
                    sal = sal * present;
                }

                Salary_new = (int) sal;


                Log.d(TAG, "salary after attendance = " + Salary_new);
                Integer finalSalary = Salary_new + bonus;

                Log.d(TAG, "with bonus = " + finalSalary);

                if (finalSalary <= 0) {
                    finalSalary = 100;
                }

                exportString += String.valueOf(id) + " " + name + " " + String.valueOf(finalSalary) + "\n\n";

                String updateTax2 = "UPDATE " + TaxTable.TABLE_NAME + " SET "
                        + TaxTable.Columns.TAX_COLLECTED + " = " + TaxTable.Columns.TAX_COLLECTED + " + " + beta
                        + " WHERE " + TaxTable.Columns.EMPLOYEE_ID + " = " + id
                        + " ; ";

                queryList.add(updateTax2);
                database.execSQL(updateTax2);

                Log.d(TAG, updateTax2);

                dataList.add(new Salary(id, salary, bonus, present, total, finalSalary, principal, rate, name));
                salaryAdapter.notifyDataSetChanged();

            } while (cursor.moveToNext());
        }

        String updateAttendance = "UPDATE " + AttendanceTable.TABLE_NAME + " SET "
                + AttendanceTable.Columns.PRESENT + " = 0 , "
                + AttendanceTable.Columns.WORKING_DAYS + " = 0 ;";

        Log.d(TAG,updateAttendance);
        queryList.add(updateAttendance);
        database.execSQL(updateAttendance);

        String updateBonus = "UPDATE " + SalaryTable.TABLE_NAME + " SET "
                + SalaryTable.Columns.BONUS + " = 0 ;";

        Log.d(TAG,updateBonus);
        queryList.add(updateBonus);
        database.execSQL(updateBonus);


        try {
            createCustomFile(exportString);
            askToCreateFile(queryList);
            ExportDatabase.Export();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createCustomFile(String exportString) throws IOException {

        File file = new File(Environment.getExternalStorageDirectory(), "Salary.txt");

        if (!file.isFile()) {
            file.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
        fileOutputStream.write(exportString.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void init() {
        queryList = new ArrayList<>();
        dataList = new ArrayList<>();
        salaryAdapter = new SalaryAdapter(dataList);
        salaryListView = (ListView) findViewById(R.id.salaryListView);
        salaryListView.setAdapter(salaryAdapter);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DispatchSalaryActivity.this, MainActivity.class));
        finish();
    }

    public void askToCreateFile(ArrayList<String> sendData) throws IOException {
        Integer queryNumber = getPrefs();
        String fileName = "Query" + String.valueOf(queryNumber) + ".txt";
        ExportDatabase.createFile(sendData, fileName);
    }

    public int getPrefs() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Integer oldValue = sharedPreferences.getInt("query", 0);
        editor.remove("query");
        editor.putInt("query", oldValue + 1);
        editor.apply();

        return oldValue;
    }

    public class SalaryAdapter extends BaseAdapter {

        ArrayList<Salary> myList;

        public SalaryAdapter(ArrayList<Salary> myList) {
            this.myList = myList;
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Salary getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.monthly_salary_item, null);

            TextView a, b, c;

            Salary salary = getItem(position);

            a = (TextView) convertView.findViewById(R.id.salaryID);
            b = (TextView) convertView.findViewById(R.id.salaryName);
            c = (TextView) convertView.findViewById(R.id.salaryValue);

            a.setText(String.valueOf(salary.getEmployee_ID()));
            b.setText(salary.getName());
            c.setText(String.valueOf(salary.getFinalSalary()));

            return convertView;
        }
    }

}
