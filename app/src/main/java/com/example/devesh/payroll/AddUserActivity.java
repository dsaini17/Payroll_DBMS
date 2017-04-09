package com.example.devesh.payroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.devesh.payroll.Database.MyDatabase;
import com.example.devesh.payroll.ExportClass.ExportDatabase;
import com.example.devesh.payroll.Models.Department;
import com.example.devesh.payroll.Tables.AttendanceTable;
import com.example.devesh.payroll.Tables.DepartmentTable;
import com.example.devesh.payroll.Tables.EmployeeTable;
import com.example.devesh.payroll.Tables.SalaryTable;
import com.example.devesh.payroll.Tables.TaxTable;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.devesh.payroll.R.id.userAddButton;

public class AddUserActivity extends AppCompatActivity {

    public static final String TAG = "AddUserActivity";
    public static final String TAG_EXPORT = "ExportFunction";

    public static final String NEW_LINE = "\n";

    EditText userName, userAddress, userEmailAddress, userSalary, userContact;
    Integer spinnerPosition;
    Spinner departmentName;
    Button addUserButton;
    SQLiteDatabase database;
    ArrayList<Department> departmentArrayList;
    ArrayList<String> departmentNameList;
    ArrayList<String> querySaveList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        init();

        departmentName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(20);

                String value = (String) parent.getItemAtPosition(position);
                Log.d(TAG, value);
                spinnerPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perform_Updation();
            }
        });

    }

    public void init() {
        database = MyDatabase.getReadable(getApplicationContext());
        userName = (EditText) findViewById(R.id.userName);
        userAddress = (EditText) findViewById(R.id.userAddress);
        userEmailAddress = (EditText) findViewById(R.id.userEmailAddress);
        userSalary = (EditText) findViewById(R.id.userSalary);
        userContact = (EditText) findViewById(R.id.userContact);
        departmentName = (Spinner) findViewById(R.id.userDepartment);
        addUserButton = (Button) findViewById(userAddButton);
        spinnerPosition = 0;
        querySaveList = new ArrayList<>();

        //departmentName = new Spinner(getApplicationContext());

        departmentArrayList = Department.getAllDepartmentInfo();
        departmentNameList = Department.getAllDepartments();
        arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.department_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentName.setAdapter(arrayAdapter);
    }

    public void perform_Updation() {
        String name = userName.getText().toString().trim();
        String address = userAddress.getText().toString().trim();
        String email = userEmailAddress.getText().toString().trim();
        String department = departmentNameList.get(spinnerPosition);
        String contact = userContact.getText().toString().trim();
        Integer salary = Integer.parseInt(userSalary.getText().toString().trim());

        Log.d(TAG, spinnerPosition + " " + department);

        database = MyDatabase.getWritable(getApplicationContext());

        String employeeQuery = " INSERT INTO " + EmployeeTable.TABLE_NAME + " ( " + EmployeeTable.Columns.NAME + ","
                + EmployeeTable.Columns.ADDRESS + ","
                + EmployeeTable.Columns.EMAIL + ","
                + EmployeeTable.Columns.CONTACT + " ) "
                + " VALUES ( " + "'" + name + "'" + "," + "'" + address + "'" + "," + "'" + email + "'" + ","
                + "'" + contact + "'" + " ) ; ";

        Log.d(TAG, employeeQuery);
        database.execSQL(employeeQuery);
        querySaveList.add(employeeQuery);

        Cursor cursor = database.rawQuery("SELECT * FROM " + EmployeeTable.TABLE_NAME + ";", null);

        boolean ifRowPresent = cursor.moveToLast();

        //Log.v(TAG, String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.ID))));

        Integer last_Id = 0;
        if (ifRowPresent) {
            last_Id = cursor.getInt(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.ID));
            Log.d(TAG, "ID is = " + last_Id);
        }


        String salaryQuery = " INSERT INTO " + SalaryTable.TABLE_NAME + " VALUES "
                + " ( " + last_Id + " , "
                + salary + ","
                + 0 + " );";

        Log.d(TAG, salaryQuery);
        database.execSQL(salaryQuery);
        querySaveList.add(salaryQuery);

        Department currDepartment = departmentArrayList.get(spinnerPosition);
        String departmentQuery = " INSERT INTO " + DepartmentTable.TABLE_NAME + " VALUES ( "
                + last_Id + ","
                + "'" + currDepartment.getName() + "'" + " , "
                + currDepartment.getDepartment_ID() + " ); ";

        Log.d(TAG, departmentQuery);
        database.execSQL(departmentQuery);
        querySaveList.add(departmentQuery);

        String taxQuery = " INSERT INTO " + TaxTable.TABLE_NAME + " VALUES ( "
                + last_Id + ","
                + 0 + " );";

        Log.d(TAG, taxQuery);
        database.execSQL(taxQuery);
        querySaveList.add(taxQuery);

        String attendanceQuery = " INSERT INTO " + AttendanceTable.TABLE_NAME + " VALUES ( "
                + last_Id + ","
                + 0 + ","
                + 0 + " );";

        Log.d(TAG, attendanceQuery);
        database.execSQL(attendanceQuery);
        querySaveList.add(attendanceQuery);

        try {
            askToCreateFile(querySaveList);
            ExportDatabase.Export();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(AddUserActivity.this, MainActivity.class));
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddUserActivity.this, MainActivity.class));
        finish();
    }
}
