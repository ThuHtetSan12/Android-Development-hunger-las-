package com.example.hunger_las_dit2b02;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        User1 user = post.getUser();

        // Bind data to views
        holder.profilePictureImageView.setImageResource(user.getProfilePictureResId());
        holder.usernameTextView.setText(user.getUsername());
        holder.restaurantInfoTextView.setText(post.getRestaurantInfo());
        holder.postImageView.setImageResource(post.getPostImageResId());
        holder.likeCountTextView.setText(String.valueOf(post.getLikeCount()));
        holder.commentCountTextView.setText(String.valueOf(post.getCommentCount()));
        holder.captionTextView.setText(post.getCaption());
        holder.dateTextView.setText("Posted on: " + post.getDate());

        // Set rating dynamically using RatingBar
        RatingBar ratingBar = holder.ratingStarsRatingBar;
        ratingBar.setRating(post.getRating());

        // Set click listeners for like, comment, and favourite buttons
        // Implement the click listeners as needed
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        public RatingBar ratingStarsRatingBar;
        ImageView profilePictureImageView;
        TextView usernameTextView;
        TextView restaurantInfoTextView;
        ImageView postImageView;
        ImageView likeIconImageView;
        TextView likeCountTextView;
        ImageView commentIconImageView;
        TextView commentCountTextView;
        ImageView favouriteIconImageView;
        TextView captionTextView;
        TextView dateTextView;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePictureImageView = itemView.findViewById(R.id.profilePictureImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            restaurantInfoTextView = itemView.findViewById(R.id.restaurantInfoTextView);
            postImageView = itemView.findViewById(R.id.postImageView);
            likeIconImageView = itemView.findViewById(R.id.likeIconImageView);
            likeCountTextView = itemView.findViewById(R.id.likeCountTextView);
            commentIconImageView = itemView.findViewById(R.id.commentIconImageView);
            commentCountTextView = itemView.findViewById(R.id.commentCountTextView);
            favouriteIconImageView = itemView.findViewById(R.id.favouriteIconImageView);
            captionTextView = itemView.findViewById(R.id.captionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);

            // RatingBar for rating
            ratingStarsRatingBar = itemView.findViewById(R.id.ratingStarsRatingBar);
        }
    }
}
