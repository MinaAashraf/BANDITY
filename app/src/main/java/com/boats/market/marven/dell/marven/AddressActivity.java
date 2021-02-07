package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AddressActivity extends FragmentActivity implements OnMapReadyCallback {
    EditText nameedittxt, areadittxt, streetdittxt, buildingdittxt, floordittxt, apartmentnum_dittxt, additionaledittxt, phoneedittxt;
    TextView namelabel, statelabel, arealabel, streetlabel, countrylabel, buildlabel, floorlabel, apartmentlabel, additionallabel, mobilelabel;
    private GoogleMap mMap;
    Button saveAddress_btn;
    Address obj;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String verificationid;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    LinearLayout addresslayout;
    Dialog dialog;
    String id, email, pass;
    EditText statedittxt;
    ArrayList<String> cities = new ArrayList<>();
    ProgressBar progressBar;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        nameedittxt = findViewById(R.id.name_edittxt3);
        statedittxt = findViewById(R.id.state_edittxt);
        areadittxt = findViewById(R.id.area_edittxt3);
        streetdittxt = findViewById(R.id.street_edittext);
        buildingdittxt = findViewById(R.id.build_edittext);
        floordittxt = findViewById(R.id.floor_edittext);
        apartmentnum_dittxt = findViewById(R.id.apartnumber_edittext);
        additionaledittxt = findViewById(R.id.additionaldirections_txt);
        namelabel = findViewById(R.id.namelabel3);
        statelabel = findViewById(R.id.statelabel);
        arealabel = findViewById(R.id.area_label);
        streetlabel = findViewById(R.id.streetlabel);
        countrylabel = findViewById(R.id.countrylabel);
        buildlabel = findViewById(R.id.buildlabel);
        floorlabel = findViewById(R.id.floorlbel);
        apartmentlabel = findViewById(R.id.apartmentnumlabel);
        additionallabel = findViewById(R.id.additionallabel);
        mobilelabel = findViewById(R.id.mobilelabel);
        phoneedittxt = findViewById(R.id.mobile_edittxt);
        saveAddress_btn = findViewById(R.id.saveadress_btn);
        addresslayout = findViewById(R.id.adresslay);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        nameedittxt.setTypeface(typeface);
        areadittxt.setTypeface(typeface);
        streetdittxt.setTypeface(typeface);
        buildingdittxt.setTypeface(typeface);
        floordittxt.setTypeface(typeface);
        apartmentnum_dittxt.setTypeface(typeface);
        additionaledittxt.setTypeface(typeface);
        mobilelabel.setTypeface(typeface);
        phoneedittxt.setTypeface(typeface);
        saveAddress_btn.setTypeface(typeface);
        namelabel.setTypeface(typeface);
        statelabel.setTypeface(typeface);
        arealabel.setTypeface(typeface);
        streetlabel.setTypeface(typeface);
        buildlabel.setTypeface(typeface);
        floorlabel.setTypeface(typeface);
        apartmentlabel.setTypeface(typeface);
        additionallabel.setTypeface(typeface);

        /*cities.add("Cairo");
        cities.add("Alexandria");
        cities.add("Giza");
        cities.add("Ismailia");
        cities.add("Aswan");
        cities.add("Asyut");
        cities.add("Beheira");
        cities.add("Beni Suef");
        cities.add("Dakahlia");
        cities.add("Damietta");
        cities.add("Faiyum");
        cities.add("Gharbia");
        cities.add("Kafr El Sheikh");
        cities.add("Luxor");
        cities.add("Matruh");
        cities.add("Minya");
        cities.add("Monufia");
        cities.add("New Valley");
        cities.add("North Sinai");
        cities.add("Port Said");
        cities.add("Qalyubia");
        cities.add("Qena");
        cities.add("Red Sea");
        cities.add("Sharqia");
        cities.add("Sohag");
        cities.add("South Sinai");
        cities.add("Suez");*/



        try {
            AddressModel addressModel = (AddressModel) getIntent().getSerializableExtra("edit");
            nameedittxt.setText(addressModel.getName());
            streetdittxt.setText(addressModel.getStreet().replace("/", ""));
            statedittxt.setText(addressModel.getState().replace("/", ""));
            floordittxt.setText(addressModel.getFloor().replace("/", ""));
            apartmentnum_dittxt.setText(addressModel.getApartment());
            phoneedittxt.setText(addressModel.getPhone());
            buildingdittxt.setText(addressModel.getBuilding());
            areadittxt.setText(addressModel.getArea());
            additionaledittxt.setText(addressModel.getAdditional());


        } catch (Exception e) {
        }


        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                email = users.getEmail();
                pass = users.getPass();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progressBar2 = findViewById(R.id.progress_bar2);

        if (nameedittxt.getText().toString().isEmpty() || areadittxt.getText().toString().isEmpty() ||
                streetdittxt.getText().toString().isEmpty() || buildingdittxt.getText().toString().isEmpty() || floordittxt.getText().toString().isEmpty() ||
                apartmentnum_dittxt.getText().toString().isEmpty() || phoneedittxt.getText().toString().isEmpty()) {
            saveAddress_btn.setEnabled(false);
            saveAddress_btn.setBackgroundResource(R.drawable.btn2);
        } else {
            saveAddress_btn.setEnabled(true);
            saveAddress_btn.setBackgroundResource(R.drawable.btn);
        }


        try {
            obj = (Address) getIntent().getExtras().get("obj");
            Scanner scan = new Scanner(obj.getAddressLine(0));
            scan.useDelimiter(",");
            ArrayList<String> addressComponentes = new ArrayList<>();
            while (scan.hasNext()) {
                addressComponentes.add(scan.next());
            }

            streetdittxt.setText(addressComponentes.get(0));
            areadittxt.setText(addressComponentes.get(1) + " , " + addressComponentes.get(2));
            String theLast = addressComponentes.get(addressComponentes.size() - 1);
            statedittxt.setText(theLast);

        } catch (Exception e) {

        }

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.verificationdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final PinView pinView = dialog.findViewById(R.id.pinview);
        final Button verify = dialog.findViewById(R.id.verifybtn);
        final Button resend = dialog.findViewById(R.id.resendbtn);
        progressBar = dialog.findViewById(R.id.progress_bar);

        verify.setTypeface(typeface);
        resend.setTypeface(typeface);
        pinView.setTypeface(typeface);
        TextView verificationTitle = dialog.findViewById(R.id.verificationTitle);
        TextView pleaseWrite = dialog.findViewById(R.id.pleaseWrite);
        verificationTitle.setTypeface(typeface);
        pleaseWrite.setTypeface(typeface);

        saveAddress_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(saveAddress_btn);
                final String phone = phoneedittxt.getText().toString();


                if (phone.length() != 11) {
                    phoneedittxt.setError("invalid number");
                    phoneedittxt.requestFocus();
                } else if (!phone.substring(0, 3).contains("012") && !phone.trim().substring(0, 3).contains("011")
                        && !phone.trim().substring(0, 3).contains("015") &&
                        !phone.trim().substring(0, 3).contains("010")) {

                    phoneedittxt.setError("invalid number");
                    phoneedittxt.requestFocus();
                } else {
                    progressBar2.setVisibility(View.VISIBLE);
                    sendCode("+2" + phone);

                    verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (pinView.getText().toString().isEmpty()) {
                                pinView.setError("Enter the verification code!");
                                pinView.requestFocus();
                            } else {
                                verifyCode(pinView.getText().toString());
                            }


                        }
                    });


                    resend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            resendVerificationCode("+2" + phone, mResendToken);
                        }
                    });

                }


            }
        });

        nameedittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    namelabel.setTextColor(Color.parseColor("#292561"));
                } else {
                    namelabel.setTextColor(Color.parseColor("#707583"));

                }
            }
        });

        statedittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    statelabel.setTextColor(Color.parseColor("#292561"));
                } else {
                    statelabel.setTextColor(Color.parseColor("#707583"));

                }
            }
        });
        areadittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    arealabel.setTextColor(Color.parseColor("#292561"));
                } else {
                    arealabel.setTextColor(Color.parseColor("#707583"));

                }
            }
        });

        streetdittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    streetlabel.setTextColor(Color.parseColor("#292561"));
                } else {
                    streetlabel.setTextColor(Color.parseColor("#707583"));

                }
            }
        });
        nameedittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    namelabel.setTextColor(Color.parseColor("#292561"));
                } else {
                    namelabel.setTextColor(Color.parseColor("#707583"));

                }
            }
        });


        buildingdittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    buildlabel.setTextColor(Color.parseColor("#292561"));
                } else {
                    buildlabel.setTextColor(Color.parseColor("#707583"));

                }
            }
        });
        floordittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    floorlabel.setTextColor(Color.parseColor("#292561"));
                } else {
                    floorlabel.setTextColor(Color.parseColor("#707583"));

                }
            }
        });

        apartmentnum_dittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    apartmentlabel.setTextColor(Color.parseColor("#292561"));
                } else {
                    apartmentlabel.setTextColor(Color.parseColor("#707583"));

                }
            }
        });

        additionaledittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    additionallabel.setTextColor(Color.parseColor("#292561"));
                } else {
                    additionallabel.setTextColor(Color.parseColor("#707583"));

                }
            }
        });

        phoneedittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mobilelabel.setTextColor(Color.parseColor("#292561"));
                } else {
                    mobilelabel.setTextColor(Color.parseColor("#707583"));

                }
            }
        });


        nameedittxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (nameedittxt.getText().toString().isEmpty() || areadittxt.getText().toString().isEmpty() ||
                        streetdittxt.getText().toString().isEmpty() || buildingdittxt.getText().toString().isEmpty() || floordittxt.getText().toString().isEmpty() ||
                        apartmentnum_dittxt.getText().toString().isEmpty() || phoneedittxt.getText().toString().isEmpty()) {
                    saveAddress_btn.setEnabled(false);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn2);
                } else {
                    saveAddress_btn.setEnabled(true);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn);
                }

                return false;
            }
        });


        streetdittxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (nameedittxt.getText().toString().isEmpty() || areadittxt.getText().toString().isEmpty() ||
                        streetdittxt.getText().toString().isEmpty() || buildingdittxt.getText().toString().isEmpty() || floordittxt.getText().toString().isEmpty() ||
                        apartmentnum_dittxt.getText().toString().isEmpty() || phoneedittxt.getText().toString().isEmpty()) {
                    saveAddress_btn.setEnabled(false);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn2);
                } else {
                    saveAddress_btn.setEnabled(true);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn);
                }

                return false;
            }
        });


        buildingdittxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (nameedittxt.getText().toString().isEmpty() || areadittxt.getText().toString().isEmpty() ||
                        streetdittxt.getText().toString().isEmpty() || buildingdittxt.getText().toString().isEmpty() || floordittxt.getText().toString().isEmpty() ||
                        apartmentnum_dittxt.getText().toString().isEmpty() || phoneedittxt.getText().toString().isEmpty()) {
                    saveAddress_btn.setEnabled(false);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn2);
                } else {
                    saveAddress_btn.setEnabled(true);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn);
                }

                return false;
            }
        });

        floordittxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (nameedittxt.getText().toString().isEmpty() || areadittxt.getText().toString().isEmpty() ||
                        streetdittxt.getText().toString().isEmpty() || buildingdittxt.getText().toString().isEmpty() || floordittxt.getText().toString().isEmpty() ||
                        apartmentnum_dittxt.getText().toString().isEmpty() || phoneedittxt.getText().toString().isEmpty()) {
                    saveAddress_btn.setEnabled(false);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn2);
                } else {
                    saveAddress_btn.setEnabled(true);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn);
                }

                return false;
            }
        });


        apartmentnum_dittxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (nameedittxt.getText().toString().isEmpty() || areadittxt.getText().toString().isEmpty() ||
                        streetdittxt.getText().toString().isEmpty() || buildingdittxt.getText().toString().isEmpty() || floordittxt.getText().toString().isEmpty() ||
                        apartmentnum_dittxt.getText().toString().isEmpty() || phoneedittxt.getText().toString().isEmpty()) {
                    saveAddress_btn.setEnabled(false);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn2);
                } else {
                    saveAddress_btn.setEnabled(true);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn);
                }

                return false;
            }
        });

        phoneedittxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (nameedittxt.getText().toString().isEmpty() || areadittxt.getText().toString().isEmpty() ||
                        streetdittxt.getText().toString().isEmpty() || buildingdittxt.getText().toString().isEmpty() || floordittxt.getText().toString().isEmpty() ||
                        apartmentnum_dittxt.getText().toString().isEmpty() || phoneedittxt.getText().toString().isEmpty()) {
                    saveAddress_btn.setEnabled(false);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn2);
                } else {
                    saveAddress_btn.setEnabled(true);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn);
                }

                return false;
            }
        });

        areadittxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (nameedittxt.getText().toString().isEmpty() || areadittxt.getText().toString().isEmpty() ||
                        streetdittxt.getText().toString().isEmpty() || buildingdittxt.getText().toString().isEmpty() || floordittxt.getText().toString().isEmpty() ||
                        apartmentnum_dittxt.getText().toString().isEmpty() || phoneedittxt.getText().toString().isEmpty()) {
                    saveAddress_btn.setEnabled(false);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn2);
                } else {
                    saveAddress_btn.setEnabled(true);
                    saveAddress_btn.setBackgroundResource(R.drawable.btn);
                }

                return false;
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney;
        try {
            sydney = new LatLng(obj.getLatitude(), obj.getLongitude());
        } catch (Exception e) {
            sydney = new LatLng(18, 17);
        }
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                startActivity(new Intent(AddressActivity.this, MapsActivity2.class).putExtra("from", getIntent().getExtras().getString("from")).putExtra("which", getIntent().getExtras().getString("which")));
                finish();
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                startActivity(new Intent(AddressActivity.this, MapsActivity2.class).putExtra("from", getIntent().getExtras().getString("from")).putExtra("which", getIntent().getExtras().getString("which")));
                finish();
                return false;
            }
        });
    }


    public void sendCode(String num) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num, 10, TimeUnit.SECONDS, AddressActivity.this, mcallbacks
        );

    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressBar2.setVisibility(View.GONE);
            verificationid = s;
            mResendToken = forceResendingToken;
            dialog.show();
            Toast.makeText(AddressActivity.this, "Code is sent", Toast.LENGTH_SHORT).show();

        }


        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
            else{
                signinWithCredential(phoneAuthCredential);

            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar2.setVisibility(View.GONE);
            Toast.makeText(AddressActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }
    };


    private void verifyCode(String code) {
        progressBar2.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signinWithCredential(credential);

    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                10,
                TimeUnit.SECONDS,
                AddressActivity.this,
                mcallbacks,
                token);
    }

    private void signinWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String name = nameedittxt.getText().toString();
                    String state = statedittxt.getText().toString();
                    String street = streetdittxt.getText().toString();
                    String building = buildingdittxt.getText().toString();
                    String floor = floordittxt.getText().toString();
                    String phone = phoneedittxt.getText().toString();
                    String addition = additionaledittxt.getText().toString();
                    String area = areadittxt.getText().toString();
                    String apartment = apartmentnum_dittxt.getText().toString();
                    final AddressModel addressModel = new AddressModel(name, building, street, area, floor, apartment, state, phone, addition);

                    if (getIntent().hasExtra("edit")) {

                        final String key = getIntent().getExtras().getString("key");
                        databaseReference.child("Users").child(id).child("addresses").child(key).setValue(addressModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firebaseAuth.signOut();
                                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(AddressActivity.this, "Address Is Edited", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                progressBar2.setVisibility(View.GONE);
                                                onBackPressed();
                                               /* if (getIntent().getExtras().getString("from").equals("account")) {
                                                    startActivity(new Intent(AddressActivity.this, HomeActivity.class).putExtra("from", "account"));
                                                    finish();
                                                } else if (getIntent().getExtras().getString("from").equals("different")) {
                                                    startActivity(new Intent(AddressActivity.this, DiffernetAddress.class).putExtra("which", getIntent().getExtras().getString("which")));
                                                    finish();
                                                }*/

                                            } else {
                                                Toast.makeText(AddressActivity.this, "Edit Failed..", Toast.LENGTH_SHORT).show();
                                                progressBar2.setVisibility(View.GONE);

                                            }
                                        }
                                    });


                                } else {
                                    Toast.makeText(AddressActivity.this, "Code Is Incorrect", Toast.LENGTH_SHORT).show();
                                    progressBar2.setVisibility(View.GONE);

                                }
                            }
                        });
                    } else {
                        databaseReference.child("Users").child(id).child("addresses").push().setValue(addressModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firebaseAuth.signOut();
                                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(AddressActivity.this, "Phone Number Is Verified", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                progressBar2.setVisibility(View.GONE);
                                             /*   if (getIntent().getExtras().getString("from").equals("checkout"))
                                                    startActivity(new Intent(AddressActivity.this, CheckoutActivity.class));

                                                else if (getIntent().getExtras().getString("from").equals("different")) {
                                                    {
                                                        startActivity(new Intent(AddressActivity.this, DiffernetAddress.class).putExtra("which", getIntent().getExtras().getString("which")));
                                                    }

                                                } else if (getIntent().getExtras().getString("from").equals("account"))
                                                    startActivity(new Intent(AddressActivity.this, HomeActivity.class).putExtra("to", "account"));

                                                finish();*/
                                                onBackPressed();
                                            } else {
                                                Toast.makeText(AddressActivity.this, "Check Internet Connection..", Toast.LENGTH_SHORT).show();
                                                progressBar2.setVisibility(View.GONE);

                                            }
                                        }
                                    });


                                } else {
                                    Toast.makeText(AddressActivity.this, "Code Is Incorrect", Toast.LENGTH_SHORT).show();
                                    progressBar2.setVisibility(View.GONE);

                                }
                            }

                        });
                    }

                } else {
                    Toast.makeText(AddressActivity.this, "Code Is Incorrect", Toast.LENGTH_SHORT).show();
                    progressBar2.setVisibility(View.GONE);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onBackPressed() {
        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        else {
           /* if (getIntent().getExtras().getString("from").equals("checkout"))
                startActivity(new Intent(AddressActivity.this, CheckoutActivity.class));

            else if (getIntent().getExtras().getString("from").equals("different")) {
                startActivity(new Intent(AddressActivity.this, DiffernetAddress.class).putExtra("which", getIntent().getExtras().getString("which")));
            } else if (getIntent().getExtras().getString("from").equals("account"))
                startActivity(new Intent(AddressActivity.this, HomeActivity.class).putExtra("to", "account"));

            finish();*/

            if (getIntent().getExtras().getString("from").equals("account")) {
                startActivity(new Intent(AddressActivity.this, HomeActivity.class).putExtra("span", "register"));
                finish();
            } else
                super.onBackPressed();
        }
    }

}
