// FitnessFragment.java
package com.cembora.fitlifepro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FitnessFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private LinearLayout layoutFitnessProgram;
    private Button buttonSaveProgress;

    private List<CheckBox> checkBoxList;

    public FitnessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        checkBoxList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fitness, container, false);

        layoutFitnessProgram = view.findViewById(R.id.layoutFitnessProgram);
        buttonSaveProgress = view.findViewById(R.id.buttonSaveProgress);

        buttonSaveProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProgress();
            }
        });

        loadFitnessProgram();

        return view;
    }

    private void loadFitnessProgram() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Belirli bir kullanıcının düzeyine ulaş
            mDatabase.child("1wpHR3bCm6onNvv5NIfuAzq0wlfyhE_4QY13KWq0NA-A").child("Sayfa1")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot planSnapshot : dataSnapshot.getChildren()) {
                                    String planKey = planSnapshot.getKey();
                                    for (DataSnapshot daySnapshot : planSnapshot.getChildren()) {
                                        String day = daySnapshot.child("day").getValue(String.class);
                                        String exercises = daySnapshot.child("exercises").getValue(String.class);

                                        // Kontrol et: exercises null veya boş mu?
                                        if (exercises != null && !exercises.isEmpty()) {
                                            List<String> exerciseList = Arrays.asList(exercises.split(", "));
                                            displayDayProgram(day, exerciseList);
                                        }
                                    }
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



    private void displayDayProgram(String day, List<String> exercises) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dayProgramView = inflater.inflate(R.layout.item_fitness_day_program, null);

        TextView textDay = dayProgramView.findViewById(R.id.textDay);
        textDay.setText(day);

        LinearLayout layoutExercises = dayProgramView.findViewById(R.id.layoutExercises);

        for (String exercise : exercises) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(exercise);
            layoutExercises.addView(checkBox);
            checkBoxList.add(checkBox);
        }

        layoutFitnessProgram.addView(dayProgramView);
    }






    private void saveProgress() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            for (int i = 0; i < checkBoxList.size(); i++) {
                CheckBox checkBox = checkBoxList.get(i);
                boolean isCompleted = checkBox.isChecked();

                mDatabase.child("userProgress").child(userId).child("day" + (i + 1))
                        .setValue(isCompleted);
            }

            showToast("Progress saved.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
