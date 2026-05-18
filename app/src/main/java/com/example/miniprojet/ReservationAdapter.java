package com.example.miniprojet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    private List<Map<String, Object>> reservationList;
    private String userRole;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ReservationAdapter(List<Map<String, Object>> reservationList, String userRole) {
        this.reservationList = reservationList;
        this.userRole = userRole;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_card, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Map<String, Object> rsv = reservationList.get(position);
        String id = (String) rsv.get("id");
        String dest = (String) rsv.get("destination");
        String ville = (String) rsv.get("ville");
        String date = (String) rsv.get("date");
        String name = (String) rsv.get("name");
        String phone = (String) rsv.get("phone");
        String notes = (String) rsv.get("notes");
        String status = (String) rsv.get("status");
        String persons = String.valueOf(rsv.get("persons"));
        String agenceId = (String) rsv.get("agenceId");

        holder.tvRsvDestination.setText("📍 " + (dest != null ? dest : "Unknown"));
        holder.tvRsvDate.setText("🗓️ " + (date != null ? date : "N/A") + " - " + (name != null ? name : "Guest"));
        holder.tvRsvPersons.setText("👥 " + persons + " persons");

        // Normalize status
        if (status == null || status.isEmpty()) status = "Pending";
        
        holder.tvRsvStatus.setVisibility(View.VISIBLE);
        holder.tvRsvStatus.setText("Status: " + status);

        if ("Accepted".equalsIgnoreCase(status)) {
            holder.tvRsvStatus.setTextColor(0xFF4CAF50); // Green
        } else if ("Refused".equalsIgnoreCase(status)) {
            holder.tvRsvStatus.setTextColor(0xFFF44336); // Red
        } else {
            holder.tvRsvStatus.setTextColor(0xFFFFFFFF); // White for Pending
        }

        // Action visibility logic
        boolean isAdminOrAgency = "Admin".equalsIgnoreCase(userRole) || "Agence".equalsIgnoreCase(userRole);
        boolean isTourist = "Touriste".equalsIgnoreCase(userRole);

        if (isAdminOrAgency) {
            holder.layoutTouristActions.setVisibility(View.GONE);
            // Show Accept/Refuse only if Pending
            if ("Pending".equalsIgnoreCase(status)) {
                holder.layoutAdminActions.setVisibility(View.VISIBLE);
            } else {
                holder.layoutAdminActions.setVisibility(View.GONE);
            }
        } else if (isTourist) {
            holder.layoutAdminActions.setVisibility(View.GONE);
            holder.layoutTouristActions.setVisibility(View.VISIBLE);
            
            // Edit only if Pending
            holder.btnEditRsv.setVisibility("Pending".equalsIgnoreCase(status) ? View.VISIBLE : View.GONE);
            
            // Rate only if Accepted
            holder.btnRate.setVisibility("Accepted".equalsIgnoreCase(status) ? View.VISIBLE : View.GONE);
            
            // Delete always visible for tourist to clean their history
            holder.btnDeleteRsv.setVisibility(View.VISIBLE);
        } else {
            holder.layoutAdminActions.setVisibility(View.GONE);
            holder.layoutTouristActions.setVisibility(View.GONE);
        }

        // Admin/Agency Button Clicks
        holder.btnAcceptRsv.setOnClickListener(v -> updateStatus(id, "Accepted", position, v.getContext()));
        holder.btnRefuseRsv.setOnClickListener(v -> updateStatus(id, "Refused", position, v.getContext()));

        // Tourist Button Clicks
        holder.btnDeleteRsv.setOnClickListener(v -> {
            if (id != null) {
                db.collection("reservations").document(id).delete().addOnSuccessListener(aVoid -> {
                    reservationList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, reservationList.size());
                    Toast.makeText(v.getContext(), "Reservation deleted", Toast.LENGTH_SHORT).show();
                });
            }
        });

        holder.btnEditRsv.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ReservationActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("nom", dest);
            intent.putExtra("ville", ville);
            intent.putExtra("date", date);
            intent.putExtra("persons", persons);
            intent.putExtra("name", name);
            intent.putExtra("phone", phone);
            intent.putExtra("notes", notes);
            intent.putExtra("agenceId", agenceId);
            intent.putExtra("isEdit", true);
            context.startActivity(intent);
        });

        holder.btnRate.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, RatingActivity.class);
            intent.putExtra("rsvId", id);
            intent.putExtra("destination", dest);
            context.startActivity(intent);
        });
    }

    private void updateStatus(String id, String newStatus, int position, Context context) {
        if (id == null) return;
        db.collection("reservations").document(id).update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    reservationList.get(position).put("status", newStatus);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Reservation " + newStatus, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvRsvDestination, tvRsvDate, tvRsvPersons, tvRsvStatus;
        LinearLayout layoutAdminActions, layoutTouristActions;
        Button btnAcceptRsv, btnRefuseRsv, btnEditRsv, btnDeleteRsv, btnRate;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRsvDestination = itemView.findViewById(R.id.tvRsvDestination);
            tvRsvDate = itemView.findViewById(R.id.tvRsvDate);
            tvRsvPersons = itemView.findViewById(R.id.tvRsvPersons);
            tvRsvStatus = itemView.findViewById(R.id.tvRsvStatus);
            layoutAdminActions = itemView.findViewById(R.id.layoutAdminActions);
            layoutTouristActions = itemView.findViewById(R.id.layoutTouristActions);
            btnAcceptRsv = itemView.findViewById(R.id.btnAcceptRsv);
            btnRefuseRsv = itemView.findViewById(R.id.btnRefuseRsv);
            btnEditRsv = itemView.findViewById(R.id.btnEditRsv);
            btnDeleteRsv = itemView.findViewById(R.id.btnDeleteRsv);
            btnRate = itemView.findViewById(R.id.btnRate);
        }
    }
}