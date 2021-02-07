package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorSpace;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.system.ErrnoException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.zip.Inflater;

import me.leolin.shortcutbadger.ShortcutBadger;

public class ProductsActivity extends AppCompatActivity {
    DatabaseReference databaseReference, databaseReference2;
    ArrayList<ProductModel> arrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    int index;
    ImageView view1, view2;
    String subName;
    String i;
    RecyclerView recyclerView;
    int viewitem;
    String categoryName;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    Typeface typeface;
    SharedPreferences sortsharedPreferences;
    SharedPreferences.Editor editor2;
    int cart_counter;
    android.support.v7.widget.Toolbar toolbar;
    TextView cart_counter_txt;
    ProgressBar progressBar;
    String orderType;
    SharedPreferences preferences;
    LinearLayout noItemLayout;

    @Override
    protected void onResume() {
        super.onResume();
        preferences = getSharedPreferences("view", 0);
        viewitem = preferences.getInt("viewitem", R.layout.listitem2);
        orderType = sortsharedPreferences.getString("sortType", "negativedate");
        progressBar.setVisibility(View.VISIBLE);
        if (viewitem == R.layout.listitem) {
            recyclerView.setLayoutManager(linearLayoutManager);
            view1.setColorFilter(Color.parseColor("#292561"));
            view2.setColorFilter(Color.parseColor("#4C5264"));
            myAdapter(databaseReference, orderType);
        } else {
            recyclerView.setLayoutManager(gridLayoutManager);
            view1.setColorFilter(Color.parseColor("#4C5264"));
            view2.setColorFilter(Color.parseColor("#292561"));
            myAdapter2(databaseReference, orderType);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("cart_counter", 0);
        cart_counter = sharedPreferences.getInt("count", 0);
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
        setContentView(R.layout.activity_products);
        try {
            if (getIntent().getExtras().getString("from").equals("edit"))
                finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBar = findViewById(R.id.progress_bar);
        noItemLayout = findViewById(R.id.noItem_layout);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        recyclerView = findViewById(R.id.recycler_view);
        typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        sortsharedPreferences = getSharedPreferences("sort", 0);
        editor2 = sortsharedPreferences.edit();


        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        ImageView cart_icon = toolbar.findViewById(R.id.cart_icon);
        ImageView back_icon = toolbar.findViewById(R.id.back_icon);
        final TextView title = toolbar.findViewById(R.id.customcategort_name);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        ImageView search_icon = toolbar.findViewById(R.id.search_icon);
        categoryName = getIntent().getExtras().getString("catName");


        if (getIntent().getExtras().getString("subName").equals("none")) {
            title.setText(categoryName);

            if (categoryName.equals("Release") || categoryName.equals("Sale"))
                databaseReference = databaseReference.child(categoryName).child("Products");
            else if (getIntent().hasExtra("monaspa")) {
                databaseReference = databaseReference.child("Monaspa").child("Products");
            } else
                databaseReference = databaseReference.child("categories").child(categoryName).child("products");


        } else {
            subName = getIntent().getExtras().getString("subName");
            title.setText(subName);
            i = getIntent().getExtras().getString("index");
            databaseReference = databaseReference.child("categories").child(categoryName).child("subCat").child(i).child("products");
        }


        title.setTypeface(typeface);

        final ImageView filter_icon = findViewById(R.id.filter_icon);
        final PopupMenu popupMenu2 = new PopupMenu(this, filter_icon);
        final DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference();
        databaseReference3.child("categories").child(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("subCat")) {
                    filter_icon.setVisibility(View.VISIBLE);
                    dataSnapshot.child("subCat").getRef().addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            popupMenu2.getMenu().add(dataSnapshot.child("name").getValue().toString());
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
                } else {
                    filter_icon.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        filter_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu2.show();
            }
        });

        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                title.setText(menuItem.getTitle().toString());
                databaseReference3.child("categories").child(categoryName).child("subCat").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.child("name").getValue().toString().equals(menuItem.getTitle())) {
                            progressBar.setVisibility(View.VISIBLE);
                            if (viewitem == R.layout.listitem)
                                myAdapter(databaseReference3.child("categories").child(categoryName).child("subCat").child(dataSnapshot.getKey()).child("products"), sortsharedPreferences.getString("sortType", "negativedate"));
                            else
                                myAdapter2(databaseReference3.child("categories").child(categoryName).child("subCat").child(dataSnapshot.getKey()).child("products"), sortsharedPreferences.getString("sortType", "negativedate"));

                            databaseReference = databaseReference3.child("categories").child(categoryName).child("subCat").child(dataSnapshot.getKey()).child("products");
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
                return false;
            }
        });


        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, SearchActivity.class));
            }
        });

        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, CartActivity.class));
            }
        });
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final RelativeLayout linearLayout = findViewById(R.id.linear);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(gridLayoutManager);
                view2.setColorFilter(Color.parseColor("#292561"));
                view1.setColorFilter(Color.parseColor("#4C5264"));
                SharedPreferences.Editor editor = preferences.edit();
                viewitem = R.layout.listitem2;
                editor.putInt("viewitem", R.layout.listitem2).apply();
                //myAdapter(databaseReference, viewitem, sortsharedPreferences.getString("sortType", "negativedate"));
                myAdapter2(databaseReference, sortsharedPreferences.getString("sortType", "negativedate"));

            }
        });

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(linearLayoutManager);
                view2.setColorFilter(Color.parseColor("#4C5264"));
                view1.setColorFilter(Color.parseColor("#292561"));
                SharedPreferences.Editor editor = preferences.edit();
                viewitem = R.layout.listitem;
                linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                editor.putInt("viewitem", R.layout.listitem).apply();
                // myAdapter(databaseReference, viewitem, sortsharedPreferences.getString("sortType", "negativedate"));
                myAdapter(databaseReference, sortsharedPreferences.getString("sortType", "negativedate"));

            }
        });


        ImageView sort_icon = findViewById(R.id.sort_icon);
        final PopupMenu popupMenu = new PopupMenu(this, sort_icon);
        popupMenu.getMenu().add("HIGH $ - LOW $");
        popupMenu.getMenu().add("LOW $ - HIGH $");
        popupMenu.getMenu().add("NEW - OLD  (default)");
        popupMenu.getMenu().add("OLD - NEW");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                progressBar.setVisibility(View.VISIBLE);
                switch (menuItem.getTitle().toString()) {
                    case "OLD - NEW":
                        if (viewitem == R.layout.listitem)
                            myAdapter(databaseReference, "date");
                        else
                            myAdapter2(databaseReference, "date");

                        break;
                    case "NEW - OLD  (default)":
                        if (viewitem == R.layout.listitem)
                            myAdapter(databaseReference, "negativedate");
                        else
                            myAdapter2(databaseReference, "negativedate");
                        break;
                    case "HIGH $ - LOW $":
                        if (viewitem == R.layout.listitem)
                            myAdapter(databaseReference, "negativeprice2");
                        else
                            myAdapter2(databaseReference, "negativeprice2");

                        break;
                    case "LOW $ - HIGH $":
                        if (viewitem == R.layout.listitem)
                            myAdapter(databaseReference, "price2");
                        else
                            myAdapter2(databaseReference, "price2");
                        break;

                }
                return false;
            }
        });


        sort_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

        final ArrayList<String> releaseList = new ArrayList<>();
        databaseReference3.child("Release").child("Products").orderByChild("date").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                releaseList.add(dataSnapshot.getKey());
                if (releaseList.size() > 20) {
                    databaseReference3.child("Release").child("Products").child(releaseList.get(0)).removeValue();
                    releaseList.remove(0);
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
     final RelativeLayout toolsLayout = findViewById(R.id.toolsLayout);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recyclerView.getAdapter().getItemCount() == 0) {
                    progressBar.setVisibility(View.GONE);
                    toolsLayout.setVisibility(View.GONE);
                    noItemLayout.setVisibility(View.VISIBLE);
                    
                }

            }
        }, 2000);

    }

    @Override
    public void onBackPressed() {

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        else
            super.onBackPressed();
    }

    public void myAdapter(final DatabaseReference mydatabaseReference, final String child) {


        final ArrayList<ProductModel> arr = new ArrayList<>();
        final FirebaseRecyclerAdapter<ProductModel, MyViewHolder> adapter = new FirebaseRecyclerAdapter<ProductModel, MyViewHolder>(
                ProductModel.class, R.layout.listitem, MyViewHolder.class, mydatabaseReference.orderByChild(child)) {

            @Override
            public boolean onFailedToRecycleView(@NonNull MyViewHolder holder) {
                progressBar.setVisibility(View.GONE);
                noItemLayout.setVisibility(View.VISIBLE);
                return super.onFailedToRecycleView(holder);
            }

            @Override
            protected void populateViewHolder(final MyViewHolder viewHolder, final ProductModel model, final int position) {

                editor2.putString("sortType", child).apply();
                arr.add(model);
                viewHolder.setData(arr.get(position).getUrl(), arr.get(position).getSort(), arr.get(position).getSale(), arr.get(position).getPrice(), arr.get(position).getRating().getRate() / arr.get(position).getRating().getRateNum());
                progressBar.setVisibility(View.GONE);
                viewHolder.price_text.setTypeface(typeface);
                viewHolder.sale_text.setTypeface(typeface);
                viewHolder.sort_text.setTypeface(typeface);

                SharedPreferences sharedPreferences = getSharedPreferences("fav", 0);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("json", null);
                Type type = new TypeToken<ArrayList<ProductModel>>() {
                }.getType();
                if (json != null)
                    arrayList = gson.fromJson(json, type);

                for (ProductModel productModel : arrayList) {
                    if (arr.get(position).getSort().equals(productModel.getSort())) {
                        viewHolder.makefavouriteimg.setEnabled(false);
                        viewHolder.makefavouriteimg.setColorFilter(Color.parseColor("#292561"));
                    }
                }

                databaseReference2.child("Monaspa").child("Products").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals(arr.get(position).getSort())) {
                            viewHolder.addIcon.setEnabled(false);
                            viewHolder.addIcon.setColorFilter(Color.parseColor("#292561"));
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
            public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                sharedPreferences = getSharedPreferences("fav", 0);

                Gson gson2 = new Gson();

                String json2 = sharedPreferences.getString("json", null);
                Type typee = new TypeToken<ArrayList<ProductModel>>() {
                }.getType();
                if (json2 != null)
                    arrayList = gson2.fromJson(json2, typee);
                index = position;

                viewHolder.close_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                        builder.setMessage("Do you want to remove " + getItem(position).getSort() + " ?").setTitle("WARNING").setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String proname = getItem(position).getSort();

                                if (categoryName.equals("Release") || categoryName.equals("Sale") || categoryName.equals("Monaspa")) {

                                    mydatabaseReference.child(proname).removeValue();
                                    arr.remove(getItem(position));
                                    notifyDataSetChanged();
                                } else {
                                    mydatabaseReference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            ProductModel model = dataSnapshot.getValue(ProductModel.class);
                                            if (model.getSort().equals(proname)) {

                                                mydatabaseReference.child(dataSnapshot.getKey()).removeValue();
                                                databaseReference2.child("Release").child("Products").child(proname).removeValue();
                                                databaseReference2.child("Sale").child("Products").child(proname).removeValue();
                                                databaseReference2.child("Monaspa").child("Products").child(proname).removeValue();
                                                sharedPreferences = getSharedPreferences("fav", 0);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                Gson gson2 = new Gson();

                                                String json2 = sharedPreferences.getString("json", null);
                                                Type type = new TypeToken<ArrayList<ProductModel>>() {
                                                }.getType();
                                                if (json2 != null)
                                                    arrayList = gson2.fromJson(json2, type);
                                                arrayList.remove(model);

                                                Gson gson = new Gson();
                                                String json = gson.toJson(arrayList);
                                                editor.putString("json", json);
                                                editor.apply();
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
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

                    }
                });

                viewHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ProductsActivity.this, ProductView.class).putExtra("model", (Serializable) arr.get(position)));
                    }
                });


                viewHolder.edit_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getIntent().getExtras().getString("subName").equals("none"))
                            startActivity(new Intent(ProductsActivity.this, EditProduct.class).putExtra("pro", arr.get(position)));
                        else
                            startActivity(new Intent(ProductsActivity.this, EditProduct.class).putExtra("pro", arr.get(position)).putExtra("categoryName", categoryName).putExtra("subName", subName));

                    }
                });

                viewHolder.addIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                        builder.setMessage("Add To occasion category ??").setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference2.child("Monaspa").child("Products").child(arr.get(position).getSort()).setValue(arr.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            Toast.makeText(ProductsActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(ProductsActivity.this, "There is a Problem..Try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                    }
                });

                viewHolder.makefavouriteimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        viewHolder.makefavouriteimg.setEnabled(false);
                        viewHolder.makefavouriteimg.setColorFilter(Color.parseColor("#292561"));
                        sharedPreferences = getSharedPreferences("fav", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson2 = new Gson();

                        String json2 = sharedPreferences.getString("json", null);
                        Type type = new TypeToken<ArrayList<ProductModel>>() {
                        }.getType();
                        if (json2 != null)
                            arrayList = gson2.fromJson(json2, type);

                        arrayList.add(arr.get(position));
                        Toast.makeText(ProductsActivity.this, "added to Favourite", Toast.LENGTH_SHORT).show();

                        Gson gson = new Gson();
                        String json = gson.toJson(arrayList);
                        editor.putString("json", json);
                        editor.apply();
                    }
                });

                viewHolder.control_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewHolder.counter % 2 == 0) {
                            viewHolder.control_icon.animate().translationX(-60).setDuration(100);
                            viewHolder.addIcon.setVisibility(View.VISIBLE);
                            viewHolder.close_icon.setVisibility(View.VISIBLE);
                            viewHolder.edit_icon.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.addIcon.setVisibility(View.GONE);
                            viewHolder.close_icon.setVisibility(View.GONE);
                            viewHolder.edit_icon.setVisibility(View.GONE);
                            viewHolder.control_icon.animate().translationX(0).setDuration(100);
                        }
                        viewHolder.counter++;
                    }
                });

            }
        };


        recyclerView.setAdapter(adapter);

    }

    public void myAdapter2(final DatabaseReference mydatabaseReference, final String child) {


        final ArrayList<ProductModel> arr = new ArrayList<>();
        final FirebaseRecyclerAdapter<ProductModel, MyViewHolder> adapter = new FirebaseRecyclerAdapter<ProductModel, MyViewHolder>(
                ProductModel.class, R.layout.listitem2, MyViewHolder.class, mydatabaseReference.orderByChild(child)) {

            @Override
            protected void populateViewHolder(final MyViewHolder viewHolder, final ProductModel model, final int position) {

                editor2.putString("sortType", child).apply();
                arr.add(model);

                viewHolder.setData(arr.get(position).getUrl(), arr.get(position).getSort(), arr.get(position).getSale(), arr.get(position).getPrice(), arr.get(position).getRating().getRate() / arr.get(position).getRating().getRateNum());
                progressBar.setVisibility(View.GONE);
                viewHolder.price_text.setTypeface(typeface);
                viewHolder.sale_text.setTypeface(typeface);
                viewHolder.sort_text.setTypeface(typeface);

                SharedPreferences sharedPreferences = getSharedPreferences("fav", 0);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("json", null);
                Type type = new TypeToken<ArrayList<ProductModel>>() {
                }.getType();
                if (json != null)
                    arrayList = gson.fromJson(json, type);

                for (ProductModel productModel : arrayList) {
                    if (arr.get(position).getSort().equals(productModel.getSort())) {
                        viewHolder.makefavouriteimg.setEnabled(false);
                        viewHolder.makefavouriteimg.setColorFilter(Color.parseColor("#292561"));
                    }
                }

                databaseReference2.child("Monaspa").child("Products").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.getKey().equals(arr.get(position).getSort())) {
                            viewHolder.addIcon.setEnabled(false);
                            viewHolder.addIcon.setColorFilter(Color.parseColor("#292561"));
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
            public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
                super.onBindViewHolder(viewHolder, position);

                sharedPreferences = getSharedPreferences("fav", 0);

                Gson gson2 = new Gson();

                String json2 = sharedPreferences.getString("json", null);
                Type typee = new TypeToken<ArrayList<ProductModel>>() {
                }.getType();
                if (json2 != null)
                    arrayList = gson2.fromJson(json2, typee);
                index = position;

                viewHolder.close_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                        builder.setMessage("Do you want to remove " + getItem(position).getSort() + " ?").setTitle("WARNING").setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String proname = getItem(position).getSort();

                                if (categoryName.equals("Release") || categoryName.equals("Sale") || categoryName.equals("Monaspa")) {

                                    mydatabaseReference.child(proname).removeValue();
                                    arr.remove(getItem(position));
                                    notifyDataSetChanged();
                                } else {
                                    mydatabaseReference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            ProductModel model = dataSnapshot.getValue(ProductModel.class);
                                            if (model.getSort().equals(proname)) {

                                                mydatabaseReference.child(dataSnapshot.getKey()).removeValue();
                                                databaseReference2.child("Release").child("Products").child(proname).removeValue();
                                                databaseReference2.child("Sale").child("Products").child(proname).removeValue();
                                                databaseReference2.child("Monaspa").child("Products").child(proname).removeValue();
                                                sharedPreferences = getSharedPreferences("fav", 0);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                Gson gson2 = new Gson();

                                                String json2 = sharedPreferences.getString("json", null);
                                                Type type = new TypeToken<ArrayList<ProductModel>>() {
                                                }.getType();
                                                if (json2 != null)
                                                    arrayList = gson2.fromJson(json2, type);
                                                arrayList.remove(model);

                                                Gson gson = new Gson();
                                                String json = gson.toJson(arrayList);
                                                editor.putString("json", json);
                                                editor.apply();
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
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

                    }
                });

                viewHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ProductsActivity.this, ProductView.class).putExtra("model", (Serializable) arr.get(position)));
                    }
                });


                viewHolder.edit_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getIntent().getExtras().getString("subName").equals("none"))
                            startActivity(new Intent(ProductsActivity.this, EditProduct.class).putExtra("pro", arr.get(position)));
                        else
                            startActivity(new Intent(ProductsActivity.this, EditProduct.class).putExtra("pro", arr.get(position)).putExtra("categoryName", categoryName).putExtra("subName", subName));
                        finish();
                    }
                });

                viewHolder.addIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                        builder.setMessage("Add To occasion category ??").setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference2.child("Monaspa").child("Products").child(arr.get(position).getSort()).setValue(arr.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            Toast.makeText(ProductsActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(ProductsActivity.this, "There is a Problem..Try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                    }
                });

                viewHolder.makefavouriteimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.makefavouriteimg.setEnabled(false);
                        viewHolder.makefavouriteimg.setColorFilter(Color.parseColor("#292561"));
                        sharedPreferences = getSharedPreferences("fav", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson2 = new Gson();

                        String json2 = sharedPreferences.getString("json", null);
                        Type type = new TypeToken<ArrayList<ProductModel>>() {
                        }.getType();
                        if (json2 != null)
                            arrayList = gson2.fromJson(json2, type);

                        arrayList.add(arr.get(position));
                        Toast.makeText(ProductsActivity.this, "added to Favourite", Toast.LENGTH_SHORT).show();

                        Gson gson = new Gson();
                        String json = gson.toJson(arrayList);
                        editor.putString("json", json);
                        editor.apply();
                    }
                });

                viewHolder.control_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewHolder.counter % 2 == 0) {
                            viewHolder.control_icon.animate().translationX(-50).setDuration(100);
                            viewHolder.addIcon.setVisibility(View.VISIBLE);
                            viewHolder.close_icon.setVisibility(View.VISIBLE);
                            viewHolder.edit_icon.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.addIcon.setVisibility(View.GONE);
                            viewHolder.close_icon.setVisibility(View.GONE);
                            viewHolder.edit_icon.setVisibility(View.GONE);
                            viewHolder.control_icon.animate().translationX(0).setDuration(100);
                        }
                        viewHolder.counter++;
                    }
                });

            }

        };

        recyclerView.setAdapter(adapter);

    }

}


