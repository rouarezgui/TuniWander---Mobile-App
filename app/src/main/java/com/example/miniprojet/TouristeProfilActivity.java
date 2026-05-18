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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TouristeProfilActivity extends AppCompatActivity {

    private TextView tvAvatar, tvProfilNom, tvProfilEmail;
    private TextView tvNbRsvs, tvNbAvis, tvBack;
    private Button btnSignOut;
    private RecyclerView rvReservations;
    private LinearLayout layoutNotifications;
    private ReservationAdapter adapter;
    private List<Map<String, Object>> reservationList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tourist_profile);

        mAuth = FirebaseAuth.getInstance();
        db    = FirebaseFirestore.getInstance();

        tvAvatar            = findViewById(R.id.tvAvatar);
        tvProfilNom         = findViewById(R.id.tvProfilNom);
        tvProfilEmail       = findViewById(R.id.tvProfilEmail);
        tvNbRsvs            = findViewById(R.id.tvNbRsvs);
        tvNbAvis            = findViewById(R.id.tvNbAvis);
        tvBack              = findViewById(R.id.tvBack);
        btnSignOut          = findViewById(R.id.btnSignOut);
        rvReservations      = findViewById(R.id.rvReservations);
        layoutNotifications = findViewById(R.id.layoutNotifications);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) { startActivity(new Intent(this, SignInActivity.class)); finish(); return; }

        tvProfilEmail.setText(user.getEmail());

        // Setup RecyclerView
        reservationList = new ArrayList<>();
        adapter = new ReservationAdapter(reservationList, "Touriste");
        rvReservations.setLayoutManager(new LinearLayoutManager(this));
        rvReservations.setAdapter(adapter);

        // Charger profil
        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name = doc.getString("name");
                        tvProfilNom.setText(name != null ? name : "Traveler");
                        if (name != null && !name.isEmpty())
                            tvAvatar.setText(String.valueOf(name.charAt(0)).toUpperCase());
                    }
                });

        // Charger réservations
        loadReservations(user.getUid());

        // Charger notifications filtered by 'All' or 'Touriste'
        loadNotifications();

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

    private void loadReservations(String userId) {
        db.collection("reservations")
                .whereEqualTo("userId", userId).get()
                .addOnSuccessListener(snap -> {
                    tvNbRsvs.setText(String.valueOf(snap.size()));
                    reservationList.clear();
                    for (QueryDocumentSnapshot document : snap) {
                        Map<String, Object> data = document.getData();
                        data.put("id", document.getId());
                        reservationList.add(data);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void loadNotifications() {
        db.collection("notifications")
                .whereIn("targetRole", Arrays.asList("All", "Touriste")).get()
                .addOnSuccessListener(snap -> {
                    layoutNotifications.removeAllViews();
                    if (snap.isEmpty()) {
                        addEmptyMsg(layoutNotifications, "No notifications");
                    } else {
                        for (QueryDocumentSnapshot doc : snap) {
                            addNotifCard(doc.getString("message"), doc.getString("date"));
                        }
                    }
                });
    }

    private void addNotifCard(String message, String date) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(0x22FFFFFF);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 10);
        card.setLayoutParams(params);
        card.setPadding(24, 16, 24, 16);

        TextView tvMsg = new TextView(this);
        tvMsg.setText("🔔 " + message);
        tvMsg.setTextColor(0xFFFFFFFF);
        tvMsg.setTextSize(13f);

        TextView tvDate = new TextView(this);
        tvDate.setText(date != null ? date : "");
        tvDate.setTextColor(0xAAFFD700);
        tvDate.setTextSize(11f);

        card.addView(tvMsg);
        card.addView(tvDate);
        layoutNotifications.addView(card);
    }

    private void addEmptyMsg(LinearLayout layout, String msg) {
        TextView tv = new TextView(this);
        tv.setText(msg);
        tv.setTextColor(0xAAFFFFFF);
        tv.setTextSize(14f);
        layout.addView(tv);
    }
}