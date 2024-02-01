//package com.example.hunger_las_dit2b02;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.PostViewHolder>{
//    private List<Post> posts;
//
//    @NonNull
//    @Override
//    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // Inflate post item layout
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
//        return new PostViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
//        // Bind post data to the views in the item layout
//        Post currentPost = posts.get(position);
//        holder.bind(currentPost);
//    }
//
//    @Override
//    public int getItemCount() {
//        return posts.size();
//    }
//    // ViewHolder class
//    static class PostViewHolder extends RecyclerView.ViewHolder {
//        private final TextView postContent;
//
//        public PostViewHolder(@NonNull View itemView) {
//            super(itemView);
//            postContent = itemView.findViewById(R.id.postContentTextView); // Replace with actual ID
//        }
//
//        public void bind(Post post) {
//            // Bind data to views
//            postContent.setText(post.getContent());
//        }
//    }
//}
