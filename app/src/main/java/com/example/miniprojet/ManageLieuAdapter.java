package com.example.miniprojet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class ManageLieuAdapter extends RecyclerView.Adapter<ManageLieuAdapter.ViewHolder> {

    private Context context;
    private List<Lieu> lieuList;
    private FirebaseFirestore db;

    public ManageLieuAdapter(Context context, List<Lieu> lieuList) {
        this.context = context;
        this.lieuList = lieuList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lieu_manage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lieu lieu = lieuList.get(position);
        holder.tvNom.setText(lieu.getNom());
        holder.tvVille.setText(lieu.getVille());

        Glide.with(context).load(lieu.getImageUrl()).placeholder(R.drawable.beach).into(holder.imgLieu);

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditLieuActivity.class);
            intent.putExtra("id", lieu.getId());
            intent.putExtra("nom", lieu.getNom());
            intent.putExtra("ville", lieu.getVille());
            intent.putExtra("categorie", lieu.getCategorie());
            intent.putExtra("description", lieu.getDescription());
            intent.putExtra("imageUrl", lieu.getImageUrl());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            db.collection("lieux").document(lieu.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    lieuList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                });
        });
    }

    @Override
    public int getItemCount() {
        return lieuList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLieu;
        TextView tvNom, tvVille;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLieu = itemView.findViewById(R.id.imgLieu);
            tvNom = itemView.findViewById(R.id.tvNom);
            tvVille = itemView.findViewById(R.id.tvVille);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}