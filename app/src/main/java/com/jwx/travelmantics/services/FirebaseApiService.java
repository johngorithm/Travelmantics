package com.jwx.travelmantics.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseApiService {

    private FirebaseApiService() {
        // Utility class
    }

    public static DatabaseReference getFbRootRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAuth getFbAuthService() {
        return FirebaseAuth.getInstance();
    }
}
