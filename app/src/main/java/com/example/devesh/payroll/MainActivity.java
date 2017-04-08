package com.example.devesh.payroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.devesh.payroll.Database.MyDatabase;
import com.example.devesh.payroll.Dialogs.attendanceDialog;
import com.example.devesh.payroll.Dialogs.giveLoanDialog;
import com.example.devesh.payroll.Dialogs.removeEmployeeDialog;
import com.example.devesh.payroll.Dialogs.removeLoanDialog;
import com.example.devesh.payroll.ExportClass.ExportDatabase;
import com.example.devesh.payroll.Models.Department;
import com.example.devesh.payroll.Models.Salary;
import com.example.devesh.payroll.Tables.AttendanceTable;
import com.example.devesh.payroll.Tables.DepartmentTable;
import com.example.devesh.payroll.Tables.EmployeeTable;
import com.example.devesh.payroll.Tables.LoanTable;
import com.example.devesh.payroll.Tables.SalaryTable;
import com.example.devesh.payroll.Tables.TaxTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity implements giveLoanDialog.DialogListener,
        removeLoanDialog.DialogInterface,attendanceDialog.attendanceListener
    ,removeEmployeeDialog.removeEmployeeListener{

    public static final String TAG_EXPORT = "ExportFunction";
    SQLiteDatabase currDatabase;
    FButton addEmployeeButton,allEmployeeButton,bonusButton,giveLoanButton,removeLoanButton,viewLoanButton,
            enterAttendanceButton,viewAttendanceButton,payrollStatisticsButton,removeEmloyeeButton,
            dispatchSalaryButton,modifyEmployeeButton;
    ArrayList<String> queryList;
    ArrayList<String> departmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setClickListeners();

        departmentList = Department.getAllDepartments();

    }

    public void init(){
        departmentList = new ArrayList<>();
        currDatabase = MyDatabase.getReadable(getApplicationContext());
        queryList = new ArrayList<>();
        addEmployeeButton = (FButton) findViewById(R.id.addEmployeeButton);
        allEmployeeButton = (FButton) findViewById(R.id.allEmployeeButton);
        bonusButton = (FButton) findViewById(R.id.bonusButton);
        giveLoanButton = (FButton) findViewById(R.id.giveLoanButton);
        removeLoanButton = (FButton) findViewById(R.id.removeLoanButton);
        viewLoanButton = (FButton) findViewById(R.id.viewLoanButton);
        enterAttendanceButton = (FButton) findViewById(R.id.enterAttendanceButton);
        viewAttendanceButton = (FButton) findViewById(R.id.viewAttendanceButton);
        dispatchSalaryButton = (FButton) findViewById(R.id.dispatchSalaryButton);
        payrollStatisticsButton = (FButton) findViewById(R.id.statsButton);
        removeEmloyeeButton = (FButton) findViewById(R.id.removeEmployeeButton);
        modifyEmployeeButton = (FButton) findViewById(R.id.modifyEmployeeButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logoutInMenu : startActivity(new Intent(MainActivity.this,PasswordActivity.class));
                finish();
                return true;

            default:return super.onOptionsItemSelected(item);
        }

    }

    public void setClickListeners(){

        addEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddUserActivity.class));
                finish();
            }
        });

        allEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AllUserActivity.class));
            }
        });

        bonusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BonusActivity.class));
                finish();
            }
        });

        giveLoanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveLoanDialog loanDialog = new giveLoanDialog();
                loanDialog.show(getSupportFragmentManager(),"Give Loan Dialog");
            }
        });

        removeLoanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLoanDialog removeLoanDialog = new removeLoanDialog();
                removeLoanDialog.show(getSupportFragmentManager(),"Remove Loan Dialog");
            }
        });

        viewLoanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoanActivity.class));
                finish();
            }
        });

        enterAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendanceDialog dialog = new attendanceDialog();
                dialog.show(getSupportFragmentManager(),"Attendance Dialog");
            }
        });

        viewAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ViewAttendanceActivity.class));
                finish();
            }
        });

        dispatchSalaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DispatchSalaryActivity.class));
                finish();
            }
        });

        removeEmloyeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEmployeeDialog dialog = new removeEmployeeDialog();
                dialog.show(getSupportFragmentManager(),"Remove Employee Dialog");
            }
        });

        payrollStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this,PasswordActivity.class));
        finish();
    }

    @Override
    public void OnPositiveClick(Bundle args) {

        queryList.clear();

        currDatabase = MyDatabase.getWritable(getApplicationContext());

        Integer eid = args.getInt("eid");
        Integer loan = args.getInt("loan");
        Float rate = args.getFloat("rate");

        String loanQuery = "SELECT * FROM "+ LoanTable.TABLE_NAME+" WHERE "+LoanTable.TABLE_NAME+"."+
                LoanTable.Columns.EMPLOYEE_ID + " = "+ eid + " ; ";

        Log.d("loanQuery",loanQuery);
        queryList.add(loanQuery);
        Cursor cursor = currDatabase.rawQuery(loanQuery,null);

        if(cursor.getCount()!=0)
            Toast.makeText(getApplicationContext(),"LOAN ALREADY PRESENT",Toast.LENGTH_SHORT).show();
        else{
            String updateQuery = "INSERT INTO "+LoanTable.TABLE_NAME+" VALUES ( "+eid
                    + " , " +  rate
                    + " , " +  loan + " ) ; ";

            Log.d("updateQuery",updateQuery);
            queryList.add(updateQuery);
            currDatabase.execSQL(updateQuery);
            Toast.makeText(getApplicationContext(),"LOAN GRANTED",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRemoveListener(Integer arg1, Integer arg2) {

        queryList.clear();

        currDatabase = MyDatabase.getWritable(getApplicationContext());

        String loanQuery = "SELECT * FROM "+ LoanTable.TABLE_NAME+" WHERE "+LoanTable.TABLE_NAME+"."+
                LoanTable.Columns.EMPLOYEE_ID + " = "+ arg1 + " ; ";

        Log.d("loanQuery",loanQuery);
        queryList.add(loanQuery);
        Cursor cursor = currDatabase.rawQuery(loanQuery,null);

        if(cursor.getCount()==0)
            Toast.makeText(getApplicationContext(),"LOAN NOT PRESENT",Toast.LENGTH_SHORT).show();
        else{
            if(cursor!=null&&cursor.moveToFirst()){
            Integer principal = cursor.getInt(cursor.getColumnIndexOrThrow(LoanTable.Columns.PRINCIPAL));
            if(principal>arg2){
                String updateQuery = "UPDATE "+LoanTable.TABLE_NAME+" SET "+
                        LoanTable.Columns.PRINCIPAL +" = "+LoanTable.Columns.PRINCIPAL + " - "+
                        arg2 + " ; ";
                currDatabase.execSQL(updateQuery);

                queryList.add(updateQuery);
            }
            else {

                String deleteQuery = "DELETE FROM " + LoanTable.TABLE_NAME + " WHERE " +
                        LoanTable.Columns.EMPLOYEE_ID + " = " + arg1 + " ; ";

                currDatabase.execSQL(deleteQuery);
                queryList.add(deleteQuery);

                if (arg2 > principal) {
                    int change = arg2 - principal;
                    Toast.makeText(getApplicationContext(), "LOAN CLEARED , TAKE BACK Rs." + change, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "LOAN CLEARED", Toast.LENGTH_LONG).show();
                }
            }
            }
        }

        try {
            askToCreateFile(queryList);
            ExportDatabase.Export();
            //Toast.makeText(getApplicationContext(),"Update Performed",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSelectDepartment(Integer index) {
        Log.d("selected_Index",index+"");

        String selectedDepartment = departmentList.get(index);

        Intent intent = new Intent(MainActivity.this,AttendanceActivity.class);
        intent.putExtra("department",selectedDepartment);
        startActivity(intent);
        finish();
    }

    @Override
    public void OnRemoveEmployee(Integer id) {
        currDatabase = MyDatabase.getReadable(getApplicationContext());
        queryList.clear();

        String loanQuery = "SELECT * FROM "+LoanTable.TABLE_NAME+" WHERE "+
                LoanTable.Columns.EMPLOYEE_ID + " = " + id + " ; ";
        queryList.add(loanQuery);
        Cursor cursor = currDatabase.rawQuery(loanQuery,null);

        if(cursor.getCount()!=0){
            if(cursor!=null&&cursor.moveToNext()){
                Integer Loan = cursor.getInt(cursor.getColumnIndexOrThrow(LoanTable.Columns.PRINCIPAL));
                Toast.makeText(getApplicationContext(),"EMPLOYEE HAS A LOAN OF RS."+Loan,Toast.LENGTH_LONG).show();
            }
        }
        else{

            currDatabase = MyDatabase.getWritable(getApplicationContext());

            String removeEmployee = "DELETE FROM "+ EmployeeTable.TABLE_NAME+" WHERE "+
                    EmployeeTable.Columns.ID + " = " + id + " ; ";

            queryList.add(removeEmployee);
            currDatabase.execSQL(removeEmployee);


            String removeDepartment = "DELETE FROM "+ DepartmentTable.TABLE_NAME+" WHERE "+
                    DepartmentTable.Columns.EMPLOYEE_ID + " = " + id + " ; ";

            queryList.add(removeDepartment);
            currDatabase.execSQL(removeDepartment);

            String removeLoan = "DELETE FROM "+ LoanTable.TABLE_NAME+" WHERE "+
                    LoanTable.Columns.EMPLOYEE_ID + " = " + id + " ; ";

            queryList.add(removeLoan);
            currDatabase.execSQL(removeLoan);

            String removeSalary = "DELETE FROM "+ SalaryTable.TABLE_NAME+" WHERE "+
                    SalaryTable.Columns.EMPLOYEE_ID + " = " + id + " ; ";

            queryList.add(removeSalary);
            currDatabase.execSQL(removeSalary);

            String removeAttendance = "DELETE FROM "+ AttendanceTable.TABLE_NAME+" WHERE "+
                    AttendanceTable.Columns.EMPLOYEE_ID + " = " + id + " ; ";

            queryList.add(removeAttendance);
            currDatabase.execSQL(removeAttendance);

            String getProvidentFund = "SELECT * FROM "+TaxTable.TABLE_NAME + " WHERE "+
                    TaxTable.Columns.EMPLOYEE_ID + " = " + id + " ; ";

            queryList.add(getProvidentFund);
            Cursor mCursor = currDatabase.rawQuery(getProvidentFund,null);

            Float providentFund = Float.valueOf(0);

            if(mCursor.getCount()!=0) {
                if (mCursor != null && mCursor.moveToFirst()) {
                    do {
                        providentFund = mCursor.getFloat(mCursor.getColumnIndexOrThrow(TaxTable.Columns.TAX_COLLECTED));
                    } while (mCursor.moveToNext());
                }


                String removeTax = "DELETE FROM " + TaxTable.TABLE_NAME + " WHERE " +
                        TaxTable.Columns.EMPLOYEE_ID + " = " + id + " ; ";

                queryList.add(removeTax);
                currDatabase.execSQL(removeTax);

                Toast.makeText(getApplicationContext(), "EMPLOYEE REMOVED , PROVIDENT FUND IS Rs." + providentFund, Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getApplicationContext(),"EMPLOYEE NOT PRESENT",Toast.LENGTH_LONG).show();
        }
        try {
            askToCreateFile(queryList);
            ExportDatabase.Export();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
