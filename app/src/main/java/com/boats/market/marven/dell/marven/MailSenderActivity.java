package com.boats.market.marven.dell.marven;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.net.URL;

public class MailSenderActivity extends AppCompatActivity {
    boolean flag;
    String attachmentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_sender);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this,ConnectionActivity.class));

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        ImageView cart = toolbar.findViewById(R.id.cart_icon);
        TextView categoryname = toolbar.findViewById(R.id.customcategort_name);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);

        categoryname.setTypeface(typeface);

        categoryname.setText("SEND EMAIL");
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MailSenderActivity.this, CartActivity.class));

            }
        });

        final TextView to = findViewById(R.id.to_txt);
        if (getIntent().hasExtra("email"))
            to.setText(getIntent().getExtras().getString("email"));
        else if (getIntent().hasExtra("total"))
            to.setText("All Users");

to.setTypeface(typeface);

        final EditText subject = findViewById(R.id.subject_edit);
            final EditText message = findViewById(R.id.message_edit);
            Button send = findViewById(R.id.send);

            subject.setTypeface(typeface);

            send.setTypeface(typeface);

            send.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    flag = true;
                    if (subject.getText().toString().isEmpty()) {
                        subject.setError("Required");
                        subject.requestFocus();
                        flag = false;
                    }

                    if (message.getText().toString().isEmpty()) {
                        message.setError("Required");
                        message.requestFocus();
                        flag = false;
                    }

                    if (flag) {
                        SendMail sendMail = new SendMail();
                        if (to.getText().toString().equals("All Users")) {
                            sendMail.sendEmail(getIntent().getExtras().getString("total"), subject.getText().toString(),"<div style=\"background-color: #6495ED;padding: 5px;height: 280px\"><img src=\"https://firebasestorage.googleapis.com/v0/b/marven-6c727.appspot.com/o/BANDITY-outlet-logo.png?alt=media&token=94020be0-ce7e-46b6-b393-b7d274b6fe4e\" style=\"margin-left: 5px ; width:150px;height:150px \"><h2 style=\"margin-top: 5px ; color:#000000\">" + message.getText() + "</h2></div>");
                        } else {
                            sendMail.sendEmail(to.getText().toString(), subject.getText().toString(),"<div style=\"background-color: #6495ED;padding: 5px;height: 280px\"><img src=\"https://firebasestorage.googleapis.com/v0/b/marven-6c727.appspot.com/o/BANDITY-outlet-logo.png?alt=media&token=94020be0-ce7e-46b6-b393-b7d274b6fe4e\" style=\"margin-left: 5px ; width:150px;height:150px \"><h2 style=\"margin-top: 5px ; color:#000000\">" + message.getText() + "</h2></div>");
                        }

                        Toast.makeText(MailSenderActivity.this, "Sent email successfully....", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }


    @Override
    public void onBackPressed() {
        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this,ConnectionActivity.class));
        else
            super.onBackPressed();
    }


    }
