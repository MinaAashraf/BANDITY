package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SplachActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);
        final ImageView logo = findViewById(R.id.logo_image);


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplachActivity.this, HomeActivity.class);

               /* Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(SplachActivity.this
                        , Pair.create((View)logo, ViewCompat.getTransitionName(logo))).toBundle();
                startActivity(intent,bundle);
              //  ActivityCompat.finishAfterTransition(SplachActivity.this);*/
                startActivity(intent);

            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 2500);


    }


}
