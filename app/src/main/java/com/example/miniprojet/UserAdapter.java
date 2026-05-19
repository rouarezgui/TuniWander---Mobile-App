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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<Map<String, Object>> userList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserAdapter(List<Map<String, Object>> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Map<String, Object> user = userList.get(position);
        String uid = (String) user.get("uid");
        String name = (String) user.get("name");
        String email = (String) user.get("email");
        String role = user.get("role") != null ? String.valueOf(user.get("role")) : null;

        boolean isVerified = isUserVerified(user.get("isVerified"));

        holder.tvUserName.setText(name != null ? name : "No Name");
        holder.tvUserEmail.setText(email != null ? email : "No Email");
        holder.tvUserRole.setText(role != null ? role : "No Role");

        if (name != null && !name.isEmpty()) {
            holder.tvUserAvatar.setText(String.valueOf(name.charAt(0)).toUpperCase());
        } else {
            holder.tvUserAvatar.setText("?");
        }

        if (!isVerified && canBeVerifiedByAdmin(role)) {
            holder.layoutVerify.setVisibility(View.VISIBLE);
        } else {
            holder.layoutVerify.setVisibility(View.GONE);
        }

        holder.btnAccept.setOnClickListener(v -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (uid != null && adapterPosition != RecyclerView.NO_POSITION) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("isVerified", true);

                db.collection("users").document(uid).update(updates)
                        .addOnSuccessListener(aVoid -> {
                            userList.get(adapterPosition).put("isVerified", true);
                            notifyItemChanged(adapterPosition);
                            Toast.makeText(v.getContext(), "User verified!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(v.getContext(), "Cannot verify this user.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnRefuse.setOnClickListener(v -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (uid != null && adapterPosition != RecyclerView.NO_POSITION) {
                db.collection("users").document(uid).delete()
                        .addOnSuccessListener(aVoid -> {
                            userList.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            notifyItemRangeChanged(adapterPosition, userList.size());
                            Toast.makeText(v.getContext(), "User refused", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(v.getContext(), "Cannot refuse this user.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private boolean isUserVerified(Object verifiedObj) {
        if (verifiedObj instanceof Boolean) {
            return (Boolean) verifiedObj;
        }
        if (verifiedObj instanceof String) {
            return "true".equalsIgnoreCase((String) verifiedObj);
        }
        return false;
    }

    private boolean canBeVerifiedByAdmin(String role) {
        if (role == null) {
            return false;
        }
        String normalizedRole = role.trim().toLowerCase();
        return normalizedRole.equals("guide")
                || normalizedRole.equals("agence")
                || normalizedRole.equals("agency");
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserAvatar, tvUserName, tvUserEmail, tvUserRole;
        View layoutVerify;
        Button btnAccept, btnRefuse;

        public UserViewHolder(@NonNull View itemView) {
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
