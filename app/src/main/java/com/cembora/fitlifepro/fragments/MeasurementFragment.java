package com.cembora.fitlifepro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MeasurementFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText editHeight, editWeight;
    private Button buttonSaveMeasurement;
    private LinearLayout layoutMeasurementHistory;

    public MeasurementFragment() {
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
        View view = inflater.inflate(R.layout.fragment_measurement, container, false);

        editHeight = view.findViewById(R.id.editHeight);
        editWeight = view.findViewById(R.id.editWeight);
        buttonSaveMeasurement = view.findViewById(R.id.buttonSaveMeasurement);
        layoutMeasurementHistory = view.findViewById(R.id.layoutMeasurementHistory);

        buttonSaveMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeasurement();
            }
        });

        loadMeasurementHistory();

        return view;
    }

    //...

    private void saveMeasurement() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String height = editHeight.getText().toString();
            String weight = editWeight.getText().toString();

            if (!height.isEmpty() && !weight.isEmpty()) {
                double heightValue = Double.parseDouble(height);
                double weightValue = Double.parseDouble(weight);

                double bmi = calculateBMI(heightValue, weightValue);

                // Ölçümleri Firebase veritabanına kaydet
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String measurementId = "measurement_" + timestamp;

                DatabaseReference measurementRef = mDatabase.child("userMeasurements").child(userId).child(measurementId);
                measurementRef.child("height").setValue(heightValue);
                measurementRef.child("weight").setValue(weightValue);
                measurementRef.child("bmi").setValue(bmi);
                measurementRef.child("timestamp").setValue(timestamp); // Tarih bilgisini ekleyelim

                showToast("Ölçümler kaydedildi.");

                // Ölçüm geçmişini tekrar yükle
                loadMeasurementHistory();
            } else {
                showToast("Lütfen boş alan bırakmayın.");
            }
        }
    }

    private void loadMeasurementHistory() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            mDatabase.child("userMeasurements").child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            layoutMeasurementHistory.removeAllViews();

                            for (DataSnapshot measurementSnapshot : dataSnapshot.getChildren()) {
                                String height = measurementSnapshot.child("height").getValue(Double.class).toString();
                                String weight = measurementSnapshot.child("weight").getValue(Double.class).toString();
                                String bmi = measurementSnapshot.child("bmi").getValue(Double.class).toString();
                                String timestamp = measurementSnapshot.child("timestamp").getValue(String.class); // Tarih bilgisini alalım

                                displayMeasurement(height, weight, bmi, timestamp);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
        }
    }

    private void displayMeasurement(String height, String weight, String bmi, String timestamp) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View measurementView = inflater.inflate(R.layout.item_measurement, null);

        TextView textMeasurementDate = measurementView.findViewById(R.id.textMeasurementDate);
        TextView textHeightWeight = measurementView.findViewById(R.id.textHeightWeight);

        // Ölçüm tarihini kullanalım
        textMeasurementDate.setText("Ölçüm Tarihi: " + timestamp);

        // Boy, kilo ve BMI bilgilerini ekleyelim
        textHeightWeight.setText("Boy: " + height + " cm, Kilo: " + weight + " kg, BMI: " + bmi);

        layoutMeasurementHistory.addView(measurementView);
    }

//...


    private double calculateBMI(double height, double weight) {
        return weight / ((height / 100) * (height / 100));
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
