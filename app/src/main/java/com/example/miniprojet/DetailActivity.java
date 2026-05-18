package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private ImageView imgDetailBg;
    private TextView tvDetailNom, tvDetailVille, tvDetailDescription, tvDetailCategorie;
    private Button btnReserver, btnShare;
    private TextView tvBack;
    private LinearLayout layoutProgramme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        imgDetailBg         = findViewById(R.id.imgDetailBg);
        tvDetailNom         = findViewById(R.id.tvDetailNom);
        tvDetailVille       = findViewById(R.id.tvDetailVille);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailCategorie   = findViewById(R.id.tvDetailCategorie);
        btnReserver         = findViewById(R.id.btnReserver);
        btnShare            = findViewById(R.id.btnShare);
        tvBack              = findViewById(R.id.tvBack);
        layoutProgramme     = findViewById(R.id.layoutProgramme);

        Intent intent      = getIntent();
        String nom         = intent.getStringExtra("nom");
        String ville       = intent.getStringExtra("ville");
        String description = intent.getStringExtra("description");
        String imageUrl    = intent.getStringExtra("imageUrl");
        String categorie   = intent.getStringExtra("categorie");

        // Programme passé comme ArrayList<String> "heure|activite"
        ArrayList<String> programmeRaw =
                intent.getStringArrayListExtra("programme");

        tvDetailNom.setText(nom);
        tvDetailVille.setText("📍 " + ville);
        tvDetailDescription.setText(description);
        tvDetailCategorie.setText(categorie);

        // Load image via Glide
        if (imageUrl != null && imageUrl.startsWith("http")) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.beach)
                    .error(R.drawable.beach)
                    .centerCrop()
                    .into(imgDetailBg);
        } else {
            int resId = getResources().getIdentifier(
                    imageUrl, "drawable", getPackageName()
            );
            imgDetailBg.setImageResource(resId != 0 ? resId : R.drawable.beach);
        }

        // Afficher programme du jour
        if (programmeRaw != null && !programmeRaw.isEmpty()) {
            for (String step : programmeRaw) {
                String[] parts = step.split("\\|");
                if (parts.length == 2) {
                    addProgrammeStep(parts[0].trim(), parts[1].trim());
                }
            }
        }

        // Back
        tvBack.setOnClickListener(v -> finish());

        // Book Now — vérifier si logged in
        btnReserver.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                // Logged in → go to ReservationActivity
                Intent i = new Intent(DetailActivity.this, ReservationActivity.class);
                i.putExtra("nom",   nom);
                i.putExtra("ville", ville);
                startActivity(i);
            } else {
                // Not logged in → go to SignIn with message
                Toast.makeText(this,
                        "Please sign in to book a tour 🔐",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DetailActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });

        // Share
        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Visit " + nom + " - Tunisia");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Check out " + nom + " in " + ville + "!\n\n"
                            + description + "\n\nDiscover more on TuniWander 🌍");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Ajouter une étape programme dynamiquement
    private void addProgrammeStep(String heure, String activite) {
        View stepView = LayoutInflater.from(this)
                .inflate(R.layout.item_programme_step, layoutProgramme, false);

        TextView tvHeure    = stepView.findViewById(R.id.tvHeure);
        TextView tvActivite = stepView.findViewById(R.id.tvActivite);

        tvHeure.setText(heure);
        tvActivite.setText(activite);

        layoutProgramme.addView(stepView);
    }
}