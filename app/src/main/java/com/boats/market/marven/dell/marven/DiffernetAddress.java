package com.boats.market.marven.dell.marven;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

public class DiffernetAddress extends AppCompatActivity {
    ArrayList<String> keys = new ArrayList<>();
    TextView name, building, street, area, floor, apartment, state, phone;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth;
    ArrayList<AddressModel> arrayList = new ArrayList<>();
    int selected;
    Adapter adapter;
    Typeface typeface;
    TextView remove_fab, edit_fab;
    int cart_counter;
    android.support.v7.widget.Toolbar toolbar;
    TextView cart_counter_txt;
    TextView applyAddress;
    ListView listView;
    TextView add;

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
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        cart_counter_txt.setTypeface(typeface);

        firebaseAuth = FirebaseAuth.getInstance();
        adapter = new Adapter(DiffernetAddress.this, android.R.layout.simple_list_item_1, arrayList);
             arrayList.clear();
             adapter.clear();

        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("default_address")) {
                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("default_address").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            selected = dataSnapshot.getValue(Integer.class);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("addresses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AddressModel addressModel = dataSnapshot.getValue(AddressModel.class);
                arrayList.add(addressModel);
                keys.add(dataSnapshot.getKey());
                listView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                arrayList.remove(keys.indexOf(dataSnapshot.getKey()));
                keys.remove(keys.indexOf(dataSnapshot.getKey()));
                adapter.notifyDataSetChanged();
                if (arrayList.size() == 0) {
                    onBackPressed();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiffernetAddress.this, AddressActivity.class).putExtra("from", "different").putExtra("which", getIntent().getExtras().getString("which")));

            }
        });

    }

    public class Adapter extends ArrayAdapter {

        public Adapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            View v = inflater.inflate(R.layout.adrdress_item, parent, false);

            name = v.findViewById(R.id.name_adress);
            building = v.findViewById(R.id.building_adress);
            street = v.findViewById(R.id.street_adress);
            area = v.findViewById(R.id.area_adress);
            floor = v.findViewById(R.id.floor_adress);
            apartment = v.findViewById(R.id.apartmentnum_adress);
            state = v.findViewById(R.id.state_adress);
            phone = v.findViewById(R.id.phone_adress);
            remove_fab = v.findViewById(R.id.remove_fab);
            edit_fab = v.findViewById(R.id.edit_fab);
            applyAddress = v.findViewById(R.id.apply);
            name.setText(arrayList.get(position).getName());
            building.setText(arrayList.get(position).getBuilding() + "/");
            street.setText(arrayList.get(position).getStreet() + "/");
            if (arrayList.get(position).getArea().length() >= 30)
                area.setText(arrayList.get(position).getArea().substring(0, 30) + "..");
            else
                area.setText(arrayList.get(position).getArea());
            floor.setText("Floor : " + arrayList.get(position).getFloor() + "/");
            apartment.setText("Apartment : " + arrayList.get(position).getApartment());
            state.setText(arrayList.get(position).getState() + "/");
            phone.setText(arrayList.get(position).getPhone());

            name.setTypeface(typeface);
            building.setTypeface(typeface);
            street.setTypeface(typeface);
            area.setTypeface(typeface);
            floor.setTypeface(typeface);
            apartment.setTypeface(typeface);
            state.setTypeface(typeface);
            phone.setTypeface(typeface);

            remove_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DiffernetAddress.this);
                    builder.setMessage("Do you want to remove this address ?").setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("addresses").child(keys.get(position)).removeValue();
                            selected = 0;
                            databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("default_address").setValue(selected);
                            notifyDataSetChanged();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

                }
            });

            edit_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddressModel addressModel = new AddressModel(arrayList.get(position).getName(), arrayList.get(position).getBuilding(), arrayList.get(position).getStreet(), arrayList.get(position).getArea(), arrayList.get(position).getFloor(), arrayList.get(position).getApartment(), arrayList.get(position).getState(), arrayList.get(position).getPhone(), arrayList.get(position).getAdditional());
                    startActivity(new Intent(DiffernetAddress.this, AddressActivity.class).putExtra("edit", (Serializable) addressModel).putExtra("from", "different").putExtra("which", getIntent().getExtras().getString("which"))
                            .putExtra("key", keys.get(position)));

                }
            });

            applyAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selected = position;
                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("default_address").setValue(position).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(DiffernetAddress.this, "Set As Default Address", Toast.LENGTH_SHORT).show();

                        }
                    });
                    adapter.notifyDataSetChanged();

                }
            });
            return v;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differnet_address);
        add = findViewById(R.id.addadress);

        typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        TextView categoryname = toolbar.findViewById(R.id.customcategort_name);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiffernetAddress.this, CartActivity.class));
            }
        });
        categoryname.setText("Change Address");
        ImageView search_icon = toolbar.findViewById(R.id.search_icon);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiffernetAddress.this, SearchActivity.class));
            }
        });
        categoryname.setTypeface(typeface);

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        listView = findViewById(R.id.listview3);


    }

    @Override
    public void onBackPressed() {

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        else {

            if (getIntent().getExtras().getString("which").equals("account")) {
                startActivity(new Intent(DiffernetAddress.this, HomeActivity.class).putExtra("span", "register"));
                finish();
            } else
                super.onBackPressed();
        }
    }
}
