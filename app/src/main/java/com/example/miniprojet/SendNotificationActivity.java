package com.example.miniprojet;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SendNotificationActivity extends AppCompatActivity {

    private TextInputEditText etMessage;
    private Spinner spinnerTarget;
    private Button btnSend, btnCancel;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        db = FirebaseFirestore.getInstance();
        etMessage = findViewById(R.id.etMessage);
        spinnerTarget = findViewById(R.id.spinnerTarget);
        btnSend = findViewById(R.id.btnSend);
        btnCancel = findViewById(R.id.btnCancel);

        // Setup Spinner
        String[] targets = {"All", "Agence", "Guide", "Touriste"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, targets);
        spinnerTarget.setAdapter(adapter);

        btnCancel.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> sendNotification());
    }

    private void sendNotification() {
        String message = etMessage.getText().toString().trim();
        String target = spinnerTarget.getSelectedItem().toString();

        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> notification = new HashMap<>();
        notification.put("message", message);
        notification.put("targetRole", target);
        notification.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));

        db.collection("notifications").add(notification)
            .addOnSuccessListener(documentReference -> {
                Toast.makeText(this, "Notification sent to " + target + "!", Toast.LENGTH_SHORT).show();
                finish();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
}