package com.example.miniprojet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LieuAdapter extends RecyclerView.Adapter<LieuAdapter.LieuViewHolder> {

    private Context context;
    private List<Lieu> lieuList;

    // Constructor
    public LieuAdapter(Context context, List<Lieu> lieuList) {
        this.context  = context;
        this.lieuList = lieuList;
    }

    @NonNull
    @Override
    public LieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item_lieu.xml
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_lieu, parent, false);
        return new LieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LieuViewHolder holder, int position) {
        Lieu lieu = lieuList.get(position);

        // Set text data
        holder.tvNom.setText(lieu.getNom());
        holder.tvVille.setText(lieu.getVille());
        holder.tvDescription.setText(lieu.getDescription());
        holder.tvCategorie.setText(lieu.getCategorie());

        // Load image — local drawable
        if (lieu.getImageUrl() != null && !lieu.getImageUrl().isEmpty()) {

            if (lieu.getImageUrl().startsWith("http")) {
                // Internet URL → not used for now
                holder.imgLieu.setImageResource(R.drawable.beach);
            } else {
                // Local drawable name → getIdentifier
                int resId = context.getResources().getIdentifier(
                        lieu.getImageUrl(),
                        "drawable",
                        context.getPackageName()
                );
                if (resId != 0) {
                    // Found → set image
                    holder.imgLieu.setImageResource(resId);
                } else {
                    // Not found → default image
                    holder.imgLieu.setImageResource(R.drawable.beach);
                }
            }

        } else {
            // Empty URL → default image
            holder.imgLieu.setImageResource(R.drawable.beach);
        }

        // Click on card or Explore button → DetailActivity
        View.OnClickListener goToDetail = v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("nom",         lieu.getNom());
            intent.putExtra("ville",       lieu.getVille());
            intent.putExtra("description", lieu.getDescription());
            intent.putExtra("imageUrl",    lieu.getImageUrl());
            intent.putExtra("categorie",   lieu.getCategorie());
            context.startActivity(intent);
        };

        holder.itemView.setOnClickListener(goToDetail);
        holder.btnExplore.setOnClickListener(goToDetail);
    }

    @Override
    public int getItemCount() {
        return lieuList.size();
    }

    // ViewHolder
    public static class LieuViewHolder extends RecyclerView.ViewHolder {

        ImageView imgLieu;
        TextView tvNom, tvVille, tvDescription, tvCategorie;
        Button btnExplore;

        public LieuViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLieu       = itemView.findViewById(R.id.imgLieu);
            tvNom         = itemView.findViewById(R.id.tvNom);
            tvVille       = itemView.findViewById(R.id.tvVille);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCategorie   = itemView.findViewById(R.id.tvCategorie);
            btnExplore    = itemView.findViewById(R.id.btnExplore);
        }
    }
}