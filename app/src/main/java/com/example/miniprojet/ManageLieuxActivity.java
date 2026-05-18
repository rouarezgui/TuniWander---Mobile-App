package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ManageLieuxActivity extends AppCompatActivity {

    private RecyclerView rvLieux;
    private LieuAdapter adapter;
    private List<Lieu> lieuList;
    private FirebaseFirestore db;
    private TextView tvBack;
    private Button btnAddLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_lieux);

        db = FirebaseFirestore.getInstance();
        rvLieux = findViewById(R.id.rvLieux);
        tvBack = findViewById(R.id.tvBack);
        btnAddLieu = findViewById(R.id.btnAddLieu);

        lieuList = new ArrayList<>();
        adapter = new LieuAdapter(this, lieuList);
        rvLieux.setLayoutManager(new LinearLayoutManager(this));
        rvLieux.setAdapter(adapter);

        tvBack.setOnClickListener(v -> finish());
        btnAddLieu.setOnClickListener(v -> startActivity(new Intent(this, AddEditLieuActivity.class)));

        loadLieux();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLieux();
    }

    private void loadLieux() {
        db.collection("lieux").get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                lieuList.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Lieu lieu = document.toObject(Lieu.class);
                    lieu.setId(document.getId());
                    lieuList.add(lieu);
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Error loading destinations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
}