package com.example.devesh.payroll.Tables;

import com.example.devesh.payroll.BasicTable;

/**
 * Created by devesh on 2/4/17.
 */

public class TaxTable extends BasicTable {

    public static final String TABLE_NAME = "TAX_TABLE";

    public static final String TABLE_CREATE_CMD = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME
            + LBR
            + Columns.EMPLOYEE_ID + TYPE_INT + UNIQUE + COMMA
            + Columns.TAX_COLLECTED + TYPE_REAL + DEFAULT + " 0 " + COMMA
            + P_KEY + LBR + Columns.EMPLOYEE_ID + RBR
            + RBR + ";";

    public interface Columns{
        String EMPLOYEE_ID = "Employee_ID";
        String TAX_COLLECTED = "Tax_Collected";
    }
}
