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

public class GuideProfilActivity extends AppCompatActivity {

    private TextView tvAvatar, tvProfilNom, tvProfilEmail, tvSpecialite;
    private TextView tvRatingValue, tvNbTours, tvNbAvis, tvBack, tvDispo, tvDispoIcon;
    private LinearLayout cardDispo, layoutTours, layoutNotifications;
    private Button btnSignOut;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isDisponible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil_guide);

        mAuth = FirebaseAuth.getInstance();
        db    = FirebaseFirestore.getInstance();

        tvAvatar            = findViewById(R.id.tvAvatar);
        tvProfilNom         = findViewById(R.id.tvProfilNom);
        tvProfilEmail       = findViewById(R.id.tvProfilEmail);
        tvSpecialite        = findViewById(R.id.tvSpecialite);
        tvRatingValue       = findViewById(R.id.tvRatingValue);
        tvNbTours           = findViewById(R.id.tvNbTours);
        tvNbAvis            = findViewById(R.id.tvNbAvis);
        tvBack              = findViewById(R.id.tvBack);
        tvDispo             = findViewById(R.id.tvDispo);
        tvDispoIcon         = findViewById(R.id.tvDispoIcon);
        cardDispo           = findViewById(R.id.cardDispo);
        layoutTours         = findViewById(R.id.layoutTours);
        layoutNotifications = findViewById(R.id.layoutNotifications);
        btnSignOut          = findViewById(R.id.btnSignOut);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) { startActivity(new Intent(this, SignInActivity.class)); finish(); return; }

        tvProfilEmail.setText(user.getEmail());

        // Charger profil guide
        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(doc -> {
                    String name      = doc.getString("name");
                    String specialite = doc.getString("specialite");
                    Double rating    = doc.getDouble("rating");
                    Long nbAvis      = doc.getLong("nbAvis");
                    Boolean dispo    = doc.getBoolean("disponible");

                    tvProfilNom.setText(name != null ? name : "Guide");
                    tvSpecialite.setText("🗺️ " + (specialite != null ? specialite : "Général"));
                    tvRatingValue.setText(String.format(" %.1f (%d avis)",
                            rating != null ? rating : 0.0,
                            nbAvis != null ? nbAvis : 0));
                    tvNbAvis.setText(String.valueOf(nbAvis != null ? nbAvis : 0));

                    if (name != null && !name.isEmpty())
                        tvAvatar.setText(String.valueOf(name.charAt(0)).toUpperCase());

                    isDisponible = dispo != null ? dispo : true;
                    updateDispoUI();
                });

        // Charger ses tours (reservations assignées)
        db.collection("reservations")
                .whereEqualTo("guideId", user.getUid()).get()
                .addOnSuccessListener(snap -> {
                    tvNbTours.setText(String.valueOf(snap.size()));
                    if (snap.isEmpty()) {
                        addEmptyMsg(layoutTours, "No tours assigned yet");
                    } else {
                        for (QueryDocumentSnapshot doc : snap) {
                            addTourCard(doc.getString("destination"),
                                    doc.getString("date"),
                                    doc.getString("name"));
                        }
                    }
                });

        // Notifications
        loadNotifications(layoutNotifications);

        // Toggle disponibilité
        cardDispo.setOnClickListener(v -> {
            isDisponible = !isDisponible;
            updateDispoUI();
            db.collection("users").document(user.getUid())
                    .update("disponible", isDisponible);
        });

        tvBack.setOnClickListener(v -> finish());

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

    private void updateDispoUI() {
        if (isDisponible) {
            tvDispoIcon.setText("✅");
            tvDispo.setText("Dispo");
        } else {
            tvDispoIcon.setText("❌");
            tvDispo.setText("Indispo");
        }
    }

    private void addTourCard(String dest, String date, String client) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(0x22FFFFFF);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, 0, 0, 10);
        card.setLayoutParams(p);
        card.setPadding(24, 16, 24, 16);

        TextView t1 = new TextView(this);
        t1.setText("📍 " + dest);
        t1.setTextColor(0xFFFFFFFF);
        t1.setTextSize(14f);
        t1.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView t2 = new TextView(this);
        t2.setText("🗓️ " + date + "  👤 " + client);
        t2.setTextColor(0xAAFFD700);
        t2.setTextSize(12f);

        card.addView(t1);
        card.addView(t2);
        layoutTours.addView(card);
    }

    private void loadNotifications(LinearLayout layout) {
        db.collection("notifications")
                .whereEqualTo("targetRole", "all").get()
                .addOnSuccessListener(snap -> {
                    if (snap.isEmpty()) {
                        addEmptyMsg(layout, "No notifications");
                    } else {
                        for (QueryDocumentSnapshot doc : snap) {
                            LinearLayout card = new LinearLayout(this);
                            card.setOrientation(LinearLayout.VERTICAL);
                            card.setBackgroundColor(0x22FFFFFF);
                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            p.setMargins(0, 0, 0, 10);
                            card.setLayoutParams(p);
                            card.setPadding(24, 16, 24, 16);
                            TextView tv = new TextView(this);
                            tv.setText("🔔 " + doc.getString("message"));
                            tv.setTextColor(0xFFFFFFFF);
                            tv.setTextSize(13f);
                            card.addView(tv);
                            layout.addView(card);
                        }
                    }
                });
    }

    private void addEmptyMsg(LinearLayout layout, String msg) {
        TextView tv = new TextView(this);
        tv.setText(msg);
        tv.setTextColor(0xAAFFFFFF);
        tv.setTextSize(14f);
        layout.addView(tv);
    }
}