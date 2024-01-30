package com.example.hunger_las_dit2b02;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder> {

    private List<Deal> deals;

    public DealsAdapter(List<Deal> deals) {
        this.deals = deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_card_layout, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        Deal deal = deals.get(position);
        holder.txtDealsRestaurant.setText(deal.getRestaurantName());
        //holder.imgRestaurantDeals.setImageResource(deal.getImagePath());
        holder.txtDealsInfo.setText(deal.getDealInformation());
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public static class DealViewHolder extends RecyclerView.ViewHolder {
        TextView txtDealsRestaurant;
        ImageView imgRestaurantDeals;
        TextView txtDealsInfo;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDealsRestaurant = itemView.findViewById(R.id.txtDealsRestaurant);
            imgRestaurantDeals = itemView.findViewById(R.id.imgRestaurantDeals);
            txtDealsInfo = itemView.findViewById(R.id.txtDealsInfo);
        }
    }
}
