package com.example.miniprojet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<Map<String, Object>> userList;

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
        String name = (String) user.get("name");
        String email = (String) user.get("email");
        String role = (String) user.get("role");

        holder.tvUserName.setText(name != null ? name : "No Name");
        holder.tvUserEmail.setText(email != null ? email : "No Email");
        holder.tvUserRole.setText(role != null ? role : "No Role");
        
        if (name != null && !name.isEmpty()) {
            holder.tvUserAvatar.setText(String.valueOf(name.charAt(0)).toUpperCase());
        } else {
            holder.tvUserAvatar.setText("?");
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserAvatar, tvUserName, tvUserEmail, tvUserRole;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserAvatar = itemView.findViewById(R.id.tvUserAvatar);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
        }
    }
}