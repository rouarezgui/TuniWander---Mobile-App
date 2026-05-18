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

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private UserAdapter adapter;
    private List<Map<String, Object>> userList;
    private FirebaseFirestore db;
    private TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        db = FirebaseFirestore.getInstance();
        rvUsers = findViewById(R.id.rvUsers);
        tvBack = findViewById(R.id.tvBack);

        userList = new ArrayList<>();
        adapter = new UserAdapter(userList);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        tvBack.setOnClickListener(v -> finish());

        loadUsers();
    }

    private void loadUsers() {
        db.collection("users").get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                userList.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    userList.add(document.getData());
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Error loading users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
}