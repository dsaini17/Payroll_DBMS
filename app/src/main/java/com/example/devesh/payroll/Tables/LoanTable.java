package com.example.devesh.payroll.Tables;

import com.example.devesh.payroll.BasicTable;

/**
 * Created by devesh on 2/4/17.
 */

public class LoanTable extends BasicTable {
    public static final String TABLE_NAME = "LOAN_TABLE";

    public static final String TABLE_CREATE_CMD = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME
            + LBR
            + Columns.EMPLOYEE_ID + TYPE_INT + UNIQUE + COMMA
            + Columns.INTEREST + TYPE_REAL + NOT_NULL + COMMA
            + Columns.PRINCIPAL + TYPE_INT + NOT_NULL + COMMA
            + P_KEY + LBR + Columns.EMPLOYEE_ID + RBR
            + RBR + ";";

    public interface Columns{
        String EMPLOYEE_ID = "Employee_ID";
        String INTEREST = "Interest_Rate";
        String PRINCIPAL = "Principal_Amount";
    }
}
