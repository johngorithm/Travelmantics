package com.jwx.travelmantics.deal_creation;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.jwx.travelmantics.constants.Constants;
import com.jwx.travelmantics.models.TravelDeal;
import com.jwx.travelmantics.services.FirebaseApiService;

public class DealPresenter {

    private DatabaseReference fbRootRef;
    private InsertView insertView;

    public DealPresenter(InsertView view){
        insertView = view;
        fbRootRef = FirebaseApiService.getFbRootRef();
    }

    void saveTravelDealData(TravelDeal travelDeal) {
        fbRootRef.child(Constants.TRAVEL_DEAL_NODE).push().setValue(travelDeal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    insertView.onSaveComplete();
                    return;
                }
                insertView.onSaveError();
            }
        });
    }
}
