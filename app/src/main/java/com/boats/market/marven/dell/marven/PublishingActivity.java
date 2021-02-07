package com.boats.market.marven.dell.marven;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;

public class PublishingActivity extends AppCompatActivity {
    ArrayList<String> publishCategories = new ArrayList<>();
    ArrayList<String> unpublishCategories = new ArrayList<>();
    String item;
    String item2;

    int cart_counter;
    android.support.v7.widget.Toolbar toolbar;
    TextView cart_counter_txt;
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("cart_counter", 0);
        cart_counter = sharedPreferences.getInt("count", 0);
        cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);
        cart_counter_txt.setText("" + cart_counter);

        if (cart_counter > 0) {
            cart_counter_txt.setVisibility(View.VISIBLE);
        } else {
            cart_counter_txt.setVisibility(View.GONE);
        }
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        cart_counter_txt.setTypeface(typeface);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publishing);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        TextView categoryname = toolbar.findViewById(R.id.customcategort_name);
        ImageView cartimage = toolbar.findViewById(R.id.cart_icon);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
         cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);

        cart_counter_txt.setText(""+cart_counter);
        cart_counter_txt.setTypeface(typeface);
        categoryname.setTypeface(typeface);
        categoryname.setText("Publishing Categories");
        cartimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PublishingActivity.this, CartActivity.class));
            }
        });

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final Spinner publishSpinner = findViewById(R.id.pub_spinner);
        final Spinner unpublishSpinner = findViewById(R.id.unpub_spinner);
        Button publish = findViewById(R.id.publish);
        Button unpublish = findViewById(R.id.unpublish);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        publish.setTypeface(typeface);
        unpublish.setTypeface(typeface);

        final ArrayAdapter publishAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, publishCategories);
        publishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter unpublishAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, unpublishCategories);
        unpublishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        databaseReference.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if (dataSnapshot.hasChild("subCat")) {

                    CategoryModel2 model = dataSnapshot.getValue(CategoryModel2.class);
                    if (model.getUrlAndPublish().isPublish()) {
                        unpublishCategories.add(key);
                        unpublishAdapter.notifyDataSetChanged();

                    } else {
                        publishCategories.add(key);
                        publishAdapter.notifyDataSetChanged();

                    }
                } else {
                    CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
                    if (model.getUrlAndPublish().isPublish()) {
                        unpublishCategories.add(key);
                        unpublishAdapter.notifyDataSetChanged();

                    } else {
                        publishCategories.add(key);
                        publishAdapter.notifyDataSetChanged();

                    }
                }
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
        });
        publishSpinner.setAdapter(publishAdapter);
        unpublishSpinner.setAdapter(unpublishAdapter);

        publishSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item = publishCategories.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                item = publishCategories.get(0);
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PublishingActivity.this);
                builder.setMessage("Do you want to publish '" + item + "' category ?").setTitle("Publish Category").setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child("categories").child(item).child("urlAndPublish").child("publish").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PublishingActivity.this, "Published", Toast.LENGTH_SHORT).show();
                                publishCategories.remove(item);
                                publishAdapter.notifyDataSetChanged();
                                unpublishCategories.add(item);
                                unpublishAdapter.notifyDataSetChanged();
                                publishSpinner.setAdapter(publishAdapter);
                                unpublishSpinner.setAdapter(unpublishAdapter);
                            }
                        });

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });


        unpublishSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item2 = unpublishCategories.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                item2 = unpublishCategories.get(0);

            }
        });

        unpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PublishingActivity.this);
                builder.setMessage("Do you want to unpublish '" + item2 + "' category ?").setTitle("UnPublish Category").setPositiveButton("UnPublish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child("categories").child(item2).child("urlAndPublish").child("publish").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PublishingActivity.this, "UnPublished", Toast.LENGTH_SHORT).show();
                                unpublishCategories.remove(item2);
                                unpublishAdapter.notifyDataSetChanged();
                                publishCategories.add(item2);
                                publishAdapter.notifyDataSetChanged();
                                publishSpinner.setAdapter(publishAdapter);
                                unpublishSpinner.setAdapter(unpublishAdapter);

                            }
                        });

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        else
            super.onBackPressed();
    }


}
