package com.example.pefranksacco.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pefranksacco.ApiService;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Double> savingsBalanceLiveData = new MutableLiveData<>();
    public MutableLiveData<Double> loanBalanceLiveData = new MutableLiveData<>();
    private ApiService apiService = new ApiService();

    public LiveData<Double> getSavingsBalance() {
        return savingsBalanceLiveData;
    }

    public LiveData<Double> getLoanBalance() {
        return loanBalanceLiveData;
    }

    public void updateSavingsBalance() {
        // Make an API call to get savings balance asynchronously
        apiService.getSavingsBalanceFromLoginResponseAsync(new ApiService.SavingsBalanceCallback() {
            @Override
            public void onSavingsBalanceComplete(double totalSavingsBalance) {
                // Update the LiveData when the balance is received
                savingsBalanceLiveData.postValue(totalSavingsBalance);
            }
        });
    }

    public void updateLoanBalance(final String loginResponse) {
        // Make an API call to get loan balance asynchronously with the loginResponse
        apiService.getLoanBalanceFromMemberLoansResponseAsync(new ApiService.LoanBalanceCallback() {
            @Override
            public void onLoanBalanceComplete(double loanBalance) {
                // Update the LiveData when the balance is received
                loanBalanceLiveData.postValue(loanBalance);
                Log.d("HomeViewModel", "Loan Balance Updated: " + loanBalance);
            }
        }, loginResponse);
    }
}
