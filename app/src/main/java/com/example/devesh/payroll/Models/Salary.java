package com.example.devesh.payroll.Models;

import java.util.Date;

/**
 * Created by devesh on 2/4/17.
 */

public class Salary {

    Integer Employee_ID;
    Integer BasicSalary;
    Integer Bonus;

    public Salary(Integer employee_ID, Integer basicSalary, Integer bonus) {
        Employee_ID = employee_ID;
        BasicSalary = basicSalary;
        Bonus = bonus;
    }

    public Integer getEmployee_ID() {
        return Employee_ID;
    }

    public void setEmployee_ID(Integer employee_ID) {
        Employee_ID = employee_ID;
    }

    public Integer getBasicSalary() {
        return BasicSalary;
    }

    public void setBasicSalary(Integer basicSalary) {
        BasicSalary = basicSalary;
    }

    public Integer getBonus() {
        return Bonus;
    }

    public void setBonus(Integer bonus) {
        Bonus = bonus;
    }
}
