package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import com.squareup.picasso.Picasso;

/*import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;*/
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class CheckoutActivity extends AppCompatActivity {
    ArrayList<AddressModel> arrayList = new ArrayList();
    ArrayList<ProductModel> products_array = new ArrayList<>();
    ArrayList<ProductModel> products_array2 = new ArrayList<>();
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    float discount = 0;
    String total;
    int k = 0;
    int i = -1;
    RadioButton cash_btn, visa_btn;
    TextView name, building, street, area, floor, apartment, state, phone, changeadress, addaddress;
    int positiion;
    Typeface typeface;
    final String PAYPAL_CLIENT_ID = "AY5sBKVjJLBBQTDi2GOnbE6Lol7eWEeSt9Gnrnh-OU4AWhupjBbkCuVn1PNq_OxEiREhRdAOV3V1NIWd";
    final int REQUSET_CODE = 0;
    PayPalConfiguration config;
    TextView totalamount, subtotal;
    boolean exist = false;
    int cart_counter;
    android.support.v7.widget.Toolbar toolbar;
    TextView cart_counter_txt;
    DecimalFormat decimalFormat = new DecimalFormat("0.#");
    float paymentAmount;
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
    }

    public class Adapter extends ArrayAdapter {

        public Adapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.checkout_cartitem, parent, false);
            ImageView imageView = v.findViewById(R.id.cart_imge);
            TextView sort = v.findViewById(R.id.cart_sort);
            TextView qut = v.findViewById(R.id.qty_txt);
            TextView price = v.findViewById(R.id.cart_price);
            TextView totalprice = v.findViewById(R.id.cart_total_price);
            qut.setTypeface(typeface);
            TextView colorTitle = v.findViewById(R.id.color);
            TextView itemColor = v.findViewById(R.id.color_txt);
            TextView sizeTitle = v.findViewById(R.id.size);
            TextView itemSize = v.findViewById(R.id.size_txt);

            TextView txt1 = v.findViewById(R.id.itemprice);
            TextView txt2 = v.findViewById(R.id.qty_txt);
            TextView txt3 = v.findViewById(R.id.total);
            txt1.setTypeface(typeface);
            txt2.setTypeface(typeface);
            txt3.setTypeface(typeface);
            colorTitle.setTypeface(typeface);
            sizeTitle.setTypeface(typeface);

            if (getIntent().getExtras().getString("from", "cart").equals("cart")) {
                Picasso.get().load(products_array.get(position).getUrl()).placeholder(R.drawable.home_logo).into(imageView);
                sort.setText(products_array.get(position).getSort());
                price.setText(products_array.get(position).getPrice());
                itemColor.setText(products_array.get(position).getColorr());
                itemSize.setText(products_array.get(position).getSizee());
                if (products_array.get(position).getTotalprice().equals("0")) {
                    totalprice.setText(String.valueOf(Float.valueOf(products_array.get(position).getPrice())));

                } else
                    totalprice.setText(products_array.get(position).getTotalprice());

                qut.setText("" + products_array.get(position).getItemnum());

            } else {
                Picasso.get().load(products_array2.get(position).getUrl()).placeholder(R.drawable.home_logo).into(imageView);
                sort.setText(products_array2.get(position).getSort());
                price.setText(products_array2.get(position).getPrice());
                itemColor.setText(products_array2.get(position).getColorr());
                itemSize.setText(products_array2.get(position).getSizee());
                if (products_array2.get(position).getTotalprice().equals("0")) {
                    totalprice.setText(String.valueOf(Float.valueOf(products_array2.get(position).getPrice())));

                } else
                    totalprice.setText(products_array2.get(position).getTotalprice());

                qut.setText("" + products_array2.get(position).getItemnum());
            }

            return v;
        }
    }

    static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        fa = this;
        typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");


        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        TextView categoryname = toolbar.findViewById(R.id.customcategort_name);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);

        cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);


        TextView itemssize = findViewById(R.id.size);
        ListView listView = findViewById(R.id.listview2);
        categoryname.setTypeface(typeface);
        progressBar = findViewById(R.id.progressbar);
        subtotal = findViewById(R.id.subtotal_txt);
        final TextView discountValue = findViewById(R.id.discountvalue);
        discountValue.setTypeface(typeface);
        totalamount = findViewById(R.id.total_amount);
        ImageView search_icon = toolbar.findViewById(R.id.search_icon);
        Button redeem_btn = findViewById(R.id.redeem_btn);
        redeem_btn.setTypeface(typeface);
        final EditText voucherCodeEdittxt = findViewById(R.id.vouchercode_edittxt);
        voucherCodeEdittxt.setTypeface(typeface);
        total = getSharedPreferences("total", 0).getString("totalvalue", "");
        subtotal.setText("EGP "+total);
        totalamount.setText(total + " + delivery fee");
        subtotal.setTypeface(typeface);
        totalamount.setTypeface(typeface);
        final LinearLayout noaddressLayout = findViewById(R.id.noadress_layout);


        // sendConvertRequest();


        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("voucherCode")) {
                     discount = dataSnapshot.child("voucherCode").child("discountVal").getValue(Float.class);
                    discountValue.setText("EGP "+discount );
                    if ((Float.valueOf(total) - discount) < 0) {
                        totalamount.setText("0 + delivery fee");
                    } else {
                        totalamount.setText(Float.valueOf(total) - discount + " + delivery fee");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        redeem_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                i = -1;
                exist = false;

                if (voucherCodeEdittxt.getText().toString().isEmpty()) {
                    voucherCodeEdittxt.setError("Enter Code");
                    voucherCodeEdittxt.requestFocus();
                } else {

                    databaseReference.child("voucherCodes").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            k = (int) dataSnapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    databaseReference.child("voucherCodes").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                            i++;
                            final DataSnapshot dataSnapshot1 = dataSnapshot;
                            final String code = dataSnapshot.child("code").getValue().toString();
                            if (voucherCodeEdittxt.getText().toString().trim().equals(code)) {
                                exist = true;
                                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("voucherCode")&& dataSnapshot.child("voucherCode").hasChild(voucherCodeEdittxt.getText().toString().trim())) {
                                            voucherCodeEdittxt.setError("You had used this voucher code before");
                                        } else {
                                            final int value = dataSnapshot1.child("value").getValue(Integer.class);
                                            int type = dataSnapshot1.child("discountType").getValue(Integer.class);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);

                                            if (type == 0) {

                                                builder.setTitle("Voucher Code").setMessage("You will have " + value + "% discount if you use your voucher code , and you can not use the same code again.")
                                                        .setPositiveButton("Use", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                discount += value * Float.valueOf(total) / 100;
                                                                discountValue.setText("EGP "+discount );
                                                                totalamount.setText(Float.valueOf(total) - discount + " + delivery fee");
                                                                Toast.makeText(CheckoutActivity.this, "Voucher Code is applied", Toast.LENGTH_SHORT).show();

                                                                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("voucherCode").child("discountVal").setValue(Float.valueOf(decimalFormat.format(discount)));
                                                                if (dataSnapshot1.child("scope").getValue().equals("Special"))
                                                                    dataSnapshot1.getRef().removeValue();
                                                                else {
                                                                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("voucherCode").child(code).setValue("usedCode");
                                                                }
                                                                voucherCodeEdittxt.setText("");


                                                            }
                                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                }).show();
                                            } else {
                                                builder.setTitle("Voucher Code").setMessage("You will have " + value + " pounds discount if you use your voucher code , and you can not use the same code again.")
                                                        .setPositiveButton("Use", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                discount += value;
                                                                discountValue.setText(" EGP "+discount);
                                                                if ((Float.valueOf(total) - discount) < 0) {
                                                                    totalamount.setText("0 + delivery fee");
                                                                } else {
                                                                    totalamount.setText(Float.valueOf(total) - discount + " + delivery fee");
                                                                }
                                                                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("voucherCode").child("discountVal").setValue(Float.valueOf(decimalFormat.format(discount)));
                                                                Toast.makeText(CheckoutActivity.this, "Voucher Code is applied", Toast.LENGTH_SHORT).show();
                                                                if (dataSnapshot1.child("scope").getValue().equals("Special"))
                                                                    dataSnapshot1.getRef().removeValue();
                                                                else {
                                                                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("voucherCode").child(code).setValue("usedCode");
                                                                }

                                                                voucherCodeEdittxt.setText("");
                                                            }
                                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                }).show();

                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            } else {
                                if (i == k - 1) {
                                    if (!exist)
                                    {
                                        voucherCodeEdittxt.setError("This is a fake code !");
                                        voucherCodeEdittxt.requestFocus();
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
        });

        search_icon.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckoutActivity.this, SearchActivity.class));
            }
        });
        cash_btn = findViewById(R.id.cash_btn);
        visa_btn = findViewById(R.id.visa_btn);


        config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PAYPAL_CLIENT_ID);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        Button placeOrder = findViewById(R.id.placeorder_btn);

        placeOrder.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (arrayList.size() != 0) {
                 //   if (cash_btn.isChecked()) {
                        if (getIntent().getExtras().getString("from").equals("cart"))
                            startActivity(new Intent(CheckoutActivity.this, PlaceOrder.class).putExtra("pro", products_array).putExtra("address", arrayList.get(positiion)).putExtra("payment", totalamount.getText().toString()).putExtra("from","cart"));
                        else
                            startActivity(new Intent(CheckoutActivity.this, PlaceOrder.class).putExtra("pro", products_array2).putExtra("address", arrayList.get(positiion)).putExtra("payment", totalamount.getText().toString()).putExtra("from","buy"));
                    /*}
                    else if (visa_btn.isChecked()) {
                            getPayment();
                        }*/
                } else {
                    startActivity(new Intent(CheckoutActivity.this, AddressActivity.class));
                    Toast.makeText(CheckoutActivity.this, "Add your address please..", Toast.LENGTH_SHORT).show();
                }

            }
        });
        String from = getIntent().getExtras().getString("from", "cart");
        if (from.equals("cart")) {
            SharedPreferences sharedPreferences = getSharedPreferences("cart", 0);
            Gson gson2 = new Gson();

            String json2 = sharedPreferences.getString("json", null);
            Type type = new TypeToken<ArrayList<ProductModel>>() {
            }.getType();
            if (json2 != null)
                products_array = gson2.fromJson(json2, type);


            Adapter adapter = new Adapter(this, android.R.layout.simple_list_item_1, products_array);
            listView.setAdapter(adapter);

            if (products_array.size() == 1)
                itemssize.setText(products_array.size() + " item");

            else
                itemssize.setText(products_array.size() + " items");
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("cart", 0);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("json2", null);
            if (json!=null)
            {
            ProductModel model = gson.fromJson(json,ProductModel.class);
              products_array2.add(model);
            }


            Adapter adapter = new Adapter(this, android.R.layout.simple_list_item_1, products_array2);
            listView.setAdapter(adapter);
        }


        final LinearLayout adress_layout = findViewById(R.id.adress_linear_layout);
        name =

                findViewById(R.id.name_adress);

        building =

                findViewById(R.id.building_adress);

        street =

                findViewById(R.id.street_adress);

        area =

                findViewById(R.id.area_adress);

        floor =

                findViewById(R.id.floor_adress);

        apartment =

                findViewById(R.id.apartmentnum_adress);

        state =

                findViewById(R.id.state_adress);

        phone = findViewById(R.id.phone_adress);

        changeadress = findViewById(R.id.changeadress);

        addaddress = findViewById(R.id.addadress_txt);

        name.setTypeface(typeface);
        building.setTypeface(typeface);
        street.setTypeface(typeface);
        area.setTypeface(typeface);
        floor.setTypeface(typeface);
        apartment.setTypeface(typeface);
        state.setTypeface(typeface);
        phone.setTypeface(typeface);
        changeadress.setTypeface(typeface);
        addaddress.setTypeface(typeface);

        databaseReference = FirebaseDatabase.getInstance().

                getReference();


        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("addresses")) {

                    databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("addresses").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            AddressModel addressModel = dataSnapshot.getValue(AddressModel.class);
                            arrayList.add(addressModel);

                            databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("default_address")) {
                                        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("default_address").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                positiion = dataSnapshot.getValue(Integer.class);
                                                name.setText(arrayList.get(positiion).getName());
                                                building.setText(arrayList.get(positiion).getBuilding() + "/");
                                                street.setText(arrayList.get(positiion).getStreet() + "/");
                                                if (arrayList.get(positiion).getArea().length()>=27)
                                                area.setText(arrayList.get(positiion).getArea().substring(0,27)+"..");
                                                else
                                                area.setText(arrayList.get(positiion).getArea());

                                                floor.setText("Floor : " + arrayList.get(positiion).getFloor() + "/");
                                                apartment.setText("Apartment : " + arrayList.get(positiion).getApartment());
                                                state.setText(arrayList.get(positiion).getState() + "/Egypt");
                                                phone.setText(arrayList.get(positiion).getPhone());
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        name.setText(arrayList.get(0).getName());
                                        building.setText(arrayList.get(0).getBuilding() + "/");
                                        street.setText(arrayList.get(0).getStreet() + "/");
                                        if (arrayList.get(0).getArea().length()>=27)
                                            area.setText(arrayList.get(0).getArea().substring(0,27)+"......");
                                        else
                                            area.setText(arrayList.get(0).getArea());
                                        floor.setText("Floor : " + arrayList.get(0).getFloor() + " / ");
                                        apartment.setText("Apartment : " + arrayList.get(0).getApartment());
                                        state.setText(arrayList.get(0).getState() + " / Egypt");
                                        phone.setText(arrayList.get(0).getPhone());
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
                    noaddressLayout.setVisibility(View.GONE);
                    changeadress.setVisibility(View.VISIBLE);
                    adress_layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);


                } else {
                    noaddressLayout.setVisibility(View.VISIBLE);
                    changeadress.setVisibility(View.GONE);
                    adress_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        itemssize.setTypeface(typeface);
        categoryname.setText("Checkout");
        backimg.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckoutActivity.this, CartActivity.class));
            }
        });

        categoryname.setTypeface(typeface);
        cartimg.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckoutActivity.this, CartActivity.class));
            }
        });

        TextView addadress = findViewById(R.id.addadress_txt);
        addadress.setTypeface(typeface);
        addadress.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckoutActivity.this, AddressActivity.class).putExtra("from", "checkout"));

            }
        });

        placeOrder.setTypeface(typeface);
        changeadress.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckoutActivity.this, DiffernetAddress.class).putExtra("which", "checkout"));


            }
        });
        changeadress.setTypeface(typeface);
        TextView subTotla = findViewById(R.id.subtotal);
        TextView discount = findViewById(R.id.discount);
        TextView totalamounttxt = findViewById(R.id.totalamount);

        subTotla.setTypeface(typeface);
        discount.setTypeface(typeface);
        totalamounttxt.setTypeface(typeface);

        TextView txt1 = findViewById(R.id.txt1);
        TextView txt2 = findViewById(R.id.txt2);
        TextView txt3 = findViewById(R.id.txt3);
        TextView txt4 = findViewById(R.id.txt4);

        txt1.setTypeface(typeface);
        txt2.setTypeface(typeface);
        txt3.setTypeface(typeface);
        txt4.setTypeface(typeface);


    }


    public void getPayment() {
        databaseReference.child("currency").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float currencyRate = dataSnapshot.getValue(Float.class);
         paymentAmount = Float.valueOf(totalamount.getText().toString().substring(0, totalamount.getText().toString().indexOf('+')))*currencyRate;
        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount), "USD", "Online Payment", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, REQUSET_CODE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSET_CODE && resultCode == RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    String paymentDetails = confirm.toJSONObject().toString(4);
                    Log.i("paymentExample", paymentDetails);

                    if (getIntent().getExtras().getString("from").equals("cart"))
                        startActivity(new Intent(CheckoutActivity.this, PlaceOrder.class).putExtra("pro", products_array).putExtra("address", arrayList.get(positiion)).putExtra("payment", totalamount.getText().toString()).putExtra("from","cart").putExtra("paymentType","visa"));
                    else
                        startActivity(new Intent(CheckoutActivity.this, PlaceOrder.class).putExtra("pro", products_array2).putExtra("address", arrayList.get(positiion)).putExtra("payment", totalamount.getText().toString()).putExtra("from","buy").putExtra("paymentType","visa"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }

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

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }




    /*public  final String ACCESS_KEY = "8d6010d345ddbb5c0459be4187cd6052";
    public  final String BASE_URL = "https://apilayer.net/api/";
    public  final String ENDPOINT = "convert";
*/
    // this object is used for executing requests to the (REST) API
      //CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     *
     * Notes:<br><br>
     *
     * A JSON response of the form {"key":"value"} is considered a simple Java JSONObject.<br>
     * To get a simple value from the JSONObject, use: <JSONObject identifier>.get<Type>("key");<br>
     *
     * A JSON response of the form {"key":{"key":"value"}} is considered a complex Java JSONObject.<br>
     * To get a complex value like another JSONObject, use: <JSONObject identifier>.getJSONObject("key")
     *
     * Values can also be JSONArray Objects. JSONArray objects are simple, consisting of multiple JSONObject Objects.
     *
     *
     */

//     sendConvertRequest() function is created to request and retrieve the data
 /*  public void sendConvertRequest(){

        // the "from", "to" and "amount" can be set as variables
        HttpGet get = new HttpGet(BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY + "&from=GBP&to=INR&amount=2");
        try {
            CloseableHttpResponse response =  httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));

            System.out.println("Single-Currency Conversion");

            // parsed JSON Objects are accessed according to the JSON resonse's hierarchy, output strings are built
            System.out.println("From : " + jsonObject.getJSONObject("query").getString("from"));
            System.out.println("To : " + jsonObject.getJSONObject("query").getString("to"));
            System.out.println("Amount : " + jsonObject.getJSONObject("query").getLong("amount"));
            System.out.println("Conversion Result : " + jsonObject.getDouble("result"));
            System.out.println("\n");
            response.close();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }*/

}


