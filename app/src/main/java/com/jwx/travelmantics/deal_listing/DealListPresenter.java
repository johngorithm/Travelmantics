package com.jwx.travelmantics.deal_listing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.jwx.travelmantics.constants.Constants;
import com.jwx.travelmantics.models.TravelDeal;
import com.jwx.travelmantics.services.FirebaseApiService;

public class DealListPresenter {

    private DatabaseReference fbRootRef;
    private DealListView dealListView;

    public DealListPresenter(DealListView view){
        dealListView = view;
        fbRootRef = FirebaseApiService.getFbRootRef();
    }

    void fetchDeals() {
        fbRootRef.child(Constants.TRAVEL_DEAL_NODE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);
                if(deal != null) {
                    deal.setUid(dataSnapshot.getKey());
                    dealListView.onDealAdded(deal);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
