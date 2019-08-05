package com.jwx.travelmantics.deal_creation;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jwx.travelmantics.constants.Constants;
import com.jwx.travelmantics.models.TravelDeal;
import com.jwx.travelmantics.services.FirebaseApiService;

class DealPresenter {

    private DatabaseReference fbRootRef;
    private InsertView insertView;
    private Context context;

    DealPresenter(InsertView view){
        insertView = view;
        fbRootRef = FirebaseApiService.getFbRootRef();
        context = (Context) view;
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


    void uploadImage(Uri imageUri) {
        if(imageUri == null || imageUri.getLastPathSegment() == null) {
            return;
        }
        final StorageReference filePath = FirebaseApiService.getStorageRootRef()
                .child(imageUri.getLastPathSegment());

        Task<UploadTask.TaskSnapshot> uploadTask = filePath.putFile(imageUri);

        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task){
                if (!task.isSuccessful()) {
                    String msg = task.getException().getMessage();
                    insertView.onSaveError(msg);
                }
                return filePath.getDownloadUrl();
            }
        });

        uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Image Successfully Uploaded", Toast.LENGTH_LONG).show();
                    Uri downloadUri = task.getResult();
                    String imgUrl = downloadUri.toString();
                    insertView.onImageUploadSuccess(imgUrl);
                } else if (task.getException() != null){
                    String msg = task.getException().getMessage();
                    insertView.onSaveError(msg);
                } else {
                    insertView.onSaveError("Unable to get image link");
                }
            }
        });
    }
}
