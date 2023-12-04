package com.example.pefranksacco;

public class LoanRepaymentItem {
    private String year;
    private String documentNo;
    private String month;
    private String amount;
    private String transactionCode;
    private String paidOn;

    public LoanRepaymentItem(String year, String documentNo, String month, String amount, String transactionCode, String paidOn) {
        this.year = year;
        this.documentNo = documentNo;
        this.month = month;
        this.amount = amount;
        this.transactionCode = transactionCode;
        this.paidOn = paidOn;
    }

    public String getYear() {
        return year;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public String getMonth() {
        return month;
    }

    public String getAmount() {
        return amount;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public String getPaidOn() {
        return paidOn;
    }
}

