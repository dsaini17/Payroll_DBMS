package com.example.devesh.payroll.Models;

/**
 * Created by devesh on 2/4/17.
 */

public class Loan {

    Integer Employee_ID;
    Integer Principal;
    Float InterestRate;

    public Loan(Integer employee_ID, Integer principal, Float interestRate) {
        Employee_ID = employee_ID;
        Principal = principal;
        InterestRate = interestRate;
    }

    public Integer getEmployee_ID() {
        return Employee_ID;
    }

    public void setEmployee_ID(Integer employee_ID) {
        Employee_ID = employee_ID;
    }

    public Integer getPrincipal() {
        return Principal;
    }

    public void setPrincipal(Integer principal) {
        Principal = principal;
    }

    public Float getInterestRate() {
        return InterestRate;
    }

    public void setInterestRate(Float interestRate) {
        InterestRate = interestRate;
    }
}
