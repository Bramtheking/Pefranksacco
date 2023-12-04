package com.example.pefranksacco.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pefranksacco.R;
import com.example.pefranksacco.Tab1Fragment;
import com.example.pefranksacco.Tab2Fragment;
import com.example.pefranksacco.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GalleryFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Set up the ViewPager2 and TabLayout
        ViewPager2 viewPager = root.findViewById(R.id.viewPager1);
        TabLayout tabLayout = root.findViewById(R.id.tabLayout11);

        // Create a TabPagerAdapter
        TabPagerAdapter adapter = new TabPagerAdapter(this);

        // Add your tab fragments here
        adapter.addFragment(new Tab1Fragment());
        adapter.addFragment(new Tab2Fragment());

        viewPager.setAdapter(adapter);

        // Connect the TabLayout with the ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set the tab titles here
            if (position == 0) {
                tab.setText("SAVINGS");
            } else if (position == 1) {
                tab.setText("ADD SAVINGS");
            }
        }).attach();

        return root;
    }
}
