package com.example.billtrack.Entity;

public class Transaction {
    private String date,reason,transactionType;
    private long amount;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Transaction(String date, String reason, String transactionType, long amount) {
        this.date = date;
        this.reason = reason;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Transaction(){}


}

