package com.example.pefranksacco;

public class LoanData {
    private long id;
    private String number;
    private String apiResponse;

    // Constructors
    public LoanData() {
        // Default constructor
    }

    public LoanData(String number, String apiResponse) {
        this.number = number;
        this.apiResponse = apiResponse;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getApiResponse() {
        return apiResponse;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setApiResponse(String apiResponse) {
        this.apiResponse = apiResponse;
    }

    // Other fields and methods as needed
}
