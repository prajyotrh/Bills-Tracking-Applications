package com.example.billtrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailET,passwordET;
    private Button login;
    private TextView forgotPass;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        login.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //progressBar.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
    }

    public void init() {
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.cirLoginButton);
        forgotPass = findViewById(R.id.forgotPassword);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_circular);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cirLoginButton :
                loginUser();
                break;
            case  R.id.forgotPassword:
                startActivity(new Intent(this,ForgotPassword.class));
                break;
        }
    }

    private void loginUser() {

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if(email.isEmpty()) {
            emailET.setError("Email is required");
            emailET.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            passwordET.setError("Password is required");
            passwordET.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Provide valid email");
            emailET.requestFocus();
            return;
        }

        if(password.length()<6) {
            passwordET.setError("Minimum password length should be 6 character.");
            passwordET.requestFocus();
            return;
        }

       // progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()) {
                                Intent intent = new Intent(MainActivity.this , Dashboard.class);
                                startActivity(intent);
                            }else {
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Eamil link has been sent.Check your email to varify your account!", Toast.LENGTH_LONG).show();
                                //progressBar.setVisibility(View.GONE);
                            }
                        }else {
                            Toast.makeText(MainActivity.this,"Login is failed! Try again!",Toast.LENGTH_LONG).show();
                           // progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

}