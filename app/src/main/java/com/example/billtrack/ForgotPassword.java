package com.example.billtrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText emailET;
    private Button resetBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        init();

        resetBtn.setOnClickListener(this);
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.editTextEmail);
        resetBtn = findViewById(R.id.forgotPasswordBtn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgotPasswordBtn:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        String email = emailET.getText().toString().trim();

        if(email.isEmpty()) {
            emailET.setError("Email is required");
            emailET.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Provide valid email");
            emailET.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this,"Check your email to reset your password!",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ForgotPassword.this,"Try again! Something wrong happened!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}