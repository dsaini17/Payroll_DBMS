package com.example.devesh.payroll.Models;

/**
 * Created by devesh on 2/4/17.
 */

public class Salary {

    Integer Employee_ID;
    Integer BasicSalary;
    Integer Bonus;
    Integer presentAttendance;
    Integer totalDays;
    Integer finalSalary;
    Integer principalLoan;
    Float rate;
    String Name;

    public Salary(Integer employee_ID, Integer basicSalary, Integer bonus, Integer presentAttendance, Integer totalDays, Integer finalSalary, Integer principalLoan, Float rate, String name) {
        Employee_ID = employee_ID;
        BasicSalary = basicSalary;
        Bonus = bonus;
        this.presentAttendance = presentAttendance;
        this.totalDays = totalDays;
        this.finalSalary = finalSalary;
        this.principalLoan = principalLoan;
        this.rate = rate;
        Name = name;
    }

    public Integer getPrincipalLoan() {
        return principalLoan;
    }

    public void setPrincipalLoan(Integer principalLoan) {
        this.principalLoan = principalLoan;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getPresentAttendance() {
        return presentAttendance;
    }

    public void setPresentAttendance(Integer presentAttendance) {
        this.presentAttendance = presentAttendance;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public Integer getFinalSalary() {
        return finalSalary;
    }

    public void setFinalSalary(Integer finalSalary) {
        this.finalSalary = finalSalary;
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
