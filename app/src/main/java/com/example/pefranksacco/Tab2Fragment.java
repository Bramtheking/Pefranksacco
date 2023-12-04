package com.example.pefranksacco;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tab2Fragment extends Fragment {
    private TextView Totalsavingstextview;
    private EditText amountEditText;
    private ApiService apiService = new ApiService(); // Create an instance of ApiService

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);

        Totalsavingstextview = rootView.findViewById(R.id.Totalsavings);
        amountEditText = rootView.findViewById(R.id.amount);

        // Call the asynchronous method to get the savings balance
        apiService.getSavingsBalanceFromLoginResponseAsync(new ApiService.SavingsBalanceCallback() {
            @Override
            public void onSavingsBalanceComplete(double totalSavingsBalance) {
                // Update the UI with the received savings balance
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Totalsavingstextview.setText("Total Savings: Ksh " + totalSavingsBalance);
                    }
                });
            }
        });

        Button repaymentButton = rootView.findViewById(R.id.contribute);
        repaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = amountEditText.getText().toString();

                if (amount.isEmpty()) {
                    amountEditText.setError("Amount is required");
                } else {
                    Date currentDate = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String repaymentdate = dateFormat.format(currentDate);
                    String member_id = ApiService.API_MEMBERID;

                    Log.d("Tab2Fragment", "Member ID: " + member_id);
                    Log.d("Tab2Fragment", "Repayment Date: " + repaymentdate);
                    Log.d("Tab2Fragment", "Amount: " + amount);

                    // Use AsyncTask to perform the network request
                    new SavingRepaymentTask().execute(member_id, repaymentdate, amount);
                    amountEditText.setText("");
                }
            }
        });

        return rootView;
    }

    private class SavingRepaymentTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String member_id = params[0];
            String repaymentdate = params[1];
            String amount = params[2];

            return apiService.savingRepayment(member_id, repaymentdate, amount);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.d("Tab2Fragment", "API Response: " + response);

            if (response.startsWith("{\"error\":\"Savings Successfully")) {
                apiService.getSavingsBalanceFromLoginResponseAsync(new ApiService.SavingsBalanceCallback() {
                    @Override
                    public void onSavingsBalanceComplete(double totalSavingsBalance) {
                        // Update the UI with the received savings balance
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Totalsavingstextview.setText("Total Savings: Ksh " + totalSavingsBalance);
                            }
                        });
                    }
                });
                showSuccessDialog("Success", "Saving payment Successful");
            } else {
                showErrorDialog("Error", "Savings Payment Failed");
            }
        }
    }

    private void showSuccessDialog(String title, String message) {
        if (isAdded()) {
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
    }

    private void showErrorDialog(String title, String message) {
        if (isAdded()) {
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
    }
}