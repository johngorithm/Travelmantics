package com.jwx.travelmantics.services;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class FirebaseApiService {

    private FirebaseApiService() {}

    private static FirebaseAuth fbAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static final int RC_SIGN_IN = 123;


    public static DatabaseReference getFbRootRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAuth getFbAuthService() {
        return FirebaseAuth.getInstance();
    }

    public static void initAuthState(final Activity caller) {
        fbAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (fbAuth.getCurrentUser() == null) {
                    FirebaseApiService.signIn(caller);
                }
            }
        };
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
