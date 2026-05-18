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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView ForgotPassword, Register;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        db    = FirebaseFirestore.getInstance();

        btnSignIn      = findViewById(R.id.btnSignIn);
        ForgotPassword = findViewById(R.id.ForgotPassword);
        Register       = findViewById(R.id.Register);
        etEmail        = findViewById(R.id.Email);
        etPassword     = findViewById(R.id.Password);

        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass  = etPassword.getText().toString().trim();

            if (email.isEmpty()) { etEmail.setError("Enter your email");    return; }
            if (pass.isEmpty())  { etPassword.setError("Enter a password"); return; }

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            // Lire le role depuis Firestore puis rediriger
                            db.collection("users").document(uid).get()
                                    .addOnSuccessListener(doc -> {
                                        String role = doc.getString("role");
                                        if (role == null) role = "Touriste";
                                        Toast.makeText(this,
                                                "Welcome back! ✅", Toast.LENGTH_SHORT).show();
                                        redirectByRole(role);
                                    });
                        } else {
                            Toast.makeText(this,
                                    "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        ForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));

        Register.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void redirectByRole(String role) {
        Intent intent;
        switch (role) {
            case "Admin":  intent = new Intent(this, AdminDashboardActivity.class); break;
            case "Guide":  intent = new Intent(this, ListeLieuxActivity.class);     break;
            case "Agence": intent = new Intent(this, ListeLieuxActivity.class);     break;
            default:       intent = new Intent(this, ListeLieuxActivity.class);     break;
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(doc -> {
                        String role = doc.getString("role");
                        if (role == null) role = "Touriste";
                        redirectByRole(role);
                    });
        }
    }
}