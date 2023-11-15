package com.cembora.fitlifepro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Switch languageSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        languageSwitch = findViewById(R.id.languageSwitch);

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // Dil seçeneğine göre uygulama dilini değiştir
                if (isChecked) {
                    setAppLanguage("en");
                } else {
                    setAppLanguage("tr");
                }
            }
        });
    }

    private void setAppLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        if (!getCurrentLanguage().equals(languageCode)) {
            getResources().getConfiguration().locale = locale;
            getResources().updateConfiguration(getResources().getConfiguration(), getResources().getDisplayMetrics());
            restartSignInActivity(); // SignInActivity'yi baştan başlat
        }
    }

    private void restartSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Bu aktiviteyi kapat
    }


    private String getCurrentLanguage() {
        return getResources().getConfiguration().locale.getLanguage();
    }

}
