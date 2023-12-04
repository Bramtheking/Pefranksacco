package com.example.pefranksacco.ui.info;

import static com.example.pefranksacco.ApiService.API_EMAIL;
import static com.example.pefranksacco.ApiService.API_PASSWORD;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pefranksacco.ApiService;
import com.example.pefranksacco.R;
import com.example.pefranksacco.UserCredential;
import com.example.pefranksacco.UserCredentialDAO;

import org.json.JSONException;
import org.json.JSONObject;

public class infoFragment extends Fragment {

    private static EditText emailAddressEditText;
    private static TextView usernameTextView;
    private static TextView genderTextView;
    private static EditText idNumberEditText;
    private static EditText phoneNumberEditText;
    private UserCredentialDAO userCredentialDAO;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info, container, false);

        // Initialize the UI elements
        View progressBar = root.findViewById(R.id.progressBar);
        emailAddressEditText = root.findViewById(R.id.editTextEmailAddress2);
        usernameTextView = root.findViewById(R.id.username1);
        genderTextView = root.findViewById(R.id.gender);
        idNumberEditText = root.findViewById(R.id.idnumber);
        phoneNumberEditText = root.findViewById(R.id.phonenumber);
        progressBar.setVisibility(View.VISIBLE);
        // Initialize UserCredentialDAO
        userCredentialDAO = new UserCredentialDAO(getActivity());

        // Execute the AsyncTask to perform the network request
        new NetworkRequestTask(userCredentialDAO).execute();

        return root;
    }

    // AsyncTask to perform the network request in the background
    private class NetworkRequestTask extends AsyncTask<Void, Void, String> {
        private UserCredentialDAO userCredentialDAO;
        private boolean showToast = false;

        NetworkRequestTask(UserCredentialDAO dao) {
            this.userCredentialDAO = dao;
        }

        @Override
        protected String doInBackground(Void... voids) {
            // This method runs on a background thread

            // Open the UserCredentialDAO
            userCredentialDAO.open();

            // Retrieve the saved email and password from MainActivity
            UserCredential savedUserCredential = userCredentialDAO.getSavedUserCredential();
            String email = "";
            String password = "";
            if (savedUserCredential != null) {
                email = savedUserCredential.getEmail();
                password = savedUserCredential.getPassword();
            }

            // Close the UserCredentialDAO before returning
            userCredentialDAO.close();
            if (API_EMAIL.isEmpty() && API_PASSWORD.isEmpty() && email.isEmpty() && password.isEmpty()) {
                showToast = true;
                return null;  // Return null to indicate conditions for Toast are met
            }


            // Use either saved credentials or API credentials for login
            return ApiService.loginUser(email.isEmpty() ? API_EMAIL : email,
                    password.isEmpty() ? API_PASSWORD : password);
        }


        @Override
        protected void onPostExecute(String response) {

            View fragmentView = getView();
            if (fragmentView != null) {
                View progressBar = fragmentView.findViewById(R.id.progressBar);
                if (progressBar != null)
                    progressBar.setVisibility(View.INVISIBLE);
                    if (showToast) {
                        // Show Toast for login conditions
                        Toast.makeText(requireContext(), "You are required to log in again", Toast.LENGTH_LONG).show();
                    }
                // This method runs on the main (UI) thread
                if (response != null) {
                    // Log the response from the API
                    Log.d("LoginUser Response", response);

                    // Update the UI based on the response
                    updateUIWithLoginResponse(response);
                } else {
                    // Handle the case where the response is null (no saved credentials).
                    // You can display an error message or take appropriate action.
                }
            }
        }
    }

    private static void updateUIWithLoginResponse(String loginResponse) {
        try {
            JSONObject jsonResponse = new JSONObject(loginResponse);
            JSONObject userObject = jsonResponse.optJSONObject("user");

            if (userObject != null) {
                String email = userObject.optString("email", "");
                String username = userObject.optString("username", "");
                String phoneNumber = userObject.optString("phone", "");
                if (phoneNumber.length() >= 6) {
                    phoneNumber = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(phoneNumber.length() - 3);
                }
                emailAddressEditText.setText(email);
                usernameTextView.setText(username);
                phoneNumberEditText.setText(phoneNumber);
            }

            JSONObject memberObject = jsonResponse.optJSONObject("member");
            if (memberObject != null) {
                String gender = memberObject.optString("gender", "");
                String idNumber = memberObject.optString("idno", "");

                if (idNumber.length() >= 4) {
                    idNumber = idNumber.substring(0, 2) + "****" + idNumber.substring(idNumber.length() - 2);
                }
                genderTextView.setText(gender);
                idNumberEditText.setText(idNumber);
            } else {
                // Handle the case where the "user" object is not present in the response.
                // You can display an error message or take appropriate action.
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error, if necessary.
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userCredentialDAO.close();
    }
}
