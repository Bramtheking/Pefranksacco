package com.example.pefranksacco;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoanRepaymentFragment extends Fragment {
    private String apiResponse = "";
    private String savedApiResponse = ""; // Keep track of the previously saved API response
    private TextView textView17;  // Make sure to define this in your XML layout

    private RecyclerView recyclerView;
    private LoanRepaymentAdapter adapter;
    private List<LoanRepaymentItem> repaymentItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_repayment, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        textView17 = view.findViewById(R.id.textView17);  // Replace with the actual ID
        repaymentItems = new ArrayList<>();

        // Set up the RecyclerView and its adapter
        adapter = new LoanRepaymentAdapter(repaymentItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        // Retrieve the saved API response and number from the database
        String[] savedLoanData = getSavedLoanData();
        if (savedLoanData != null) {
            String savedNumber = savedLoanData[0];
            String savedApiResponse = savedLoanData[1];

            // Display savedNumber in textView17
            textView17.setText(savedNumber);

            // If the savedApiResponse is different from the newly loaded data, proceed with loading
            if (savedApiResponse != null && !savedApiResponse.equals(apiResponse)) {
                // API response has changed, proceed with parsing and displaying data
                apiResponse = savedApiResponse; // Update the apiResponse to avoid unnecessary reloading

                Log.d("LoanRepaymentFragment", "api_Response: " + apiResponse);

                try {
                    JSONObject jsonResponse = new JSONObject(apiResponse);

                    // Access the "data" array
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    // Clear the existing data in the list before adding new items
                    repaymentItems.clear();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);

                        // Extract data from JSON and create LoanRepaymentItem
                        String year = "year: " + dataObject.getString("year");
                        String documentNo = "Document No " + dataObject.getString("documentno");
                        String month = "Month: " + dataObject.getString("month");
                        String amount = "Amount: " + dataObject.getString("amount");
                        String transactionCode = "Transaction Code" + dataObject.getString("transactioncode");
                        String paidOn = "Paid On: " + dataObject.getString("paidon");

                        LoanRepaymentItem repaymentItem = new LoanRepaymentItem(year, documentNo, month, amount, transactionCode, paidOn);
                        repaymentItems.add(repaymentItem);
                    }

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                    Log.e("LoanRepaymentFragment", "Error parsing JSON: " + e.getMessage());
                }
            } else {
                // Data has not changed, no need to reload
                Log.d("LoanRepaymentFragment", "Data has not changed. No need to reload.");
            }
        } else {
            // savedLoanData is null, display an empty activity
            // You can customize this part based on your requirements
            Log.d("LoanRepaymentFragment", "savedLoanData is null. Displaying an empty activity.");
        }
    }

    // Retrieve the saved API response from the database
    private String[] getSavedLoanData() {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns to retrieve
        String[] columns = {DatabaseHelper.COLUMN_NUMBER, DatabaseHelper.COLUMN_API_RESPONSE};

        // Define the sort order to get the highest row ID (latest data) first
        String sortOrder = DatabaseHelper.COLUMN_LOAN_ID + " DESC"; // DESC for descending order

        try (Cursor cursor = db.query(
                DatabaseHelper.LOAN_DATA_TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder,
                "1" // Limit to 1 row, which will be the latest data
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                String number = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER));
                String apiResponse = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_API_RESPONSE));
                return new String[]{number, apiResponse};
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that might occur during database query
            Log.e("LoanRepaymentFragment", "Error retrieving saved API response: " + e.getMessage());
        } finally {
            db.close();
        }

        return null;
    }
}
