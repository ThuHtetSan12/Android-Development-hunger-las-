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
    TextView userName;
    Button logout;
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;

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

            // Create sample Post objects
            Post post1 = new Post(R.drawable.logo, "Lindy", "Mr Prata - Location 1",
                    R.drawable.logo, 150, 30, "Yummers", "January 10, 2024");

            Post post2 = new Post(R.drawable.logo, "Angie", "Yakiniku GO - Location 2",
                    R.drawable.logo, 200, 40, "hardcoded data", "January 15, 2024");

            Post post3 = new Post(R.drawable.logo, "San", "Sanook Kitchen - Location 3",
                    R.drawable.logo, 120, 25, "yayayay", "January 20, 2024");

            // Add posts to the list
            postList.add(post1);
            postList.add(post2);
            postList.add(post3);

            return postList;
        }
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Initialize views
//        logout = view.findViewById(R.id.logout);
//        userName = view.findViewById(R.id.userName);
//
//        // Google Sign-In configuration
//        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gClient = GoogleSignIn.getClient(requireContext(), gOptions);
//
//        // Check if the user is already signed in
//        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(requireContext());
//        if (gAccount != null) {
//            String gName = gAccount.getDisplayName();
//            userName.setText(gName);
//        }
//
//        // Set onClickListener for the logout button
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        requireActivity().finish();
//                        startActivity(new Intent(requireContext(), Login.class));
//                    }
//                });
//            }
//        });
//    }
//}