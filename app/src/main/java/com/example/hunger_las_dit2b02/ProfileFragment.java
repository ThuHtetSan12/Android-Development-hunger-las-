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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ProfileFragment extends Fragment {
    TextView userName;
    Button logout;
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;
    SharedPreferences pref;
    private static final String USER_ID_KEY = "user_id";
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
        userName = view.findViewById(R.id.userName);

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
            showUserIdMessage(savedUserId);

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
}