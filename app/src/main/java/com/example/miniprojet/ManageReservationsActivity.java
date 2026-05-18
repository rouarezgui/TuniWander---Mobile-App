package com.example.miniprojet;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManageReservationsActivity extends AppCompatActivity {

    private RecyclerView rvReservations;
    private ReservationAdapter adapter;
    private List<Map<String, Object>> reservationList;
    private FirebaseFirestore db;
    private TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reservations);

        db = FirebaseFirestore.getInstance();
        rvReservations = findViewById(R.id.rvReservations);
        tvBack = findViewById(R.id.tvBack);

        reservationList = new ArrayList<>();
        rvReservations.setLayoutManager(new LinearLayoutManager(this));

        tvBack.setOnClickListener(v -> finish());

        checkUserRoleAndLoad();
    }

    private void checkUserRoleAndLoad() {
        String uid = FirebaseAuth.getInstance().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getEmail() : "";
        
        if (uid == null) return;

        db.collection("users").document(uid).get().addOnSuccessListener(doc -> {
            String role = doc.getString("role");
            
            // Extra safety for Admin Roua
            if (email != null && (email.equalsIgnoreCase("roua@gmail.com") || email.equalsIgnoreCase("rou@gmail.com"))) {
                role = "Admin";
            }
            
            if (role == null) role = "Touriste";
            
            final String finalRole = role;
            adapter = new ReservationAdapter(reservationList, finalRole);
            rvReservations.setAdapter(adapter);
            
            loadReservations(finalRole, uid);
        });
    }

    private void loadReservations(String role, String uid) {
        Query query = db.collection("reservations");
        
        // If Agency, only show reservations for their destinations
        if ("Agence".equalsIgnoreCase(role)) {
            query = query.whereEqualTo("agenceId", uid);
        }
        // If Touriste, show only their own (fallback, normally they use TouristeProfilActivity)
        else if ("Touriste".equalsIgnoreCase(role)) {
            query = query.whereEqualTo("userId", uid);
        }
        // Admin sees all

        query.get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                reservationList.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Map<String, Object> data = document.getData();
                    data.put("id", document.getId());
                    reservationList.add(data);
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Error loading reservations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
}