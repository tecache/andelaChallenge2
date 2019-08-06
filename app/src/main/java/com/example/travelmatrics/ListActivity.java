package com.example.travelmatrics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;

    public ListActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /* FirebaseUtil.openReference("traveldeals");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TextView txtDeals = findViewById(R.id.textView2);
                TravelDeal mTravelDeals = dataSnapshot.getValue(TravelDeal.class);
                txtDeals.setText(txtDeals.getText() + "\n" +mTravelDeals.getTitle());


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildListener);*/

       FirebaseUtil.openReference("traveldeals", this);
        RecyclerView rvDeals = findViewById(R.id.rv_deals);
        final DataAdaptor adaptor = new DataAdaptor();
        rvDeals.setAdapter(adaptor);
        LinearLayoutManager dealsLayout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvDeals.setLayoutManager(dealsLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.back_button:
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        FirebaseUtil.detachListener();
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        FirebaseUtil.attachListenet();
//    }
}
