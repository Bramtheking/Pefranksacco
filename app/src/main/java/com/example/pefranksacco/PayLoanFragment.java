package com.example.pefranksacco;

import static com.example.pefranksacco.ApiService.API_EMAIL;
import static com.example.pefranksacco.ApiService.API_PASSWORD;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pefranksacco.ui.home.HomeViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PayLoanFragment extends Fragment {
    private EditText amountEditText;
    private TextView totalLoanDueTextView, repaymentDateTextView;
    private static final String TAG = "PayLoan";
    private ApiService apiService;
    private String loginResponse;
    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pay_loan, container, false);

        totalLoanDueTextView = rootView.findViewById(R.id.Totalsavings);
        repaymentDateTextView = rootView.findViewById(R.id.appliedon);
        amountEditText = rootView.findViewById(R.id.amount);
        Button payLoanButton = rootView.findViewById(R.id.contribute);

        // Set labels for amount and total loan due
        amountEditText.setHint("Amount");

        // Set repayment date to the current date and disable user input
        setAndDisableCurrentDate();

        // Set a click listener for the "Pay Loan" button
        payLoanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = amountEditText.getText().toString();

                // Validate the amount
                if (amount.isEmpty()) {
                    amountEditText.setError("Amount is required");
                } else {
                    // Get the current date
                    Date currentDate = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = dateFormat.format(currentDate);

                    // Log the data being passed to the API
                    Log.d(TAG, "Repayment Date: " + formattedDate);
                    Log.d(TAG, "Amount: " + amount);

                    // Call the payloan method with the obtained amount and formattedDate using AsyncTask
                    new PayLoanTask().execute(ApiService.API_MEMBERID, formattedDate, amount);
                    amountEditText.setText("");
                }
            }
        });

        // Initial login to fetch the total loan balance
        performLoginAndUpdateUI();

        return rootView;
    }

    // Show a success dialog
    private void showSuccessDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Show an error dialog
    private void showErrorDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setAndDisableCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);

        repaymentDateTextView.setText("Repayment Date: " + formattedDate);
        repaymentDateTextView.setFocusable(false);
        repaymentDateTextView.setClickable(false);
    }

    // Method to perform login and update the total loan balance
    private void performLoginAndUpdateUI() {
        new LoginTask().execute();
    }

    private class LoginTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return apiService.loginUser(API_EMAIL, API_PASSWORD);
        }

        @Override
        protected void onPostExecute(String response) {
            loginResponse = response;
            double loanBalance = apiService.getLoanBalanceFromLoginResponse(loginResponse);
            String loanBalanceText = "Total Loan Due: Ksh " + loanBalance;
            totalLoanDueTextView.setText(loanBalanceText);
        }
    }

    private class PayLoanTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String memberId = params[0];
            String formattedDate = params[1];
            String amount = params[2];

            // Perform the loan repayment here
            return apiService.payloan(memberId, formattedDate, amount, loginResponse);
        }

        @Override
        protected void onPostExecute(String response) {
            Log.d(TAG, "payloan Response: " + response);
            if (response.endsWith("{\"error\":\"Successfully made loan repayment\",\"auth\":true}") || response.startsWith("{\"error\":\"Successfully made")) {
                showSuccessDialog("Success", "Loan Repayment Successful");
                // After successful repayment, update the total loan balance by performing login
                performLoginAndUpdateUI();
            } else {
                showErrorDialog("Error", "Loan Repayment Failed");
            }
        }
    }
}
