package com.example.pefranksacco.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pefranksacco.databinding.FragmentReportsBinding; // Correct binding import
import com.example.pefranksacco.ui.reports.reportsViewModel;

public class reportsFragment extends Fragment {

    private FragmentReportsBinding binding; // Correct binding class

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel reportsViewModel =
                new ViewModelProvider(this).get(reportsViewModel.class);

        binding = FragmentReportsBinding.inflate(inflater, container, false); // Correct binding inflation
        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}