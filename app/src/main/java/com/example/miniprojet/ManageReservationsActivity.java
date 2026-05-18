package com.example.miniprojet;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
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
        adapter = new ReservationAdapter(reservationList);
        rvReservations.setLayoutManager(new LinearLayoutManager(this));
        rvReservations.setAdapter(adapter);

        tvBack.setOnClickListener(v -> finish());

        loadReservations();
    }

    private void loadReservations() {
        db.collection("reservations").get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                reservationList.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    reservationList.add(document.getData());
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Error loading reservations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
}