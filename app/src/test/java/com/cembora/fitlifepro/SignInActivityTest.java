package com.cembora.fitlifepro;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Executor;

public class SignInActivityTest {

    @Mock
    private FirebaseAuth mockAuth;

    @Mock
    private Task<AuthResult> mockTask;

    @Mock
    private FirebaseUser mockUser;

    @InjectMocks
    private SignInActivity signInActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void signInUser_Successful() {
        when(mockAuth.signInWithEmailAndPassword(any(), any())).thenReturn(mockTask);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);

        signInActivity.signInUser("muhammed.buga@hotmail.com", "muhammed");

        // Giriş işlemi başarılı olmalı ve kullanıcıya yönlendirilmeli
        verify(mockAuth).signInWithEmailAndPassword("muhammed.buga@hotmail.com", "muhammed");
        verify(mockTask).addOnCompleteListener((Executor) any(), any(OnCompleteListener.class));
        verify(signInActivity).showToast("Giriş başarılı!");
        verify(signInActivity).startActivity(any(Intent.class));
        verify(signInActivity).finish();
    }

    @Test
    public void signInUser_Failure() {
        when(mockAuth.signInWithEmailAndPassword(any(), any())).thenReturn(mockTask);
        when(mockTask.isSuccessful()).thenReturn(false);

        signInActivity.signInUser("test@example.com", "password");

        // Giriş işlemi başarısız olmalı ve hata mesajı gösterilmeli
        verify(mockAuth).signInWithEmailAndPassword("test@example.com", "password");
        verify(mockTask).addOnCompleteListener((Executor) any(), any(OnCompleteListener.class));
        verify(signInActivity).showToast("Giriş başarısız. Lütfen tekrar deneyin.");
    }
}
