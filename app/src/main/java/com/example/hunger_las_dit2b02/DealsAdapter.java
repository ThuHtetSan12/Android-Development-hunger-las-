package com.example.hunger_las_dit2b02;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        holder.txtDealsInfo.setText(deal.getDealInformation());

        // image retrieval
        retrieveImageForDeal(deal.getImagePath(), holder.imgRestaurantDeals);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    private void retrieveImageForDeal(String imagePath, ImageView imageView) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("deals").child(imagePath);

        try {
            final File localFile = File.createTempFile("deal", "jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image download successful
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                    })
                    .addOnFailureListener(exception -> {
                        // Handle failures
                        Log.e("DealsAdapter", "Error downloading image: " + exception.getMessage());
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
