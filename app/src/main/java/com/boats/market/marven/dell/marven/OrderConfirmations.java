package com.boats.market.marven.dell.marven;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.*;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderConfirmations extends AppCompatActivity {
    ImageView received_icon, delivering_icon, delivered_icon, status_icon;
    TextView status_title;
    Button track_order;
    TextView name, building, street, area, floor, apartment, state, phone;
    Typeface typeface;
    ListView listView;
    Order order = null;
    String key = "";
    DatabaseReference databaseReference;
    Button startDeliveyBtn, home_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmations);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        TextView title = toolbar.findViewById(R.id.customcategort_name);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);
        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        title.setTypeface(typeface);
        backimg.setVisibility(View.GONE);
        cartimg.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);
        title.setText("Order Confirmation");

        received_icon = findViewById(R.id.recived_icon);
        delivering_icon = findViewById(R.id.delivering_icon);
        delivered_icon = findViewById(R.id.delivered_icon);
        status_icon = findViewById(R.id.status_icon);
        status_title = findViewById(R.id.status_title);
        track_order = findViewById(R.id.track_btn);

        name = findViewById(R.id.name_adress);
        building = findViewById(R.id.building_adress);
        area = findViewById(R.id.area_adress);
        floor = findViewById(R.id.floor_adress);
        street = findViewById(R.id.street_adress);
        apartment = findViewById(R.id.apartmentnum_adress);
        state = findViewById(R.id.state_adress);
        phone = findViewById(R.id.phone_adress);

        listView = findViewById(R.id.items_list);

        Intent comingIntent = getIntent();
        order = (Order) comingIntent.getSerializableExtra("order");
        setOrderData(order);

        home_btn = findViewById(R.id.home_btn);
        startDeliveyBtn = findViewById(R.id.start_delivery_btn);

        if (comingIntent.hasExtra("orderKey")) {
            key = comingIntent.getStringExtra("orderKey");
            home_btn.setVisibility(View.GONE);
            startDeliveyBtn.setVisibility(View.VISIBLE);
        }

        track_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderConfirmations.this, TrackingPath.class).putExtra("userId", order.getuserId()).putExtra("key", key));
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderConfirmations.this,HomeActivity.class));
                finish();
            }
        });

        startDeliveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmations.this);
                builder.setMessage("You will start delivering, your location will be tracked by the client. Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkLocationPermession();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });


    }

    public void setOrderData(Order order) {
        setIconStatus(order.getStatus());
        setUserAddress(order.getAddressModel());
        setItemList(order.getArrayList());
    }


    private void setIconStatus(int status) {
        switch (status) {
            case 1:
                status_title.setText("ORDER RECEIEVED");
                status_icon.setImageResource(R.drawable.check_icon);
                break;
            case 2:
                delivering_icon.setColorFilter(Color.parseColor("#58b950"));
                status_title.setText("Delivering");
                status_icon.setImageResource(R.drawable.delivering_icon);
                track_order.setVisibility(View.VISIBLE);
                break;
            case 3:
                delivering_icon.setColorFilter(Color.parseColor("#58b950"));
                delivered_icon.setColorFilter(Color.parseColor("#58b950"));
                status_title.setText("Order Delivered");
                status_icon.setImageResource(R.drawable.delivered_icon);
                track_order.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void openDeliveringLayout() {
        delivering_icon.setColorFilter(Color.parseColor("#58b950"));
        status_title.setText("Delivering");
        status_icon.setImageResource(R.drawable.delivering_icon);
        track_order.setVisibility(View.VISIBLE);
    }

    private void setUserAddress(AddressModel address) {
        name.setText(address.getName());
        state.setText(address.getState() + "/");
        street.setText(address.getStreet() + "/");
        floor.setText("Floor : " + address.getFloor() + "/");
        area.setText(address.getArea());
        phone.setText(address.getPhone());
        name.setText(address.getName());
        building.setText(address.getBuilding() + "/");
        apartment.setText("Apartment : " + address.getApartment());
    }


    public class MyAdapter extends ArrayAdapter {
        ArrayList<SecondryProduct> arrayList;

        public MyAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
            arrayList = (ArrayList<SecondryProduct>) objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.checkout_cartitem, parent, false);
            ImageView itemImage = view.findViewById(R.id.cart_imge);
            TextView itemName = view.findViewById(R.id.cart_sort);
            TextView qtyTitle = view.findViewById(R.id.qty);
            TextView itemQty = view.findViewById(R.id.qty_txt);
            TextView priceTitle = view.findViewById(R.id.itemprice);
            TextView itemPrice = view.findViewById(R.id.cart_price);
            TextView totalTitle = view.findViewById(R.id.total);
            TextView itemTotal = view.findViewById(R.id.cart_total_price);
            TextView colorTitle = view.findViewById(R.id.color);
            TextView itemColor = view.findViewById(R.id.color_txt);
            TextView sizeTitle = view.findViewById(R.id.size);
            TextView itemSize = view.findViewById(R.id.size_txt);

            SecondryProduct product = arrayList.get(position);
            Picasso.get().load(product.getUrl()).into(itemImage);
            itemName.setText(product.getName());
            itemQty.setText(product.getQuantity() + "");
            itemPrice.setText(product.getPrice());
            itemTotal.setText(product.getTotalPrice());
            itemColor.setText(product.getColor());
            itemSize.setText(product.getSize());

            itemName.setTypeface(typeface);
            qtyTitle.setTypeface(typeface);
            itemQty.setTypeface(typeface);
            priceTitle.setTypeface(typeface);
            itemPrice.setTypeface(typeface);
            totalTitle.setTypeface(typeface);
            itemTotal.setTypeface(typeface);
            itemColor.setTypeface(typeface);
            colorTitle.setTypeface(typeface);
            itemSize.setTypeface(typeface);
            sizeTitle.setTypeface(typeface);

            return view;
        }
    }

    private void setItemList(ArrayList<SecondryProduct> arrayList) {
        MyAdapter adapter = new MyAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
    }

    public void checkLocationPermession() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED)
            requestLocationPermession();
        else
            changeStatus();
    }

    public void requestLocationPermession() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeStatus();
            } else
                Toast.makeText(this, "Device Location is Required", Toast.LENGTH_SHORT).show();

        }


    }

    public void changeStatus() {
        databaseReference.child("Users").child(order.getuserId()).child("orders").child(key).child("status").getRef().setValue(2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startDeliveyBtn.setText("Delivered successfully");
                    openDeliveringLayout();
                    startTrackingLocation();
                }
            }
        });

    }

    public void startTrackingLocation() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(order.getuserId()).child("orders").child(key);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (android.location.Location location : locationResult.getLocations()) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    databaseReference.child("DeliveringLocation").setValue(new DeliveringLocation(latitude, longitude));
                }
            }
        };

        FusedLocationProviderClient providerClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        providerClient.requestLocationUpdates(request, locationCallback, null);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("from")){
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        else
        super.onBackPressed();
    }
}
