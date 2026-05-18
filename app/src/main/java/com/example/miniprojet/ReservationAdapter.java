package com.example.miniprojet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    private List<Map<String, Object>> reservationList;

    public ReservationAdapter(List<Map<String, Object>> reservationList) {
        this.reservationList = reservationList;
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
        
        String dest = (String) rsv.get("destination");
        String date = (String) rsv.get("date");
        String name = (String) rsv.get("name");
        String persons = String.valueOf(rsv.get("persons"));

        holder.tvRsvDestination.setText("📍 " + (dest != null ? dest : "Unknown"));
        holder.tvRsvDate.setText("🗓️ " + (date != null ? date : "N/A") + " - " + (name != null ? name : "Guest"));
        holder.tvRsvPersons.setText("👥 " + persons + " persons");
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvRsvDestination, tvRsvDate, tvRsvPersons;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRsvDestination = itemView.findViewById(R.id.tvRsvDestination);
            tvRsvDate = itemView.findViewById(R.id.tvRsvDate);
            tvRsvPersons = itemView.findViewById(R.id.tvRsvPersons);
        }
    }
}