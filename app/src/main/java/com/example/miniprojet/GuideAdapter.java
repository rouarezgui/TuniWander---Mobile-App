package com.example.miniprojet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideViewHolder> {
    private List<Map<String, Object>> guideList;
    private String currentAgencyId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public GuideAdapter(List<Map<String, Object>> guideList, String currentAgencyId) {
        this.guideList = guideList;
        this.currentAgencyId = currentAgencyId;
    }

    @NonNull
    @Override
    public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new GuideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideViewHolder holder, int position) {
        Map<String, Object> guide = guideList.get(position);
        String uid = (String) guide.get("uid");
        String name = (String) guide.get("name");
        String email = (String) guide.get("email");
        String specialite = (String) guide.get("specialite");
        String agenceId = (String) guide.get("agenceId");

        holder.tvUserName.setText(name != null ? name : "Unknown Guide");
        holder.tvUserEmail.setText(email != null ? email : "");
        holder.tvUserRole.setText("Spec: " + (specialite != null ? specialite : "General"));
        
        if (name != null && !name.isEmpty()) {
            holder.tvUserAvatar.setText(String.valueOf(name.charAt(0)).toUpperCase());
        } else {
            holder.tvUserAvatar.setText("G");
        }

        holder.layoutVerify.setVisibility(View.VISIBLE);
        
        if (currentAgencyId.equals(agenceId)) {
            holder.btnAccept.setText("Release");
            holder.btnAccept.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFF4444)); // Red
        } else {
            holder.btnAccept.setText("Hire Guide");
            holder.btnAccept.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFD700)); // Gold
        }

        holder.btnRefuse.setVisibility(View.GONE); // Use btnAccept for toggle

        holder.btnAccept.setOnClickListener(v -> {
            if (uid == null) return;
            
            String newAgencyId = currentAgencyId.equals(agenceId) ? "" : currentAgencyId;
            
            db.collection("users").document(uid).update("agenceId", newAgencyId)
                .addOnSuccessListener(aVoid -> {
                    guide.put("agenceId", newAgencyId);
                    notifyItemChanged(position);
                    String msg = newAgencyId.isEmpty() ? "Guide released" : "Guide hired!";
                    Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT).show();
                });
        });
    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }

    static class GuideViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserAvatar, tvUserName, tvUserEmail, tvUserRole;
        View layoutVerify;
        Button btnAccept, btnRefuse;

        public GuideViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserAvatar = itemView.findViewById(R.id.tvUserAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            layoutVerify = itemView.findViewById(R.id.layoutVerify);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnRefuse = itemView.findViewById(R.id.btnRefuse);
        }
    }
}
