package com.example.pefranksacco;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class next extends AppCompatActivity {
    private EditText usernameEditText1, passwordEditText1, repeatPasswordEditText1, emailEditText1;
    private EditText firstname1, middlename1, lastname1, payrollno1, Idno, mobilenumber;
    private Spinner genderSpinner;
    private CheckBox checkBox;
    private TextView dobTextView; // Change to TextView for Date of Birth

    // Define initial date for the DatePicker dialog (you can customize this)
    private int initialYear = 2000;
    private int initialMonth = 0; // Month is 0-based, e.g., 0 is January
    private int initialDay = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next);
        setupGenderSpinner();

        // Initialize UI elements
        usernameEditText1 = findViewById(R.id.usernameEditText1);
        passwordEditText1 = findViewById(R.id.passwordEditText1);
        repeatPasswordEditText1 = findViewById(R.id.passwordEditText2);
        emailEditText1 = findViewById(R.id.emailEditText1);
        firstname1 = findViewById(R.id.firstname);
        middlename1 = findViewById(R.id.middlename);
        lastname1 = findViewById(R.id.lastname);
        payrollno1 = findViewById(R.id.payrollno);
        Idno = findViewById(R.id.idno);
        mobilenumber = findViewById(R.id.mobilenumber);
        genderSpinner = findViewById(R.id.genderSpinner);
        checkBox = findViewById(R.id.checkBox);

        // Date of Birth TextView
        dobTextView = findViewById(R.id.dob);
        dobTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        TextView privacypolicy;
        TextView signin;
        TextView termofservice;

        privacypolicy = findViewById(R.id.privacypolicy);
        signin = findViewById(R.id.signin);
        signin.setOnClickListener(view -> {
            Intent intent = new Intent(next.this, MainActivity.class);
            startActivity(intent);
        });
        privacypolicy.setOnClickListener(view -> {
            Intent intent = new Intent(next.this, privacypolicy.class);
            startActivity(intent);
        });

        termofservice = findViewById(R.id.termofservice);
        termofservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(next.this, termofservice.class);
                startActivity(intent);
            }
        });

        Button signUpButton = findViewById(R.id.SignUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    signUp(); // Proceed with sign up if there's internet
                } else {
                    Toast.makeText(next.this, "No internet access", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupGenderSpinner() {
        String[] genderOptions = {"Male", "Female"};
        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedGender = genderOptions[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String formattedDate = formatDate(year, month, day);
                        dobTextView.setText(formattedDate);
                    }
                },
                initialYear, initialMonth, initialDay
        );
        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int day) {
        String formattedMonth = (month < 9) ? "0" + (month + 1) : String.valueOf(month + 1);
        String formattedDay = (day < 10) ? "0" + day : String.valueOf(day);
        return year + "-" + formattedMonth + "-" + formattedDay;
    }

    private void signUp() {
        // Get input values from EditText fields
        String username = usernameEditText1.getText().toString();
        String password = passwordEditText1.getText().toString();
        String repeatPassword = repeatPasswordEditText1.getText().toString();
        String personal_email = emailEditText1.getText().toString();
        String firstname = firstname1.getText().toString();
        String middlename = middlename1.getText().toString();
        String lastname = lastname1.getText().toString();
        String payrollno = payrollno1.getText().toString();
        String idno = Idno.getText().toString();
        String dob = dobTextView.getText().toString(); // Use dobTextView for Date of Birth
        String mobileno = mobilenumber.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();
        boolean acceptedTerms = checkBox.isChecked();

        // Rest of the signUp() method remains unchanged...
        // ...
        if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || personal_email.isEmpty() ||
                firstname.isEmpty() || lastname.isEmpty() || payrollno.isEmpty() || idno.isEmpty() || dob.isEmpty() || mobileno.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repeatPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            // Display an error message for weak password
            Toast.makeText(next.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return; // Stop further execution
        }
        if ( !isStrongPassword(password)) {
            // Show an error in the password EditText
            passwordEditText1.setError("Password must be at least 8 characters long and include uppercase, lowercase, number, and special character");
            return; // Stop further execution
        } else {
            // Clear any previous errors
            passwordEditText1.setError(null);
        }

        if (!acceptedTerms) {
            Toast.makeText(this, "Please accept the terms of service", Toast.LENGTH_SHORT).show();
            return;
        }

        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkAvailable()) {
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        Log.d("RegistrationRequest", "Username: " + username +
                                ", Password: " + password +
                                ", Email: " + personal_email +
                                ", First Name: " + firstname +
                                ", Middle Name: " + middlename +
                                ", Last Name: " + lastname +
                                ", Payroll: " + payrollno +
                                ", ID: " + idno +
                                ", Date of Birth: " + dob +
                                ", Mobile: " + mobileno +
                                ", Gender: " + gender);
                        String registrationResponse = ApiService.registerMember(personal_email,
                                firstname, middlename, payrollno, lastname,gender, idno, dob, mobileno, username,  password);
                        Log.d("RegistrationResponse", "Response: " + registrationResponse);
                        if (registrationResponse.equals("{\"error\":\"Success\",\"auth\":true}")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(next.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(next.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else if (registrationResponse.equals("Error:null")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(next.this, "Sign Up Successful ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(next.this, next.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(next.this, "Registration Error: " + registrationResponse, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(next.this, "No internet access", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        backgroundThread.start();
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
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }

        return false;
    }
}
