package com.example.pefranksacco;

import static com.example.pefranksacco.ApiService.API_KEY;
import static com.example.pefranksacco.ApiService.API_MEMBERID;
import static com.example.pefranksacco.ApiService.API_PASSWORD;
import static com.example.pefranksacco.ApiService.API_USERNAME;
import static com.example.pefranksacco.ApiService.BASE_URL;
import static com.example.pefranksacco.ApiService.deviceSerialNumber;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Tab1Fragment extends Fragment {
    private View fragmentView;
    private RecyclerView recyclerView;
    private SavingsAdapter adapter;
    private List<SavingsItem> savingsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        fragmentView=view;
        View progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.savingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize your savingsList with the data from the API response
        savingsList = createSavingsListFromApiResponse(); // You need to implement this method
        progressBar.setVisibility(View.VISIBLE);
        adapter = new SavingsAdapter(savingsList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Implement a method to populate your savingsList from the API response
    private List<SavingsItem> createSavingsListFromApiResponse() {
        List<SavingsItem> savingsList = new ArrayList<>();

        // Use AsyncTask to perform the network request in the background
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();

                    // Create the request body JSON
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
                        return response.body().string();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String responseBody) {
                View progressBar = fragmentView.findViewById(R.id.progressBar);
                if (progressBar != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                if (responseBody != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        // Get the "data" array which contains the list of savings items
                        JSONArray savingsArray = jsonResponse.optJSONArray("data");

                        if (savingsArray != null) {
                            for (int i = 0; i < savingsArray.length(); i++) {
                                JSONObject savingsObject = savingsArray.getJSONObject(i);

                                // Extract the relevant data for each SavingsItem
                                String documentno = savingsObject.optString("documentno", "");
                                double amount = savingsObject.optDouble("amount", 0.0);
                                String date = savingsObject.optString("date", "");
                                double money_in = savingsObject.optDouble("money_in", 0.0);
                                double money_out = savingsObject.optDouble("money_out", 0.0);
                                String month = savingsObject.optString("month", "");
                                String year = savingsObject.optString("year", "");
                                // Create a SavingsItem object
                                SavingsItem savingsItem = new SavingsItem(documentno,date,money_in,money_out,month,year);

                                // Add the SavingsItem to the list
                                savingsList.add(savingsItem);
                            }
                        }
                        // Update the adapter or UI here after the background task completes
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();

        return savingsList;
    }


}
