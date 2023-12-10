package com.cembora.fitlifepro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText inputEmailSignIn, inputPasswordSignIn;
    private TextView textCreateNewAccount;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        inputEmailSignIn = findViewById(R.id.inputEmail);
        inputPasswordSignIn = findViewById(R.id.inputPassword);
        textCreateNewAccount = findViewById(R.id.textCreateNewAccount);

        textCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmailSignIn.getText().toString().trim();
                String password = inputPasswordSignIn.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    showToast("Lütfen tüm alanları doldurun.");
                } else {
                    signInUser(email, password);
                }
            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            showToast("Giriş başarılı!");
                            // Giriş başarılıysa GoalSelectionActivity'e yönlendir
                            Intent intent = new Intent(SignInActivity.this, GoalSelectionActivity.class);
                            startActivity(intent);
                            finish(); // Bu aktiviteyi kapat
                        } else {
                            showToast("Giriş başarısız. Lütfen tekrar deneyin.");
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
