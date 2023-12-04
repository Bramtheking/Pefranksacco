package com.example.pefranksacco;
public class LoanItem {
    private String id;
    private String membersId;
    private String loanProduct;
    private String loanRefNo;
    private String status;
    private String approvedAmount;
    private String appliedAmount;
private  String appliedon;
    public LoanItem(String id, String membersId, String loanProduct, String loanRefNo, String status, String approvedAmount, String appliedAmount, String appliedon) {
        this.id = id;
        this.membersId = membersId;
        this.loanProduct = loanProduct;
        this.loanRefNo = loanRefNo;
        this.status = status;
        this.approvedAmount = approvedAmount;
        this.appliedAmount = appliedAmount;
        this.appliedon=appliedon;
    }
    public String getId() {
        return id;
    }
    public String getMembersId() {
        return membersId;
    }

    public String getLoanProduct() {
        return loanProduct;
    }

    public String getLoanRefNo() {
        return loanRefNo;
    }

    public String getStatus() {
        return status;
    }

    public String getApprovedAmount() {
        return approvedAmount;
    }

    public String getAppliedAmount() {
        return appliedAmount;
    }
    // Getter methods for each field
    public String getappliedon() {
        return appliedon;
    }
}
