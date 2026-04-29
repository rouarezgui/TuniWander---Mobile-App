package com.example.miniprojet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListeLieuxActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LieuAdapter adapter;
    private List<Lieu> lieuList;
    private List<Lieu> lieuListFull;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_lieux);

        recyclerView = findViewById(R.id.recyclerView);
        etSearch     = findViewById(R.id.etSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Data fictive — Firebase later
        lieuListFull = new ArrayList<>();
        lieuListFull.add(new Lieu("1", "Sidi Bou Said",  "Tunis",    "A magical blue and white village overlooking the Mediterranean sea.",     "sidi_bou_said", "Village"));
        lieuListFull.add(new Lieu("2", "Djerba",          "Médenine", "An island of sun, beaches and authentic Tunisian culture.",              "djerba", "Island"));
        lieuListFull.add(new Lieu("3", "Tozeur",          "Tozeur",   "Gateway to the Sahara desert, palm oases and salt lakes.",               "tozeur", "Desert"));
        lieuListFull.add(new Lieu("4", "Carthage",        "Tunis",    "Ancient ruins of one of the greatest civilizations in history.",         "carthage", "History"));
        lieuListFull.add(new Lieu("5", "Hammamet",        "Nabeul",   "Famous beach resort on the beautiful Mediterranean coast.",              "hammamet", "Beach"));
        lieuListFull.add(new Lieu("6", "Kairouan",        "Kairouan", "One of the holiest cities in the Islamic world, UNESCO heritage.",       "kairouan", "Religion"));
        lieuListFull.add(new Lieu("7", "El Djem",         "Mahdia",   "Home to one of the best preserved Roman amphitheaters in the world.",    "eljem", "History"));
        lieuListFull.add(new Lieu("8", "Tabarka",         "Jendouba", "A coastal gem known for diving, coral reefs and jazz festival.",         "tabarka", "Beach"));

        lieuList = new ArrayList<>(lieuListFull);

        adapter = new LieuAdapter(this, lieuList);
        recyclerView.setAdapter(adapter);

        // Search filter
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Filter by nom or ville
    private void filterList(String query) {
        lieuList.clear();
        if (query.isEmpty()) {
            lieuList.addAll(lieuListFull);
        } else {
            for (Lieu lieu : lieuListFull) {
                if (lieu.getNom().toLowerCase().contains(query.toLowerCase()) ||
                        lieu.getVille().toLowerCase().contains(query.toLowerCase()) ||
                        lieu.getCategorie().toLowerCase().contains(query.toLowerCase())) {
                    lieuList.add(lieu);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}