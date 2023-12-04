package com.example.pefranksacco.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pefranksacco.LoanApplicationFragment;
import com.example.pefranksacco.LoanFragment;
import com.example.pefranksacco.LoanRepaymentFragment;
import com.example.pefranksacco.PayLoanFragment;
import com.example.pefranksacco.R;
import com.example.pefranksacco.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SlideshowFragment extends Fragment  {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;
    private LoanRepaymentFragment loanRepaymentFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);


        // Initialize views
        tabLayout = view.findViewById(R.id.tabLayout1);
        viewPager = view.findViewById(R.id.viewPager);

        // Initialize the ViewPagerAdapter
        adapter = new ViewPagerAdapter(getParentFragmentManager());

        // Add fragments to the adapter
        adapter.addFragment(new LoanFragment(), "Applied Loans");
        adapter.addFragment(new LoanApplicationFragment(), "Loan Application");
        adapter.addFragment(new PayLoanFragment(), "Pay Loan");
        adapter.addFragment(new LoanRepaymentFragment(), "Loan Repayment");
        // Set the adapter to the ViewPager2
        viewPager.setAdapter(adapter);

        // Use TabLayoutMediator to connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set the tab's text based on the position
            switch (position) {
                case 0:
                    tab.setText("Applied Loans");
                    break;
                case 1:
                    tab.setText("Loan Application");
                    break;
                case 2:
                    tab.setText("Pay Loan");
                    break;
                case 3:
                    tab.setText("Loan Repayment");
                    break;
            }
        }).attach();

        return view;
    }


    }



