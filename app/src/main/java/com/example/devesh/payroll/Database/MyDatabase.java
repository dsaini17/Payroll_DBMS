package com.example.devesh.payroll.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.devesh.payroll.Tables.AttendanceTable;
import com.example.devesh.payroll.Tables.DepartmentTable;
import com.example.devesh.payroll.Tables.EmployeeTable;
import com.example.devesh.payroll.Tables.LoanTable;
import com.example.devesh.payroll.Tables.SalaryTable;
import com.example.devesh.payroll.Tables.TaxTable;

/**
 * Created by devesh on 2/4/17.
 */

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "PAYROLL";
    public static final int DB_VERSION = 1;
    static MyDatabase myDatabase = null;

    public static SQLiteDatabase getReadable(Context context){
        if (myDatabase==null){
            myDatabase = new MyDatabase(context);
        }
        return myDatabase.getReadableDatabase();
    }

    public static SQLiteDatabase getWritable(Context context){
        if (myDatabase==null){
            myDatabase = new MyDatabase(context);
        }
        return myDatabase.getWritableDatabase();
    }

    public MyDatabase(Context context) {
        super(context, DB_NAME, null , DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create all Tables

        db.execSQL(EmployeeTable.TABLE_CREATE_CMD);
        db.execSQL(DepartmentTable.TABLE_CREATE_CMD);
        db.execSQL(SalaryTable.TABLE_CREATE_CMD);
        db.execSQL(AttendanceTable.TABLE_CREATE_CMD);
        db.execSQL(TaxTable.TABLE_CREATE_CMD);
        db.execSQL(LoanTable.TABLE_CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
