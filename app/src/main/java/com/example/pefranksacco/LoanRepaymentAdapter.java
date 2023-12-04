package com.example.pefranksacco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LoanRepaymentAdapter extends RecyclerView.Adapter<LoanRepaymentAdapter.ViewHolder> {

    private List<LoanRepaymentItem> repaymentItems;

    public LoanRepaymentAdapter(List<LoanRepaymentItem> repaymentItems) {
        this.repaymentItems = repaymentItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan_repayment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the ViewHolder
        LoanRepaymentItem item = repaymentItems.get(position);

        // Set the data to your TextViews in item_loan_repayment.xml
        holder.yearTextView.setText(item.getYear());
        holder.documentNoTextView.setText(item.getDocumentNo());
        holder.monthTextView.setText(item.getMonth());
        holder.amountTextView.setText(item.getAmount());
        holder.transactionCodeTextView.setText(item.getTransactionCode());
        holder.paidOnTextView.setText(item.getPaidOn());
    }

    @Override
    public int getItemCount() {
        return repaymentItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView yearTextView, documentNoTextView, monthTextView, amountTextView, transactionCodeTextView, paidOnTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize your TextViews here
            yearTextView = itemView.findViewById(R.id.Year);
            documentNoTextView = itemView.findViewById(R.id.Documentno);
            monthTextView = itemView.findViewById(R.id.LoanRefNo);
            amountTextView = itemView.findViewById(R.id.Amount);
            transactionCodeTextView = itemView.findViewById(R.id.transactioncode);
            paidOnTextView = itemView.findViewById(R.id.paidon);
        }
    }
}
