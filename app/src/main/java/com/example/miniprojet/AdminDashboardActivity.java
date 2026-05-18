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
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvStatUsers, tvStatLieux, tvStatRsvs;
    private LinearLayout btnManageLieux, btnManageUsers, btnManageRsvs, btnSendNotif;
    private Button btnSignOut;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();

        tvStatUsers    = findViewById(R.id.tvStatUsers);
        tvStatLieux    = findViewById(R.id.tvStatLieux);
        tvStatRsvs     = findViewById(R.id.tvStatRsvs);
        btnManageLieux = findViewById(R.id.btnManageLieux);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageRsvs  = findViewById(R.id.btnManageRsvs);
        btnSendNotif   = findViewById(R.id.btnSendNotif);
        btnSignOut     = findViewById(R.id.btnSignOut);

        loadStats();

        btnManageLieux.setOnClickListener(v ->
                startActivity(new Intent(this, ManageLieuxActivity.class)));

        btnManageUsers.setOnClickListener(v ->
                startActivity(new Intent(this, ManageUsersActivity.class)));

        btnManageRsvs.setOnClickListener(v ->
                startActivity(new Intent(this, ManageReservationsActivity.class)));

        btnSendNotif.setOnClickListener(v ->
                startActivity(new Intent(this, SendNotificationActivity.class)));

        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
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

    private void loadStats() {
        db.collection("users").get().addOnSuccessListener(snap ->
                tvStatUsers.setText(String.valueOf(snap.size())));

        db.collection("lieux").get().addOnSuccessListener(snap ->
                tvStatLieux.setText(String.valueOf(snap.size())));

        db.collection("reservations").get().addOnSuccessListener(snap ->
                tvStatRsvs.setText(String.valueOf(snap.size())));
    }
}