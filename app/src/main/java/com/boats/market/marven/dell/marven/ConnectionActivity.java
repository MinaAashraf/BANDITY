package com.boats.market.marven.dell.marven;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");

        Toolbar toolbar = findViewById(R.id.custom_toolbar3);
        setSupportActionBar(toolbar);
        ImageView back = toolbar.findViewById(R.id.back_icon);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView title = toolbar.findViewById(R.id.customcategort_name);
        title.setTypeface(typeface);

        final CheckConnection checkConnection = new CheckConnection(this);
        TextView tryAgain = findViewById(R.id.tryagain);
        SpannableString spannableString = new SpannableString(tryAgain.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if (checkConnection.isConnecting()) {
                    ConnectionActivity.super.onBackPressed();
                } else
                    Toast.makeText(ConnectionActivity.this, "No internet connection..", Toast.LENGTH_SHORT).show();
            }
        };

        tryAgain.setTypeface(typeface);
        spannableString.setSpan(clickableSpan,tryAgain.getText().toString().lastIndexOf('t'),tryAgain.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tryAgain.setText(spannableString);
        tryAgain.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting()) {
            if (getIntent().hasExtra("from"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exit").setMessage("Do you want to exit ?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                        finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
            else{
            startActivity(new Intent(ConnectionActivity.this, HomeActivity.class));
            finish();
            }
        }
        else
            super.onBackPressed();
    }
}
