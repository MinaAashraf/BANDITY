package com.boats.market.marven.dell.marven;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.ui.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Quota;

public class UsersForAdmin extends AppCompatActivity {
    ArrayList<String> userNames = new ArrayList<>();
    ArrayList<String> userMails = new ArrayList<>();

    Adapter adapter;

    Typeface typeface;

    class Adapter extends ArrayAdapter {
        public Adapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.user_item, parent, false);
            TextView userName = view.findViewById(R.id.userName);
            final TextView userMail = view.findViewById(R.id.userMail);
            ImageView mail = view.findViewById(R.id.mail);
            userName.setText(userNames.get(position));
            userMail.setText(userMails.get(position));

            userMail.setTypeface(typeface);
            userName.setTypeface(typeface);


            mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(UsersForAdmin.this, MailSenderActivity.class).putExtra("email", userMails.get(position)));

                }
            });

            return view;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_for_admin);
        typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        final ListView listView = findViewById(R.id.usersList);
        adapter = new Adapter(UsersForAdmin.this, android.R.layout.simple_list_item_1, userNames);
        listView.setAdapter(adapter);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        ImageView sendEmailToTotal = toolbar.findViewById(R.id.cart_icon);
        TextView categoryname = toolbar.findViewById(R.id.customcategort_name);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
        TextView counter = toolbar.findViewById(R.id.cart_counter_txt);
        counter.setVisibility(View.GONE);
        sendEmailToTotal.setImageResource(R.drawable.mail_icon);
        sendEmailToTotal.setColorFilter(Color.parseColor("#ffffff"));
        categoryname.setText("USERS");
        categoryname.setTypeface(typeface);

        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UsersForAdmin.this, SearchActivity.class));
                finish();
            }
        });

        sendEmailToTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recypients = userMails.toString();
                startActivity(new Intent(UsersForAdmin.this, MailSenderActivity.class).putExtra("total", recypients.substring(1, recypients.length() - 1)));

            }
        });

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        databaseReference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userNames.add(dataSnapshot.child("name").getValue().toString());
                userMails.add(dataSnapshot.child("email").getValue().toString());
                adapter.notifyDataSetChanged();

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
