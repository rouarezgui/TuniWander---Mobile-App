package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class ProfilActivity extends AppCompatActivity {

    private TextView tvAvatar, tvProfilNom, tvProfilEmail, tvRole, tvBack;
    private Button btnSignOut;
    private LinearLayout layoutReservations;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil_guide);

        mAuth = FirebaseAuth.getInstance();
        db    = FirebaseFirestore.getInstance();

        tvAvatar           = findViewById(R.id.tvAvatar);
        tvProfilNom        = findViewById(R.id.tvProfilNom);
        tvProfilEmail      = findViewById(R.id.tvProfilEmail);
        tvRole             = findViewById(R.id.tvRole);
        tvBack             = findViewById(R.id.tvBack);
        btnSignOut         = findViewById(R.id.btnSignOut);
        layoutReservations = findViewById(R.id.layoutReservations);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            // Pas connecté → redirect SignIn
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        // Afficher email Firebase
        tvProfilEmail.setText(user.getEmail());

        // Charger profil depuis Firestore
        db.collection("users").document(user.getUid())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name = doc.getString("name");
                        String role = doc.getString("role");

                        tvProfilNom.setText(name != null ? name : "Traveler");
                        tvRole.setText(role != null ? role : "Touriste");

                        // Avatar = première lettre du nom
                        if (name != null && !name.isEmpty()) {
                            tvAvatar.setText(String.valueOf(name.charAt(0)).toUpperCase());
                        } else {
                            tvAvatar.setText("T");
                        }
                    }
                });

        // Charger ses réservations
        db.collection("reservations")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.isEmpty()) {
                        addEmptyReservations();
                    } else {
                        for (QueryDocumentSnapshot doc : snapshot) {
                            String destination = doc.getString("destination");
                            String date        = doc.getString("date");
                            String persons     = doc.getString("persons");
                            addReservationCard(destination, date, persons);
                        }
                    }
                })
                .addOnFailureListener(e -> addEmptyReservations());

        tvBack.setOnClickListener(v -> finish());

        btnSignOut.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
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

    private void addReservationCard(String destination, String date, String persons) {
        View card = LayoutInflater.from(this)
                .inflate(R.layout.item_reservation_card, layoutReservations, false);

        TextView tvDest    = card.findViewById(R.id.tvRsvDestination);
        TextView tvDate    = card.findViewById(R.id.tvRsvDate);
        TextView tvPersons = card.findViewById(R.id.tvRsvPersons);

        tvDest.setText("📍 " + (destination != null ? destination : "—"));
        tvDate.setText("🗓️ " + (date != null ? date : "—"));
        tvPersons.setText("👥 " + (persons != null ? persons : "—") + " persons");

        layoutReservations.addView(card);
    }

    private void addEmptyReservations() {
        TextView tv = new TextView(this);
        tv.setText("No reservations yet — go explore!");
        tv.setTextColor(0xAAFFFFFF);
        tv.setTextSize(14f);
        layoutReservations.addView(tv);
    }
}