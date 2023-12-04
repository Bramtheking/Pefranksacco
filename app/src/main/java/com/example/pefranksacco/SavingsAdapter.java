package com.example.pefranksacco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SavingsAdapter extends RecyclerView.Adapter<SavingsAdapter.SavingsViewHolder> {
    private List<SavingsItem> savingsList;

    public SavingsAdapter(List<SavingsItem> savingsList) {
        this.savingsList = savingsList;
    }

    @NonNull
    @Override
    public SavingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.savings_item, parent, false);
        return new SavingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavingsViewHolder holder, int position) {
        SavingsItem item = savingsList.get(position);
        holder.documentnoTextView.setText("Document No: " + item.getDocumentNo());

        holder.moneyInTextView.setText("Money In: " + item.getMoneyIn());
        holder.moneyOutTextView.setText("Money Out: " + item.getMoneyOut());
        holder.monthTextView.setText("Month: " + item.getMonth());
        holder.yearTextView.setText("Year: " + item.getYear());
    }

    @Override
    public int getItemCount() {
        return savingsList.size();
    }

    public class SavingsViewHolder extends RecyclerView.ViewHolder {
        TextView documentnoTextView;

        TextView moneyInTextView;
        TextView moneyOutTextView;
        TextView monthTextView;
        TextView yearTextView;

        public SavingsViewHolder(@NonNull View itemView) {
            super(itemView);
            documentnoTextView = itemView.findViewById(R.id.documentnoTextView);

            moneyInTextView = itemView.findViewById(R.id.moneyInTextView);
            moneyOutTextView = itemView.findViewById(R.id.moneyOutTextView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
        }
    }
}
