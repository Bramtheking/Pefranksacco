package com.example.pefranksacco;

import static com.example.pefranksacco.ApiService.deviceSerialNumber;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private UserCredentialDAO userCredentialDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize UI elements
        View progressBar = findViewById(R.id.progressBar);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton5);
        TextView here = findViewById(R.id.here);
        TextView here2 = findViewById(R.id.here2);
        CheckBox rememberMeCheckbox = findViewById(R.id.checkBox3);
        ImageView viewPasswordButton = findViewById(R.id.eye);

        userCredentialDAO = new UserCredentialDAO(this);
        userCredentialDAO.open();
        String stringBuildModel = "Model: " + Build.MODEL +
                "\nManufacturer: " + Build.MANUFACTURER +
                "\nDevice: " + Build.DEVICE+ Build.SERIAL;
        Log.d("DeviceInfo", stringBuildModel);

        // Initialize UserCredentialDAO
        UserCredential savedUserCredential = userCredentialDAO.getSavedUserCredential();
        if (savedUserCredential != null) {
            emailEditText.setText(savedUserCredential.getEmail());
            passwordEditText.setText(savedUserCredential.getPassword());
        }
        if (savedUserCredential != null) {
            // Directly go to the welcome activity
            goToWelcomeActivity();
        }
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "App cannot work without internet connection", Toast.LENGTH_SHORT).show();
        }

        viewPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = passwordEditText.getInputType();
                if ((inputType & InputType.TYPE_TEXT_VARIATION_PASSWORD) > 0) {
                    // Password is currently hidden, show it
                    passwordEditText.setInputType(inputType & ~InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    // Password is currently visible, hide it
                    passwordEditText.setInputType(inputType | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, next.class);
                startActivity(intent);
            }
        });
        here2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, username.class);
                startActivity(intent);
            }
        });



        // Call the registerDeviceAsync method with the provided JSON data
        // Call the registerDeviceAsync method with the provided JSON data
        ApiService.registerDeviceAsync("6e59326e30919d7d0b106075c433096d5812ef01aa0926d66367fbe1843104dc",
                deviceSerialNumber, "1", new ApiService.RegisterDeviceCallback() {
                    @Override
                    public void onRegistrationComplete(String response) {
                        try {
                            // Parse the JSON response
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d("init Response", response);

                            // Check if the response indicates that the device is not registered
                            if (jsonResponse.optBoolean("auth", true)) {
                                // Device is not registered, display the device serial number



                            } else {

                                        Intent intent = new Intent(MainActivity.this, serial_no.class);
                                        startActivity(intent);
                                        finish();
                                    }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        // Set a click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the entered email and password
                if (isNetworkAvailable()) {
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    if (  email.isEmpty() ) {
                        Toast.makeText(MainActivity.this, "Please input Username ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ( password.isEmpty()  ) {
                        Toast.makeText(MainActivity.this, "Please input  Password", Toast.LENGTH_SHORT).show();
                        return;
                    }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                  });

                // Start a background thread for network operations
                Thread backgroundThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                           if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            // Call the API to validate the user's credentials using the loginUserAsync method
                            ApiService.loginUserAsync(email, password, new ApiService.LoginCallback() {
                                @Override
                                public void onLoginComplete(String response) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });

                                    // Check if the response indicates a successful login
                                    if (response != null && response.startsWith("{\"auth\":true,\"user\":{")) {
                                        try {
                                            // Parse the JSON response to get the user object
                                            JSONObject jsonResponse = new JSONObject(response);
                                            JSONObject userObject = jsonResponse.getJSONObject("user");

                                            // Check if password_reset_code is null
                                            String passwordResetCode = userObject.optString("password_reset_code", null);
                                            if ((passwordResetCode == null|| passwordResetCode.isEmpty())) {
                                                // Password reset code is null, go to WelcomeActivity

                                                if (rememberMeCheckbox.isChecked()) {
                                                    // Insert the email and password into the SQLite database

                                                }
                                                // Password reset code is not null, go to ResetActivity
                                                Intent intent = new Intent(MainActivity.this, username.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                if (rememberMeCheckbox.isChecked()) {
                                                    // Insert the email and password into the SQLite database
                                                    insertUserCredential(email, password);
                                                }

                                                // If the API validates the user, show a success dialog
                                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                goToWelcomeActivity();
                                                updateSavedValues();

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        // If not valid, show an error dialog
                                        Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    }
                 });

                // Start the background thread
                backgroundThread.start();

            }else {
                    // Display a message when there is no internet connection
                    Toast.makeText(MainActivity.this, "App cannot work without internet connection", Toast.LENGTH_SHORT).show();
                }}

        });
    }






    private void updateSavedValues() {
        // Set saved values like apiResponse and COLUMN_NUMBER to null in the database
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_API_RESPONSE, (String) null); // Set apiResponse to null
        values.put(DatabaseHelper.COLUMN_NUMBER, (String) null); // Set COLUMN_NUMBER to null

        // Update all rows (if any) in the table
        db.update(DatabaseHelper.LOAN_DATA_TABLE_NAME, values, null, null);

        db.close();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
    private void insertUserCredential(String email, String password) {
        // Insert the user's email and password into the SQLite database
        UserCredential userCredential = new UserCredential(email, password);
        userCredentialDAO.insertUserCredential(userCredential);
    }

    private void goToWelcomeActivity() {
        Intent intent = new Intent(MainActivity.this, welcome.class);
        startActivity(intent);
        finish(); // Finish the login activity so that the user cannot go back
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userCredentialDAO.close();
    }
}
