package com.example.travelmatrics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdaptor extends RecyclerView.Adapter<DataAdaptor.DataView>{
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;
    private ImageView imageDeal;

    public DataAdaptor(){
      //  FirebaseUtil.openReference("traveldeal", Activity callActivity);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        this.deals = FirebaseUtil.mDeals;
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,String s) {
                TravelDeal mTravelDeals = dataSnapshot.getValue(TravelDeal.class);
               // Log.d("deal",mTravelDeals.getTitle());
                mTravelDeals.setId(dataSnapshot.getKey());
                deals.add(mTravelDeals);
                notifyItemInserted(deals.size()-1);

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
        mDatabaseReference.addChildEventListener(mChildListener);
    }

    @Override
    public DataView onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row,parent,false);
                return new DataView(itemView);
    }

    @Override
    public void onBindViewHolder(DataView holder, int position) {
        TravelDeal deal = deals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
                return deals.size();
    }

    public class DataView extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTitle;
        TextView txtdescription;
        TextView txtprice;


        public DataView(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtdescription = itemView.findViewById(R.id.txt_description);
            txtprice = itemView.findViewById(R.id.txt_price);
            imageDeal = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }
        public void bind(TravelDeal deal)
        {
            txtTitle.setText(deal.getTitle());
            txtdescription.setText(deal.getDescription());
            txtprice.setText(deal.getPrice());
            showImage(deal.getImageUrl());

            }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TravelDeal getPosition = deals.get(position);
            Intent intent = new Intent(view.getContext(),Main2Activity.class);
            intent.putExtra("deals", getPosition);
            view.getContext().startActivity(intent);

        }


        private void showImage(String url){
            if(url != null && url.isEmpty() == false){
                Picasso.with(imageDeal.getContext())
                        .load(url)
                        .resize(160,160)
                        .centerCrop()
                        .into(imageDeal);
            }
        }
    }
}