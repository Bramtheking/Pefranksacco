package com.example.pefranksacco;

import static com.example.pefranksacco.ApiService.API_MEMBERID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoanApplicationFragment extends Fragment {

    private EditText amountEditText, interests, purposeEditText, durationEditText;
    private Spinner spinner;
    private Button applyButton;
    private TextView appliedOnEditText;
    private TextView errorMessageTextView; // Add this TextView

    private String[] parts;
    private int selectedDuration;
    private int selectedinterest;// Store the selected duration as an integer

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.loan_application, container, false);

        // Initialize UI elements
        amountEditText = rootView.findViewById(R.id.amount);
        interests = rootView.findViewById(R.id.Documentno);
        purposeEditText = rootView.findViewById(R.id.purpose);
        durationEditText = rootView.findViewById(R.id.Month); // Initialize the durationEditText
        appliedOnEditText = rootView.findViewById(R.id.appliedon);
        spinner = rootView.findViewById(R.id.spinner);
        applyButton = rootView.findViewById(R.id.contribute);
        errorMessageTextView = rootView.findViewById(R.id.errorMessageTextView); // Initialize the TextView


        // Find the asterisk TextView
        TextView amountAsteriskLabel = rootView.findViewById(R.id.textView28);

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used, but required for TextWatcher
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if text is being entered
                if (s.length() > 0) {
                    // Hide the asterisk when text is entered
                    amountAsteriskLabel.setVisibility(View.INVISIBLE);
                } else {
                    // Show the asterisk when no text is entered
                    amountAsteriskLabel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used, but required for TextWatcher
            }
        });

        String member_id = API_MEMBERID;

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.dropdown_options,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedValue = spinnerAdapter.getItem(position).toString();
                int[] Interest = new int[]{10, 0, 0, 20};
                int[] Duration = new int[]{36, 10, 48, 3};

                parts = selectedValue.split(":");
                if (parts.length > 0) {
                    selectedValue = parts[0];
                    spinner.setSelection(spinnerAdapter.getPosition(selectedValue));
                }

                if (selectedValue.equals("1")) {
                    interests.setText("Interest " + Interest[0]);
                    selectedDuration = Duration[0];
                    selectedinterest=Interest[0];
                    durationEditText.setText("Duration(Months) " + Duration[0]);
                } else if (selectedValue.equals("2")) {
                    interests.setText("Interest " + Interest[1]);
                    selectedDuration = Duration[1];
                    selectedinterest=Interest[1];
                    durationEditText.setText("Duration(Months) " + Duration[1]);
                } else if (selectedValue.equals("3")) {
                    interests.setText("Interest " + Interest[2]);
                    selectedDuration = Duration[2];
                    durationEditText.setText("Duration(Months) " + Duration[2]);
                    selectedinterest=Interest[2];
                } else if (selectedValue.equals("4")) {
                    interests.setText("Interest " + Interest[3]);
                    selectedDuration = Duration[3];
                    durationEditText.setText("Duration(Months) " + Duration[3]);
                    selectedinterest=Interest[3];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });

        setAndDisableCurrentDate();

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = amountEditText.getText().toString();
                int duration = Integer.parseInt(String.valueOf(selectedDuration)); // Convert the integer to a string
                int interest = Integer.parseInt(String.valueOf(selectedinterest));
                String purpose = purposeEditText.getText().toString();
                String appliedOn = appliedOnEditText.getText().toString();
                String saccoloanid = parts[0];

                // Clear any previous error message
                errorMessageTextView.setVisibility(View.GONE);

                if (amount.isEmpty()) {
                    errorMessageTextView.setText("Amount is required");
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    return;
                }

                try {
                    double amountValue = Double.parseDouble(amount);
                    if (amountValue < 100 || amountValue > 10000000) {
                        errorMessageTextView.setText("Amount must be between 100 and 10,000,000");
                        errorMessageTextView.setVisibility(View.VISIBLE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    errorMessageTextView.setText("Invalid amount format");
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    return;
                }

                String selectedValue = spinner.getSelectedItem().toString();
                if (selectedValue.equals("Select an option")) {
                    errorMessageTextView.setText("Please select a spinner option");
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    return;
                }

                new ApplyForLoanAsyncTask().execute(saccoloanid, member_id, appliedOn, amount, String.valueOf(duration), String.valueOf(interest), purpose);
                amountEditText.setText("");
                interests.setText("");
                purposeEditText.setText("");
                durationEditText.setText("");  // Initialize the durationEditText
                appliedOnEditText .setText("");
                spinner.setSelection(spinnerAdapter.getPosition("Select an option"));

            }
        });

        return rootView;
    }

    private void setAndDisableCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);

        appliedOnEditText.setText(formattedDate);
        appliedOnEditText.setFocusable(false);
        appliedOnEditText.setClickable(true);
    }

    private class ApplyForLoanAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String member_id = params[1];
            String saccoloanid = params[0];
            String appliedon = params[2];
            String amount = params[3];
            String duration = params[4];
            String interest = params[5];
            String purpose = params[6];

            return ApiService.applyForLoan(member_id, saccoloanid, appliedon, amount, duration, interest, purpose);
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                if (response.startsWith("{\"error\":\"Successfully applied")) {
                    // Show a success message using AlertDialog
                    showSuccessDialog("Success", "Loan application Successful");
                } else {
                    // Check for specific error message
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String errorMessage = jsonResponse.optString("error", "");

                        if (errorMessage.equals("Member has similar existing loan")) {
                            // Show a specific error message for the given condition
                            showSpecificErrorDialog("Error", "You already have a similar existing loan");
                        } else {
                            // Show a generic error message using AlertDialog
                            showErrorDialog("Error", "An error has occurred while applying loan");
                        }
                    } catch (JSONException e) {
                        // Show a generic error message using AlertDialog
                        showErrorDialog("Error", "Unexpected response from the server");
                    }
                }
            } else {
                // Show a generic error message using AlertDialog
                showErrorDialog("Error", "Unexpected response from the server");
            }
        }

        private void showSpecificErrorDialog(String title, String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // You can perform any action here or simply dismiss the dialog
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }


        private void showSuccessDialog(String title, String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // You can perform any action here or simply dismiss the dialog
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void showErrorDialog(String title, String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // You can perform any action here or simply dismiss the dialog
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
