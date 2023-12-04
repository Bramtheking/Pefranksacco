package com.example.pefranksacco;

import static com.example.pefranksacco.username.username3;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiService {

    public static final String API_KEY = "6e59326e30919d7d0b106075c433096d5812ef01aa0926d66367fbe1843104dc";
    public static final String BASE_URL = "https://esacco.pefranksmartsolutions.com/api/v1/";

    public static String API_USERNAME = "";
    public static String API_PASSWORD = "";
    public static String API_MEMBERID = "";
    public static String username4 = "";
    public static String API_EMAIL = "";
    private static Context appContext;
    static String deviceSerialNumber;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 100;

    // Check and request permission method
    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    // Check and request permission method



    // Move your device serial number initialization logic here
    private static void initializeDeviceSerialNumber() {
        deviceSerialNumber = UniqueDeviceIdentifier.generateUniqueIdentifier();
    }


    // Check and request permission using the stored application context
    public static class UniqueDeviceIdentifier {

        public static String generateUniqueIdentifier() {
            StringBuilder uniqueIdentifier = new StringBuilder();

            uniqueIdentifier.append(Build.BOARD);
            uniqueIdentifier.append(Build.BOOTLOADER);
            uniqueIdentifier.append(Build.BRAND);
            uniqueIdentifier.append(Build.DEVICE);
            uniqueIdentifier.append(Build.HARDWARE);
            uniqueIdentifier.append(Build.MODEL);
            uniqueIdentifier.append(Build.PRODUCT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                uniqueIdentifier.append(Build.SOC_MODEL);
            }

            return uniqueIdentifier.toString();
        }
    }



    public static void getSavingsBalanceFromLoginResponseAsync(final SavingsBalanceCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initializeDeviceSerialNumber();
                double savingsBalance = getSavingsBalanceFromLoginResponse();
                callback.onSavingsBalanceComplete(savingsBalance);
            }
        }).start();
    }

    public static void getLoanBalanceFromMemberLoansResponseAsync(final LoanBalanceCallback callback, final String loginResponse) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initializeDeviceSerialNumber();
                double loanBalance = getLoanBalanceFromLoginResponse(loginResponse);
                callback.onLoanBalanceComplete(loanBalance);
            }
        }).start();
    }

    public interface SavingsBalanceCallback {
        void onSavingsBalanceComplete(double savingsBalance);
    }

    public interface LoanBalanceCallback {
        void onLoanBalanceComplete(double loanBalance);
    }
    public static void makeApiRequestAsync(final String loan_id, final ApiCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initializeDeviceSerialNumber();
                makeApiRequest(loan_id, callback);
            }
        }).start();
    }

    public interface ApiCallback {
        void onApiRequestComplete(String response);
        void onApiRequestError(String error);
    }
    public static void checkUsernameAsync(String username, final UsernameCheckCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initializeDeviceSerialNumber();
                    OkHttpClient client = new OkHttpClient();

                    // Create JSON request body
                    JSONObject json = new JSONObject();
                    json.put("key", API_KEY);
                    json.put("username", username);
                    json.put("device_serial_no", deviceSerialNumber);
                    Log.d("Request Body", json.toString());

                    RequestBody requestBody = RequestBody.create(json.toString(), MediaType.parse("application/json"));

                    // Create the request
                    Request request = new Request.Builder()
                            .url(BASE_URL + "/account/verifyusername")  // Replace with the actual API endpoint
                            .post(requestBody)
                            .build();
                    API_USERNAME = username;
                    // Execute the request
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();
                    Log.d("Request Body", requestBody.toString());
                    // Pass the response to the callback
                    callback.onUsernameCheckComplete(responseBody);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onUsernameCheckError("Error: " + e.getMessage());
                }
            }
        }).start();
    }

    public interface UsernameCheckCallback {
        void onUsernameCheckComplete(String response);
        void onUsernameCheckError(String error);
    }
    public static void resetPasswordAsync( final String newPassword,final String password,String repeatPassword , final ApiCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initializeDeviceSerialNumber();
                resetPassword(newPassword,password,repeatPassword, callback);
            }
        }).start();
    }
    public static void resetPassword1Async( final String newPassword,final String password,String repeatPassword , final ApiCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initializeDeviceSerialNumber();
                resetPassword1(newPassword,password,repeatPassword, callback);
            }
        }).start();
    }
    public static void registerDeviceAsync(String key, String dsseviceSerialNumber, String from, RegisterDeviceCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initializeDeviceSerialNumber();
                    OkHttpClient client = new OkHttpClient();

                    // Create JSON request body
                    String json = "{\"key\":\"" + key + "\",\"device_serial_no\":\"" + deviceSerialNumber + "\",\"from\":\"" + from + "\"}";
                    Log.d("Request Body", json);
                    RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));

                    // Create the request
                    Request request = new Request.Builder()
                            .url(BASE_URL + "/account/init")
                            .post(requestBody)
                            .build();

                    // Execute the request
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();

                    // Pass the response to the callback
                    callback.onRegistrationComplete(responseBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface RegisterDeviceCallback {
        void onRegistrationComplete(String response);
    }

    private static void resetPassword(String password_reset_code,String password, String repeatPassword, final ApiCallback callback) {
        try {
            OkHttpClient client = new OkHttpClient();

            // Create a JSON object with the reset password data
            JSONObject json = new JSONObject();
            json.put("key", API_KEY);
            json.put("device_serial_no", deviceSerialNumber);
            json.put("username", API_USERNAME);
            json.put("password_reset_code", password_reset_code);
            json.put("password", password);
            json.put("password_confirm", repeatPassword);

            Log.d("Reset Password Request", json.toString());

            // Create the request body
            MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBodyObj = RequestBody.create(JSON_MEDIA_TYPE, json.toString());

            Request request = new Request.Builder()
                    .url(BASE_URL + "account/resetpassword") // Replace with the actual API endpoint
                    .post(requestBodyObj)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                callback.onApiRequestComplete(responseBody);
                Log.d("Reset Password Response", responseBody);
            } else {
                String errorMessage = "Error: " + response.code() + " - " + response.message();
                callback.onApiRequestError(errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onApiRequestError("Error: " + e.getMessage());
        }
    }
    private static void resetPassword1(String password_reset_code,String password, String repeatPassword, final ApiCallback callback) {
        try {
            OkHttpClient client = new OkHttpClient();

            // Create a JSON object with the reset password data
            JSONObject json = new JSONObject();
            json.put("key", API_KEY);
            json.put("device_serial_no", deviceSerialNumber);
            json.put("username", API_USERNAME);
            json.put("password_reset_code", password_reset_code);
            json.put("password", password);
            json.put("password_confirm", repeatPassword);

            Log.d("Reset Password Request1", json.toString());

            // Create the request body
            MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBodyObj = RequestBody.create(JSON_MEDIA_TYPE, json.toString());

            Request request = new Request.Builder()
                    .url(BASE_URL + "account/resetpassword") // Replace with the actual API endpoint
                    .post(requestBodyObj)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                callback.onApiRequestComplete(responseBody);
                Log.d("Reset Password Response", responseBody);
            } else {
                String errorMessage = "Error: " + response.code() + " - " + response.message();
                callback.onApiRequestError(errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onApiRequestError("Error: " + e.getMessage());
        }
    }

    public static String registerMember(String personal_email, String firstname, String middlename, String payrollno, String lastname, String gender, String idno, String dob, String mobileno, String username, String password) {
        try {
            OkHttpClient client = new OkHttpClient();

            // Create a JSON object with the data
            JSONObject json = new JSONObject();
            json.put("personal_email", personal_email);
            json.put("firstname", firstname);
            json.put("middlename", middlename);
            json.put("payrollno", payrollno);
            json.put("lastname", lastname);
            json.put("gender", gender);
            json.put("idno", idno);
            json.put("dob", dob);
            json.put("mobileno", mobileno);
            json.put("username", username);
            json.put("password", password);


            Log.d("registration request", json.toString());

            JSONObject requestBody = new JSONObject();
            requestBody.put("key", API_KEY);
            requestBody.put("username", "");
            requestBody.put("password", "");
            requestBody.put ("device_serial_no", deviceSerialNumber);
            requestBody.put("json", json);
            requestBody.put("member", JSONObject.NULL);
            requestBody.put("error", "");

            MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBodyObj = RequestBody.create(JSON_MEDIA_TYPE, requestBody.toString());

            Request request = new Request.Builder()
                    .url(BASE_URL + "members/register")
                    .post(requestBodyObj)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return responseBody;
            } else {
                return "Error: " + response.code() + " - " + response.message();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static void loginUserAsync(final String email, final String password, final LoginCallback callback) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                if (params.length < 2) {
                    return "Error: Not enough parameters provided";
                }

                String email = params[0];
                String password = params[1];
                initializeDeviceSerialNumber();
                return loginUser(email, password);
            }

            @Override
            protected void onPostExecute(String result) {
                callback.onLoginComplete(result);
            }
        }.execute(email, password);
    }

    public interface LoginCallback {
        void onLoginComplete(String result);
    }

    public static String applyForLoan(String member_id, String saccoloanid, String appliedon, String amount, String duration, String interest, String purpose) {
        try {
            OkHttpClient client = new OkHttpClient();

            // Create a JSON object with the loan application data
            JSONObject json = new JSONObject();
            json.put("member_id", member_id);
            json.put("saccoloanid", saccoloanid);
            json.put("appliedon", appliedon);
            json.put("amount", amount);
            json.put("duration", duration);
            json.put("interest", interest);
            json.put("purpose", purpose);

            Log.d("Loan Application Request", json.toString());

            JSONObject requestBody = new JSONObject();
            requestBody.put("username", API_USERNAME); // Use stored username
            requestBody.put("key", API_KEY);
            requestBody.put("password", API_PASSWORD);
            requestBody.put ("device_serial_no", deviceSerialNumber);

            requestBody.put("json", json);

            MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBodyObj = RequestBody.create(JSON_MEDIA_TYPE, requestBody.toString());

            Request request = new Request.Builder()
                    .url(BASE_URL + "loans/apply")
                    .post(requestBodyObj)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Log.d("Loan Application Response", responseBody); // Log the response
                return responseBody;
            } else {
                String errorMessage = "Error: " + response.code() + " - " + response.message();
                Log.e("Loan Application Error", errorMessage); // Log the error
                return errorMessage;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Loan Application Exception", e.getMessage()); // Log the exception
            return "Error: " + e.getMessage();
        }
    }

    public static String getMemberLoans() {
        try {
            OkHttpClient client = new OkHttpClient();

            // Create a JSON object with the member ID and username
            JSONObject requestBody = new JSONObject();
            requestBody.put("key", API_KEY);
            requestBody.put("username", API_USERNAME);
            requestBody.put("password", API_PASSWORD);
            requestBody.put ("device_serial_no", deviceSerialNumber);
            requestBody.put("member_id", API_MEMBERID);

            MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBodyObj = RequestBody.create(JSON_MEDIA_TYPE, requestBody.toString());

            Request request = new Request.Builder()
                    .url(BASE_URL + "loans/memberloans")
                    .post(requestBodyObj)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return responseBody;
            } else {
                return "Error: " + response.code() + " - " + response.message();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static String payloan(String member_id, String repaymentdate, String amount, String loginResponse) {
        try {
            OkHttpClient client = new OkHttpClient();

            // Create a JSON object with the saving repayment data
            JSONObject json = new JSONObject();
            json.put("member_id", member_id);
            json.put("repaymentdate", repaymentdate);
            json.put("amount", amount);

            JSONObject requestBody = new JSONObject();
            requestBody.put("key", API_KEY);
            requestBody.put("username", API_USERNAME);
            requestBody.put("password", API_PASSWORD);
            requestBody.put ("device_serial_no", deviceSerialNumber);
            requestBody.put("json", json);
            Log.d("Request Body", requestBody.toString());
            MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBodyObj = RequestBody.create(JSON_MEDIA_TYPE, requestBody.toString());

            Request request = new Request.Builder()
                    .url(BASE_URL + "loans/payloan") // Replace with the actual API endpoint
                    .post(requestBodyObj)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                Log.d("Login Response", String.valueOf(response));
                String responseBody = response.body().string();
                return responseBody;
            } else {
                return "Error: " + response.code() + " - " + response.message();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static String savingRepayment(String member_id, String repaymentdate, String amount) {
        try {
            OkHttpClient client = new OkHttpClient();

            // Create a JSON object with the saving repayment data
            JSONObject json = new JSONObject();
            json.put("member_id", member_id);
            json.put("repaymentdate", repaymentdate);
            json.put("amount", amount);

            JSONObject requestBody = new JSONObject();
            requestBody.put("key", API_KEY);
            requestBody.put("username", API_USERNAME);
            requestBody.put("password", API_PASSWORD);
            requestBody.put ("device_serial_no", deviceSerialNumber);
            requestBody.put("json", json);

            MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBodyObj = RequestBody.create(JSON_MEDIA_TYPE, requestBody.toString());

            Request request = new Request.Builder()
                    .url(BASE_URL + "savings/savingsrepayment")
                    .post(requestBodyObj)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                return responseBody;
            } else {
                return "Error: " + response.code() + " - " + response.message();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static String loginUser(String email, String password) {
        try {
            OkHttpClient client = new OkHttpClient();

            // Create a JSON object with the login data
            JSONObject json = new JSONObject();
            json.put("email", email); // Use email for login
            json.put("password", password);

            JSONObject requestBody = new JSONObject();
            requestBody.put("email", email); // Use email for login
            requestBody.put("password", password);
            requestBody.put ("device_serial_no", deviceSerialNumber);
            requestBody.put("key", API_KEY);
            Log.d("Request Body", requestBody.toString());
            MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBodyObj = RequestBody.create(JSON_MEDIA_TYPE, requestBody.toString());

            Request request = new Request.Builder()
                    .url(BASE_URL + "auth/login")
                    .post(requestBodyObj)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Log.d("Login Response", responseBody);

                // Extract and store the username from the provided email
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONObject userObject = jsonResponse.optJSONObject("user");
                if (userObject != null) {
                    String username = userObject.optString("username", "");
                    API_USERNAME = username;
                    String memberid = userObject.optString("memberid", "");
                    API_MEMBERID = memberid;
                    email = userObject.optString("email", "");
                    API_EMAIL = email;
                    API_PASSWORD = password;
                }

                return responseBody;
            } else {
                return "Error: " + response.code() + " - " + response.message();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static double getSavingsBalanceFromLoginResponse() {
        try {
            OkHttpClient client = new OkHttpClient();

            JSONObject requestBody = new JSONObject();
            requestBody.put("key", API_KEY);
            requestBody.put("username", API_USERNAME);
            requestBody.put("password", API_PASSWORD);
            requestBody.put ("device_serial_no", deviceSerialNumber);
            requestBody.put("member_id", API_MEMBERID);

            MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBodyObj = RequestBody.create(JSON_MEDIA_TYPE, requestBody.toString());

            Request request = new Request.Builder()
                    .url(BASE_URL + "savings/membersavings")
                    .post(requestBodyObj)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);

                // Extract the "data" array from the response
                JSONArray data = jsonResponse.getJSONArray("data");

                double totalSavingsBalance = 0.0;

                // Iterate through the data array and sum up the "amount" values
                for (int i = 0; i < data.length(); i++) {
                    JSONObject entry = data.getJSONObject(i);
                    double amount = entry.optDouble("amount", 0.0);
                    totalSavingsBalance += amount;
                }

                // Log the total savings balance
                Log.d("Savings Balance Response", "Total Savings Balance: " + totalSavingsBalance);

                return totalSavingsBalance;
            } else {
                return 0.0; // Return a default value or handle the error as needed
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0; // Return a default value or handle the error as needed
        }
    }


    public static double getLoanBalanceFromLoginResponse(String loginResponse) {
        try {
            if (loginResponse == null) {
                // Handle the case where the response is null
                return 0.0;
            }

            // Log the loginResponse before processing
            Log.d("Login Response", loginResponse);

            JSONObject jsonResponse = new JSONObject(loginResponse);
            String loanBalanceString = jsonResponse.optString("loan_balance", "0.00");

            // Log the loan balance before parsing
            Log.d("Loan Balance", "Raw Loan Balance String: " + loanBalanceString);

            // Parse the loan balance string to a double
            double loanBalance = parseLoanBalance(loanBalanceString);

            // Log the parsed loan balance
            Log.d("Loan Balance", "Parsed Loan Balance: " + loanBalance);

            return loanBalance;
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error or other exceptions
            return 0.0;
        }
    }


    private static double parseLoanBalance(String loanBalanceString) {
        try {
            // Remove commas from the string and parse it to a double
            if (loanBalanceString != null) {
                String sanitizedBalanceString = loanBalanceString.replace(",", "");
                return Double.parseDouble(sanitizedBalanceString);
            } else {
                // Handle the case where the loanBalanceString is null
                return 0.0;
            }
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            // Handle parsing errors
            return 0.0;
        }
    }

    public static void makeApiRequest(final String loan_id, final ApiCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    // Create a JSON object with the request data
                    JSONObject json = new JSONObject();
                    json.put("key", API_KEY);
                    json.put("loan_id", loan_id);
                    json.put("member_id", API_MEMBERID);
                    json.put("username", API_USERNAME);
                    json.put ("device_serial_no", deviceSerialNumber);

                    Log.d("ApiRequestData", json.toString()); // Log the request data

                    // Create the request body
                    MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBodyObj = RequestBody.create(JSON_MEDIA_TYPE, json.toString());

                    Request request = new Request.Builder()
                            .url(BASE_URL + "loans/memberloanrepayment") // Replace with your actual API endpoint
                            .post(requestBodyObj)
                            .addHeader("Authorization", "Bearer " + API_KEY)
                            .addHeader("Content-Type", "application/json")
                            .build();

                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        callback.onApiRequestComplete(responseBody);
                    } else {
                        String errorMessage = "Error: " + response.code() + " - " + response.message();
                        callback.onApiRequestError(errorMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onApiRequestError("Error: " + e.getMessage());
                }
            }
        }).start();
    }


}
