package com.example.devesh.payroll.Models;

/**
 * Created by devesh on 2/4/17.
 */

public class Tax {

    Integer Employee_ID;
    Float Amount;

    public Tax(Integer employee_ID, Float amount) {
        Employee_ID = employee_ID;
        Amount = amount;
    }

    public Integer getEmployee_ID() {
        return Employee_ID;
    }

    public void setEmployee_ID(Integer employee_ID) {
        Employee_ID = employee_ID;
    }

    public Float getAmount() {
        return Amount;
    }

    public void setAmount(Float amount) {
        Amount = amount;
    }
}
