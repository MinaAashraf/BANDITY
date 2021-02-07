package com.boats.market.marven.dell.marven;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    ArrayList<ProductModel> arrayList = new ArrayList<>();

    int cart_counter;
    android.support.v7.widget.Toolbar toolbar;
    TextView cart_counter_txt;
    boolean equal;
    boolean flag = false;
    ArrayList<String> keys = new ArrayList<>();
    LinearLayout noResultLayout;
    ProgressBar progressBar;
    TextView noReesultTxt;
    int index = 0, productNum = 0;
    long childrenCount;
    Typeface typeface;


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
        typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        cart_counter_txt.setTypeface(typeface);
    }

    class Adapter extends ArrayAdapter {

        public Adapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.listitem, parent, false);
            HomeActivity.categoryNameString = "Favourites";
            ImageView proimg = v.findViewById(R.id.pro_image);
            TextView prosort = v.findViewById(R.id.thesort);
            TextView prosale = v.findViewById(R.id.sale);
            TextView proprice = v.findViewById(R.id.price);
            TextView sale_percent = v.findViewById(R.id.sale_percent);
            TextView egp = v.findViewById(R.id.egp2);
            RatingBar ratingBar = v.findViewById(R.id.rating_bar);
            ImageView removeimg = v.findViewById(R.id.make_favourite);
            String sale = arrayList.get(position).getSale();
            String price = arrayList.get(position).getPrice();
            removeimg.setVisibility(View.GONE);
            ratingBar.setRating(arrayList.get(position).getRating().getRate() / arrayList.get(position).getRating().getRateNum());
            Picasso.get().load(arrayList.get(position).getUrl()).into(proimg);
            prosort.setText(arrayList.get(position).getSort());
            proprice.setText("" + price);
            if (sale.equals("")) {
                prosale.setVisibility(View.GONE);
                sale_percent.setVisibility(View.GONE);
                egp.setVisibility(View.GONE);
            } else {
                prosale.setText("" + sale);
                sale_percent.setText((int) ((Float.valueOf(sale) - Float.valueOf(price)) / Float.valueOf(sale) * 100) + "% OFF");
            }

            return v;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);
        TextView categoryname = toolbar.findViewById(R.id.customcategort_name);
        final EditText searchEditText = toolbar.findViewById(R.id.search_edittext);
        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);

        noResultLayout = findViewById(R.id.noresult_layout);
        noReesultTxt = findViewById(R.id.noresult_txt);
        progressBar = findViewById(R.id.progress_bar);

        cart_counter_txt.setText("" + cart_counter);
        final Adapter adapter = new Adapter(this, android.R.layout.simple_list_item_1, arrayList);
        final ListView listView = findViewById(R.id.listview_search);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(SearchActivity.this, ProductView.class).putExtra("model", (Serializable) arrayList.get(i)));
            }
        });

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        categoryname.setVisibility(View.GONE);
        searchEditText.setVisibility(View.VISIBLE);
        searchIcon.setColorFilter(Color.parseColor("#292561"));
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, CartActivity.class));
            }
        });
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                noResultLayout.setVisibility(View.GONE);
                if (searchEditText.getText().toString().length() < 1) {
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                }
                if (searchEditText.getText().toString().length() >= 1) {
                    progressBar.setVisibility(View.VISIBLE);
                    final String word = searchEditText.getText().toString().toLowerCase().trim();
                    keys = new ArrayList<>();
                    index = 0;
                    childrenCount = 0;
                    databaseReference.child("categories").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            childrenCount = dataSnapshot.getChildrenCount();
                            index++;
                            productNum = 0;
                            if (dataSnapshot.hasChild("subCat")) {

                                dataSnapshot.child("subCat").getRef().addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        dataSnapshot.child("products").getRef().addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                productNum++;
                                                String key = dataSnapshot.getKey().toLowerCase().trim();
                                                if (word.length() <= key.length()) {
                                                    if (key.contains(word)) {
                                                        progressBar.setVisibility(View.GONE);
                                                        noResultLayout.setVisibility(View.GONE);
                                                        keys.add(dataSnapshot.getKey());
                                                        boolean exist = false;
                                                        for (ProductModel productModel : arrayList) {
                                                            if (productModel.getSort().equals(dataSnapshot.getKey())) {
                                                                exist = true;
                                                                break;
                                                            }

                                                        }
                                                        flag = false;
                                                        if (!exist) {
                                                            arrayList.add(dataSnapshot.getValue(ProductModel.class));
                                                            adapter.notifyDataSetChanged();
                                                            for (int i = 0; i < arrayList.size(); i++) {
                                                                for (int j = 0; j < keys.size(); j++) {
                                                                    if (arrayList.get(i).getSort().equals(keys.get(j))) {
                                                                        flag = true;
                                                                    }
                                                                }
                                                                if (!flag) {
                                                                    arrayList.remove(i);
                                                                    adapter.notifyDataSetChanged();
                                                                }
                                                            }
                                                        }


                                                    } else {
                                                        for (int i = 0; i < arrayList.size(); i++) {
                                                            for (int j = 0; j < keys.size(); j++) {
                                                                if (arrayList.get(i).getSort().equals(keys.get(j))) {
                                                                    flag = true;
                                                                }
                                                            }
                                                            if (!flag) {
                                                                arrayList.remove(i);
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                        }

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
                            if (!dataSnapshot.hasChild("subCat")) {
                                dataSnapshot.child("products").getRef().addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                        String key = dataSnapshot.getKey().toLowerCase().trim();
                                        if (word.length() <= key.length()) {
                                            if (key.contains(word)) {
                                                progressBar.setVisibility(View.GONE);
                                                noResultLayout.setVisibility(View.GONE);
                                                keys.add(dataSnapshot.getKey());
                                                boolean exist = false;
                                                for (ProductModel productModel : arrayList) {
                                                    if (productModel.getSort().equals(dataSnapshot.getKey())) {
                                                        exist = true;
                                                        break;
                                                    }

                                                }
                                                flag = false;
                                                if (!exist) {
                                                    arrayList.add(dataSnapshot.getValue(ProductModel.class));
                                                    adapter.notifyDataSetChanged();
                                                    for (int i = 0; i < arrayList.size(); i++) {
                                                        for (int j = 0; j < keys.size(); j++) {
                                                            if (arrayList.get(i).getSort().equals(keys.get(j))) {
                                                                flag = true;
                                                            }
                                                        }
                                                        if (!flag) {
                                                            arrayList.remove(i);
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                }


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
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            if (arrayList.isEmpty() && !searchEditText.getText().toString().isEmpty()) {
                                progressBar.setVisibility(View.GONE);
                                noResultLayout.setVisibility(View.VISIBLE);
                            } else
                                progressBar.setVisibility(View.GONE);
                        }
                    }
                }, 2000);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

// hide keyboard when click enter key :

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    return true;
                }

                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (!checkConnection.isConnecting()) {
            startActivity(new Intent(SearchActivity.this, HomeActivity.class));
            finish();
        } else
            super.onBackPressed();

    }

    CheckConnection checkConnection = new CheckConnection(this);
}
