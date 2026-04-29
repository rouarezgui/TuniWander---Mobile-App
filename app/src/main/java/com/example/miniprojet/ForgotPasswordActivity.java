package com.example.miniprojet;

import android.annotation.SuppressLint;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etEmailReset;
    private Button btnResetPassword;
    private TextView tvBackToLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        etEmailReset     = findViewById(R.id.etEmailReset);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin    = findViewById(R.id.tvBackToLogin);

        btnResetPassword.setOnClickListener(v -> {
            String email = etEmailReset.getText().toString().trim();

            if (email.isEmpty()) {
                etEmailReset.setError("Please enter your email");
                return;
            }

            // Firebase later → show success for now
            Toast.makeText(this,
                    "Reset link sent! Check your email ✅",
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });

        tvBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
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