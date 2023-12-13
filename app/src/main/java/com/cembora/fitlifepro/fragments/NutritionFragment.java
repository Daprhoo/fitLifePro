package com.cembora.fitlifepro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cembora.fitlifepro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NutritionFragment extends Fragment {

    private DatabaseReference mDatabase;
    private LinearLayout layoutNutritionPlan;

    public NutritionFragment() {
        // Gerekli public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        layoutNutritionPlan = view.findViewById(R.id.layoutNutritionPlan);

        // Kullanıcıdan seçilen hedefi al ve beslenme planını yükle
        getSelectedGoal();

        return view;
    }

    private void getSelectedGoal() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userRef = mDatabase.child("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String selectedGoal = dataSnapshot.child("selectedGoal").getValue(String.class);
                        if (selectedGoal != null && !selectedGoal.isEmpty()) {
                            // Hedef bilgisini kullanarak beslenme planını yükle
                            loadNutritionPlan(selectedGoal.toLowerCase());
                        } else {
                            showToast("Hedef bilgisi alınamadı.");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    private void loadNutritionPlan(String selectedGoal) {
        DatabaseReference nutritionPlanRef = mDatabase.child("nutritionPlans").child(selectedGoal);

        nutritionPlanRef.child("plan1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String mealContent = dataSnapshot.getValue(String.class);
                    displayMealPlan("Akşam Yemeği", mealContent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        nutritionPlanRef.child("plan2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String mealContent = dataSnapshot.getValue(String.class);
                    displayMealPlan("Kahvaltı", mealContent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        nutritionPlanRef.child("plan3").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String mealContent = dataSnapshot.getValue(String.class);
                    displayMealPlan("Öğle Yemeği", mealContent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void displayMealPlan(String mealType, String mealContent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View mealPlanView = inflater.inflate(R.layout.item_nutrition_plan, null);

        TextView textMealType = mealPlanView.findViewById(R.id.textMealType);
        TextView textMealContent = mealPlanView.findViewById(R.id.textMealContent);

        textMealType.setText(mealType);
        textMealContent.setText(mealContent);

        layoutNutritionPlan.addView(mealPlanView);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
