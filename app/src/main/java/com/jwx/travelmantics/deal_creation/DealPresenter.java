package com.jwx.travelmantics.deal_creation;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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
    private static final String TAG = "DealPresenter";

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

    void deleteDeal(final TravelDeal travelDeal) {
        fbRootRef.child(Constants.TRAVEL_DEAL_NODE).child(travelDeal.getUid()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Deal Deleted Successfully";
                        if (task.isSuccessful()) {
                            insertView.onDeleteDeal(msg);
                            String imageName = travelDeal.getImageName();
                            if (imageName != null && !imageName.isEmpty()) {
                                Log.i(TAG, "deleteDeal(): Image has storage name!"+imageName);
                                deleteDealImage(travelDeal.getImageName());
                            } else {
                                Log.i(TAG, "deleteDeal(): Image has NO storage name!"+imageName);
                            }

                            return;
                        }
                        msg = "Unable to Delete deal. Mind trying again?";
                        insertView.onDeleteDealError(msg);

                    }
                });
    }

    private void deleteDealImage(final String imageName) {
        StorageReference ref = FirebaseApiService.getStorageRootRef().child(imageName);
        ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "deleteDealImage -> onComplete: Image deleted successfully");
                    return;
                }

                if (task.getException() != null) {
                    String exceptionMessage = task.getException().getMessage();
                    Log.i(TAG, "deleteDealImage -> onComplete: "+exceptionMessage);
                }
                Log.i(TAG, "deleteDealImage -> onComplete: Unable to delete image");
            }
        });
    }


    void uploadImage(Uri imageUri) {
        if(imageUri == null || imageUri.getLastPathSegment() == null) {
            return;
        }
        final StorageReference filePath = FirebaseApiService.getStorageRootRef()
                .child(Constants.DEAL_IMAGE_FOLDER_NODE)
                .child(imageUri.getLastPathSegment());

        Task<UploadTask.TaskSnapshot> uploadTask = filePath.putFile(imageUri);

        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task){
                return filePath.getDownloadUrl();
            }
        });

        uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Image Successfully Uploaded. Ensure to SAVE the update",
                            Toast.LENGTH_LONG).show();

                    String imgUrl = task.getResult().toString();
                    String imageName = task.getResult().getLastPathSegment();

                    Log.i(TAG, "onComplete Url: "+imgUrl);
                    Log.i(TAG, "onComplete Path: "+imageName);

                    insertView.onImageUploadSuccess(imgUrl, imageName);
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
