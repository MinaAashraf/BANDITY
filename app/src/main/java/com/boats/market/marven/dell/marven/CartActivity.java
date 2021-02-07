package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    ImageView cart_image, add_icon, remove_icon;
    TextView cart_sort, cart_sale, cart_price, cart_total_price, item_num;
    LinearLayout emptytxt;
    TextView txt1, txt2, txt3;
    ListView listView;
    float total = 0;
    TextView totalfinal;
    static Activity fa;
    Typeface typeface;
    ArrayList<ProductModel> arrayList = new ArrayList<>();
    CardView cardView;
    TextView remove_fab, edit_fab;

    public class MyArrayAdapter extends ArrayAdapter {

        public MyArrayAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);


        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.cartitem, parent, false);

            cart_image = v.findViewById(R.id.cart_imge);
            cart_sort = v.findViewById(R.id.cart_sort);
            cart_sale = v.findViewById(R.id.cart_sale);
            cart_price = v.findViewById(R.id.cart_price);
            cart_total_price = v.findViewById(R.id.cart_total_price);
            add_icon = v.findViewById(R.id.add_icon);
            remove_icon = v.findViewById(R.id.remove_icon);
            item_num = v.findViewById(R.id.item_num);
            remove_fab = v.findViewById(R.id.remove_fab);
            edit_fab = v.findViewById(R.id.edit_fab);
            txt1 = v.findViewById(R.id.txt1);
            txt2 = v.findViewById(R.id.txt2);
            txt3 = v.findViewById(R.id.txt3);

            txt1.setTypeface(typeface);
            txt2.setTypeface(typeface);
            txt3.setTypeface(typeface);
            cart_sort.setTypeface(typeface);
            cart_sale.setTypeface(typeface);
            cart_total_price.setTypeface(typeface);
            cart_price.setTypeface(typeface);

            cart_sort.setText(arrayList.get(position).getSort());
            Picasso.get().load(arrayList.get(position).getUrl()).placeholder(R.drawable.home_logo).into(cart_image);
            if (arrayList.get(position).getSale().equals(""))
                cart_sale.setText("" + arrayList.get(position).getSale());
            else
                cart_sale.setText("EGP " + arrayList.get(position).getSale());

            cart_price.setText("EGP " + arrayList.get(position).getPrice());
            add_icon.setImageResource(arrayList.get(position).getAddicon());

            item_num.setText("" + arrayList.get(position).getItemnum());
            if (arrayList.get(position).getTotalprice().equals("0")) {
                cart_total_price.setText(arrayList.get(position).getPrice());

            } else
                cart_total_price.setText(arrayList.get(position).getTotalprice());

            add_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int counter = arrayList.get(position).getItemnum() + 1;

                    SharedPreferences sharedPreferences = getSharedPreferences("cart_counter", 0);
                    SharedPreferences.Editor editor2 = sharedPreferences.edit();
                    int count = sharedPreferences.getInt("count", 0);
                    editor2.putInt("count", ++count).apply();

                    arrayList.get(position).setItemnum(counter);
                    arrayList.get(position).setTotalprice("" + (counter * Float.valueOf(arrayList.get(position).getPrice())));

                    total = 0;
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (!arrayList.get(i).getTotalprice().equals("0"))
                            total += Float.valueOf(arrayList.get(i).getTotalprice());
                        else
                            total += Float.valueOf(arrayList.get(i).getPrice());
                    }
                    totalfinal.setText("" + total);

                    notifyDataSetChanged();
                    SharedPreferences.Editor editor = getSharedPreferences("cart", 0).edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(arrayList);
                    editor.putString("json", json);
                    editor.apply();

                }
            });

            remove_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (arrayList.get(position).getItemnum() > 1) {
                        int counter = arrayList.get(position).getItemnum() - 1;

                        arrayList.get(position).setItemnum(counter);
                        arrayList.get(position).setTotalprice("" + (counter * Float.valueOf(arrayList.get(position).getPrice())));

                        SharedPreferences sharedPreferences = getSharedPreferences("cart_counter", 0);
                        SharedPreferences.Editor editor2 = sharedPreferences.edit();
                        int count = sharedPreferences.getInt("count", 0);
                        editor2.putInt("count", --count).apply();

                        notifyDataSetChanged();

                        total = 0;
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (!arrayList.get(i).getTotalprice().equals("0"))
                                total += Float.valueOf(arrayList.get(i).getTotalprice());
                            else
                                total += Float.valueOf(arrayList.get(i).getPrice());
                        }
                        totalfinal.setText("" + total);

                        SharedPreferences.Editor editor = getSharedPreferences("cart", 0).edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(arrayList);
                        editor.putString("json", json);
                        editor.apply();
                    }
                }
            });

            edit_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(CartActivity.this, ProductView.class).putExtra("position", position).putExtra("model", arrayList.get(position)).putExtra("from", "cart"));
                    finish();
                }
            });

            remove_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences sharedPreferences2 = getSharedPreferences("cart_counter", 0);
                    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                    int count = 0;
                    for (ProductModel model : arrayList) {
                        count += model.getItemnum();
                    }
                    editor2.putInt("count", count - arrayList.get(position).getItemnum()).apply();
                    arrayList.remove(position);


                    notifyDataSetChanged();

                    SharedPreferences sharedPreferences = getSharedPreferences("cart", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(arrayList);
                    editor.putString("json", json);
                    editor.apply();

                    total = 0;
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (!arrayList.get(i).getTotalprice().equals("0"))
                            total += Float.valueOf(arrayList.get(i).getTotalprice());
                        else
                            total += Float.valueOf(arrayList.get(i).getPrice());
                    }
                    totalfinal.setText("" + total);

                    if (arrayList.size() == 0) {
                        emptytxt.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        cardView.setVisibility(View.GONE);


                    } else {
                        emptytxt.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);

                    }
                }
            });

            return v;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");

        fa = this;
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        TextView categoryname = toolbar.findViewById(R.id.customcategort_name);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
        cardView = findViewById(R.id.bottoncard);
        ImageView search_icon = toolbar.findViewById(R.id.search_icon);
        search_icon.setVisibility(View.GONE);
        RelativeLayout relativeLayout = toolbar.findViewById(R.id.relativeview);
        RelativeLayout relativeLayout2 = toolbar.findViewById(R.id.relative);
        relativeLayout.removeView(relativeLayout2);
        categoryname.setText("CART");

        categoryname.setTypeface(typeface);


        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
            }
        });

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        SharedPreferences sharedPreferences2 = getSharedPreferences("cart", 0);
        Gson gson2 = new Gson();
        String json2 = sharedPreferences2.getString("json", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        if (json2 != null)
            arrayList = gson2.fromJson(json2, type);


        totalfinal = findViewById(R.id.totalfinal);
        for (int i = 0; i < arrayList.size(); i++) {
            if (!arrayList.get(i).getTotalprice().equals("0"))
                total += Float.valueOf(arrayList.get(i).getTotalprice());
            else
                total += Float.valueOf(arrayList.get(i).getPrice());
        }
        totalfinal.setText("" + total);

        MyArrayAdapter adapter = new MyArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);


        emptytxt = findViewById(R.id.noitem);
        listView = findViewById(R.id.cart_list);
        listView.setAdapter(adapter);
        TextView additem = findViewById(R.id.aadditems_txt);
        TextView checkout = findViewById(R.id.checkout_txt);
        TextView noitemtxt = findViewById(R.id.emptytxt);
        noitemtxt.setTypeface(typeface);
        Button startShopping = findViewById(R.id.startshopping_txt);
        startShopping.setTypeface(typeface);
        startShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
                finish();
            }
        });

        additem.setTypeface(typeface);
        checkout.setTypeface(typeface);
        totalfinal.setTypeface(typeface);

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
                finish();
            }
        });

        additem.setTypeface(typeface);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(CartActivity.this, "Please Sign In First..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CartActivity.this, HomeActivity.class).putExtra("span", "register"));
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("total", 0).edit();
                    editor.putString("totalvalue", totalfinal.getText().toString()).apply();
                    startActivity(new Intent(CartActivity.this, CheckoutActivity.class).putExtra("from", "cart"));
                }
            }
        });

        if (arrayList.size() == 0) {
            emptytxt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);

        } else {
            emptytxt.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        }


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
