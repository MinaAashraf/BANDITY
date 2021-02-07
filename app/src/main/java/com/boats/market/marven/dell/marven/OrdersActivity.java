package com.boats.market.marven.dell.marven;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    ArrayList<Order> ordersArray = new ArrayList<>();
    ArrayList<String> keys = new ArrayList<>();
    ListView listView;

    @Override
    protected void onResume() {
        super.onResume();
        if (ordersArray != null && listView != null) {
            final Adapter adapter = new Adapter(this, android.R.layout.simple_list_item_1, ordersArray);
            listView.setAdapter(adapter);
        }
    }

    public class Adapter extends ArrayAdapter {
        public Adapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.order_item, parent, false);
            TextView date = v.findViewById(R.id.date);
            TextView price = v.findViewById(R.id.price);
            ImageView status_icon = v.findViewById(R.id.status_icon);
            Order order = ordersArray.get(position);
            date.setText(order.getDate());
            price.setText(order.getPrice());
            switch (order.getStatus()) {
                case 1:
                    status_icon.setImageResource(R.drawable.check_icon);
                    break;
                case 2:
                    status_icon.setImageResource(R.drawable.delivering_icon);
                    break;
                case 3:
                    status_icon.setImageResource(R.drawable.delivered_icon);
                    break;
                default:
                    break;
            }

            return v;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        listView = findViewById(R.id.listview);
        final Adapter adapter = new Adapter(this, android.R.layout.simple_list_item_1, ordersArray);
        listView.setAdapter(adapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").orderByChild("orders").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                dataSnapshot.child("orders").getRef().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ordersArray.add(dataSnapshot.getValue(Order.class));
                        keys.add(dataSnapshot.getKey());
                        adapter.notifyDataSetChanged();

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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(OrdersActivity.this, OrderConfirmations.class).putExtra("order", ordersArray.get(position)).putExtra("admin", true).putExtra("orderKey", keys.get(position)));
            }
        });
    }
}
