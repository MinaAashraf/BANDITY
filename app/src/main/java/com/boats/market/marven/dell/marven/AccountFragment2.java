package com.boats.market.marven.dell.marven;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 6/22/2019.
 */

public class AccountFragment2 extends Fragment {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<AddressModel> arrayList = new ArrayList<>();
    ArrayList<Order> ordersArray = new ArrayList<>();
    LinearLayout address_account_layout, noaddress_layout;
    ProgressBar progressBar, progressBar2;
    String additional, stre, stat, pho, ar, apart, flo, build, nam;
    ArrayList<String> keysArray = new ArrayList<>();
    String thiskey;
    Fragment accountfragment = new MainAccountFragment();

    Typeface typeface;

   public class Adapter extends ArrayAdapter {

        public Adapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.order_item, parent, false);
            TextView date = v.findViewById(R.id.date);
            TextView price = v.findViewById(R.id.price);
            ImageView status_icon = v.findViewById(R.id.status_icon);
            Order order = ordersArray.get(position);
            date.setText(order.getDate());
            price.setText(order.getPrice());
            switch (order.getStatus())
            {
                case 1:
                    status_icon.setImageResource(R.drawable.check_icon); break;
                case 2:
                    status_icon.setImageResource(R.drawable.delivering_icon);break;
                case 3:
                    status_icon.setImageResource(R.drawable.delivered_icon);break;
                default:break;
            }

