package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class PlaceOrder extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<ProductModel> products_array = new ArrayList<>();
    ArrayList<ProductModel> arrayList;
   ArrayList <SecondryProduct> secondryArray;
    String payment;
    AddressModel addressModel;
    String finalMessage;
    int i = 0;
    RelativeLayout confirm_lay;
    FirebaseAuth firebaseAuth;
    RelativeLayout confirmationLayout;
    String price , paymentType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        arrayList = new ArrayList<>();
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        addressModel = (AddressModel) getIntent().getExtras().get("address");

        confirmationLayout = findViewById(R.id.confirmation_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        TextView title = toolbar.findViewById(R.id.customcategort_name);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);
        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        title.setTypeface(typeface);

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        confirm_lay = findViewById(R.id.confirmation_layout);
        backimg.setVisibility(View.GONE);
        cartimg.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        TextView txt = findViewById(R.id.txt);
        txt.setTypeface(typeface);
        Button back = findViewById(R.id.back_btn);
        final Button confirm = findViewById(R.id.confirm_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String message = "<br><br><h2 style=\"color: #292561\">The Order :</h2>";
        products_array = (ArrayList) getIntent().getExtras().get("pro");
        secondryArray = new ArrayList<>();
        for (ProductModel productModel: products_array) {
            secondryArray.add(new SecondryProduct(productModel.getSort(),productModel.getUrl(),productModel.getPrice(),productModel.getTotalprice(),productModel.getColorr(),productModel.getSizee(),productModel.getItemnum()));
            message +=  "<br>Name :" + products_array.get(i).getSort() + "<br>Color :" + products_array.get(i).getColorr() + "<br>Size :" + products_array.get(i).getSizee() + "<br>Quantity :" + products_array.get(i).getItemnum() + "<br><br>";
        }
        back.setTypeface(typeface);
        confirm.setTypeface(typeface);
        finalMessage = message;
        databaseReference = FirebaseDatabase.getInstance().getReference();


        payment = getIntent().getExtras().getString("payment");
         price = payment;
        if (getIntent().hasExtra("paymentType")) {
            paymentType = "online";
            payment += "<br>Paid online by PayPal.Check your PayPal account";

            confirm();
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentType = "Cash";
                payment += "<br>will be paid by cash";
                confirm();

            }
        });
    /*    home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlaceOrder.this, HomeActivity.class).putExtra("orders", products_array));
                finish();

            }
        });*/


    }

    @Override
    public void onBackPressed() {

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        else {


            if (i == 1) {
                startActivity(new Intent(PlaceOrder.this, HomeActivity.class).putExtra("orders", products_array));
                finish();

            } else {
                super.onBackPressed();
            }
        }
    }


    public void confirm() {
        i = 1;
        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("voucherCode").child("discountVal").setValue(0);
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat postFormater = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
         FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Order order = new Order(secondryArray,addressModel,1, postFormater.format(currentDate),price,paymentType,user.getUid());
        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("orders").push().setValue(order);


        SendMail sendMail = new SendMail();
        sendMail.sendEmail("Bandity.eg@gmail.com", "BANDITY APP", "<div style=\"background-color: #c6ccd7 ;padding: 40px\"><img src=\"https://firebasestorage.googleapis.com/v0/b/marven-6c727.appspot.com/o/account_logo.png?alt=media&token=a0159abd-ecc4-4748-a6f3-84c8c8cbc931\" style=\"margin-top: 10px ;margin-left: 0px ; width:300px;height:300px;\">><br><h2 style=\"color: #292561\">User Data :</h2>UserName :" + addressModel.getName() + "<br>UserPhone :" + addressModel.getPhone() + "<br>UserEmail :" + firebaseAuth.getCurrentUser().getEmail() + "<br>UserAddress :<br>" + addressModel.getBuilding() + " / " + addressModel.getArea() + " / " + addressModel.getStreet() + "<br>" + "Floor : " + addressModel.getFloor() + " / " + "Apartment : " + addressModel.getApartment() + "<br>" + addressModel.getState() + " / Egypt" + "<br>Additional directions : " + addressModel.getAdditional() + "<br>" + finalMessage + "<br><h2 style=\"color: #292561\">Payment :</h2>" + payment + "</div>");
        sendMail.sendEmail(firebaseAuth.getCurrentUser().getEmail(), "Thanks for your order", "<div style=\"background-color: #6495ED;padding: 5px;height: 280px\"><img src=\"https://firebasestorage.googleapis.com/v0/b/marven-6c727.appspot.com/o/BANDITY-outlet-logo.png?alt=media&token=94020be0-ce7e-46b6-b393-b7d274b6fe4e\" style=\"margin-left: 5px ; width:150px;height:150px \"><h1 style=\"margin-top: 5px ; color:#000000\">Thanks for ordering,<br>" + addressModel.getName() + "</h1></div><div style=\"background-color: #c6ccd7; color: #000;padding:5px;width:500px;\"><h2>We hope you enjoyed the<br>experience.</h2></div><br><br><p style=\"color:#000\"><h3>Your order has been <span style=\"color: #292561\">confirmed</span> to<br>Marven stores,Your product(s) will be<br>delivered within <span style=\"color: #292561\">1 weak</span></h3></p><br><br><br><p style=\"text-align: center\">Don't reply to this email, messages will not be read<br>Marven stores</p>");
        SharedPreferences.Editor editor2 = getSharedPreferences("cart_counter", 0).edit();

        if (getIntent().getExtras().getString("from", "cart").equals("cart")) {
            editor2.putInt("count", 0).apply();
            SharedPreferences.Editor editor = getSharedPreferences("cart", 0).edit();
            Gson gson = new Gson();
            String json = gson.toJson(arrayList);
            editor.putString("json", json);
            editor.apply();
        }
        startActivity(new Intent(PlaceOrder.this,OrderConfirmations.class).putExtra("order",(Serializable) order).putExtra("from","PlaceOrder"));
        finish();
    }
}
