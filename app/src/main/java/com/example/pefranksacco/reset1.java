package com.example.pefranksacco;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class reset1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset1);

        final EditText otp = findViewById(R.id.otp);
        final EditText newPasswordEditText = findViewById(R.id.editTextText3);
        final EditText repeatPasswordEditText = findViewById(R.id.editTextText2);
        Button resetButton = findViewById(R.id.button2);

        ImageView viewPasswordButton10 = findViewById(R.id.eye10);
        ImageView viewPasswordButton100 = findViewById(R.id.eye100);

        viewPasswordButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = newPasswordEditText.getInputType();
                if ((inputType & InputType.TYPE_TEXT_VARIATION_PASSWORD) > 0) {
                    // Password is currently hidden, show it
                    newPasswordEditText.setInputType(inputType & ~InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    // Password is currently visible, hide it
                    newPasswordEditText.setInputType(inputType | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        viewPasswordButton100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = repeatPasswordEditText.getInputType();
                if ((inputType & InputType.TYPE_TEXT_VARIATION_PASSWORD) > 0) {
                    // Password is currently hidden, show it
                    repeatPasswordEditText.setInputType(inputType & ~InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    // Password is currently visible, hide it
                    repeatPasswordEditText.setInputType(inputType | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  password_reset_code= otp.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String repeatPassword = repeatPasswordEditText.getText().toString();

                // Check if any of the EditTexts is empty
                if (TextUtils.isEmpty(password_reset_code) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(repeatPassword)) {
                    // Display error inside the EditText to prompt user input
                    if (TextUtils.isEmpty(password_reset_code)) {
                        otp.setError("Please enter The otp sent to you ");
                    }
                    if (TextUtils.isEmpty(newPassword)) {
                        newPasswordEditText.setError("Enter new password");
                    }
                    if (TextUtils.isEmpty(repeatPassword)) {
                        repeatPasswordEditText.setError("Repeat new password");
                    }
                    return;
                }

                // Check if passwords match
                if (!newPassword.equals(repeatPassword)) {
                    Toast.makeText(reset1.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the password length is at least 8 characters
                if (newPassword.length() < 8) {
                    Toast.makeText(reset1.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the password is strong
                if (!isStrongPassword(newPassword)) {
                    // Show an error in the password EditText
                    newPasswordEditText.setError("Password must be at least 8 characters long and include uppercase, lowercase, number, and special character");
                    return;
                } else {
                    // Clear any previous errors
                    newPasswordEditText.setError(null);
                }

                // Call the resetPasswordAsync method from ApiService
                ApiService.resetPassword1Async(password_reset_code, newPassword,repeatPassword, new ApiService.ApiCallback() {
                    @Override
                    public void onApiRequestComplete(String response) {
                        // Parse the response and handle success
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean auth = jsonResponse.optBoolean("auth", false);
                            String successMessage = jsonResponse.optString("error", "");
                            String errorMessage = jsonResponse.optString("message", "");

                            if (auth) {
                                // Password reset successful
                                // Display success message or navigate to Welcome class
                                showMessage(successMessage);
                                Intent intent = new Intent(reset1.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Password reset failed
                                if ("This device is not registered with us!".equals(errorMessage)) {
                                    showMessage("Error: This device is not registered with us!");
                                } else if ("This device does not registered to this member".equals(errorMessage)) {
                                    // Display specific error message for device not registered
                                    showMessage("Error: This device is not registered to this member");
                                } else {
                                    // Display the general error message
                                    showMessage("Error: " + errorMessage);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showMessage("Error parsing response");
                        }
                    }

                    @Override
                    public void onApiRequestError(String error) {
                        // Handle API request error
                        showMessage("API Request Error: " + error);
                    }
                });

            }
        });
    }

    // Method to display a message (you can customize this based on your UI)
    private void showMessage(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(reset1.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isStrongPassword(String password) {
        // Add your criteria for a strong password, for example:
        // At least one uppercase letter
        // At least one lowercase letter
        // At least one digit
        // At least one special character

        // Example check (modify based on your criteria):
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?& #])[A-Za-z\\d@$!%*?& #]+$");
    }
}
