package com.example.travelmatrics;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Main2Activity extends AppCompatActivity {

    public static FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    EditText txtTitle;
    EditText txtPrice;
    EditText txtDescription;
    ImageView imageView;
    Button btnImage;
    TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        txtTitle = findViewById(R.id.Title_Text);
        txtPrice = findViewById(R.id.Price_Text);
        txtDescription= findViewById(R.id.Description_text);
        imageView = findViewById(R.id.upload_imageView);

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("deals");

        if(deal == null){
            deal = new TravelDeal();
        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
        showImage(deal.getImageUrl());
        btnImage = findViewById(R.id.select_button);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg" );
                intent.putExtra(intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "choose from listed"),42 );
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            //noinspection SimplifiableIfStatement
            case R.id.save_menu:
                saveNote();
                Toast.makeText(this, "Deal Saved", Toast.LENGTH_SHORT).show();
                clean();
                back();
                return true;
            case R.id.delete_menu:
                delete();
                Toast.makeText(this, "Deal deleted", Toast.LENGTH_SHORT).show();
                back();
                return true;
            case R.id.logout_menu:
                mFirebaseAuth.getInstance().signOut();
                //logOut();
                Toast.makeText(this, "signing out", Toast.LENGTH_SHORT).show();
                // back();
                default:
            return super.onOptionsItemSelected(item);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 42 && resultCode == RESULT_OK ){
            Uri imageUri = data.getData();
            StorageReference ref = FirebaseUtil.mReference.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String uri = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                   // String pictureName = taskSnapshot.getStorage().getPath();
                    deal.setImageUrl(uri);
//                  //deal.setImageUrl(url);
//                    deal.setImageName(pictureName);
//                    Log.d("Url: ", url);
//                    Log.d("Name", pictureName);
                   showImage(uri);
                }
            });

        }
    }

    private void showImage(String url) {
        if(url != null && url.isEmpty() == false){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(url)
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(imageView);
        }
    }

    private void clean() {
        txtTitle.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        txtTitle.requestFocus();

    }

    private void saveNote() {

        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());

        if(deal.getId() == null){
            mDatabaseReference.push().setValue(deal);
        }else{
            mDatabaseReference.child(deal.getId()).setValue(deal);
        }


    }
    private void delete(){
        if(deal == null){
            Toast.makeText(this,"do you want to delete", Toast.LENGTH_LONG).show();
            return;
        }
        mDatabaseReference.child(deal.getId()).removeValue();
    }

    private void back(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
    }


