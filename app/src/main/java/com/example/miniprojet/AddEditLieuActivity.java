package com.example.miniprojet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddEditLieuActivity extends AppCompatActivity {

    private TextInputEditText etNom, etVille, etCategorie, etDescription;
    private Button btnSave, btnCancel, btnSelectImage;
    private ImageView imgPreview;
    private ProgressBar progressBar;
    private TextView tvTitle, tvSelectGuideLabel;
    private Spinner spinnerGuides;
    
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private String lieuId = null;
    private Uri imageUri = null;
    private String existingImageUrl = null;
    private String currentRole = "Touriste";
    private List<String> guideNames = new ArrayList<>();
    private List<String> guideIds = new ArrayList<>();

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imgPreview.setImageURI(imageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_lieu);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        tvTitle = findViewById(R.id.tvTitle);
        etNom = findViewById(R.id.etNom);
        etVille = findViewById(R.id.etVille);
        etCategorie = findViewById(R.id.etCategorie);
        etDescription = findViewById(R.id.etDescription);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgPreview = findViewById(R.id.imgPreview);
        progressBar = findViewById(R.id.progressBar);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        
        tvSelectGuideLabel = findViewById(R.id.tvSelectGuideLabel);
        spinnerGuides = findViewById(R.id.spinnerGuides);

        checkUserRoleAndSetupUI();

        // Check if editing
        if (getIntent().hasExtra("id")) {
            lieuId = getIntent().getStringExtra("id");
            tvTitle.setText("Edit Destination");
            etNom.setText(getIntent().getStringExtra("nom"));
            etVille.setText(getIntent().getStringExtra("ville"));
            etCategorie.setText(getIntent().getStringExtra("categorie"));
            etDescription.setText(getIntent().getStringExtra("description"));
            existingImageUrl = getIntent().getStringExtra("imageUrl");
            
            if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
                Glide.with(this).load(existingImageUrl).placeholder(R.drawable.beach).into(imgPreview);
            }
        }

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImageAndSave();
            } else if (lieuId != null && existingImageUrl != null) {
                saveLieu(existingImageUrl);
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserRoleAndSetupUI() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        db.collection("users").document(uid).get().addOnSuccessListener(doc -> {
            currentRole = doc.getString("role");
            if ("Agence".equals(currentRole)) {
                tvSelectGuideLabel.setVisibility(View.VISIBLE);
                spinnerGuides.setVisibility(View.VISIBLE);
                loadAgencyGuides(uid);
            }
        });
    }

    private void loadAgencyGuides(String agencyId) {
        db.collection("users")
                .whereEqualTo("role", "Guide")
                .whereEqualTo("agenceId", agencyId)
                .get()
                .addOnSuccessListener(snap -> {
                    guideNames.add("No Guide Assigned");
                    guideIds.add("");
                    for (QueryDocumentSnapshot doc : snap) {
                        guideNames.add(doc.getString("name"));
                        guideIds.add(doc.getId());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, guideNames);
                    spinnerGuides.setAdapter(adapter);
                });
    }

    private void uploadImageAndSave() {
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        String fileName = UUID.randomUUID().toString();
        StorageReference ref = storage.getReference().child("lieux_images/" + fileName);

        ref.putFile(imageUri)
            .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                saveLieu(uri.toString());
            }))
            .addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private void saveLieu(String imageUrl) {
        String nom = etNom.getText().toString().trim();
        String ville = etVille.getText().toString().trim();
        String cat = etCategorie.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();

        if (nom.isEmpty() || ville.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btnSave.setEnabled(true);
            return;
        }

        String currentUid = FirebaseAuth.getInstance().getUid();
        Map<String, Object> lieu = new HashMap<>();
        lieu.put("nom", nom);
        lieu.put("ville", ville);
        lieu.put("categorie", cat);
        lieu.put("description", desc);
        lieu.put("imageUrl", imageUrl);
        
        if ("Agence".equals(currentRole)) {
            lieu.put("agenceId", currentUid);
            int selectedPos = spinnerGuides.getSelectedItemPosition();
            if (selectedPos > 0) {
                lieu.put("guideId", guideIds.get(selectedPos));
            }
        }

        if (lieuId == null) {
            lieu.put("rating", 0.0);
            lieu.put("nbAvis", 0);
            lieu.put("programme", new ArrayList<>());
        }

        if (lieuId != null) {
            db.collection("lieux").document(lieuId).update(lieu)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });
        } else {
            db.collection("lieux").add(lieu)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });
        }
    }
}