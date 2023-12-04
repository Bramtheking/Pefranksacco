package com.example.pefranksacco;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pefranksacco.databinding.ActivityWelcomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;

public class welcome extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityWelcomeBinding binding;
    private NavigationView navigationView;
    private DrawerLayout drawer; // Declare DrawerLayout
    private UserCredentialDAO userCredentialDAO;
    // Create

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String stringBuildModel = "Model: " + Build.MODEL +
                "\nManufacturer: " + Build.MANUFACTURER +
                "\nDevice: " + Build.DEVICE+ Build.SERIAL;
        Log.d("DeviceInfo", stringBuildModel);

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarWelcome.toolbar);
        binding.appBarWelcome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = binding.drawerLayout; // Initialize the DrawerLayout
        navigationView = binding.navView;
        userCredentialDAO = new UserCredentialDAO(this);
        userCredentialDAO.open();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_savings, R.id.nav_loan, R.id.Reports, R.id.info, R.id.nav_Settings)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_welcome);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // Always call the loginUser method
        // You can display a login screen or handle the login process here

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (!isNetworkAvailable()) {
                    Toast.makeText(welcome.this, "No internet access", Toast.LENGTH_SHORT).show();
                }

                // Check which item was selected and perform navigation accordingly
                if (id == R.id.nav_home) {
                    // Navigate to the HomeFragment even if it's the current destination
                    navController.navigate(R.id.nav_home);
                    drawer.closeDrawer(GravityCompat.START); // Close the drawer
                } else {
                    // For other items, let NavigationUI handle navigation
                    boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                    if (handled) {
                        drawer.closeDrawer(GravityCompat.START); // Close the drawer
                    }
                }

                return true;
            }
        });
        logBuildInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

    private void logBuildInformation() {
        Field[] fields = Build.class.getFields();

        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            try {
                String fieldName = field.getName();
                Object value = field.get(null);

                stringBuilder.append("Build.")
                        .append(fieldName)
                        .append(" = ")
                        .append(value)
                        .append("\n");

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String result = stringBuilder.toString();
        Log.d("BuildInformation", result);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_Settings) {
            // Replace with the ID of the fragment you want to navigate to
            int destinationId = R.id.nav_Settings;

            // Navigate to the desired fragment
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_welcome);
            navController.navigate(destinationId);
            return true;
        } else if (id == R.id.action_logout) {
            // Delete saved email and password
            clearLoginDetails();

            // Navigate to the MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_welcome);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void clearLoginDetails() {
        userCredentialDAO.clearLoginDetails();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }

        return false;
    }
}
