//Name: Thu Htet San
//Admin No: 2235022
//Class: DIT/FT/2B/02
//Date: 30.01.2024

package com.example.hunger_las_dit2b02;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    private static final int EDIT_PROFILE_REQUEST_CODE = 100;
    TextView userName, name, bio;
    ImageView profileImage;
    Button logout;
    Button editProfileButton;
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;
    private SharedPreferences pref;
    private static final String USER_ID_KEY = "user_id";
    private FirebaseFirestore fstore;
    private TextView followersCount;
    private TextView followingCount;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize Firestore
        fstore = FirebaseFirestore.getInstance();

        // Fetch data from Firebase
        fetchPostsFromFirebase();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        logout = view.findViewById(R.id.logout);
        userName = view.findViewById(R.id.username);
        name = view.findViewById(R.id.nameTextView);
        bio = view.findViewById(R.id.bioTextView);
        profileImage = view.findViewById(R.id.profileimage);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        ImageView settingsIcon = view.findViewById(R.id.settingsIcon);
        followersCount = view.findViewById(R.id.followersCountTextView);
        followingCount = view.findViewById(R.id.followingCountTextView);

        // Google Sign-In configuration
          gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
          gClient = GoogleSignIn.getClient(requireContext(), gOptions);

        // Check if the user is already signed in
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (gAccount != null) {
            String gName = gAccount.getDisplayName();
            userName.setText(gName);
        }
        String savedUserId = getSavedUserId();
        if (!TextUtils.isEmpty(savedUserId)) {
            //showUserIdMessage(savedUserId);
            getUserDataFromFirestore(savedUserId);
            //add update ui

        }else{
            logoutAndRedirectToLogin();
        }
        // Set onClickListener for the logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAndRedirectToLogin();
            }
        });

        Button editProfileButton = view.findViewById(R.id.editProfileButton); // assuming 'view' is your fragment's root view
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event here
                // For example, you can open the EditProfile activity
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE);
            }
        });


        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to SettingsActivity
                startActivity(new Intent(requireContext(), Setting.class));
            }
        });
    }

    private String getSavedUserId() {
        if (getActivity() != null) {
            pref = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
            return pref.getString(USER_ID_KEY, "");
        }else{
            return "";
        }
    }

    private void clearSavedUserId() {
        pref = requireActivity().getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(USER_ID_KEY);
        editor.apply();
    }
    private void logoutAndRedirectToLogin(){
        clearSavedUserId();
        gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                requireActivity().finish();
                startActivity(new Intent(requireContext(), Login.class));
            }
        });
    }

    private void showUserIdMessage(String userId) {
        // Example: Display a Toast message with the user ID
        Toast.makeText(requireContext(), "User ID: " + userId, Toast.LENGTH_SHORT).show();
    }

    private void getUserDataFromFirestore(String userId){
        fstore = FirebaseFirestore.getInstance();
        DocumentReference userRef = fstore.collection("users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    user = documentSnapshot.toObject(User.class);

                    // Update the UI with the user data
                    updateUIWithUserData(user);

                    Toast.makeText(getActivity(), "Welcome, " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();
                } else {
                    // Document does not exist
                    Toast.makeText(getActivity(), "User not found in Firestore", Toast.LENGTH_SHORT).show();
                }

            }


        });
    }
    private void updateUIWithUserData(User user) {
        userName.setText(user.getUsername());
        name.setText(user.getName());
        bio.setText(user.getBio());

        // Load profile image using Glide
        if (user.getImgUrl() != null) {
            Glide.with(requireContext())
                    .load(user.getImgUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileImage);
        }
        followersCount.setText(String.valueOf(user.getFollowers().size()));
        followingCount.setText(String.valueOf(user.getFollowing().size()));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Reload the user data after a successful edit
            String savedUserId = getSavedUserId();
            if (!TextUtils.isEmpty(savedUserId)) {
                getUserDataFromFirestore(savedUserId);

                // Refresh posts after the user edits the profile

                //postAdapter.setData(updatedPosts);
            }
        }
    }


    private void fetchPostsFromFirebase() {
        String savedUserId = getSavedUserId();
        // Assume you have a "posts" collection in Firestore
        fstore.collection("posts")
                .whereEqualTo("userid", savedUserId) // Add this filter condition
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Post> postList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String caption = document.getString("caption");
                            Timestamp timestamp = document.getTimestamp("date");
                            long restaurantIdLong = document.getLong("restaurant_id");
                            String restaurantId = String.valueOf(restaurantIdLong);
                            String imageUrl = document.getString("imageUrl");
                            int rating = document.getLong("rating").intValue(); // Assuming rating is stored as a number
                            String userId = document.getString("userid");
                            String date = timestamp != null ? new SimpleDateFormat("h:mm a", Locale.US).format(timestamp.toDate()) : "";


                            int likeCount = 0; // Hardcoded value
                            int commentCount = 0; // Hardcoded

                            Post post = new Post(user, restaurantId, imageUrl, likeCount, commentCount, caption, date, rating);
                            postList.add(post);
                        }

                        // Check if the fragment is still attached to the activity before updating UI
                        if (isAdded()) {
                            // Set the adapter with the retrieved data
                            postAdapter = new PostAdapter(postList, getContext());
                            recyclerView.setAdapter(postAdapter);
                        }
                    } else {
                        // Handle errors
                    }
                });
    }
}