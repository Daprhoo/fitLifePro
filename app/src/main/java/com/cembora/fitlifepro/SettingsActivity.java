package com.cembora.fitlifepro;

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
        getResources().getConfiguration().locale = locale;
        getResources().updateConfiguration(getResources().getConfiguration(), getResources().getDisplayMetrics());
        recreate(); // Activity'yi yeniden oluşturarak dil değişikliklerini uygula
    }
}
