package com.cembora.fitlifepro.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cembora.fitlifepro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PurchasesFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Button buttonFitness, buttonNutrition, buttonFull;

    public PurchasesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_purchases, container, false);

        buttonFitness = view.findViewById(R.id.buttonFitness);
        buttonNutrition = view.findViewById(R.id.buttonNutrition);
        buttonFull = view.findViewById(R.id.buttonFull);

        buttonFitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd("Fitness paketi", "Size özel profesyonel fitness eğitmenleriyle görüşün! Kaydolun ve sizi arayalım!");
            }
        });

        buttonNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd("Beslenme paketi", "Diyetisyenlere görüşün! Kaydolun ve sizi arayalım!");
            }
        });

        buttonFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd("Full paket", "Her ikisini de içeren özel paketimizi deneyin! Kaydolun ve sizi arayalım!");
            }
        });

        return view;
    }

    private void showAd(String packageName, String packageContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(packageName)
                .setMessage(packageContent)
                .setPositiveButton("Kaydol", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Kullanıcıya mesaj göster
                        showToast("KAYITLI TELEFON NUMARASINDAN SİZİ ARAYACAĞIZ");
                    }
                })
                .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

}
