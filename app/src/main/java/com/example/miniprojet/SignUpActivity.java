package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword, etExtraField;
    private TextInputLayout layoutExtraField;
    private Button btnSignUp;
    private TextView tvGoToSignIn;
    private LinearLayout cardTouriste, cardGuide, cardAgence, cardAdmin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Role sélectionné — défaut Touriste
    private String selectedRole = "Touriste";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db    = FirebaseFirestore.getInstance();

        etName            = findViewById(R.id.etName);
        etEmail           = findViewById(R.id.etEmail);
        etPassword        = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etExtraField      = findViewById(R.id.etExtraField);
        layoutExtraField  = findViewById(R.id.layoutExtraField);
        btnSignUp         = findViewById(R.id.btnSignUp);
        tvGoToSignIn      = findViewById(R.id.tvGoToSignIn);
        cardTouriste      = findViewById(R.id.cardTouriste);
        cardGuide         = findViewById(R.id.cardGuide);
        cardAgence        = findViewById(R.id.cardAgence);
        cardAdmin         = findViewById(R.id.cardAdmin);

        // Sélectionner Touriste par défaut
        selectRole("Touriste");

        // Role card clicks
        if (cardTouriste != null) cardTouriste.setOnClickListener(v -> selectRole("Touriste"));
        if (cardGuide != null)    cardGuide.setOnClickListener(v    -> selectRole("Guide"));
        if (cardAgence != null)   cardAgence.setOnClickListener(v   -> selectRole("Agence"));
        if (cardAdmin != null)    cardAdmin.setOnClickListener(v    -> selectRole("Admin"));

        btnSignUp.setOnClickListener(v -> {
            String name    = etName.getText().toString().trim();
            String email   = etEmail.getText().toString().trim();
            String pass    = etPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();
            String extra   = etExtraField.getText().toString().trim();

            // Validation
            if (name.isEmpty())        { etName.setError("Enter your name");           return; }
            if (email.isEmpty())       { etEmail.setError("Enter your email");         return; }
            if (pass.isEmpty())        { etPassword.setError("Enter a password");      return; }
            if (pass.length() < 6)     { etPassword.setError("Min 6 characters");      return; }
            if (!pass.equals(confirm)) { etConfirmPassword.setError("Passwords don't match"); return; }

            // Automatic Admin role for specific email
            if (email.equalsIgnoreCase("admin@gmail.com")) {
                selectedRole = "Admin";
            }

            // Firebase Auth — créer user
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();

                            // Données de base communes
                            Map<String, Object> user = new HashMap<>();
                            user.put("uid", uid);
                            user.put("name",  name);
                            user.put("email", email);
                            user.put("role",  selectedRole);

                            // Données spécifiques selon le role
                            switch (selectedRole) {
                                case "Guide":
                                    user.put("specialite", extra.isEmpty() ? "Général" : extra);
                                    user.put("rating",     0.0);
                                    user.put("nbAvis",     0);
                                    user.put("disponible", true);
                                    break;
                                case "Agence":
                                    user.put("nomAgence",  extra.isEmpty() ? name : extra);
                                    user.put("verified",   false);
                                    break;
                                case "Admin":
                                    // Admin specific data if any
                                    break;
                                default: // Touriste
                                    user.put("nbReservations", 0);
                                    break;
                            }

                            // Sauvegarder dans Firestore
                            db.collection("users").document(uid).set(user)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this,
                                                "Welcome " + name + "! Role: " + selectedRole, Toast.LENGTH_SHORT).show();
                                        redirectByRole();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error saving user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this,
                                    "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        tvGoToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Sélectionner visuellement un role
    private void selectRole(String role) {
        selectedRole = role;

        // Reset tous les cards
        int defaultBg = 0x33FFFFFF;
        if (cardTouriste != null) cardTouriste.setBackgroundColor(defaultBg);
        if (cardGuide != null)    cardGuide.setBackgroundColor(defaultBg);
        if (cardAgence != null)   cardAgence.setBackgroundColor(defaultBg);
        if (cardAdmin != null)    cardAdmin.setBackgroundColor(defaultBg);

        // Highlight le card sélectionné
        int goldBg = 0xCCFFD700;
        switch (role) {
            case "Touriste": if (cardTouriste != null) cardTouriste.setBackgroundColor(goldBg); break;
            case "Guide":    if (cardGuide != null)    cardGuide.setBackgroundColor(goldBg);    break;
            case "Agence":   if (cardAgence != null)   cardAgence.setBackgroundColor(goldBg);   break;
            case "Admin":    if (cardAdmin != null)    cardAdmin.setBackgroundColor(goldBg);    break;
        }

        // Afficher le champ extra selon le role
        if (role.equals("Guide")) {
            layoutExtraField.setVisibility(View.VISIBLE);
            layoutExtraField.setHint("Spécialité (ex: Désert, Histoire...)");
        } else if (role.equals("Agence")) {
            layoutExtraField.setVisibility(View.VISIBLE);
            layoutExtraField.setHint("Nom de l'agence");
        } else {
            layoutExtraField.setVisibility(View.GONE);
        }
    }

    // Rediriger selon le role après connexion
    private void redirectByRole() {
        Intent intent;
        switch (selectedRole) {
            case "Guide":
                intent = new Intent(this, GuideProfilActivity.class);
                break;
            case "Agence":
                intent = new Intent(this, AgenceProfilActivity.class);
                break;
            case "Admin":
                intent = new Intent(this, AdminDashboardActivity.class);
                break;
            default:
                intent = new Intent(this, ListeLieuxActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}