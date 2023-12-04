package com.example.pefranksacco;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class serial_no extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial_no);

        // Make the TextView initially invisible
        TextView serialNumberTextView = findViewById(R.id.serialnumber2);

        // Call the registerDeviceAsync method with the provided JSON data
        ApiService.registerDeviceAsync("6e59326e30919d7d0b106075c433096d5812ef01aa0926d66367fbe1843104dc",
                ApiService.deviceSerialNumber, "1", new ApiService.RegisterDeviceCallback() {
                    @Override
                    public void onRegistrationComplete(String response) {
                        try {
                            // Parse the JSON response
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d("inits Response", response);

                            // Check if the response indicates that the device is not registered
                            if (!jsonResponse.optBoolean("auth", true)) {
                                // Device is not registered, display the device serial number
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String deviceSerialNumber = ApiService.deviceSerialNumber;
                                        serialNumberTextView.setText("Device Serial Number: " + deviceSerialNumber);
                                        serialNumberTextView.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else {
                                // Device is registered, hide the serialNumberTextView
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        serialNumberTextView.setVisibility(View.GONE);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    // Method for handling the click event on serialNumberTextView
    public void onSerialNumberClick(View view) {
        TextView serialNumberTextView = findViewById(R.id.serialnumber2);

        // Get the serial number without the label
        String serialNumber = serialNumberTextView.getText().toString().replace("Device Serial Number: ", "");

        // Copy the text to the clipboard
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Serial Number", serialNumber);
        clipboard.setPrimaryClip(clip);

        // Show a toast or perform any other action to indicate that the text is copied
        Toast.makeText(this, "Serial number copied", Toast.LENGTH_SHORT).show();
    }

    // Method for handling the click event on the exit button
    public void onExitButtonClick(View view) {
        finish(); // Close the activity and exit the app
    }
}