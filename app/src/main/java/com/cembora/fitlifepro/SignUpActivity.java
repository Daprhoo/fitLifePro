package com.cembora.fitlifepro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cembora.fitlifepro.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputUserName,inputEmailSignUp, inputPasswordSignUp, inputConfirmationPasswordSignUp;
    private Button buttonSignUp;
    private ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();


        inputUserName = findViewById(R.id.userName);
        inputEmailSignUp = findViewById(R.id.inputEmailSignUp);
        inputPasswordSignUp = findViewById(R.id.inputPasswordSignUp);
        inputConfirmationPasswordSignUp = findViewById(R.id.inputConfirmationPasswordSignUp);
        buttonSignUp = findViewById(R.id.buttonSignUp);


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(true);
                String email = inputEmailSignUp.getText().toString().trim();
                String password = inputPasswordSignUp.getText().toString().trim();
                String confirmPassword = inputConfirmationPasswordSignUp.getText().toString().trim();
                String userName  = inputUserName.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    showToast("Lütfen tüm alanları doldurun.");
                    loading(false);
                } else if (!password.equals(confirmPassword)) {
                    showToast("Şifreler uyuşmuyor.");
                    loading(false);
                } else {
                    // İşlemi bir iş parçacığında çalıştır
                    runInBackground(email, password, userName);
                }
            }
        });

    }
    private void runInBackground(final String email, final String password, final String userName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Uzun süren işlemleri burada gerçekleştir
                registerUser(email, password,userName);
            }
        }).start();
    }

    private void registerUser(String email, String password, String userName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Başarılı kayıt durumu
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                showToast("signed in successfully");
                                                Intent intent = new Intent(getApplicationContext(), GoalSelectionActivity.class);
                                                intent.putExtra("userName",userName);
                                                loading(false);
                                                startActivity(intent);
                                                finish(); // Close the current activity
                                            } else {
                                                showToast("something gone wrong");
                                            }
                                        }
                                    });
                            showToast("Kayıt işlemi başarılı!");
                        } else {
                            // Başarısız kayıt durumu
                            loading(false);
                            showToast("Kayıt işlemi başarısız. Lütfen tekrar deneyin.");
                        }
                    }
                });
    }
    private void loading(boolean isLoading){
        if (isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.buttonSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
