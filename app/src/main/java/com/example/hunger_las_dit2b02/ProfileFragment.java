//Name: Thu Htet San
//Admin No: 2235022
//Class: DIT/FT/2B/02
//Date: 30.01.2024

package com.example.hunger_las_dit2b02;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    TextView userName;
    Button logout;
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;
    private SharedPreferences pref;
    private static final String USER_ID_KEY = "user_id";
    private FirebaseFirestore fstore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        logout = view.findViewById(R.id.logout);
        userName = view.findViewById(R.id.username);
        ImageView settingsIcon = view.findViewById(R.id.settingsIcon);

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
//                if (documentSnapshot.exists()) {

                    User user = documentSnapshot.toObject(User.class);

                    // Update the UI with the user data
                    updateUIWithUserData(user);

                    Toast.makeText(getActivity(), "Welcome, " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Document does not exist
//                    Toast.makeText(getActivity(), "User not found in Firestore", Toast.LENGTH_SHORT).show();
//                }
            }


        });
    }
    private void updateUIWithUserData(User user) {
        userName.setText(user.getUsername());

    }
}