package com.example.miniprojet;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManageGuidesActivity extends AppCompatActivity {

    private RecyclerView rvGuides;
    private GuideAdapter adapter;
    private List<Map<String, Object>> guideList;
    private FirebaseFirestore db;
    private String currentAgencyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_guides);

        db = FirebaseFirestore.getInstance();
        currentAgencyId = FirebaseAuth.getInstance().getUid();

        rvGuides = findViewById(R.id.rvGuides);
        TextView tvBack = findViewById(R.id.tvBack);

        guideList = new ArrayList<>();
        adapter = new GuideAdapter(guideList, currentAgencyId);
        rvGuides.setLayoutManager(new LinearLayoutManager(this));
        rvGuides.setAdapter(adapter);

        tvBack.setOnClickListener(v -> finish());

        loadGuides();
    }

    private void loadGuides() {
        // Fetch all users with role 'Guide'
        db.collection("users")
            .whereEqualTo("role", "Guide")
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                guideList.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Map<String, Object> data = document.getData();
                    data.put("uid", document.getId());
                    guideList.add(data);
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Error loading guides: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
}