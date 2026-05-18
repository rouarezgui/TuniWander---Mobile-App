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

import java.util.HashMap;
import java.util.Map;

public class ReservationActivity extends AppCompatActivity {

    private TextInputEditText etName, etPhone, etDate, etPersons, etNotes;
    private Button btnConfirm;
    private TextView tvDestination, tvBack, tvTitle;
    private FirebaseFirestore db;
    
    private String reservationId = null;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservation);

        db = FirebaseFirestore.getInstance();

        tvTitle       = findViewById(R.id.tvTitle);
        etName        = findViewById(R.id.etRsvName);
        etPhone       = findViewById(R.id.etRsvPhone);
        etDate        = findViewById(R.id.etRsvDate);
        etPersons     = findViewById(R.id.etRsvPersons);
        etNotes       = findViewById(R.id.etRsvNotes);
        btnConfirm    = findViewById(R.id.btnConfirmReservation);
        tvDestination = findViewById(R.id.tvDestination);
        tvBack        = findViewById(R.id.tvBack);

        String nom      = getIntent().getStringExtra("nom");
        String ville    = getIntent().getStringExtra("ville");
        String agenceId = getIntent().getStringExtra("agenceId");
        
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        reservationId = getIntent().getStringExtra("id");

        if (isEdit) {
            tvTitle.setText("Update Reservation");
            btnConfirm.setText("Update Booking");
            etName.setText(getIntent().getStringExtra("name"));
            etPhone.setText(getIntent().getStringExtra("phone"));
            etDate.setText(getIntent().getStringExtra("date"));
            etPersons.setText(getIntent().getStringExtra("persons"));
            etNotes.setText(getIntent().getStringExtra("notes"));
        }

        tvDestination.setText("📍 " + nom + ", " + ville);

        tvBack.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> {
            String currentUid = FirebaseAuth.getInstance().getUid();
            
            db.collection("users").document(currentUid).get().addOnSuccessListener(doc -> {
                String role = doc.getString("role");
                if ("Guide".equals(role)) {
                    Toast.makeText(this, "Guides are not allowed to book tours ❌", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name    = etName.getText().toString().trim();
                String phone   = etPhone.getText().toString().trim();
                String date    = etDate.getText().toString().trim();
                String persons = etPersons.getText().toString().trim();
                String notes   = etNotes.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || date.isEmpty() || persons.isEmpty()) {
                    Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> reservation = new HashMap<>();
                reservation.put("name",        name);
                reservation.put("phone",       phone);
                reservation.put("date",        date);
                reservation.put("persons",     persons);
                reservation.put("notes",       notes);
                reservation.put("destination", nom);
                reservation.put("ville",       ville);
                reservation.put("agenceId",    agenceId != null ? agenceId : "");
                reservation.put("userId",      currentUid);

                if (isEdit) {
                    db.collection("reservations").document(reservationId).update(reservation)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Reservation updated! ✅", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                } else {
                    reservation.put("status", "Pending");
                    db.collection("reservations").add(reservation)
                            .addOnSuccessListener(ref -> {
                                Toast.makeText(this, "Reservation confirmed! ✅", Toast.LENGTH_LONG).show();
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}