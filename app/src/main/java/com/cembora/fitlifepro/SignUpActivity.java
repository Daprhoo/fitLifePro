package com.cembora.fitlifepro;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmailSignUp, inputPasswordSignUp, inputConfirmationPasswordSignUp;
    private Button buttonSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        inputEmailSignUp = findViewById(R.id.inputEmailSignUp);
        inputPasswordSignUp = findViewById(R.id.inputPasswordSignUp);
        inputConfirmationPasswordSignUp = findViewById(R.id.inputConfirmationPasswordSignUp);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kullanıcının girdiği bilgileri al
                String email = inputEmailSignUp.getText().toString().trim();
                String password = inputPasswordSignUp.getText().toString().trim();
                String confirmPassword = inputConfirmationPasswordSignUp.getText().toString().trim();

                // Gerekli alanları kontrol et
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    showToast("Lütfen tüm alanları doldurun.");
                } else if (!password.equals(confirmPassword)) {
                    showToast("Şifreler uyuşmuyor.");
                } else {
                    // Firebase Authentication kullanarak kayıt işlemini gerçekleştir
                    registerUser(email, password);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Başarılı kayıt durumu
                            showToast("Kayıt işlemi başarılı!");
                            finish(); // Kayıt işlemi tamamlandığında SignUpActivity'yi kapat
                        } else {
                            // Başarısız kayıt durumu
                            showToast("Kayıt işlemi başarısız. Lütfen tekrar deneyin.");
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
