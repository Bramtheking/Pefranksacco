package com.example.pefranksacco;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {
    private List<LoanItem> items;
    private ViewPager2 viewPager;
    private DatabaseHelper databaseHelper;
    private Fragment fragment;
    private View fragmentProgressBar;

    public LoanAdapter(List<LoanItem> items, ViewPager2 viewPager, DatabaseHelper dbHelper, Fragment fragment, LoanFragment loanFragment, View progressBar) {
        this.items = items;
        this.viewPager = viewPager;
        this.databaseHelper = dbHelper;
        this.fragment = fragment;
        this.fragmentProgressBar = progressBar;  // Initialize the ProgressBar field
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoanItem item = items.get(position);

        // Bind data to the views in the item layout
        holder.idTextView.setText(item.getId());

        String loanProduct = item.getLoanProduct();
        String status = item.getStatus();


        // Apply text changes based on the loan product
        if ("Loan Product: 1".equals(loanProduct)) {
            holder.loanProductTextView.setText("Development Loans");
        } else if ("Loan Product: 2".equals(loanProduct)) {
            holder.loanProductTextView.setText("Emergency Loans");
        } else if ("Loan Product: 3".equals(loanProduct)) {
            holder.loanProductTextView.setText("Product Loans");
        } else if ("Loan Product: 4".equals(loanProduct)) {
            holder.loanProductTextView.setText("Advance Loan");
        } else {
            // Handle other cases if needed
            holder.loanProductTextView.setText(loanProduct);
        }

        // Apply text color changes based on status
        if ("Status: Disbursed".equals(status)) {
            holder.statusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_blue_light));
        } else if ("Status: Completed".equals(status)) {
            holder.statusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
        }
        holder.statusTextView.setText(item.getStatus());
        holder.loanRefNoTextView.setText(item.getLoanRefNo());
        holder.approvedAmountTextView.setText(item.getApprovedAmount());
        holder.appliedAmountTextView.setText(item.getAppliedAmount());
        holder.appliedonTextview.setText(item.getappliedon());

        // Set an onClickListener for the loanButton
        holder.loanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager == null) {
                    Log.e("LoanAdapter", "ViewPager is null when trying to navigate");
                    return;  // Avoid proceeding with null viewPager
                }
                Log.d("LoanAdapter", "ViewPager: " + viewPager);
                // Get the loan ID and other required information
                String loanProduct = item.getLoanProduct();

                // Change the value of loan_id based on the loanProduct
                String loan_id = ""; // Initialize loan_id with an empty string

                if ("Loan Product: 1".equals(loanProduct)) {
                    loan_id = "1";
                } else if ("Loan Product: 2".equals(loanProduct)) {
                    loan_id = "2";
                } else if ("Loan Product: 3".equals(loanProduct)) {
                    loan_id = "3";
                } else if ("Loan Product: 4".equals(loanProduct)) {
                    loan_id = "4";
                } else {
                    // Handle other cases if needed
                    // You can keep loan_id as an empty string or set a default value
                }

                fragmentProgressBar.setVisibility(View.VISIBLE);
                // Make the API request

                String finalLoan_id = item.getId();
                ApiService.makeApiRequestAsync(loan_id, new ApiService.ApiCallback() {
                    @Override
                    public void onApiRequestComplete(String response) {
                        Log.d("ApiRequest", "APIs Response: " + response);
                        // Handle API response
                        saveLoanData(finalLoan_id,response);

                        fragmentProgressBar.setVisibility(View.INVISIBLE);
                        // Use post method to navigate to another fragment on the main thread
                        viewPager.post(new Runnable() {
                            @Override
                            public void run() {
                                if (viewPager == null) {
                                    Log.e("LoanAdapter", "ViewPager is null when trying to navigate");
                                    return;  // Avoid proceeding with null viewPager

                                }
                                Log.d("LoanAdapter", "ViewPager: " + viewPager);
                                Bundle bundle = new Bundle();
                                bundle.putString("response", response);
                                fragment.setArguments(bundle);

                                if (viewPager != null) {
                                    try {
                                        viewPager.setCurrentItem(3, true);
                                    } catch (Exception e) {
                                        Log.e("LoanAdapter", "Exception during navigation: " + e.getMessage());
                                    }


                                }

                            }
                        });
                    }

                    @Override
                    public void onApiRequestError(String error) {
                        fragmentProgressBar.setVisibility(View.INVISIBLE);
                        // Handle API error
                    }
                });
            }
        });
    }


        private void saveLoanData(String number, String apiResponse) {
            LoanData loanData = new LoanData();
            loanData.setNumber(number);
            loanData.setApiResponse(apiResponse);

            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NUMBER, number);
            values.put(DatabaseHelper.COLUMN_API_RESPONSE, apiResponse);

            long newRowId = db.insert(DatabaseHelper.LOAN_DATA_TABLE_NAME, null, values);
            db.close();

            // Log the data saved
            Log.d("LoanAdapter", "Saved API response with newRowId: " + newRowId);
        }



        @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView;
        TextView loanProductTextView;
        TextView loanRefNoTextView;
        TextView statusTextView;
        TextView approvedAmountTextView;
        TextView appliedAmountTextView;
        TextView appliedonTextview;
        Button loanButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize and find the views in the item layout
            idTextView = itemView.findViewById(R.id.transactioncode);
            loanProductTextView = itemView.findViewById(R.id.Documentno);
            loanRefNoTextView = itemView.findViewById(R.id.LoanRefNo);
            statusTextView = itemView.findViewById(R.id.Year);
            approvedAmountTextView = itemView.findViewById(R.id.paidon);
            appliedAmountTextView = itemView.findViewById(R.id.Amount);
            loanButton = itemView.findViewById(R.id.loanr);
            appliedonTextview=itemView.findViewById(R.id.appliedon);
        }
    }
}