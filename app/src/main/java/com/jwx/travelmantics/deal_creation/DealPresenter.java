package com.jwx.travelmantics.deal_creation;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.jwx.travelmantics.constants.Constants;
import com.jwx.travelmantics.models.TravelDeal;
import com.jwx.travelmantics.services.FirebaseApiService;

class DealPresenter {

    private DatabaseReference fbRootRef;
    private InsertView insertView;

    DealPresenter(InsertView view){
        insertView = view;
        fbRootRef = FirebaseApiService.getFbRootRef();
    }

    void saveTravelDealData(TravelDeal travelDeal) {
        fbRootRef.child(Constants.TRAVEL_DEAL_NODE).push().setValue(travelDeal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg;
                if (task.isSuccessful()) {
                    msg = "Your deal is saved successfully";
                    insertView.onSaveComplete(msg);
                    return;
                }
                msg = "Unable to Save deal. Mind trying?";
                insertView.onSaveError(msg);
            }
        });
    }

    void updateDeal(TravelDeal travelDeal) {
        fbRootRef.child(Constants.TRAVEL_DEAL_NODE).child(travelDeal.getUid()).updateChildren(travelDeal.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg;
                        if (task.isSuccessful()) {
                            msg = "Update Successful";
                            insertView.onSaveComplete(msg);
                            return;
                        }
                        msg = "Failed to Update!. Mind Trying again?";
                        insertView.onSaveError(msg);
                    }
                });
    }

    void deleteDeal(TravelDeal travelDeal) {
        fbRootRef.child(Constants.TRAVEL_DEAL_NODE).child(travelDeal.getUid()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Deal Deleted Successfully";
                        if (task.isSuccessful()) {
                            insertView.onDeleteDeal(msg);
                            return;
                        }
                        msg = "Unable to Delete deal. Mind trying again?";
                        insertView.onDeleteDealError(msg);

                    }
                });
    }
}
