package com.jwx.travelmantics.services;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jwx.travelmantics.constants.Constants;
import com.jwx.travelmantics.deal_listing.ListActivity;

import java.util.Arrays;
import java.util.List;

public class FirebaseApiService {
    private static final String TAG = "FirebaseApiService";

    private FirebaseApiService() {}

    private static FirebaseAuth fbAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    public static FirebaseStorage firebaseStorage;
    public static StorageReference storageReference;
    private static final int RC_SIGN_IN = 123;
    public static boolean isAdmin;


    public static DatabaseReference getFbRootRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAuth getFbAuthService() {
        return FirebaseAuth.getInstance();
    }

    public static void initAuthState(final ListActivity caller) {
        fbAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (fbAuth.getCurrentUser() == null) {
                    FirebaseApiService.signIn(caller);
                    return;
                }

                String uid = firebaseAuth.getUid();
                checkAdmin(uid, caller);
            }
        };
    }

    public static void initStorage() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("deal_images");
    }

    public static StorageReference getStorageRootRef() {
        firebaseStorage = FirebaseStorage.getInstance();
        return  firebaseStorage.getReference().child("deal_images");
    }

    private static void checkAdmin(String uid, final ListActivity caller) {
        isAdmin = false;
        FirebaseApiService.getFbRootRef().child(Constants.ADMIN_NODE).child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                isAdmin = true;
                caller.showMenu();
                Log.i(TAG, "onChildAdded: You are an Administrator");
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

    private static void signIn(Activity callerActivity) {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        // Create and launch sign-in intent
        callerActivity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static void addAuthListener() {
        fbAuth.addAuthStateListener(authStateListener);
    }

    public static void removeAuthListener() {
        fbAuth.removeAuthStateListener(authStateListener);
    }
}
