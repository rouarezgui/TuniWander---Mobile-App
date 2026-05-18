package com.example.miniprojet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddEditLieuActivity extends AppCompatActivity {

    private TextInputEditText etNom, etVille, etCategorie, etDescription, etImageUrl;
    private Button btnSave, btnCancel;
    private TextView tvTitle;
    private FirebaseFirestore db;
    private String lieuId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_lieu);

        db = FirebaseFirestore.getInstance();

        tvTitle = findViewById(R.id.tvTitle);
        etNom = findViewById(R.id.etNom);
        etVille = findViewById(R.id.etVille);
        etCategorie = findViewById(R.id.etCategorie);
        etDescription = findViewById(R.id.etDescription);
        etImageUrl = findViewById(R.id.etImageUrl);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Check if editing
        if (getIntent().hasExtra("id")) {
            lieuId = getIntent().getStringExtra("id");
            tvTitle.setText("Edit Destination");
            etNom.setText(getIntent().getStringExtra("nom"));
            etVille.setText(getIntent().getStringExtra("ville"));
            etCategorie.setText(getIntent().getStringExtra("categorie"));
            etDescription.setText(getIntent().getStringExtra("description"));
            etImageUrl.setText(getIntent().getStringExtra("imageUrl"));
        }

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveLieu());
    }

    private void saveLieu() {
        String nom = etNom.getText().toString().trim();
        String ville = etVille.getText().toString().trim();
        String cat = etCategorie.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String img = etImageUrl.getText().toString().trim();

        if (nom.isEmpty() || ville.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> lieu = new HashMap<>();
        lieu.put("nom", nom);
        lieu.put("ville", ville);
        lieu.put("categorie", cat);
        lieu.put("description", desc);
        lieu.put("imageUrl", img);
        
        // Add default values for new destinations
        if (lieuId == null) {
            lieu.put("rating", 0.0);
            lieu.put("nbAvis", 0);
            lieu.put("programme", new ArrayList<>());
        }

        if (lieuId != null) {
            // Update
            db.collection("lieux").document(lieuId).update(lieu)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });
        } else {
            // Create
            db.collection("lieux").add(lieu)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });
        }
    }
}