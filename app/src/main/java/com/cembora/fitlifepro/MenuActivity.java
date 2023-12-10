package com.cembora.fitlifepro;

// MenuActivity.java

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        TextView textViewEmail = headerView.findViewById(R.id.textViewEmail);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();
            textViewEmail.setText(email);
        }
        // Başlangıçta ilk fragment'ı yükle
        loadFragment(new ProgressFragment());

        // Menü elemanlarına tıklama dinleyicilerini ekle
        setupNavigationView();
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(item -> {
            // Seçilen menü elemanına göre fragment'ı yükle
            switch (item.getItemId()) {
                case R.id.navProgress:
                    loadFragment(new ProgressFragment());
                    break;
                case R.id.navNutrition:
                    loadFragment(new NutritionFragment());
                    break;
                case R.id.navFitness:
                    loadFragment(new FitnessFragment());
                    break;
                case R.id.navMeasurement:
                    loadFragment(new MeasurementFragment());
                    break;
                case R.id.navPurchases:
                    loadFragment(new PurchasesFragment());
                    break;
            }

            // Menüyü kapat
            drawerLayout.closeDrawers();

            return true;
        });

        // Toolbar'da menü butonuna tıklama dinleyicisi ekle
        TextView toolbarMenu = findViewById(R.id.toolbarMenu);
        if (toolbarMenu != null) {
            toolbarMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Menüyü aç/kapat
                    if (drawerLayout.isDrawerOpen(navigationView)) {
                        drawerLayout.closeDrawers();
                    } else {
                        drawerLayout.openDrawer(navigationView);
                    }
                }
            });
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
