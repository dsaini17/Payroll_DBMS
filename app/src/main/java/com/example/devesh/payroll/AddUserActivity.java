package com.example.devesh.payroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.devesh.payroll.Database.MyDatabase;
import com.example.devesh.payroll.Models.Department;
import com.example.devesh.payroll.Tables.AttendanceTable;
import com.example.devesh.payroll.Tables.DepartmentTable;
import com.example.devesh.payroll.Tables.EmployeeTable;
import com.example.devesh.payroll.Tables.SalaryTable;
import com.example.devesh.payroll.Tables.TaxTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.devesh.payroll.R.id.userAddButton;

public class AddUserActivity extends AppCompatActivity {

    public static final String TAG = "AddUserActivity";
    public static final String TAG_EXPORT = "ExportFunction";

    public static final String NEW_LINE = "\n";

    EditText userName, userAddress , userEmailAddress , userSalary , userContact ;
    Integer spinnerPosition;
    Spinner departmentName;
    Button addUserButton;
    SQLiteDatabase database;
    ArrayList<Department> departmentArrayList;
    ArrayList<String> departmentNameList;

    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        init();

        departmentName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView)parent.getChildAt(0)).setTextSize(20);

                String value = (String) parent.getItemAtPosition(position);
                Log.d(TAG,value);
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

    public void init(){
        database = MyDatabase.getReadable(getApplicationContext());
        userName = (EditText) findViewById(R.id.userName);
        userAddress = (EditText) findViewById(R.id.userAddress);
        userEmailAddress = (EditText) findViewById(R.id.userEmailAddress);
        userSalary = (EditText) findViewById(R.id.userSalary);
        userContact = (EditText) findViewById(R.id.userContact);
        departmentName = (Spinner) findViewById(R.id.userDepartment);
        addUserButton = (Button) findViewById(userAddButton);
        spinnerPosition = 0;

        //departmentName = new Spinner(getApplicationContext());

        departmentArrayList = Department.getAllDepartmentInfo();
        departmentNameList = Department.getAllDepartments();
        arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.department_array,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentName.setAdapter(arrayAdapter);
    }

    public void perform_Updation(){
        String name = userName.getText().toString().trim();
        String address = userAddress.getText().toString().trim();
        String email = userAddress.getText().toString().trim();
        String department = departmentNameList.get(spinnerPosition);
        Integer contact = Integer.valueOf(userContact.getText().toString().trim());
        Integer salary = Integer.valueOf(userSalary.getText().toString().trim());

        Log.d(TAG,spinnerPosition + " " + department);

        database = MyDatabase.getWritable(getApplicationContext());

        String employeeQuery = " INSERT INTO "+ EmployeeTable.TABLE_NAME + " ( "+ EmployeeTable.Columns.NAME + ","
                + EmployeeTable.Columns.ADDRESS + ","
                + EmployeeTable.Columns.EMAIL + ","
                + EmployeeTable.Columns.CONTACT + " ) "
                + " VALUES ( "+ "'" +name+"'" + ","+ "'" +address+"'" + "," + "'" +email+"'" + "," + contact +" );";

        Log.d(TAG,employeeQuery);
        database.execSQL(employeeQuery);

        Cursor cursor = database.rawQuery("SELECT * FROM "+EmployeeTable.TABLE_NAME+";",null);
        cursor.move(cursor.getCount());
        //cursor.moveToLast();

        Integer employeeID = cursor.getInt(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.ID));
        Log.d(TAG,"ID is = "+cursor.getInt(cursor.getColumnIndexOrThrow(EmployeeTable.Columns.ID)));

        try {
            Export();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String salaryQuery = " INSERT INTO "+ SalaryTable.TABLE_NAME + " VALUES "
                + " ( " + employeeID + " , "
                + salary + ","
                + 0 + " );";

        Log.d(TAG,salaryQuery);
        database.execSQL(salaryQuery);

        Department currDepartment = departmentArrayList.get(spinnerPosition);
        String departmentQuery = " INSERT INTO "+ DepartmentTable.TABLE_NAME + " VALUES ( "
                + employeeID + ","
                + "'" + currDepartment.getName() + "'" + " , "
                + currDepartment.getDepartment_ID() + " ); ";

        Log.d(TAG,departmentQuery);
        database.execSQL(departmentQuery);

        String taxQuery = " INSERT INTO "+ TaxTable.TABLE_NAME + " VALUES ( "
                + employeeID + ","
                + 0 + " );";

        Log.d(TAG,taxQuery);
        database.execSQL(taxQuery);

        String attendanceQuery = " INSERT INTO "+ AttendanceTable.TABLE_NAME + " VALUES ( "
                + employeeID + ","
                + 0 + ","
                + 0 + " );";

        Log.d(TAG,attendanceQuery);
        database.execSQL(attendanceQuery);

        try {
            createFile(employeeQuery,departmentQuery,taxQuery,attendanceQuery,salaryQuery);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Export();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(AddUserActivity.this,MainActivity.class));
    }

    public void Export() throws IOException {
        File inFile = new File(Environment.getDataDirectory()+"/data/com.example.devesh.payroll/databases/"+ MyDatabase.DB_NAME);
        Log.d(TAG_EXPORT,Environment.getDataDirectory()+"/data/com.example.devesh.payroll/databases/"+ MyDatabase.DB_NAME);
        FileInputStream fileInputStream = new FileInputStream(inFile);

        File outFile = new File(Environment.getExternalStorageDirectory()+"/"+MyDatabase.DB_NAME);
        Log.d(TAG_EXPORT,Environment.getExternalStorageDirectory()+"/"+MyDatabase.DB_NAME);
        FileOutputStream fileOutputStream = new FileOutputStream(outFile);

        byte[] buffer = new byte[1024];
        int length;

        while ((length=fileInputStream.read(buffer))>0){
            fileOutputStream.write(buffer,0,length);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        fileInputStream.close();
    }

    public void createFile(String a,String b,String c,String d,String e) throws IOException {

        Integer queryNumber = getPrefs();
        String fileName = "Query"+String.valueOf(queryNumber)+".txt";
        File file = new File(Environment.getExternalStorageDirectory(),fileName);

        if(!file.isFile()){
            file.createNewFile();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file,true);
        fileOutputStream.write(a.getBytes());
        fileOutputStream.write(NEW_LINE.getBytes());
        fileOutputStream.write(b.getBytes());
        fileOutputStream.write(NEW_LINE.getBytes());
        fileOutputStream.write(c.getBytes());
        fileOutputStream.write(NEW_LINE.getBytes());
        fileOutputStream.write(d.getBytes());
        fileOutputStream.write(NEW_LINE.getBytes());
        fileOutputStream.write(e.getBytes());
        fileOutputStream.write(NEW_LINE.getBytes());


    }

    public int getPrefs(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Integer oldValue = sharedPreferences.getInt("query",0);
        editor.remove("query");
        editor.putInt("query",oldValue+1);
        Log.d("Prefs", " old = "+oldValue + "new = "+sharedPreferences.getInt("query",-1));
        editor.apply();

        return oldValue;
    }

}
