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

    // Form fields
    private TextInputEditText etName, etPhone, etDate, etPersons, etNotes;
    private Button btnConfirm;
    private TextView tvDestination, tvBack;

    // Firebase
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservation);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Link views
        etName        = findViewById(R.id.etRsvName);
        etPhone       = findViewById(R.id.etRsvPhone);
        etDate        = findViewById(R.id.etRsvDate);
        etPersons     = findViewById(R.id.etRsvPersons);
        etNotes       = findViewById(R.id.etRsvNotes);
        btnConfirm    = findViewById(R.id.btnConfirmReservation);
        tvDestination = findViewById(R.id.tvDestination);
        tvBack        = findViewById(R.id.tvBack);

        // Get destination info from DetailActivity
        String nom   = getIntent().getStringExtra("nom");
        String ville = getIntent().getStringExtra("ville");
        tvDestination.setText("📍 " + nom + ", " + ville);

        // Back → close activity
        tvBack.setOnClickListener(v -> finish());

        // Confirm reservation
        btnConfirm.setOnClickListener(v -> {

            String name    = etName.getText().toString().trim();
            String phone   = etPhone.getText().toString().trim();
            String date    = etDate.getText().toString().trim();
            String persons = etPersons.getText().toString().trim();
            String notes   = etNotes.getText().toString().trim();

            // Validation
            if (name.isEmpty())    { etName.setError("Please enter your name");           return; }
            if (phone.isEmpty())   { etPhone.setError("Please enter your phone");         return; }
            if (date.isEmpty())    { etDate.setError("Please enter a date");              return; }
            if (persons.isEmpty()) { etPersons.setError("Please enter number of persons"); return; }

            // Build reservation map
            Map<String, Object> reservation = new HashMap<>();
            reservation.put("name",        name);
            reservation.put("phone",       phone);
            reservation.put("date",        date);
            reservation.put("persons",     persons);
            reservation.put("notes",       notes);
            reservation.put("destination", nom);
            reservation.put("ville",       ville);
            reservation.put("userId",
                    FirebaseAuth.getInstance().getCurrentUser() != null
                            ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                            : "guest"
            );

            // Save to Firestore collection "reservations"
            db.collection("reservations")
                    .add(reservation)
                    .addOnSuccessListener(ref -> {
                        Toast.makeText(this,
                                "Reservation confirmed! We'll contact you soon.",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ReservationActivity.this, ListeLieuxActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show()
                    );
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}