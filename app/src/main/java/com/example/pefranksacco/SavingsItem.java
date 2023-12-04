package com.example.pefranksacco;

public class SavingsItem {
    private String documentNo;
    private double amount;
    private double moneyIn;
    private double moneyOut;
    private String month;
    private String year;

    public SavingsItem(String documentNo,  String date, double moneyIn, double moneyOut, String month, String year) {
        this.documentNo = documentNo;

        this.moneyIn = moneyIn;
        this.moneyOut = moneyOut;
        this.month = month;
        this.year = year;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public double getAmount() {
        return amount;
    }


    public double getMoneyIn() {
        return moneyIn;
    }

    public double getMoneyOut() {
        return moneyOut;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}
