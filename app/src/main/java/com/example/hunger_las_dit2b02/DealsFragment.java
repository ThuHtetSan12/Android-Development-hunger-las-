package com.example.hunger_las_dit2b02;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class DealsFragment extends Fragment {

    private static class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder> {
        private List<Deal> deals;

        public DealsAdapter(List<Deal> deals) {
            this.deals = deals;
        }

        @NonNull
        @Override
        public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.deal_card_layout, parent, false);
            return new DealViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
            Deal deal = deals.get(position);

            // Bind data to the ViewHolder
            holder.restaurantNameTextView.setText(deal.getRestaurantName());
            holder.restaurantImageView.setImageResource(deal.getRestaurantImage());
            holder.dealInfoTextView.setText(deal.getDealInformation());

            // Handle "Find Out More" button click
            holder.findOutMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle button click ( Redirect to website )
                }
            });
        }

        @Override
        public int getItemCount() {
            return deals.size();
        }

        public static class DealViewHolder extends RecyclerView.ViewHolder {
            TextView restaurantNameTextView;
            ImageView restaurantImageView;
            TextView dealInfoTextView;
            Button findOutMoreButton;

            public DealViewHolder(@NonNull View itemView) {
                super(itemView);
                restaurantNameTextView = itemView.findViewById(R.id.txtDealsRestaurant);
                restaurantImageView = itemView.findViewById(R.id.imgRestaurantDeals);
                dealInfoTextView = itemView.findViewById(R.id.txtDealsInfo);
                findOutMoreButton = itemView.findViewById(R.id.btnDealsSeeMore);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deals, container, false);

        // Create a list of hardcoded deals
        List<Deal> deals = new ArrayList<>();
        deals.add(new Deal("Restaurant 1", R.drawable.logo, "50% off on all items"));
        deals.add(new Deal("Restaurant 2", R.drawable.logo, "Free dessert with every meal"));
        deals.add(new Deal("Restaurant 3", R.drawable.logo, "50% off on all items"));

        // Set up the RecyclerView and attach the adapter
        RecyclerView recyclerView = view.findViewById(R.id.cardDeals);
        DealsAdapter adapter = new DealsAdapter(deals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
