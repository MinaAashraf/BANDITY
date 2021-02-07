package com.boats.market.marven.dell.marven;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


import static com.boats.market.marven.dell.marven.R.id.home_logo;
import static com.boats.market.marven.dell.marven.R.id.placeholder;

public class HomeActivity extends AppCompatActivity {
    static ImageView home_logo;
    public static String currentFragment = null;
    public static String categoryNameString = null;
    public static TextView categoryname;
    ImageView backimg;
    static android.support.v7.widget.Toolbar toolbar2, toolbar, toolbar3;
    static ImageView home_icon, list_icon, account_icon, favourite_icon, about_icon;

    ImageView cartimage, cartimage2;
    Fragment MainFragment = new MainAccountFragment();
    Fragment homeFragmet = new HomeFragment();
    Fragment categoriesFragment = new CategoriesFragment();
    Fragment favouriteFragment = new FavouriteFragment();
    Fragment aboutFragment = new AboutFragment();
    CheckConnection checkConnection = new CheckConnection(this);
    int cart_counter;
    TextView cart_counter_txt, cart_counter_txt2;

    @Override
    protected void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();


        SharedPreferences sharedPreferences = getSharedPreferences("cart_counter", 0);
        cart_counter = sharedPreferences.getInt("count", 0);
        cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);
        cart_counter_txt2 = toolbar2.findViewById(R.id.cart_counter_txt);
        cart_counter_txt.setText("" + cart_counter);
        cart_counter_txt2.setText("" + cart_counter);

        if (cart_counter > 0) {
            cart_counter_txt.setVisibility(View.VISIBLE);
            cart_counter_txt2.setVisibility(View.VISIBLE);
        } else {
            cart_counter_txt.setVisibility(View.GONE);
            cart_counter_txt2.setVisibility(View.GONE);
        }
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        cart_counter_txt.setTypeface(typeface);
        cart_counter_txt2.setTypeface(typeface);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "bahnch.TTF");

        toolbar2 = findViewById(R.id.custom_toolbar2);
        toolbar3 = findViewById(R.id.custom_toolbar3);
        toolbar = findViewById(R.id.custom_toolbar);
        toolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar2);

        categoryname = toolbar.findViewById(R.id.customcategort_name);
        backimg = toolbar.findViewById(R.id.back_icon);
        cartimage = toolbar.findViewById(R.id.cart_icon);
        cartimage2 = toolbar2.findViewById(R.id.cart_icon2);
        TextView title = toolbar2.findViewById(R.id.title);


        title.setTypeface(typeface2);
        home_logo = toolbar2.findViewById(R.id.home_logo);
        ImageView search_icon2 = toolbar2.findViewById(R.id.search_icon);
        ImageView search_icon = toolbar.findViewById(R.id.search_icon);


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(placeholder, homeFragmet);
        transaction.commit();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (!checkConnection.isConnecting()) {
            startActivity(new Intent(this, ConnectionActivity.class).putExtra("from", "home"));
        }

        categoryname.setTypeface(typeface);
        home_icon = findViewById(R.id.home_icon);
        list_icon = findViewById(R.id.list_icon);
        account_icon = findViewById(R.id.account_icon);
        favourite_icon = findViewById(R.id.favourites_icon);
        about_icon = findViewById(R.id.about_icon);

        if (getIntent().hasExtra("orders")) {
            ArrayList<ProductModel> orders = new ArrayList<>();
            orders = (ArrayList) getIntent().getExtras().get("orders");
            databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("orders").setValue(orders);
        }


        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });

        search_icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });


        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        try {


            if (getIntent().getExtras().getString("span").equals("register")) {
                home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                //  fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_rigth);
                fragmentTransaction.replace(placeholder, MainFragment);
                fragmentTransaction.commit();

                toolbar.setVisibility(View.VISIBLE);
                toolbar2.setVisibility(View.GONE);
                setSupportActionBar(toolbar);
            }
        } catch (Exception e) {
        }


        try {


            if (getIntent().getExtras().get("to").equals("account")) {
                home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                //   fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_rigth);
                fragmentTransaction.replace(placeholder, MainFragment);
                fragmentTransaction.commit();
                toolbar.setVisibility(View.VISIBLE);
                toolbar2.setVisibility(View.GONE);
                setSupportActionBar(toolbar);
            }
        } catch (Exception e) {
        }


        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!checkConnection.isConnecting()) {
                    startActivity(new Intent(HomeActivity.this, ConnectionActivity.class).putExtra("from", "home"));
                } else {
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    toolbar.setVisibility(View.GONE);
                    toolbar2.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar2);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    //     fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_rigth);
                    fragmentTransaction.replace(R.id.placeholder, homeFragmet);
                    fragmentTransaction.commit();
                }


            }
        });
        cartimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));

            }
        });

        cartimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        list_icon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                if (!checkConnection.isConnecting()) {
                    startActivity(new Intent(HomeActivity.this, ConnectionActivity.class).putExtra("from", "home"));
                } else {
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    //       fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_rigth);
                    fragmentTransaction.replace(R.id.placeholder, categoriesFragment);
                    fragmentTransaction.commit();

                }


            }
        });

        account_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkConnection.isConnecting()) {
                    startActivity(new Intent(HomeActivity.this, ConnectionActivity.class).putExtra("from", "home"));
                } else {
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    //     fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_rigth);
                    fragmentTransaction.replace(R.id.placeholder, MainFragment);
                    fragmentTransaction.commit();

                }


            }
        });

        favourite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkConnection.isConnecting()) {
                    startActivity(new Intent(HomeActivity.this, ConnectionActivity.class).putExtra("from", "home"));
                } else {
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    //   fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_rigth);
                    fragmentTransaction.replace(R.id.placeholder, favouriteFragment);
                    fragmentTransaction.commit();

                }


            }
        });
        about_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkConnection.isConnecting()) {
                    startActivity(new Intent(HomeActivity.this, ConnectionActivity.class).putExtra("from", "home"));
                } else {
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    // fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_rigth);
                    fragmentTransaction.replace(R.id.placeholder, aboutFragment);
                    fragmentTransaction.commit();


                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        displayPreviousFragment(currentFragment);
    }

    @SuppressLint("ResourceType")
    public void displayPreviousFragment(String currentFragment) {
        //creating fragment object
        Fragment fragment = null;
        if (!checkConnection.isConnecting()) {
            startActivity(new Intent(HomeActivity.this, ConnectionActivity.class).putExtra("from", "home"));
        } else {
            //initializing the fragment object which is selected
            switch (currentFragment) {
                case "Fragment_2":
                case "Fragment_uniqe":
                    fragment = new HomeFragment();
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    toolbar.setVisibility(View.GONE);
                    toolbar2.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar2);
                    break;
                case "Fragment_3":
                    fragment = new CategoriesFragment();
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));

                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);

                    break;
                case "Fragment_4":
                    fragment = new MainAccountFragment();
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);

                    break;
                case "Fragment_5":
                    fragment = new CategoriesFragment();
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));

                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);

                    break;
                case "Fragment_6":
                    fragment = new MainAccountFragment();
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));

                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);

                    break;
                case "Fragment_7":
                    fragment = new FavouriteFragment();
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));

                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);

                    break;

                case "Fragment_8":
                    fragment = new CategoriesFragment();
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));

                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);

                    break;

                case "Fragment_1":
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
                    break;

                case "Fragment_0":
                    fragment = new SigninFragment();
                    home_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    list_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    favourite_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    about_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.icons_color));
                    account_icon.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary));
                    toolbar2.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);
            }

            //replacing the fragment

            if (fragment != null) {

                if (!currentFragment.equals("Fragment_0")) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    //  ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_rigth);
                    ft.replace(R.id.placeholder, fragment);
                    ft.commit();
                } else {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    //    ft.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_rigth);
                    ft.replace(R.id.placeholder2, fragment);
                    ft.commit();
                }
            }
        }

    }
}
