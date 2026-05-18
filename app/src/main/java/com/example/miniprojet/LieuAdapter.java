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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LieuAdapter extends RecyclerView.Adapter<LieuAdapter.LieuViewHolder> {

    private final Context context;
    private final List<Lieu> lieuList;

    public LieuAdapter(Context context, List<Lieu> lieuList) {
        this.context  = context;
        this.lieuList = lieuList;
    }

    @NonNull
    @Override
    public LieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_lieu, parent, false);
        return new LieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LieuViewHolder holder, int position) {
        Lieu lieu = lieuList.get(position);

        holder.tvNom.setText(lieu.getNom());
        holder.tvVille.setText(lieu.getVille());
        holder.tvDescription.setText(lieu.getDescription());
        holder.tvCategorie.setText(lieu.getCategorie());

        // Load image via Glide (URL) ou drawable local
        String imageUrl = lieu.getImageUrl();
        if (imageUrl != null && imageUrl.startsWith("http")) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.beach)
                    .error(R.drawable.beach)
                    .centerCrop()
                    .into(holder.imgLieu);
        } else if (imageUrl != null) {
            int resId = context.getResources().getIdentifier(
                    imageUrl, "drawable", context.getPackageName()
            );
            if (resId != 0) {
                holder.imgLieu.setImageResource(resId);
            } else {
                Glide.with(context).load(imageUrl).placeholder(R.drawable.beach).into(holder.imgLieu);
            }
        }

        // Click → DetailActivity avec programme
        View.OnClickListener goToDetail = v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("nom",         lieu.getNom());
            intent.putExtra("ville",       lieu.getVille());
            intent.putExtra("description", lieu.getDescription());
            intent.putExtra("imageUrl",    lieu.getImageUrl());
            intent.putExtra("categorie",   lieu.getCategorie());
            intent.putExtra("agenceId",    lieu.getAgenceId());
            intent.putExtra("guideId",     lieu.getGuideId());

            // Passer programme comme ArrayList<String> "heure|activite"
            ArrayList<String> programmeList = new ArrayList<>();
            if (lieu.getProgramme() != null) {
                for (Map<String, String> step : lieu.getProgramme()) {
                    String heure    = step.get("heure");
                    String activite = step.get("activite");
                    if (heure != null && activite != null) {
                        programmeList.add(heure + "|" + activite);
                    }
                }
            }
            intent.putStringArrayListExtra("programme", programmeList);
            context.startActivity(intent);
        };

        holder.itemView.setOnClickListener(goToDetail);
        holder.btnExplore.setOnClickListener(goToDetail);
    }

    @Override
    public int getItemCount() {
        return lieuList.size();
    }

    // ─── ViewHolder ───────────────────────────────────────────────
    public static class LieuViewHolder extends RecyclerView.ViewHolder {

        ImageView imgLieu;
        TextView  tvNom, tvVille, tvDescription, tvCategorie;
        Button    btnExplore;

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