package com.example.travelmatrics;


import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil  {

    public static FirebaseStorage mStorage;
    public static StorageReference mReference;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil mFirebase;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mListener;
    public static ArrayList<TravelDeal> mDeals;
    private static final int RC_SIGN_IN = 123;
    private static ListActivity caller;
    //private static Activity caller;


    private FirebaseUtil(){    }
   public static void openReference(String ref, final ListActivity callActivity ){
      if(mFirebase == null) {
          mFirebase = new FirebaseUtil();
          mFirebaseDatabase = FirebaseDatabase.getInstance();
          mFirebaseAuth = mFirebaseAuth.getInstance();
      }
           caller = callActivity;
            mListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    // Choose authentication providers
                    FirebaseUtil.signIn();
                   // Toast.makeText(callActivity.getBaseContext(),"We are creating an connection", Toast.LENGTH_LONG).show();
                }
            };

        mDeals = new ArrayList<>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
        connectStorage();
   }

//   public static void attachListenet(){
//        mFirebaseAuth.addAuthStateListener(mListener);
//   }
//   public static void detachListener(){
//        mFirebaseAuth.removeAuthStateListener(mListener);
//   }
   public static void connectStorage(){
        mStorage = FirebaseStorage.getInstance();
        mReference = mStorage.getReference().child("deals_pictures");
   }
     public static void signIn(){
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());



    //Create and launch sign-in intent
            caller.startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }
   }
