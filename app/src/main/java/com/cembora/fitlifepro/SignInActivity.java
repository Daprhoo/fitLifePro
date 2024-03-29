package com.cembora.fitlifepro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cembora.fitlifepro.databinding.ActivitySignInBinding;
import com.cembora.fitlifepro.fragments.MenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private EditText inputEmailSignIn, inputPasswordSignIn;
    private TextView textCreateNewAccount;
    private ActivitySignInBinding binding;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();



        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, redirect to GoalSelectionActivity
            startActivity(new Intent(SignInActivity.this, MenuActivity.class));
            finish(); // Close this activity
        }

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
                loading(true);
                String email = inputEmailSignIn.getText().toString().trim();
                String password = inputPasswordSignIn.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    showToast("Lütfen tüm alanları doldurun.");
                    loading(false);
                } else {
                    signInUser(email, password);

                }
            }
        });
    }



    void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            showToast("Giriş başarılı!");
                            // Giriş başarılıysa GoalSelectionActivity'e yönlendir
                            Intent intent = new Intent(SignInActivity.this, MenuActivity.class);
                            loading(false);
                            startActivity(intent);
                            finish(); // Bu aktiviteyi kapat
                        } else {
                            loading(false);
                            showToast("Giriş başarısız. Lütfen tekrar deneyin.");
                        }
                    }
                });
    }

    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void loading(boolean isLoading){
        if (isLoading){
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.buttonSignIn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }

    }


}
