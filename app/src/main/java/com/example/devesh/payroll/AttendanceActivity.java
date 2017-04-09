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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.devesh.payroll.Database.MyDatabase;
import com.example.devesh.payroll.ExportClass.ExportDatabase;
import com.example.devesh.payroll.Models.User;
import com.example.devesh.payroll.Tables.AttendanceTable;
import com.example.devesh.payroll.Tables.DepartmentTable;
import com.example.devesh.payroll.Tables.EmployeeTable;

import java.io.IOException;
import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {

    public static final String TAG = "Attendance Activity";

    ListView attendanceListView;
    TextView departmentTextView;
    AttendanceAdapter attendanceAdapter;
    ArrayList<User> dataList;
    ArrayList<String> queryList;
    SQLiteDatabase currDatabase;
    String dept;
    EditText total_days;
    Integer Total_Working_Days = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        dept = getIntent().getStringExtra("department");

        init();


        departmentTextView.setText(dept);

        fetchData();
    }

    public void init() {
        attendanceListView = (ListView) findViewById(R.id.attendanceListView);
        departmentTextView = (TextView) findViewById(R.id.departmentTextView);
        dataList = new ArrayList<>();
        queryList = new ArrayList<>();
        total_days = (EditText) findViewById(R.id.totalDays);
        attendanceAdapter = new AttendanceAdapter(dataList);
        attendanceListView.setAdapter(attendanceAdapter);
    }

    public void fetchData() {

        currDatabase = MyDatabase.getReadable(getApplicationContext());

        queryList.clear();

        String dataFetchQuery = "SELECT " + EmployeeTable.TABLE_NAME + "." + EmployeeTable.Columns.ID + " , "
                + EmployeeTable.TABLE_NAME + "." + EmployeeTable.Columns.NAME +
                " FROM " + EmployeeTable.TABLE_NAME + " JOIN " + DepartmentTable.TABLE_NAME
                + " ON " + EmployeeTable.TABLE_NAME + "." + EmployeeTable.Columns.ID
                + " = " + DepartmentTable.TABLE_NAME + "." + DepartmentTable.Columns.EMPLOYEE_ID
                + " WHERE " + DepartmentTable.TABLE_NAME + "." + DepartmentTable.Columns.NAME
                + " = " + "'" + dept + "'" + " ; ";

        queryList.add(dataFetchQuery);

        Cursor cursor = currDatabase.rawQuery(dataFetchQuery, null);

        Log.d("dataFetchQuery", String.valueOf(cursor.getCount()));

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.NAME));
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.ID));

                dataList.add(new User(id, name));
                attendanceAdapter.notifyDataSetChanged();
                Log.d("dataFetchQuery", "values added");
            } while (cursor.moveToNext());
        }

        performUpdate(queryList);

    }

    public void performUpdate(ArrayList<String> queryList) {
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
        startActivity(new Intent(AttendanceActivity.this, MainActivity.class));
        finish();
    }

    public class AttendanceAdapter extends BaseAdapter {

        ArrayList<User> myList;

        public AttendanceAdapter(ArrayList<User> myList) {
            this.myList = myList;
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public User getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.attendance_item, null, false);

            final TextView id, name, progressText;
            SeekBar seekBar;
            final CheckBox checkBox;

            id = (TextView) convertView.findViewById(R.id.attendanceID);
            name = (TextView) convertView.findViewById(R.id.attendanceName);
            progressText = (TextView) convertView.findViewById(R.id.progressTextView);
            seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);
            checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxClick);

            final User user = getItem(position);

            id.setText(String.valueOf(user.getEmployee_Id()));
            name.setText(user.getEmployee_Name());
            progressText.setText("");

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    Total_Working_Days = Integer.valueOf(total_days.getText().toString().trim());
                    seekBar.setMax(Total_Working_Days);
                    progressText.setText(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(true);

                    Integer present = 0;

                    if (progressText.getText().toString().trim().isEmpty())
                        present = 0;
                    else {
                        present = Integer.valueOf(progressText.getText().toString().trim());
                    }
                    queryList.clear();
                    currDatabase = MyDatabase.getWritable(getApplicationContext());
                    String yesQuery = " UPDATE " + AttendanceTable.TABLE_NAME + " SET "
                            + AttendanceTable.Columns.PRESENT + " = " + present
                            + " , "
                            + AttendanceTable.Columns.WORKING_DAYS + " = " + Total_Working_Days
                            + " WHERE " + AttendanceTable.Columns.EMPLOYEE_ID + " = " + user.getEmployee_Id() + " ; ";

                    Log.d("yesQuery", yesQuery);

                    queryList.add(yesQuery);
                    currDatabase.execSQL(yesQuery);
                    performUpdate(queryList);

                }
            });

            return convertView;
        }
    }


}
