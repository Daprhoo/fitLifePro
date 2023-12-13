package com.cembora.fitlifepro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cembora.fitlifepro.fragments.MenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class GoalSelectionActivity extends AppCompatActivity {

    private RadioGroup radioGroupGoals;
    private Button buttonSelectGoal;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_selection);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        radioGroupGoals = findViewById(R.id.radioGroupGoals);
        buttonSelectGoal = findViewById(R.id.buttonSelectGoal);

        buttonSelectGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleContinueButtonClick();
            }
        });
    }

    private void handleContinueButtonClick() {
        int selectedId = radioGroupGoals.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        if (radioButton != null) {
            String selectedGoal = radioButton.getText().toString();

            // Firebase veritabanına seçilen hedefi ve diğer bilgileri kaydet
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("selectedGoal", selectedGoal);

                mDatabase.child("users").child(userId).updateChildren(userMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showToast("Hedef kaydedildi.");

                                    startActivity(new Intent(GoalSelectionActivity.this, MenuActivity.class));
                                } else {
                                    showToast("Hedef kaydedilemedi. Lütfen tekrar deneyin.");
                                }
                            }
                        });
            } else {
                showToast("Lütfen bir hedef seçin.");
            }
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
