package com.example.miniprojet;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListeLieuxActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LieuAdapter adapter;
    private List<Lieu> lieuList;
    private List<Lieu> lieuListFull;
    private EditText etSearch;

    // Chips
    private TextView chipAll, chipPlage, chipHistoire, chipDesert,
            chipIle, chipNature, chipRating, tvProfil;

    private String activeChip = "All";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_lieux);

        recyclerView  = findViewById(R.id.recyclerView);
        etSearch      = findViewById(R.id.etSearch);
        chipAll       = findViewById(R.id.chipAll);
        chipPlage     = findViewById(R.id.chipPlage);
        chipHistoire  = findViewById(R.id.chipHistoire);
        chipDesert    = findViewById(R.id.chipDesert);
        chipIle       = findViewById(R.id.chipIle);
        chipNature    = findViewById(R.id.chipNature);
        chipRating    = findViewById(R.id.chipRating);
        tvProfil      = findViewById(R.id.tvProfil);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lieuListFull = new ArrayList<>();
        lieuList     = new ArrayList<>();

        adapter = new LieuAdapter(this, lieuList);
        recyclerView.setAdapter(adapter);

        loadFromFirestore();

        // Chip clicks
        chipAll.setOnClickListener(v      -> selectChip("All"));
        chipPlage.setOnClickListener(v    -> selectChip("Plage"));
        chipHistoire.setOnClickListener(v -> selectChip("Histoire"));
        chipDesert.setOnClickListener(v   -> selectChip("Désert"));
        chipIle.setOnClickListener(v      -> selectChip("Île"));
        chipNature.setOnClickListener(v   -> selectChip("Nature"));
        chipRating.setOnClickListener(v   -> selectChip("Rating"));

        // Search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters(s.toString());
            }
        });

        // Profil icon → redirect selon role
        tvProfil.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore.getInstance()
                        .collection("users").document(uid).get()
                        .addOnSuccessListener(doc -> {
                            String role = doc.getString("role");
                            if (role == null) role = "Touriste";
                            redirectProfil(role);
                        });
            } else {
                startActivity(new Intent(this, SignInActivity.class));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void redirectProfil(String role) {
        Intent intent;
        switch (role) {
            case "Admin":  intent = new Intent(this, AdminDashboardActivity.class);  break;
            case "Guide":  intent = new Intent(this, GuideProfilActivity.class);     break;
            case "Agence": intent = new Intent(this, AgenceProfilActivity.class);    break;
            default:       intent = new Intent(this, TouristeProfilActivity.class);  break;
        }
        startActivity(intent);
    }

    private void selectChip(String chip) {
        activeChip = chip;

        // Reset all chips
        int inactiveColor = 0x33FFFFFF;
        int activeColor   = 0xCCFFD700;
        int inactiveText  = 0xFFFFD700;
        int activeText    = 0xFF000000;

        chipAll.setBackgroundColor(inactiveColor);     chipAll.setTextColor(inactiveText);
        chipPlage.setBackgroundColor(inactiveColor);   chipPlage.setTextColor(inactiveText);
        chipHistoire.setBackgroundColor(inactiveColor);chipHistoire.setTextColor(inactiveText);
        chipDesert.setBackgroundColor(inactiveColor);  chipDesert.setTextColor(inactiveText);
        chipIle.setBackgroundColor(inactiveColor);     chipIle.setTextColor(inactiveText);
        chipNature.setBackgroundColor(inactiveColor);  chipNature.setTextColor(inactiveText);
        chipRating.setBackgroundColor(inactiveColor);  chipRating.setTextColor(inactiveText);

        // Highlight active
        switch (chip) {
            case "All":      chipAll.setBackgroundColor(activeColor);     chipAll.setTextColor(activeText);      break;
            case "Plage":    chipPlage.setBackgroundColor(activeColor);   chipPlage.setTextColor(activeText);    break;
            case "Histoire": chipHistoire.setBackgroundColor(activeColor);chipHistoire.setTextColor(activeText); break;
            case "Désert":   chipDesert.setBackgroundColor(activeColor);  chipDesert.setTextColor(activeText);   break;
            case "Île":      chipIle.setBackgroundColor(activeColor);     chipIle.setTextColor(activeText);      break;
            case "Nature":   chipNature.setBackgroundColor(activeColor);  chipNature.setTextColor(activeText);   break;
            case "Rating":   chipRating.setBackgroundColor(activeColor);  chipRating.setTextColor(activeText);   break;
        }

        applyFilters(etSearch.getText().toString());
    }

    private void applyFilters(String query) {
        lieuList.clear();

        for (Lieu lieu : lieuListFull) {
            // Filter by category
            boolean matchCategory = activeChip.equals("All") ||
                    activeChip.equals("Rating") ||
                    lieu.getCategorie().equalsIgnoreCase(activeChip);

            // Filter by search
            boolean matchSearch = query.isEmpty() ||
                    lieu.getNom().toLowerCase().contains(query.toLowerCase()) ||
                    lieu.getVille().toLowerCase().contains(query.toLowerCase()) ||
                    lieu.getCategorie().toLowerCase().contains(query.toLowerCase());

            if (matchCategory && matchSearch) lieuList.add(lieu);
        }

        // Sort by rating if chip Rating
        if (activeChip.equals("Rating")) {
            Collections.sort(lieuList, (a, b) ->
                    Double.compare(
                            b.getRating() != null ? b.getRating() : 0.0,
                            a.getRating() != null ? a.getRating() : 0.0
                    )
            );
        }

        adapter.notifyDataSetChanged();
    }

    private void loadFromFirestore() {
        FirebaseFirestore.getInstance().collection("lieux").get()
                .addOnSuccessListener(snap -> {
                    lieuListFull.clear();
                    for (var doc : snap) {
                        Lieu lieu = doc.toObject(Lieu.class);
                        lieuListFull.add(lieu);
                    }
                    if (lieuListFull.isEmpty()) lieuListFull.addAll(LieuData.getAll());
                    lieuList.addAll(lieuListFull);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    lieuListFull.addAll(LieuData.getAll());
                    lieuList.addAll(lieuListFull);
                    adapter.notifyDataSetChanged();
                });
    }
}