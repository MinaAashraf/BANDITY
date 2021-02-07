package com.boats.market.marven.dell.marven;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dell on 6/19/2019.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView product_image, makefavouriteimg, close_icon, edit_icon, addIcon, control_icon;
    TextView sort_text, sale_text, price_text, sale_percent,egp;
    RatingBar ratingBar;

    View item;
    int counter = 0;

    public MyViewHolder(View itemView) {
        super(itemView);
        item = itemView;
    }

    public void setData(String url, String sort, String sale, String price,float rating) {
        product_image = item.findViewById(R.id.pro_image);
        sort_text = item.findViewById(R.id.thesort);
        sale_text = item.findViewById(R.id.sale);
        sale_percent = item.findViewById(R.id.sale_percent);
        price_text = item.findViewById(R.id.price);
        close_icon = item.findViewById(R.id.close_icon);
        edit_icon = item.findViewById(R.id.proEdit_icon);
        addIcon = item.findViewById(R.id.addToMonaspa);
        makefavouriteimg = item.findViewById(R.id.make_favourite);
        ratingBar = item.findViewById(R.id.rating_bar);
        egp=item.findViewById(R.id.egp2);
        ratingBar.setRating(rating);
        Picasso.get().load(url).placeholder(R.drawable.home_logo).into(product_image);
        sort_text.setText(sort);
        if (sale.equals("")) {
            sale_text.setVisibility(View.GONE);
            sale_percent.setVisibility(View.GONE);
            egp.setVisibility(View.GONE);
        } else {
            sale_text.setText(sale);
            sale_percent.setText((int) ((Float.valueOf(sale) - Float.valueOf(price)) / Float.valueOf(sale) * 100) + "% OFF");
        }
        price_text.setText(price);

        control_icon = item.findViewById(R.id.controlles);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference3.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("admin")) {
                        control_icon.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


}