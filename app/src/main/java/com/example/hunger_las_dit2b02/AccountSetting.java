package com.example.hunger_las_dit2b02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AccountSetting extends AppCompatActivity {
    GoogleSignInClient gClient;
    private SharedPreferences pref;
    private static final String USER_ID_KEY = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To have the back button!!
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.account_setting);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onLogOutClick(){

        clearSavedUserId();
        gClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Sign out successful
                Intent intent = new Intent(AccountSetting.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void clearSavedUserId() {
        SharedPreferences pref = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(USER_ID_KEY);
        editor.apply();
    }

    public void onChangePasswordClick(View view) {
        // Handle the click event here
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);
    }
}
