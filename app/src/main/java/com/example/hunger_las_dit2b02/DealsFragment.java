package com.example.hunger_las_dit2b02;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DealsFragment extends Fragment {

    private static final String TAG = "DealsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deals, container, false);

        EditText searchEditText = view.findViewById(R.id.txtSearch);
        RecyclerView recyclerView = view.findViewById(R.id.cardDeals);
        DealsAdapter adapter = new DealsAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed in this case
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter deals based on the search query
                adapter.filterDeals(charSequence.toString());
            }

            public void afterTextChanged(Editable editable) {
                // Not needed in this case
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("deals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Deal> deals = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String restaurantName = document.getString("restaurantName");
                                String imagePath = document.getString("imagePath");
                                String dealInformation = document.getString("dealInformation");

                                deals.add(new Deal(restaurantName, imagePath, dealInformation));
                                Log.d(TAG, "Retrieved deal: " + restaurantName);
                            }

                            // Sort deals to display in ascending alphabetical order
                            Collections.sort(deals, new Comparator<Deal>() {
                                public int compare(Deal deal1, Deal deal2) {
                                    return deal1.getRestaurantName().compareToIgnoreCase(deal2.getRestaurantName());
                                }
                            });

                            adapter.setDeals(deals);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return view;
    }
}
