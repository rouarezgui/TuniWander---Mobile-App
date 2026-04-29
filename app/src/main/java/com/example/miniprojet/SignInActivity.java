package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class SignInActivity extends AppCompatActivity {

    private Button btnSignIn;
    private TextView ForgotPassword, Register;
    private TextInputEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        btnSignIn      = findViewById(R.id.btnSignIn);
        ForgotPassword = findViewById(R.id.ForgotPassword);
        Register       = findViewById(R.id.Register);
        etEmail        = findViewById(R.id.Email);
        etPassword     = findViewById(R.id.Password);

        btnSignIn.setOnClickListener(v -> {
            String email    = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                return;
            }
            if (password.isEmpty()) {
                etPassword.setError("Please enter your password");
                return;
            }

            // Firebase later → direct navigation for now
            Toast.makeText(this, "Welcome back! ✅", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignInActivity.this, ListeLieuxActivity.class);
            startActivity(intent);
            finish();
        });

        ForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        Register.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}