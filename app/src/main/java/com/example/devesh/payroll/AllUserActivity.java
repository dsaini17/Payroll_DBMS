package com.example.devesh.payroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.devesh.payroll.Database.MyDatabase;
import com.example.devesh.payroll.Dialogs.userDialog;
import com.example.devesh.payroll.ExportClass.ExportDatabase;
import com.example.devesh.payroll.Models.Employee;
import com.example.devesh.payroll.Models.Tax;
import com.example.devesh.payroll.Tables.DepartmentTable;
import com.example.devesh.payroll.Tables.EmployeeTable;
import com.example.devesh.payroll.Tables.SalaryTable;
import com.example.devesh.payroll.Tables.TaxTable;

import java.io.IOException;
import java.util.ArrayList;

public class AllUserActivity extends AppCompatActivity {

    public static final String TAG = "AllUserActivity";

    ListView allUserListView;
    ArrayList<Employee> dataList;
    SQLiteDatabase database;
    CustomAdapter customAdapter;
    ArrayList<String> queryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        init();

        String queryAll = " SELECT "+ EmployeeTable.TABLE_NAME+".* , "+ DepartmentTable.TABLE_NAME+"."+DepartmentTable.Columns.NAME+ " , "+ SalaryTable.TABLE_NAME
                +"."+SalaryTable.Columns.SALARY + " , "+ TaxTable.TABLE_NAME +"."+TaxTable.Columns.TAX_COLLECTED+ " FROM " +EmployeeTable.TABLE_NAME +
                " JOIN " + DepartmentTable.TABLE_NAME + " ON "+ EmployeeTable.TABLE_NAME+"."+EmployeeTable.Columns.ID + " = " + DepartmentTable.TABLE_NAME+"."+DepartmentTable.Columns.EMPLOYEE_ID +
        " JOIN " + SalaryTable.TABLE_NAME + " ON "+ EmployeeTable.TABLE_NAME+"."+EmployeeTable.Columns.ID + " = " + SalaryTable.TABLE_NAME+"."+SalaryTable.Columns.EMPLOYEE_ID +
        " JOIN " + TaxTable.TABLE_NAME + " ON "+ EmployeeTable.TABLE_NAME+"."+EmployeeTable.Columns.ID + " = " + TaxTable.TABLE_NAME+"."+TaxTable.Columns.EMPLOYEE_ID +" ;";

        Log.d(TAG,queryAll);

        queryList.add(queryAll);

        database = MyDatabase.getReadable(getApplicationContext());

        Cursor cursor = database.rawQuery(queryAll,null);
        Log.d(TAG,cursor.getCount()+" "+cursor.getColumnCount() + " "+ cursor.getColumnNames());
        String[] column = cursor.getColumnNames();

        for(int i=0;i<column.length;i++){
            Log.d(TAG,column[i]);
        }
        if(cursor!=null&&cursor.moveToFirst()){
                do{
                String name = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.NAME));
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.ID));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.ADDRESS));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.EMAIL));
                String department = cursor.getString(cursor.getColumnIndexOrThrow(DepartmentTable.Columns.NAME));
                Integer contact = cursor.getInt(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.CONTACT));
                Integer salary = cursor.getInt(cursor.getColumnIndexOrThrow(SalaryTable.Columns.SALARY));
                Float tax = cursor.getFloat(cursor.getColumnIndexOrThrow(TaxTable.Columns.TAX_COLLECTED));

                dataList.add(new Employee(id,name,address,contact,department,email,salary,tax));
                customAdapter.notifyDataSetChanged();
            }while (cursor.moveToNext());
        }

        try {
            askToCreateFile(queryList);
            ExportDatabase.Export();
        } catch (IOException e) {
            e.printStackTrace();
        }

        customAdapter.notifyDataSetChanged();

    }

    public void askToCreateFile(ArrayList<String> sendData) throws IOException {
        Integer queryNumber = getPrefs();
        String fileName = "Query"+String.valueOf(queryNumber)+".txt";
        ExportDatabase.createFile(sendData,fileName);
    }

    public int getPrefs(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Integer oldValue = sharedPreferences.getInt("query",0);
        editor.remove("query");
        editor.putInt("query",oldValue+1);
        // Log.d("Prefs", " old = "+oldValue + "new = "+sharedPreferences.getInt("query",-1));
        editor.apply();

        return oldValue;
    }

    public void init(){
        queryList = new ArrayList<>();
        allUserListView = (ListView) findViewById(R.id.allUserListView);
        dataList = new ArrayList<>();
        customAdapter = new CustomAdapter(dataList);
        allUserListView.setAdapter(customAdapter);
    }

    public class CustomAdapter extends BaseAdapter{

        ArrayList<Employee> myList;

        public CustomAdapter(ArrayList<Employee> myList) {
            this.myList = myList;
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Employee getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.all_user_item,null);

            TextView id_TextView , name_TextView;

            id_TextView = (TextView) convertView.findViewById(R.id.user_id_TextView);
            name_TextView = (TextView) convertView.findViewById(R.id.user_name_TextView);

            final Employee currEmployee = getItem(position);

            Log.d(TAG,String.valueOf(currEmployee.getEmployee_ID()));
            id_TextView.setText(String.valueOf(currEmployee.getEmployee_ID()));
            name_TextView.setText(currEmployee.getName());

            name_TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name",currEmployee.getName());
                    bundle.putString("address",currEmployee.getAddress());
                    bundle.putString("department",currEmployee.getDepartment());
                    bundle.putString("email",currEmployee.getEmail());
                    bundle.putInt("salary",currEmployee.getSalary());
                    bundle.putInt("contact",currEmployee.getContact());
                    bundle.putFloat("tax",currEmployee.getTax_Amount());

                    userDialog Dialog = new userDialog();
                    Dialog.setArguments(bundle);
                    Dialog.show(getSupportFragmentManager(),"User Dialog");
                }
            });

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AllUserActivity.this,MainActivity.class));
        finish();
    }
}
