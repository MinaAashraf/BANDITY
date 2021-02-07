package com.boats.market.marven.dell.marven;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 6/27/2019.
 */

public class FavouriteFragment extends Fragment {
    ArrayList<ProductModel> arrayList = new ArrayList<>();
    ImageView removeimg;
    Typeface typeface;
    ListView listView;
    RelativeLayout emptyRelative;
RatingBar ratingBar ;

    public class MyAdapter extends ArrayAdapter {
        public MyAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.listitem, parent, false);
            HomeActivity.categoryNameString = "Favourites";
            ImageView proimg = v.findViewById(R.id.pro_image);
            TextView prosort = v.findViewById(R.id.thesort);
            TextView prosale = v.findViewById(R.id.sale);
            TextView proprice = v.findViewById(R.id.price);
            TextView sale_percent = v.findViewById(R.id.sale_percent);
            TextView egp = v.findViewById(R.id.egp2);
            String sale = arrayList.get(position).getSale();
            String price = arrayList.get(position).getPrice();
            removeimg = v.findViewById(R.id.remove_fav);
            removeimg.setVisibility(View.VISIBLE);
            ratingBar = v.findViewById(R.id.rating_bar);
            ratingBar.setRating(arrayList.get(position).getRating().getRate()/arrayList.get(position).getRating().getRateNum());
            ImageView favourite_icon = v.findViewById(R.id.make_favourite);
            favourite_icon.setVisibility(View.GONE);
            proprice.setTypeface(typeface);
            prosale.setTypeface(typeface);
            prosort.setTypeface(typeface);
            LinearLayout linearLayout = v.findViewById(R.id.mylayout);
            linearLayout.setVisibility(View.GONE);
            Picasso.get().load(arrayList.get(position).getUrl()).placeholder(R.drawable.home_logo).into(proimg);
            prosort.setText(arrayList.get(position).getSort());
            proprice.setText("" + price);
            if (sale.equals("")){
                prosale.setVisibility(View.GONE);
                sale_percent.setVisibility(View.GONE);
                egp.setVisibility(View.GONE);
            }
            else{
                prosale.setText("" + sale);
                sale_percent.setText((int) ((Float.valueOf(sale) - Float.valueOf(price)) / Float.valueOf(sale) * 100) + "% OFF");
            }


            removeimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    arrayList.remove(position);
                    notifyDataSetChanged();

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fav", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(arrayList);
                    editor.putString("json", json);
                    editor.apply();


                    if (arrayList.size() == 0) {
                        emptyRelative.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    } else {
                        emptyRelative.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }

                }
            });


            return v;

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favouritesfragment, container, false);
        HomeActivity.currentFragment = "Fragment_6";
        HomeActivity.categoryname.setText("Favourites");


        listView = v.findViewById(R.id.listview);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "gotham.ttf");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fav", 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("json", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        if (json != null)
            arrayList = gson.fromJson(json, type);

        MyAdapter adapter = new MyAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        emptyRelative = v.findViewById(R.id.emptytxt);

        if (arrayList.size() == 0) {
            emptyRelative.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            emptyRelative.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        TextView noitemTxt = v.findViewById(R.id.noitemtxt);
        Button startShopping = v.findViewById(R.id.startshopping_txt);
        noitemTxt.setTypeface(typeface);
        startShopping.setTypeface(typeface);
        startShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), ProductView.class).putExtra("model", (Serializable) arrayList.get(i)));

            }
        });
        return v;
    }
}
