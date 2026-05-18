package com.example.miniprojet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RatingActivity extends AppCompatActivity {

    private TextView tvDestination, tvLabel, tvCancel;
    private TextView star1, star2, star3, star4, star5;
    private Button btnSubmit;
    private int selectedRating = 0;
    private FirebaseFirestore db;

    private final String[] labels = {"", "😞 Poor", "😐 Fair", "🙂 Good", "😊 Very Good", "🤩 Excellent!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rating);

        db = FirebaseFirestore.getInstance();

        String rsvId       = getIntent().getStringExtra("rsvId");
        String destination = getIntent().getStringExtra("destination");

        tvDestination = findViewById(R.id.tvRatingDestination);
        tvLabel       = findViewById(R.id.tvRatingLabel);
        btnSubmit     = findViewById(R.id.btnSubmitRating);
        tvCancel      = findViewById(R.id.tvCancel);
        star1         = findViewById(R.id.star1);
        star2         = findViewById(R.id.star2);
        star3         = findViewById(R.id.star3);
        star4         = findViewById(R.id.star4);
        star5         = findViewById(R.id.star5);

        tvDestination.setText("📍 " + destination);

        // Star clicks
        TextView[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            final int rating = i + 1;
            stars[i].setOnClickListener(v -> setRating(rating, stars));
        }

        btnSubmit.setOnClickListener(v -> {
            if (selectedRating == 0) {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save rating to Firestore
            String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                    ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "guest";

            java.util.Map<String, Object> ratingData = new java.util.HashMap<>();
            ratingData.put("userId",      uid);
            ratingData.put("rsvId",       rsvId);
            ratingData.put("destination", destination);
            ratingData.put("rating",      selectedRating);
            ratingData.put("date",        new java.util.Date().toString());

            db.collection("ratings").add(ratingData)
                    .addOnSuccessListener(ref -> {
                        // Update average rating for the lieu
                        updateLieuRating(destination, selectedRating);
                        Toast.makeText(this,
                                "Thank you for your rating! " + labels[selectedRating],
                                Toast.LENGTH_LONG).show();
                        finish();
                    });
        });

        tvCancel.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setRating(int rating, TextView[] stars) {
        selectedRating = rating;
        for (int i = 0; i < stars.length; i++) {
            stars[i].setText(i < rating ? "⭐" : "☆");
        }
        tvLabel.setText(labels[rating]);
    }

    private void updateLieuRating(String destination, int newRating) {
        // Find lieu by nom and update average rating
        db.collection("lieux")
                .whereEqualTo("nom", destination).get()
                .addOnSuccessListener(snap -> {
                    if (!snap.isEmpty()) {
                        var doc = snap.getDocuments().get(0);
                        Double currentRating = doc.getDouble("rating");
                        Long   nbAvis        = doc.getLong("nbAvis");

                        double cr = currentRating != null ? currentRating : 0.0;
                        long   na = nbAvis != null ? nbAvis : 0L;

                        // Calcul nouvelle moyenne
                        double newAvg = ((cr * na) + newRating) / (na + 1);

                        doc.getReference().update(
                                "rating", Math.round(newAvg * 10.0) / 10.0,
                                "nbAvis", na + 1
                        );
                    }
                });
    }
}