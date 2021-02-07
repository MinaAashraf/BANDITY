package com.boats.market.marven.dell.marven;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductView extends AppCompatActivity {
    boolean exist;
    ProductModel model;
    ArrayList<ProductModel> arrayList = new ArrayList<>();
    int counter = 1;
    float rate_value = 0f;
    DatabaseReference databaseReference3;
    int num;
    SharedPreferences sharedPreferences, sharedPreferences2;
    ViewHolderAdapter2 adapter2 = null;
    ViewHolderAdapter3 adapter3 = null;
    int cart_counter;
    android.support.v7.widget.Toolbar toolbar;
    TextView cart_counter_txt;
    SharedPreferences sharedPreferences3;
    ArrayList <ProductModel> fav_arrayList = new ArrayList<>();
    @Override

    protected void onResume() {
        super.onResume();
        sharedPreferences2 = getSharedPreferences("cart_counter", 0);
        cart_counter = sharedPreferences2.getInt("count", 0);
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
        setContentView(R.layout.activity_product_view);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");


        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);
        final ImageView backimg = toolbar.findViewById(R.id.back_icon);
        TextView title = toolbar.findViewById(R.id.customcategort_name);
        cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);
        title.setText("Product View");
        model = (ProductModel) getIntent().getExtras().get("model");

        final ImageView fav_icon = findViewById(R.id.favourites_icon);
        sharedPreferences3 = getSharedPreferences("fav", 0);
        Gson gson = new Gson();
        String json = sharedPreferences3.getString("json", null);
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        if (json != null)
            fav_arrayList = gson.fromJson(json, type);

        for (ProductModel productModel : fav_arrayList) {
            if (model.getSort().equals(productModel.getSort())) {
                fav_icon.setEnabled(false);
                fav_icon.setColorFilter(Color.parseColor("#4d489b"));            }
        }


        fav_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fav_icon.setEnabled(false);
                fav_icon.setColorFilter(Color.parseColor("#4d489b"));
                sharedPreferences3 = getSharedPreferences("fav", 0);
                SharedPreferences.Editor editor = sharedPreferences3.edit();
                Gson gson2 = new Gson();

                String json2 = sharedPreferences3.getString("json", null);
                Type type = new TypeToken<ArrayList<ProductModel>>() {
                }.getType();
                if (json2 != null)
                    fav_arrayList = gson2.fromJson(json2, type);

                fav_arrayList.add(model);
                Toast.makeText(ProductView.this, "added to Favourite", Toast.LENGTH_SHORT).show();

                Gson gson = new Gson();
                String json = gson.toJson(fav_arrayList);
                editor.putString("json", json);
                editor.apply();

            }
        });


        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductView.this, CartActivity.class).putExtra("from", "view"));
            }
        });
        title.setTypeface(typeface);
        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductView.this, SearchActivity.class));
                finish();
            }
        });
        LinearLayout goToRate = findViewById(R.id.goToRate_layout);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rating_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RatingBar smallRatingbar = dialog.findViewById(R.id.smallRatingbar);

        final RatingBar ratingBarForUsed = dialog.findViewById(R.id.rating_bar);
        final Button rate_btn = dialog.findViewById(R.id.rate_btn);
        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
        final RatingBar ratingBar = findViewById(R.id.rating_bar);
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        goToRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        final TextView ratersNum = dialog.findViewById(R.id.raters_num);
        final TextView startNum = dialog.findViewById(R.id.ratingVaueTxt);
        startNum.setTypeface(typeface);
        if (model.getSubkey().equals("none")) {
            databaseReference3.child("categories").child(model.getCategoryName()).child("products").child(model.getSort()).child("rating").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    rate_value = dataSnapshot.child("rate").getValue(Float.class);
                    num = dataSnapshot.child("rateNum").getValue(Integer.class);
                    ratingBar.setRating(rate_value / num);
                    smallRatingbar.setRating(rate_value / num);
                    DecimalFormat decimalFormat = new DecimalFormat(".#");
                    if (rate_value!= 0){
                        startNum.setText("" + decimalFormat.format(rate_value / num) + "/" + "5");
                    }
                    if (num > 0){
                        ratersNum.setText(num + " ratings");
                        ratersNum.setVisibility(View.VISIBLE);
                    }
                    else
                        ratersNum.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            databaseReference3.child("categories").child(model.getCategoryName()).child("subCat").child(model.getSubkey()).child("products").child(model.getSort()).child("rating").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    rate_value = dataSnapshot.child("rate").getValue(Float.class);
                    num = dataSnapshot.child("rateNum").getValue(Integer.class);
                    ratingBar.setRating(rate_value / num);
                    smallRatingbar.setRating(rate_value / num);
                    DecimalFormat decimalFormat = new DecimalFormat(".#");
                    if (rate_value != 0)
                        startNum.setText("" + decimalFormat.format(rate_value / num) + "/" + "5");
                    if (num > 0){
                        ratersNum.setText(num + " ratings");
                        ratersNum.setVisibility(View.VISIBLE);
                    }
                    else
                        ratersNum.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        final LinearLayout linearLayout = dialog.findViewById(R.id.linear);
        final TextView thank_msg = dialog.findViewById(R.id.thank_msg);
        thank_msg.setTypeface(typeface);
        final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference2.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("rates") && dataSnapshot.child("rates").hasChild(model.getSort())) {
                        float value = dataSnapshot.child("rates").child(model.getSort()).getValue(Float.class);
                        ratingBarForUsed.setEnabled(false);
                        linearLayout.setVisibility(View.GONE);
                        thank_msg.setVisibility(View.VISIBLE);
                        ratingBarForUsed.setRating(value);
                    } else {
                        linearLayout.setVisibility(View.VISIBLE);
                        thank_msg.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(ProductView.this, "Sign in is requested", Toast.LENGTH_SHORT).show();
                    ratingBarForUsed.setRating(0);
                } else {
                    if (ratingBarForUsed.getRating() == 0) {
                        Toast.makeText(ProductView.this, "Do your rate please !", Toast.LENGTH_SHORT).show();
                    } else {
                        num++;
                        final Rating rating = new Rating(rate_value + ratingBarForUsed.getRating(), num);
                        if (model.getSubkey().equals("none"))
                            databaseReference3.child("categories").child(model.getCategoryName()).child("products").child(model.getSort()).child("rating").setValue(rating);
                        else
                            databaseReference3.child("categories").child(model.getCategoryName()).child("subCat").child(model.getSubkey()).child("products").child(model.getSort()).child("rating").setValue(rating);

                        databaseReference3.child("Monaspa").child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(model.getSort()))
                                    dataSnapshot.child(model.getSort()).child("rating").getRef().setValue(rating);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        databaseReference3.child("Release").child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(model.getSort()))
                                    dataSnapshot.child(model.getSort()).child("rating").getRef().setValue(rating);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        databaseReference3.child("Sale").child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(model.getSort()))
                                    dataSnapshot.child(model.getSort()).child("rating").getRef().setValue(rating);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        databaseReference3.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("rates").child(model.getSort()).setValue(ratingBarForUsed.getRating());
                        Toast.makeText(ProductView.this, "Thanks for your rate", Toast.LENGTH_SHORT).show();
                        ratingBarForUsed.setEnabled(false);
                        linearLayout.setVisibility(View.GONE);
                        thank_msg.setVisibility(View.VISIBLE);
                        dialog.dismiss();

                    }
                }

            }
        });


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (ratingBarForUsed.isEnabled())
                    ratingBarForUsed.setRating(0);
            }
        });
        //  ImageView vote_icon = findViewById(R.id.vote_icon);
       /* vote_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
*/
        TextView sale_percent = findViewById(R.id.sale_percent);
        Button addToCart = findViewById(R.id.internal_add_tocart);
        TextView proName = findViewById(R.id.name);
        TextView proSale = findViewById(R.id.sale);
        TextView proprice = findViewById(R.id.price);
        TextView egp = findViewById(R.id.egp2);
        TextView description = findViewById(R.id.desciption);
        proName.setText(model.getSort());
        if (model.getSale().isEmpty()) {
            proSale.setVisibility(View.GONE);
            sale_percent.setVisibility(View.GONE);
            egp.setVisibility(View.GONE);
        } else {
            proSale.setText(model.getSale());
            sale_percent.setText((int) ((Float.valueOf(model.getSale()) - Float.valueOf(model.getPrice())) / Float.valueOf(model.getSale()) * 100) + "% OFF");
        }
        TextView descriptionLabel = findViewById(R.id.description_label);
        sale_percent.setTypeface(typeface);
        descriptionLabel.setTypeface(typeface);
        addToCart.setTypeface(typeface);
        proName.setTypeface(typeface);
        proSale.setTypeface(typeface);
        proprice.setTypeface(typeface);
        description.setTypeface(typeface);

        TextView choosecolor = findViewById(R.id.choseColortxt);
        TextView chooseSize = findViewById(R.id.chooseSizetxt);

        choosecolor.setTypeface(typeface);
        chooseSize.setTypeface(typeface);

        final TextView itemNum = findViewById(R.id.item_num);
        ImageView positiveIcon = findViewById(R.id.add_icon);
        ImageView negativeIcon = findViewById(R.id.remove_icon);


        proprice.setText(model.getPrice());
        description.setText(model.getDescription());
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


        if (getIntent().hasExtra("position")) {
            adapter2 = new ViewHolderAdapter2(this, model.getColores(), model.getColores().indexOf(model.getColorr()));
            adapter3 = new ViewHolderAdapter3(this, model.getSizes(), model.getSizes().indexOf(model.getSizee()));
            counter = model.getItemnum();
            itemNum.setText(String.valueOf(counter));

        } else {
            adapter2 = new ViewHolderAdapter2(this, model.getColores(), 0);
            adapter3 = new ViewHolderAdapter3(this, model.getSizes(), 0);
        }

        positiveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                itemNum.setText(String.valueOf(counter));
            }
        });
        negativeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter > 1) {
                    counter--;
                    itemNum.setText(String.valueOf(counter));
                }
            }
        });

        recyclerView.setAdapter(adapter2);


        RecyclerView recyclerView2 = findViewById(R.id.recycler_view2);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(adapter3);
        LinearLayout btns_layout = findViewById(R.id.btnsLayout);
        Button save_changes = findViewById(R.id.save_changes);
        if (getIntent().hasExtra("position")) {

            btns_layout.setVisibility(View.GONE);
            save_changes.setVisibility(View.VISIBLE);
        }

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("cart", 0);

                Gson gson2 = new Gson();
                String json2 = sharedPreferences.getString("json", null);
                Type type = new TypeToken<ArrayList<ProductModel>>() {
                }.getType();
                if (json2 != null)
                    arrayList = gson2.fromJson(json2, type);

                int position = getIntent().getExtras().getInt("position");
                arrayList.get(position).setItemnum(Integer.valueOf(itemNum.getText().toString()));
                arrayList.get(position).setColorr(model.getColores().get(adapter2.getIndex()));
                arrayList.get(position).setSizee(model.getSizes().get(adapter3.getIndex()));

                int count = 0;
                for (ProductModel item : arrayList) {
                    count += item.getItemnum();
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(arrayList);
                editor.putString("json", json);
                editor.apply();

                SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                editor1.putInt("count", count).apply();
                cart_counter_txt.setVisibility(View.VISIBLE);
                cart_counter_txt.setText(count + "");

                startActivity(new Intent(ProductView.this, CartActivity.class));
                finish();

            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exist = false;
                String color = model.getColores().get(adapter2.getIndex());
                String size = model.getSizes().get(adapter3.getIndex());

                model.setColorr(color);
                model.setSizee(size);
                model.setItemnum(Integer.valueOf(itemNum.getText().toString()));
                model.setTotalprice("" + counter * model.getPrice2());
                SharedPreferences sharedPreferences = getSharedPreferences("cart", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                Gson gson2 = new Gson();
                String json2 = sharedPreferences.getString("json", null);
                Type type = new TypeToken<ArrayList<ProductModel>>() {
                }.getType();
                if (json2 != null)
                    arrayList = gson2.fromJson(json2, type);
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getSort().equals(model.getSort()) && arrayList.get(i).getSizee().equals(model.getSizee()) && arrayList.get(i).getColorr().equals(model.getColorr()) && arrayList.get(i).getDescription().equals(model.getDescription())) {
                        arrayList.get(i).setItemnum(arrayList.get(i).getItemnum() + model.getItemnum());
                        Toast.makeText(ProductView.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    arrayList.add(model);
                    Toast.makeText(ProductView.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                }
                int count = 0;
                for (ProductModel item : arrayList) {
                    count += item.getItemnum();
                }

                Gson gson = new Gson();
                String json = gson.toJson(arrayList);
                editor.putString("json", json);
                editor.apply();

                SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                editor1.putInt("count", count).apply();
                cart_counter_txt.setVisibility(View.VISIBLE);
                cart_counter_txt.setText(count + "");
            }
        });

        Button buyNow_btn = findViewById(R.id.buyNow_btn);
        buyNow_btn.setTypeface(typeface);
        buyNow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(ProductView.this, "Please Sign In First..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProductView.this, HomeActivity.class).putExtra("span", "register"));
                } else {
                    String color = model.getColores().get(adapter2.getIndex());
                    String size = model.getSizes().get(adapter3.getIndex());
                    model.setColorr(color);
                    model.setSizee(size);
                    model.setItemnum(Integer.valueOf(itemNum.getText().toString()));
                    model.setTotalprice("" + counter * model.getPrice2());
                    SharedPreferences.Editor editor2 = getSharedPreferences("cart", 0).edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(model);
                    editor2.putString("json2", json);
                    editor2.apply();
                    SharedPreferences.Editor editor = getSharedPreferences("total", 0).edit();
                    editor.putString("totalvalue", model.getTotalprice()).apply();
                    startActivity(new Intent(ProductView.this, CheckoutActivity.class).putExtra("from", "buy"));
                }
            }
        });


        ViewPager viewPager = findViewById(R.id.viewPager);
        TapAdapter adapter = new TapAdapter(ProductView.this);
        viewPager.setAdapter(adapter);
        dot_layout = findViewById(R.id.dot_layout);
        if (model.getUrlimages().size() > 1)
            addDotsIndicator(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dot_layout.removeAllViews();
                addDotsIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void addDotsIndicator(int position) {
        dots = new TextView[model.getUrlimages().size()];
        for (int i = 0; i < model.getUrlimages().size(); i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(25);
            dots[i].setTextColor(Color.parseColor("#7b828b"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 0, 0);
            dots[i].setLayoutParams(params);
            dot_layout.addView(dots[i]);

        }
        if (dots.length > 0) {
            dots[position].setTextColor(Color.parseColor("#292561"));
        }
    }

    TextView[] dots;
    LinearLayout dot_layout;

    class TapAdapter extends PagerAdapter {
        private Context context;


        public TapAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            return model.getUrlimages().size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == (RelativeLayout) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.viewpager_item, container, false);
            ImageView imageView = view.findViewById(R.id.imageview);
            Picasso.get().load(model.getUrlimages().get(position)).placeholder(R.drawable.home_logo).into(imageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((RelativeLayout) object);
        }

    }

    @Override
    public void onBackPressed() {

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        else {
            if (!getIntent().hasExtra("from")) {
                super.onBackPressed();
            } else {
                startActivity(new Intent(this, CartActivity.class));
                finish();
            }
        }
    }
}
