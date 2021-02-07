package com.boats.market.marven.dell.marven;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by dell on 6/20/2019.
 */

public class AboutFragment extends Fragment {
    TextView ratingtxt, sharingtxt, storebrachestxt, facepagetxt, policytxt, termstxt, call, showlove, info, policy, callustxt;
    ImageView ratingimg, sharingimg, storebrachesimg, facepageimg, policyimg, termsimg;
    CardView callus_card;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_fragment, container, false);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "gotham.ttf");


        HomeActivity.currentFragment = "Fragment_7";
        HomeActivity.categoryname.setText("About");
        callus_card = v.findViewById(R.id.callus_card);
        callustxt = v.findViewById(R.id.call_us_txt);
        ratingtxt = v.findViewById(R.id.rating_txt);
        sharingtxt = v.findViewById(R.id.sharing_txt);
        storebrachestxt = v.findViewById(R.id.storebranches_txt);
        facepagetxt = v.findViewById(R.id.facepage_txt);
        policytxt = v.findViewById(R.id.policy_txt);
        termstxt = v.findViewById(R.id.terms_txt);
        call = v.findViewById(R.id.callus);
        info = v.findViewById(R.id.info);
        policy = v.findViewById(R.id.policy);
        showlove = v.findViewById(R.id.showyourlove);


        ratingtxt.setTypeface(typeface);
        sharingtxt.setTypeface(typeface);
        storebrachestxt.setTypeface(typeface);
        facepagetxt.setTypeface(typeface);
        policytxt.setTypeface(typeface);
        termstxt.setTypeface(typeface);
        call.setTypeface(typeface);
        info.setTypeface(typeface);
        policy.setTypeface(typeface);
        showlove.setTypeface(typeface);
        callustxt.setTypeface(typeface);

        ratingimg = v.findViewById(R.id.rating_img);
        sharingimg = v.findViewById(R.id.sharing_img);
        storebrachesimg = v.findViewById(R.id.storebranches_image);
        facepageimg = v.findViewById(R.id.facepage_img);
        policyimg = v.findViewById(R.id.policy_img);
        termsimg = v.findViewById(R.id.terms_img);

        RelativeLayout facebook = v.findViewById(R.id.stores_laout);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.facebook.com/Bandity.eg/?ref=aymt_homepage_panel&eid=ARAC2vQtYdswLuToYZU9vVcAn2cE4PFNriBiqrrRkLSA-cX84umFrEie8Fr7WBJ9298sulT_goYd6DlC"));
                startActivity(new Intent(intent));


            }
        });
        RelativeLayout instagram = v.findViewById(R.id.page_layout);
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://l.facebook.com/l.php?u=https%3A%2F%2Fwww.instagram.com%2Fbandity.eg%2F%3Ffbclid%3DIwAR1xmCFHo2YWo66fZ8gxPlFLuk23VTOuYlIHhATreuPiEYYDIVUGtPf2NmE&h=AT0lLD9dM3t6SSoEHORNHvZkuIqnRJ5WaJrBUY0EzIsia9rLuX0319ucO_YhmrP6H18atChLK_3pjqBBU4Zc0EviU1wwAFHR7qCTJ7D1Nag-zRnGtl7XxcKxU75w5_36BG0uWw"));
                startActivity(new Intent(intent));
            }
        });



        RelativeLayout rating_layout = v.findViewById(R.id.rate_layout);
        rating_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));

                }
            }
        });
        RelativeLayout share_layout = v.findViewById(R.id.share_layout);

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Marven" + "\n" + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                startActivity(Intent.createChooser(intent, "Share App.."));
            }
        });


        callus_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 0);
                } else {
                    makePhoneCall();

                }
            }
        });

        RelativeLayout refundPolicy = v.findViewById(R.id.policy_layout);
        refundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://sites.google.com/view/marvenapp-refund-policy/home"));
                startActivity(new Intent(intent));
            }
        });


        RelativeLayout termsOfUse = v.findViewById(R.id.termsOfUse_layout);
        termsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://sites.google.com/view/marvenapp-terms-of-use/home"));
                startActivity(new Intent(intent));
            }
        });
        return v;
    }


    public void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:01551104560"));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(getActivity(), "Permession DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
