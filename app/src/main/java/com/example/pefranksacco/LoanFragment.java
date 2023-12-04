package com.example.pefranksacco;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoanFragment extends Fragment {
    private View fragmentView;
    private RecyclerView recyclerView;
    private LoanAdapter loanAdapter;
    private List<LoanItem> loanItems;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.members_loan, container, false);
        dbHelper = new DatabaseHelper(requireContext());  // Initialize dbHelper here
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize RecyclerView and its adapter
        View progressBar = fragmentView.findViewById(R.id.progressBar);
        recyclerView = fragmentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loanItems = new ArrayList<>();

        // Get the ViewPager2 from your activity or parent fragment
        ViewPager2 viewPager = requireActivity().findViewById(R.id.viewPager);  // Change to your actual ViewPager2 ID

        if (viewPager != null) {
            if (dbHelper == null) {
                // Initialize dbHelper if it's null
                dbHelper = new DatabaseHelper(requireContext());
            }

            loanAdapter = new LoanAdapter(loanItems, viewPager, dbHelper, this, this, progressBar);
            recyclerView.setAdapter(loanAdapter);
        } else {
            // Handle the case where viewPager is null
            Log.e("LoanFragment", "ViewPager2 is null");
        }

        progressBar.setVisibility(View.VISIBLE);

        // Call the AsyncTask to perform the API request
        new GetMemberLoansTask().execute();
    }

    private class GetMemberLoansTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            // Call the method to get member loans (assuming this method performs the network request)
            return ApiService.getMemberLoans();
        }

        @Override
        protected void onPostExecute(String response) {
            View progressBar = fragmentView.findViewById(R.id.progressBar);

            // Check if the fragment is still attached before updating the UI
            if (isAdded()) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("data")) {
                        JSONArray data = jsonResponse.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject loanData = data.getJSONObject(i);
                            String id = "#" + loanData.getString("id");
                            String membersId = "Members ID: " + loanData.getString("memberid");
                            String loanProduct = "Loan Product: " + loanData.getString("saccoloanid");
                            String loanRefNo = "Loan Reference No: " + loanData.getString("loanrefno");
                            String status = "Status: " + loanData.getString("status");
                            String approvedAmount = "Approved Amount: " + loanData.getString("approved_amount");
                            String appliedAmount = "Applied Amount: " + loanData.getString("applied_amount");
                            String appliedon = "Date Applied: " + loanData.getString("appliedon");
                            // Create a LoanItem and add it to the list
                            LoanItem loanItem = new LoanItem(id, membersId, loanProduct, loanRefNo, status, approvedAmount, appliedAmount,appliedon);
                            loanItems.add(loanItem);
                        }

                        if (loanAdapter != null) {
                            // Notify the adapter that the data has changed
                            loanAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("LoanFragment", "loanAdapter is null");
                        }
                    } else {
                        // Handle the case where "data" is an empty array
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "No Loans applied", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing errors or other issues
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
