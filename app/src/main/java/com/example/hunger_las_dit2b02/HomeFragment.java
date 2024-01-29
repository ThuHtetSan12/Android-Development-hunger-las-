package com.example.hunger_las_dit2b02;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_home, container, false);

            // Initialize RecyclerView
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Create sample data
            List<Post> postList = createSamplePosts();

            // Create and set the adapter
            postAdapter = new PostAdapter(postList);
            recyclerView.setAdapter(postAdapter);

            return view;
        }

        private List<Post> createSamplePosts() {
            List<Post> postList = new ArrayList<>();
            User1 user1 = new User1(R.drawable.logo, "Lindy");
            User1 user2 = new User1(R.drawable.logo, "Angie");
            User1 user3 = new User1(R.drawable.logo, "San");

            // Create sample Post objects
            Post post1 = new Post(user1, "Mr Prata - Location 1",
                    R.drawable.logo, 150, 30, "Yummers", "January 10, 2024", 5);

            Post post2 = new Post(user2, "Yakiniku GO - Location 2",
                    R.drawable.logo, 200, 40, "hardcoded data", "January 15, 2024", 1);

            Post post3 = new Post(user3, "Sanook Kitchen - Location 3",
                    R.drawable.logo, 120, 25, "yayayay", "January 20, 2024", 3);

            // Add posts to the list
            postList.add(post1);
            postList.add(post2);
            postList.add(post3);

            return postList;
        }
    }