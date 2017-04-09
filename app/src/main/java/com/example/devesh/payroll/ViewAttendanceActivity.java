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
import com.example.devesh.payroll.Models.Attendance;
import com.example.devesh.payroll.Tables.AttendanceTable;
import com.example.devesh.payroll.Tables.EmployeeTable;

import java.io.IOException;
import java.util.ArrayList;

public class ViewAttendanceActivity extends AppCompatActivity {

    ArrayList<String> queryList;
    SQLiteDatabase database;
    ArrayList<Attendance> dataList;
    ListView listView;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        init();

        fetchData();
    }

    public void init() {
        queryList = new ArrayList<>();
        dataList = new ArrayList<>();
        customAdapter = new CustomAdapter(dataList);
        listView = (ListView) findViewById(R.id.viewAttendanceListView);
        listView.setAdapter(customAdapter);
    }

    public void fetchData() {

        queryList.clear();

        database = MyDatabase.getReadable(getApplicationContext());

        String fetchQuery = " SELECT " + EmployeeTable.Columns.NAME + " , " + AttendanceTable.TABLE_NAME + ".*"
                + " FROM " + AttendanceTable.TABLE_NAME + " JOIN " + EmployeeTable.TABLE_NAME
                + " ON " + AttendanceTable.TABLE_NAME + "." + AttendanceTable.Columns.EMPLOYEE_ID + " = "
                + EmployeeTable.TABLE_NAME + "." + EmployeeTable.Columns.ID + " ; ";

        Log.d("fetchQuery", fetchQuery);
        queryList.add(fetchQuery);

        Cursor cursor = database.rawQuery(fetchQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(AttendanceTable.Columns.EMPLOYEE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.NAME));
                Integer p = cursor.getInt(cursor.getColumnIndexOrThrow(AttendanceTable.Columns.PRESENT));
                Integer t = cursor.getInt(cursor.getColumnIndexOrThrow(AttendanceTable.Columns.WORKING_DAYS));
                dataList.add(new Attendance(name, id, t, p));
                customAdapter.notifyDataSetChanged();
            } while (cursor.moveToNext());
        }

        try {
            askToCreateFile(queryList);
            ExportDatabase.Export();
            //   Toast.makeText(getApplicationContext(),"Update Performed",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewAttendanceActivity.this, MainActivity.class));
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
        // Log.d("Prefs", " old = "+oldValue + "new = "+sharedPreferences.getInt("query",-1));
        editor.apply();

        return oldValue;
    }

    public class CustomAdapter extends BaseAdapter {

        ArrayList<Attendance> myList;

        public CustomAdapter(ArrayList<Attendance> myList) {
            this.myList = myList;
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Attendance getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.view_all_attendance, null);

            TextView a, b, c, d;

            a = (TextView) convertView.findViewById(R.id.id);
            b = (TextView) convertView.findViewById(R.id.name);
            c = (TextView) convertView.findViewById(R.id.p);
            d = (TextView) convertView.findViewById(R.id.t);

            Attendance attendance = getItem(position);

            a.setText(String.valueOf(attendance.getEmployee_ID()));
            b.setText(attendance.getEmployee_Name());
            c.setText(String.valueOf(attendance.getPresent()));
            d.setText(String.valueOf(attendance.getWorkingDays()));

            return convertView;
        }
    }
}
