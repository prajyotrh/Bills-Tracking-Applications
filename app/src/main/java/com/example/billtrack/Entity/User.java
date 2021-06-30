package com.example.billtrack.Entity;

public class User {
    public User(long accountBalance, String fname, String lname) {
        this.accountBalance = accountBalance;
        this.fname = fname;
        this.lname = lname;
    }

    private long accountBalance;
    private String fname,lname;

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public long  getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(long accountBalance) {
        this.accountBalance = accountBalance;
    }

    public User(){}

}
