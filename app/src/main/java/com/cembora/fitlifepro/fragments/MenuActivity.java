package com.cembora.fitlifepro.fragments;

// MenuActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cembora.fitlifepro.R;
import com.cembora.fitlifepro.SignInActivity;
import com.cembora.fitlifepro.fragments.FitnessFragment;
import com.cembora.fitlifepro.fragments.MeasurementFragment;
import com.cembora.fitlifepro.fragments.NutritionFragment;
import com.cembora.fitlifepro.fragments.ProgressFragment;
import com.cembora.fitlifepro.fragments.PurchasesFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    FirebaseAuth mAuth;
    private ImageView signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        signOut = findViewById(R.id.signOut);
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
        setListeners();
    }

    private void setListeners(){
        signOut.setOnClickListener(v->onClickedSignOut());
    }

    private void onClickedSignOut(){
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

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
