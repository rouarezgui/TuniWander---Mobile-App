package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AgenceProfilActivity extends AppCompatActivity {

    private TextView tvNomAgence, tvProfilEmail, tvVerified, tvNbLieux, tvNbGuides, tvBack;
    private Button btnAddLieu, btnMyLieux, btnSignOut;
    private LinearLayout layoutNotifications;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil_agence);

        mAuth = FirebaseAuth.getInstance();
        db    = FirebaseFirestore.getInstance();

        tvNomAgence         = findViewById(R.id.tvNomAgence);
        tvProfilEmail       = findViewById(R.id.tvProfilEmail);
        tvVerified          = findViewById(R.id.tvVerified);
        tvNbLieux           = findViewById(R.id.tvNbLieux);
        tvNbGuides          = findViewById(R.id.tvNbGuides);
        tvBack              = findViewById(R.id.tvBack);
        btnAddLieu          = findViewById(R.id.btnAddLieu);
        btnMyLieux          = findViewById(R.id.btnMyLieux);
        btnSignOut          = findViewById(R.id.btnSignOut);
        layoutNotifications = findViewById(R.id.layoutNotifications);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) { startActivity(new Intent(this, SignInActivity.class)); finish(); return; }

        tvProfilEmail.setText(user.getEmail());

        // Charger profil agence
        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(doc -> {
                    String nomAgence = doc.getString("nomAgence");
                    Boolean verified = doc.getBoolean("verified");

                    tvNomAgence.setText(nomAgence != null ? nomAgence : "My Agency");

                    if (Boolean.TRUE.equals(verified)) {
                        tvVerified.setText("✅ Verified Agency");
                        tvVerified.setBackgroundColor(0x33FFD700);
                    } else {
                        tvVerified.setText("⏳ Pending Verification");
                    }
                });

        // Count lieux de cette agence
        db.collection("lieux")
                .whereEqualTo("agenceId", user.getUid()).get()
                .addOnSuccessListener(snap -> tvNbLieux.setText(String.valueOf(snap.size())));

        // Count guides affiliés
        db.collection("users")
                .whereEqualTo("role", "Guide")
                .whereEqualTo("agenceId", user.getUid()).get()
                .addOnSuccessListener(snap -> tvNbGuides.setText(String.valueOf(snap.size())));

        // Notifications
        db.collection("notifications")
                .whereEqualTo("targetRole", "all").get()
                .addOnSuccessListener(snap -> {
                    if (snap.isEmpty()) {
                        TextView tv = new TextView(this);
                        tv.setText("No notifications");
                        tv.setTextColor(0xAAFFFFFF);
                        tv.setTextSize(14f);
                        layoutNotifications.addView(tv);
                    } else {
                        for (QueryDocumentSnapshot doc : snap) {
                            TextView tv = new TextView(this);
                            tv.setText("🔔 " + doc.getString("message"));
                            tv.setTextColor(0xFFFFFFFF);
                            tv.setTextSize(13f);
                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            p.setMargins(0, 0, 0, 8);
                            tv.setLayoutParams(p);
                            layoutNotifications.addView(tv);
                        }
                    }
                });

        tvBack.setOnClickListener(v -> finish());

        btnAddLieu.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditLieuActivity.class)));

        btnMyLieux.setOnClickListener(v ->
                startActivity(new Intent(this, ListeLieuxActivity.class)));

        btnSignOut.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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