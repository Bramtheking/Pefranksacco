package com.example.pefranksacco.ui.home;

import static com.example.pefranksacco.ApiService.API_EMAIL;
import static com.example.pefranksacco.ApiService.API_PASSWORD;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pefranksacco.ApiService;
import com.example.pefranksacco.R;
import com.example.pefranksacco.UserCredential;
import com.example.pefranksacco.UserCredentialDAO;
import com.example.pefranksacco.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private String loginResponse;
    private double loanBalance;
    private UserCredentialDAO userCredentialDAO;
    private ApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Initialize the ViewModel
        View progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        apiService = new ApiService();
        userCredentialDAO = new UserCredentialDAO(requireContext());
        userCredentialDAO.open();
        if (!isNetworkAvailable()) {
            Toast.makeText(requireContext(), "App cannot work without internet connection", Toast.LENGTH_LONG).show();
            // You may want to handle this case and update UI accordingly
            return root; // Return without further initialization
        }

        // Get the saved email and password from MainActivity
        UserCredential savedUserCredential = userCredentialDAO.getSavedUserCredential();
        String email = "";
        String password = "";

        if (savedUserCredential != null) {
            email = savedUserCredential.getEmail();
            password = savedUserCredential.getPassword();
        }

        // Close the UserCredentialDAO before making network requests
        userCredentialDAO.close();

        if (!isNetworkAvailable()) {
            Toast.makeText(requireContext(), "App cannot work without internet connection", Toast.LENGTH_LONG).show();
            // You may want to handle this case and update UI accordingly
            progressBar.setVisibility(View.GONE); // Hide the progress bar
            return root; // Return without making network requests
        }
        if (API_EMAIL.isEmpty() && API_PASSWORD.isEmpty() && email.isEmpty() && password.isEmpty()) {
            Toast.makeText(requireContext(), "You are required to log in again", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            return root; // Return without making network requests
        }

        // Call the loginUser method from ApiService
        apiService.loginUserAsync(email.isEmpty() ? API_EMAIL : email,
                password.isEmpty() ? API_PASSWORD : password, new ApiService.LoginCallback() {
                    @Override
                    public void onLoginComplete(String response) {
                        // Handle the login response here
                        // For example, update the UI based on the response
                        // Update the UI on the main thread using a Handler
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                // Here you can call the ViewModel functions to update balances
                                homeViewModel.updateSavingsBalance();
                                homeViewModel.updateLoanBalance(response);
                            }
                        });
                    }
                });


        // Get the TextViews from the binding
        TextView savingsBalanceTextView = binding.textView216;
        TextView loanBalanceTextView = binding.textView26;

        // Observe LiveData for savings balance
        homeViewModel.getSavingsBalance().observe(getViewLifecycleOwner(), savingsBalance -> {
            // Update the TextView with the savings balance
            String savingsBalanceText = "KSH " + String.valueOf(savingsBalance);
            savingsBalanceTextView.setText(savingsBalanceText);
            // Set color for "KSH" text
            savingsBalanceTextView.setTextColor(getResources().getColor(R.color.ksh_color));
        });

        // Observe LiveData for loan balance
        homeViewModel.getLoanBalance().observe(getViewLifecycleOwner(), loanBalance -> {
            // Update the TextView with the loan balance
            String loanBalanceText = "KSH " + String.valueOf(loanBalance);
            loanBalanceTextView.setText(loanBalanceText);
            // Set color for "KSH" text
            loanBalanceTextView.setTextColor(getResources().getColor(R.color.ksh_color));
            this.loanBalance = loanBalance;
        });

        // Get the buttons from the binding
        Button savingsButton = binding.button;
        Button loanButton = binding.button1;

        // Set click listeners for the buttons
        savingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the SavingsFragment when the Savings button is clicked
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.nav_savings); // Replace with the actual ID of your SavingsFragment
            }
        });

        loanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the LoanFragment when the Loan button is clicked
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.nav_loan); // Replace with the actual ID of your LoanFragment
            }
        });

        return root;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                // For older versions of Android
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }

        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public double getLoanBalance() {
        return loanBalance;
    }
}
