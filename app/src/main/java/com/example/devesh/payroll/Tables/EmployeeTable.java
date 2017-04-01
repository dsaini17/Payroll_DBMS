package com.example.devesh.payroll.Tables;


import com.example.devesh.payroll.BasicTable;

/**
 * Created by devesh on 2/4/17.
 */

public class EmployeeTable extends BasicTable {

    public static final String TABLE_NAME = "EMPLOYEE_TABLE";

    public static final String TABLE_CREATE_CMD = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME
            + LBR
            + Columns.ID + TYPE_INT_PK + COMMA
            + Columns.NAME + TYPE_TEXT + NOT_NULL + COMMA
            + Columns.ADDRESS + TYPE_TEXT + COMMA
            + Columns.CONTACT + TYPE_INT + COMMA
            + Columns.EMAIL + TYPE_TEXT
            + RBR + ";";

    public interface Columns{
        String ID = "Employee_ID";
        String NAME = "Employee_Name";
        String ADDRESS = "Address";
        String CONTACT = "Contact_No";
        String EMAIL = "Email_Address";
    }
}
