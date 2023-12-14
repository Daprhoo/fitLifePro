package com.cembora.fitlifepro.fragments;
// ProgressFragment.java

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cembora.fitlifepro.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProgressFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUserProgress(view);
        loadOtherUsersProgress(view);
    }

    private void loadUserProgress(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            mDatabase.child("userProgress").child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int totalDays = (int) dataSnapshot.getChildrenCount();
                            int completedDays = 0;

                            for (DataSnapshot progressSnapshot : dataSnapshot.getChildren()) {
                                Boolean isCompleted = progressSnapshot.getValue(Boolean.class);
                                if (isCompleted != null && isCompleted) {
                                    completedDays++;
                                }
                            }

                            // Yüzdeyi hesapla
                            int percentage = (totalDays > 0) ? (completedDays * 100) / totalDays : 0;

                            // Progress bilgilerini ekrana güncelle
                            updateProgressView(view, percentage);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
        }
    }

    private void loadOtherUsersProgress(View view) {
        mDatabase.child("userProgress")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Diğer kullanıcıların ilerleme tablosunu yükle ve göster
                        Map<String, Integer> userProgressMap = new HashMap<>();

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            int totalDays = (int) userSnapshot.getChildrenCount();
                            int completedDays = 0;

                            for (DataSnapshot progressSnapshot : userSnapshot.getChildren()) {
                                Boolean isCompleted = progressSnapshot.getValue(Boolean.class);
                                if (isCompleted != null && isCompleted) {
                                    completedDays++;
                                }
                            }

                            int percentage = (totalDays > 0) ? (completedDays * 100) / totalDays : 0;
                            userProgressMap.put(userSnapshot.getKey(), percentage);
                        }

                        // Örnek olarak, "textOtherUsersProgress" TextView'ine başka bir kullanıcıya ait ilerleme bilgilerini ekleyebilirsiniz
                        TextView textOtherUsersProgress = view.findViewById(R.id.textOtherUsersProgress);
                        StringBuilder progressText = new StringBuilder("Diğer Kullanıcıların İlerleme Durumu:\n");

                        for (Map.Entry<String, Integer> entry : userProgressMap.entrySet()) {
                            progressText.append(entry.getKey()).append(": ").append(entry.getValue()).append("%\n");
                        }

                        textOtherUsersProgress.setText(progressText.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                        Toast.makeText(getContext(), "Diğer kullanıcıların ilerleme bilgileri yüklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateProgressView(View view, int percentage) {
        CircularProgressIndicator circularProgress = view.findViewById(R.id.circularProgress);
        TextView textProgress = view.findViewById(R.id.textProgress);

        circularProgress.setProgress(percentage);
        textProgress.setText(String.format("%d%% Tamamlandı", percentage));
    }
}
