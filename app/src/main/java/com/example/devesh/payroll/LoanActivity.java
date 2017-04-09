package com.example.devesh.payroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.devesh.payroll.Database.MyDatabase;
import com.example.devesh.payroll.ExportClass.ExportDatabase;
import com.example.devesh.payroll.Models.Loan;
import com.example.devesh.payroll.Tables.EmployeeTable;
import com.example.devesh.payroll.Tables.LoanTable;

import java.io.IOException;
import java.util.ArrayList;

public class LoanActivity extends AppCompatActivity {

    CustomAdapter customAdapter;
    ListView loanListView;
    ArrayList<Loan> dataList;
    SQLiteDatabase database;
    ArrayList<String> queryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        queryList = new ArrayList<>();
        dataList = new ArrayList<>();
        database = MyDatabase.getReadable(getApplicationContext());

        loanListView = (ListView) findViewById(R.id.loanListView);
        customAdapter = new CustomAdapter(dataList);
        loanListView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        addData();

    }

    public void addData() {

        queryList.clear();

        database = MyDatabase.getReadable(getApplicationContext());

        String requestData = "SELECT " + LoanTable.TABLE_NAME + ".* , " + EmployeeTable.TABLE_NAME + "." + EmployeeTable.Columns.NAME
                + " FROM " + LoanTable.TABLE_NAME + " , " + EmployeeTable.TABLE_NAME
                + " WHERE " + LoanTable.TABLE_NAME + "." + LoanTable.Columns.EMPLOYEE_ID + " = "
                + EmployeeTable.TABLE_NAME + "." + EmployeeTable.Columns.ID + " ; ";

        Log.d("request", requestData);

        queryList.add(requestData);

        Cursor cursor = database.rawQuery(requestData, null);

        String[] strings = cursor.getColumnNames();

        for (int i = 0; i < strings.length; i++) {
            Log.d("coulums", strings[i]);
        }

        if (cursor.getCount() != 0) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.NAME));
                    Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(LoanTable.Columns.EMPLOYEE_ID));
                    Integer amount = cursor.getInt(cursor.getColumnIndexOrThrow(LoanTable.Columns.PRINCIPAL));
                    Float rate = cursor.getFloat(cursor.getColumnIndexOrThrow(LoanTable.Columns.INTEREST));

                    dataList.add(new Loan(id, amount, name, rate));
                    customAdapter.notifyDataSetChanged();
                } while (cursor.moveToNext());
            }
        }

        try {
            askToCreateFile(queryList);
            ExportDatabase.Export();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        // Log.d("Prefs", " old = "+oldValue + "new = "+sharedPreferences.getInt("query",-1));
        editor.apply();


        return oldValue;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoanActivity.this, MainActivity.class));
        finish();
    }

    public class CustomAdapter extends BaseAdapter {

        ArrayList<Loan> myList;

        public CustomAdapter(ArrayList<Loan> myList) {
            this.myList = myList;
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Loan getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.view_all_loan_item, null);

            final TextView a, b, c;

            a = (TextView) convertView.findViewById(R.id.viewLoanID);
            b = (TextView) convertView.findViewById(R.id.viewLoanName);
            c = (TextView) convertView.findViewById(R.id.viewLoanAmount);

            Loan loan = getItem(position);

            a.setText(String.valueOf(loan.getEmployee_ID()));
            b.setText(loan.getName());
            c.setText(String.valueOf(loan.getPrincipal()));

            return convertView;
        }
    }
}


