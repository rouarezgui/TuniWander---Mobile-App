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
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

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
            String emailInput = etEmail.getText().toString().trim();
            String passInput  = etPassword.getText().toString().trim();

            if (emailInput.isEmpty()) { etEmail.setError("Enter your email");    return; }
            if (passInput.isEmpty())  { etPassword.setError("Enter a password"); return; }

            mAuth.signInWithEmailAndPassword(emailInput, passInput)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                            String uid = mAuth.getCurrentUser().getUid();
                            String emailFromAuth = mAuth.getCurrentUser().getEmail();

                            db.collection("users").document(uid).get()
                                    .addOnSuccessListener(doc -> {
                                        if (doc.exists()) {
                                            String role = doc.getString("role");
                                            Boolean isVerified = doc.getBoolean("isVerified");

                                            boolean isRouaAdmin = isRouaAdminEmail(emailFromAuth);
                                            boolean isAdmin = isRouaAdmin || "Admin".equals(role);
                                            
                                            boolean isVerifiedUser = (isVerified != null && isVerified);

                                            if (isAdmin || isVerifiedUser) {
                                                String finalRole = isAdmin ? "Admin" : (role != null ? role : "Touriste");
                                                Toast.makeText(this, "Welcome back! ✅", Toast.LENGTH_SHORT).show();
                                                if (isRouaAdmin) {
                                                    ensureRouaAdminProfile(uid, emailFromAuth, doc.getString("name"),
                                                            () -> redirectByRole(finalRole));
                                                } else {
                                                    redirectByRole(finalRole);
                                                }
                                            } else {
                                                Toast.makeText(this, "Your account is not verified yet by Admin Roua ⏳", Toast.LENGTH_LONG).show();
                                                mAuth.signOut();
                                            }
                                        } else {
                                            if (isRouaAdminEmail(emailFromAuth)) {
                                                ensureRouaAdminProfile(uid, emailFromAuth, "Roua Rezgui",
                                                        () -> redirectByRole("Admin"));
                                            } else {
                                                Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show();
                                                mAuth.signOut();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error fetching profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                    });
                        } else {
                            Toast.makeText(this, "Error: " + (task.getException() != null ? task.getException().getMessage() : "Authentication failed"), Toast.LENGTH_LONG).show();
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
            case "Guide":  intent = new Intent(this, GuideProfilActivity.class);    break;
            case "Agence": intent = new Intent(this, AgenceProfilActivity.class);   break;
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
            String emailFromAuth = mAuth.getCurrentUser().getEmail();
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            String role = doc.getString("role");
                            Boolean isVerified = doc.getBoolean("isVerified");
                            
                            boolean isRouaAdmin = isRouaAdminEmail(emailFromAuth);
                            boolean isAdmin = isRouaAdmin || "Admin".equals(role);
                            
                            if (isAdmin || (isVerified != null && isVerified)) {
                                String finalRole = isAdmin ? "Admin" : (role != null ? role : "Touriste");
                                if (isRouaAdmin) {
                                    ensureRouaAdminProfile(uid, emailFromAuth, doc.getString("name"),
                                            () -> redirectByRole(finalRole));
                                } else {
                                    redirectByRole(finalRole);
                                }
                            } else {
                                mAuth.signOut();
                            }
                        }
                        else if (isRouaAdminEmail(emailFromAuth)) {
                            ensureRouaAdminProfile(uid, emailFromAuth, "Roua Rezgui",
                                    () -> redirectByRole("Admin"));
                        }
                    });
        }
    }

    private boolean isRouaAdminEmail(String email) {
        return email != null && email.equalsIgnoreCase("roua@gmail.com");
    }

    private void ensureRouaAdminProfile(String uid, String email, String name, Runnable onReady) {
        Map<String, Object> adminData = new HashMap<>();
        adminData.put("uid", uid);
        adminData.put("email", email != null ? email : "roua@gmail.com");
        adminData.put("name", name != null && !name.trim().isEmpty() ? name : "Roua Rezgui");
        adminData.put("role", "Admin");
        adminData.put("isVerified", true);

        db.collection("users").document(uid)
                .set(adminData, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this,
                                "Admin profile sync failed: " +
                                        (task.getException() != null ? task.getException().getMessage() : "unknown error"),
                                Toast.LENGTH_LONG).show();
                    }
                    onReady.run();
                });
    }
}
