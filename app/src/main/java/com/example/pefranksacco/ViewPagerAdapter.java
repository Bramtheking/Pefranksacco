package com.example.pefranksacco;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList();

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager.getPrimaryNavigationFragment());
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LoanFragment();
            case 1:
                return new LoanApplicationFragment();
            case 2:
                return new PayLoanFragment();
            case 3:
                return new LoanRepaymentFragment();
            default:
                return new LoanFragment();
        }
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public void addFragment(PayLoanFragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
}
