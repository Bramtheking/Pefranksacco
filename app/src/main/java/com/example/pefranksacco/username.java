package com.example.pefranksacco;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class username extends AppCompatActivity {
    public static String username3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username);

        Button checkButton = findViewById(R.id.Check);
        EditText usernameEditText = findViewById(R.id.editTextText7);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username3  = usernameEditText.getText().toString();

                // Call the checkUsernameAsync method
                ApiService.checkUsernameAsync(username3, new ApiService.UsernameCheckCallback() {
                    @Override
                    public void onUsernameCheckComplete(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean auth = jsonResponse.optBoolean("auth", false);
                            Log.d("UsernameCheck Response", response);
                            if (auth) {
                                // If authentication is true, show a success message
                                showSuccessMessage();
                                // Go to Reset.class
                                Intent intent = new Intent(username.this, reset.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Handle the case where authentication is false
                                if (jsonResponse.has("error")) {
                                    String errorMessage = jsonResponse.getString("error");
                                    showErrorMessage(errorMessage);
                                } else {
                                    showErrorMessage("Unknown error occurred.");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                            showErrorMessage("Error parsing JSON response.");
                        }
                    }

                    @Override
                    public void onUsernameCheckError(String error) {
                        // Handle API request error
                        showErrorMessage("API Request Error: " + error);
                    }
                });
            }
        });
    }

    private void showSuccessMessage() {
        runOnUiThread(() -> {
            Toast.makeText(username.this, "Successfully verified your username", Toast.LENGTH_SHORT).show();
        });
    }

    private void showErrorMessage(String message) {
        runOnUiThread(() -> {
            Toast.makeText(username.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}
