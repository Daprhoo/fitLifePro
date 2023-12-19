// FitnessFragment.java
package com.cembora.fitlifepro.fragments;

import android.os.Bundle;
import android.text.TextUtils;
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
    private TextView textFitnessday1Exersices;
    private TextView textFitnessday2Exersices;
    private TextView textFitnessday3Exersices;

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


        textFitnessday1Exersices = view.findViewById(R.id.textFitnessday1Exersices);
        textFitnessday2Exersices = view.findViewById(R.id.textFitnessday2Exersices);
        textFitnessday3Exersices = view.findViewById(R.id.textFitnessday3Exersices);

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
                            loadFitnessPlan(selectedGoal.toLowerCase());
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

    private void loadFitnessPlan(String selectedGoal) {
        DatabaseReference fitnessPlanRef = mDatabase.child("fitnessPlans");

        fitnessPlanRef.child("plan1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fitnessContentday1 = dataSnapshot.child("day1").child("egzersizler").getValue(String.class);
                    setExercises(textFitnessday1Exersices, fitnessContentday1);
                    displayDayProgram("Day 1", fitnessContentday1);

                    String fitnessContentday2 = dataSnapshot.child("day2").child("egzersizler").getValue(String.class);
                    setExercises(textFitnessday2Exersices, fitnessContentday2);
                    displayDayProgram("Day 2", fitnessContentday2);

                    String fitnessContentday3 = dataSnapshot.child("day3").child("egzersizler").getValue(String.class);
                    setExercises(textFitnessday3Exersices, fitnessContentday3);
                    displayDayProgram("Day 3", fitnessContentday3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        fitnessPlanRef.child("plan2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fitnessContentday1 = dataSnapshot.child("day1").child("egzersizler").getValue(String.class);
                    setExercises(textFitnessday1Exersices, fitnessContentday1);

                    String fitnessContentday2 = dataSnapshot.child("day2").child("egzersizler").getValue(String.class);
                    setExercises(textFitnessday2Exersices, fitnessContentday2);

                    String fitnessContentday3 = dataSnapshot.child("day3").child("egzersizler").getValue(String.class);
                    setExercises(textFitnessday3Exersices, fitnessContentday3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        fitnessPlanRef.child("plan3").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fitnessContentday1 = dataSnapshot.child("day1").child("egzersizler").getValue(String.class);
                    setExercises(textFitnessday1Exersices, fitnessContentday1);

                    String fitnessContentday2 = dataSnapshot.child("day2").child("egzersizler").getValue(String.class);
                    setExercises(textFitnessday2Exersices, fitnessContentday2);

                    String fitnessContentday3 = dataSnapshot.child("day3").child("egzersizler").getValue(String.class);
                    setExercises(textFitnessday3Exersices, fitnessContentday3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void displayDayProgram(String day, String exercises) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dayProgramView = inflater.inflate(R.layout.item_fitness_day_program, null);

        TextView textDay = dayProgramView.findViewById(R.id.textDay);
        textDay.setText(day);

        LinearLayout layoutExercises = dayProgramView.findViewById(R.id.layoutExercises);

        // For TextView
        TextView textExercises = new TextView(getContext());
        textExercises.setId(View.generateViewId());
        setExercises(textExercises, exercises);
        layoutExercises.addView(textExercises);

        // For CheckBoxes
        addCheckBoxes(layoutExercises, exercises);

        layoutFitnessProgram.addView(dayProgramView);
    }

    private void setExercises(TextView textView, String exercises) {
        String[] exerciseArray = exercises.split(",");
        StringBuilder formattedExercises = new StringBuilder();

        for (String exercise : exerciseArray) {
            formattedExercises.append(exercise.trim()).append("\n");
        }

        textView.setText(formattedExercises.toString().trim());
    }

    private void addCheckBoxes(LinearLayout layout, String exercises) {
        String[] exerciseArray = exercises.split(",");

        for (String exercise : exerciseArray) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(exercise.trim());
            layout.addView(checkBox);
            checkBoxList.add(checkBox);
        }
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
