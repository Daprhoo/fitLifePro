package com.cembora.fitlifepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ImageView settingsImageView = findViewById(R.id.settings);

        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // settings ImageView'e tıklandığında settings ekranına geç
                startActivity(new Intent(SignInActivity.this, SettingsActivity.class));
            }
        });
    }
}