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

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvGoToSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Link to XML
        etName            = findViewById(R.id.etName);
        etEmail           = findViewById(R.id.etEmail);
        etPassword        = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp         = findViewById(R.id.btnSignUp);
        tvGoToSignIn      = findViewById(R.id.tvGoToSignIn);

        // Create Account button
        btnSignUp.setOnClickListener(v -> {

            String name     = etName.getText().toString().trim();
            String email    = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm  = etConfirmPassword.getText().toString().trim();

            // Validation
            if (name.isEmpty()) {
                etName.setError("Please enter your name");
                return;
            }
            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                return;
            }
            if (password.isEmpty()) {
                etPassword.setError("Please enter a password");
                return;
            }
            if (password.length() < 6) {
                etPassword.setError("Min 6 characters");
                return;
            }
            if (!password.equals(confirm)) {
                etConfirmPassword.setError("Passwords do not match");
                return;
            }

            // For now → direct navigation (Firebase later)
            Toast.makeText(this, "Account created! ✅", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, ListeLieuxActivity.class);
            startActivity(intent);
            finish();
        });

        // Go to Sign In
        tvGoToSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}