            return v;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_fragment2, container, false);
        HomeActivity.currentFragment = "Fragment_5";
        HomeActivity.categoryname.setText("Account");

        firebaseAuth = FirebaseAuth.getInstance();
        final TextView itemssize = v.findViewById(R.id.size);

        typeface = Typeface.createFromAsset(getActivity().getAssets(), "gotham.ttf");


        databaseReference = FirebaseDatabase.getInstance().getReference();
        TextView startshopping_txt = v.findViewById(R.id.startshopping_txt);
        TextView adress_txt = v.findViewById(R.id.addadress_txt);
        TextView logouttxt = v.findViewById(R.id.logout_txt);
        TextView changePass_txt = v.findViewById(R.id.changepass_txt);
        address_account_layout = v.findViewById(R.id.adress_account_layout);
        noaddress_layout = v.findViewById(R.id.noadress_layout);

        final TextView accountname = v.findViewById(R.id.accountname);
        final TextView accountemail = v.findViewById(R.id.accountemail);
        final TextView name, building, street, area, floor, apartment, state, phone, country, change, edit, remove;
        name = v.findViewById(R.id.name_acount);
        building = v.findViewById(R.id.building_account);
        street = v.findViewById(R.id.street_account);
        area = v.findViewById(R.id.area_account);
        floor = v.findViewById(R.id.floor_account);
        apartment = v.findViewById(R.id.apartmentnum_account);
        state = v.findViewById(R.id.state_account);
        phone = v.findViewById(R.id.phone_account);
        change = v.findViewById(R.id.change_address_account);
        country = v.findViewById(R.id.country);
        edit = v.findViewById(R.id.edittxt);
        final ListView listView = v.findViewById(R.id.listview);
        final Adapter adapter = new Adapter(getActivity(), android.R.layout.simple_list_item_1, ordersArray);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(getActivity(),OrderConfirmations.class).putExtra("order",ordersArray.get(position)));
            }
        });


        startshopping_txt.setTypeface(typeface);
        adress_txt.setTypeface(typeface);
        logouttxt.setTypeface(typeface);
        changePass_txt.setTypeface(typeface);
        accountname.setTypeface(typeface);
        accountemail.setTypeface(typeface);
        name.setTypeface(typeface);
        building.setTypeface(typeface);
        street.setTypeface(typeface);
        area.setTypeface(typeface);
        floor.setTypeface(typeface);
        apartment.setTypeface(typeface);
        state.setTypeface(typeface);
        phone.setTypeface(typeface);
        change.setTypeface(typeface);
        country.setTypeface(typeface);
        edit.setTypeface(typeface);

        final LinearLayout orderLayout = v.findViewById(R.id.orders_layout);
        final LinearLayout ordersListLayout = v.findViewById(R.id.listLayout);
        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("orders")) {
                    orderLayout.setVisibility(View.GONE);
                    ordersListLayout.setVisibility(View.VISIBLE);
                    dataSnapshot.child("orders").getRef().addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ordersArray.add(dataSnapshot.getValue(Order.class));
                            adapter.notifyDataSetChanged();
                            if (ordersArray.size() == 1)
                                itemssize.setText(ordersArray.size() + " order");

                            else
                                itemssize.setText(ordersArray.size() + " orders");
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        progressBar = v.findViewById(R.id.progressBar2);
        progressBar2 = v.findViewById(R.id.progressBar3);
        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("addresses")) {

                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("addresses").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            AddressModel addressModel = dataSnapshot.getValue(AddressModel.class);
                            arrayList.add(addressModel);
                            keysArray.add(dataSnapshot.getKey());
                            databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("default_address")) {

                                        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("default_address").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                int positiion = dataSnapshot.getValue(Integer.class);

                                                name.setText(arrayList.get(positiion).getName());
                                                building.setText(arrayList.get(positiion).getBuilding() + "/");
                                                street.setText(arrayList.get(positiion).getStreet() + "/");
                                                if (arrayList.get(positiion).getArea().length()>=27)
                                                    area.setText(arrayList.get(positiion).getArea().substring(0,27)+"..");
                                                else
                                                    area.setText(arrayList.get(positiion).getArea());
                                                floor.setText("Floor : " + arrayList.get(positiion).getFloor() + "/");
                                                apartment.setText("Apartment : " + arrayList.get(positiion).getApartment());
                                                state.setText(arrayList.get(positiion).getState() + "/");
                                                phone.setText(arrayList.get(positiion).getPhone());
                                                country.setText("Egypt");

                                                additional = arrayList.get(positiion).getAdditional();
                                                pho = arrayList.get(positiion).getPhone();
                                                stre = arrayList.get(positiion).getStreet();
                                                ar = arrayList.get(positiion).getArea();
                                                stat = arrayList.get(positiion).getState();
                                                build = arrayList.get(positiion).getBuilding();
                                                apart = arrayList.get(positiion).getApartment();
                                                flo = arrayList.get(positiion).getFloor();
                                                nam = arrayList.get(positiion).getName();
                                                thiskey = keysArray.get(positiion);

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else {

                                        name.setText(arrayList.get(0).getName());
                                        building.setText(arrayList.get(0).getBuilding() + " / ");
                                        street.setText(arrayList.get(0).getStreet() + " / ");
                                        if (arrayList.get(0).getArea().length()>=27)
                                            area.setText(arrayList.get(0).getArea().substring(0,27)+"..");
                                        else
                                            area.setText(arrayList.get(0).getArea());
                                        floor.setText("Floor : " + arrayList.get(0).getFloor() + "/");
                                        apartment.setText("Apartment : " + arrayList.get(0).getApartment());
                                        state.setText(arrayList.get(0).getState() + "/");
                                        phone.setText(arrayList.get(0).getPhone());
                                        country.setText("Egypt");
                                        additional = arrayList.get(0).getAdditional();


                                        additional = arrayList.get(0).getAdditional();
                                        pho = arrayList.get(0).getPhone();
                                        stre = arrayList.get(0).getStreet();
                                        ar = arrayList.get(0).getArea();
                                        stat = arrayList.get(0).getState();
                                        build = arrayList.get(0).getBuilding();
                                        apart = arrayList.get(0).getApartment();
                                        flo = arrayList.get(0).getFloor();
                                        nam = arrayList.get(0).getName();
                                        thiskey = keysArray.get(0);

                                    }
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
                    address_account_layout.setVisibility(View.VISIBLE);
                    change.setVisibility(View.VISIBLE);
                    noaddress_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                } else {
                    address_account_layout.setVisibility(View.GONE);
                    change.setVisibility(View.GONE);
                    noaddress_layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                progressBar2.setVisibility(View.GONE);
                accountname.setText(users.getName());
                accountemail.setText(users.getEmail());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RelativeLayout changePass_layout = v.findViewById(R.id.changepass_layout);
        changePass_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangePassword.class));
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to logout ?").setPositiveButton("LogOut", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseAuth.signOut();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.placeholder2, accountfragment);
                fragmentTransaction.commit();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        RelativeLayout logout_layout = v.findViewById(R.id.logout_layout);
        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });

        startshopping_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();

            }
        });

        adress_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddressActivity.class).putExtra("from", "account"));
                getActivity().finish();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DiffernetAddress.class).putExtra("which", "account"));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddressModel addressModel = new AddressModel(nam, build, stre, ar, flo, apart, stat, pho, additional);
                startActivity(new Intent(getActivity(), AddressActivity.class).putExtra("edit", (Serializable) addressModel).putExtra("from","account")
                        .putExtra("key", thiskey));
            }
        });


        TextView myOrders = v.findViewById(R.id.myorders);
        TextView noOrders = v.findViewById(R.id.noorders);
        TextView myaddresses = v.findViewById(R.id.myaddresses);
        TextView addAdress = v.findViewById(R.id.addaddress);

        myOrders.setTypeface(typeface);
        noOrders.setTypeface(typeface);
        myaddresses.setTypeface(typeface);
        addAdress.setTypeface(typeface);


        return v;
    }
}
