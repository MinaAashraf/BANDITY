package com.boats.market.marven.dell.marven;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoucherCode extends AppCompatActivity {
    boolean flag;
    int position, position2;
    int childsNum, counter = 0;
    boolean exist = false;
    int postion_removed = 0;
    Dialog removingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_code);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final EditText codeEdittxt = findViewById(R.id.makevoucherCode_edit), codevalue = findViewById(R.id.codeValue_edit);
        final Button applycode = findViewById(R.id.apply_code);

        final Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        TextView remove_txt = findViewById(R.id.remove_code_txt);
        removingDialog = new Dialog(this);
        removingDialog.setContentView(R.layout.remove_vcode_dialog);
        removingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Spinner removeSpinner = removingDialog.findViewById(R.id.remove_spinner);
        Button remove_btn = removingDialog.findViewById(R.id.remove_btn);
        remove_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removingDialog.show();
            }
        });

        final ArrayList<String> allCodes = new ArrayList<>();

        removeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                postion_removed = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                postion_removed = 0;
            }
        });
        final ArrayAdapter removeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allCodes);
        removeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        removeSpinner.setAdapter(removeAdapter);

        databaseReference.child("voucherCodes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String code = dataSnapshot.child("code").getValue().toString();
                String scope = dataSnapshot.child("scope").getValue().toString();
                allCodes.add(code + " (" + scope + ")");
                removeAdapter.notifyDataSetChanged();
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


        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = allCodes.get(postion_removed).substring(0,allCodes.get(postion_removed).indexOf(" "));
                databaseReference.child("voucherCodes").child(code).removeValue();
                allCodes.remove(postion_removed);
                removeAdapter.notifyDataSetChanged();
                Toast.makeText(VoucherCode.this, "Remove succeeded", Toast.LENGTH_SHORT).show();
                removingDialog.dismiss();
            }
        });


        final Spinner spinner = findViewById(R.id.spinner);
        final Spinner spinner2 = findViewById(R.id.spinner2);
        String arr[] = {"By Percentage", "By Pounds"};
        String arr2[] = {"Special", "Global"};


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                position = 0;
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position2 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                position2 = 0;
            }
        });


        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);
        final ImageView backimg = toolbar.findViewById(R.id.back_icon);
        TextView title = toolbar.findViewById(R.id.customcategort_name);
        SharedPreferences sharedPreferences = getSharedPreferences("cart_counter", 0);
        int cart_counter = sharedPreferences.getInt("count", 0);
        TextView cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);
        if (cart_counter > 0)
            cart_counter_txt.setVisibility(View.VISIBLE);
        else
            cart_counter_txt.setVisibility(View.GONE);
        cart_counter_txt.setText("" + cart_counter);
        cart_counter_txt.setTypeface(typeface);
        title.setText("Voucher Codes");
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VoucherCode.this, CartActivity.class));

            }
        });
        title.setTypeface(typeface);
        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VoucherCode.this, SearchActivity.class));
                finish();
            }
        });
        applycode.setTypeface(typeface);
        codeEdittxt.setTypeface(typeface);
        codevalue.setTypeface(typeface);
        applycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(applycode);
                flag = true;
                if (codeEdittxt.getText().toString().isEmpty()) {
                    codeEdittxt.setError("Enter Code");
                    codeEdittxt.requestFocus();
                    flag = false;
                }
                Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
                Matcher matcher =pattern.matcher(codeEdittxt.getText().toString());
                if (!matcher.matches())
                {
                    codeEdittxt.setError("Invalid special characters");
                    codeEdittxt.requestFocus();
                    flag = false;
                }

                if (codevalue.getText().toString().isEmpty()) {
                    codevalue.setError("Required");
                    codevalue.requestFocus();
                    flag = false;
                }
                if (flag) {
                    counter = 0;
                    exist = false;
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("voucherCodes")) {
                                childsNum = (int) dataSnapshot.child("voucherCodes").getChildrenCount();

                                databaseReference.child("voucherCodes").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                        if (codeEdittxt.getText().toString().equals(dataSnapshot.child("code").getValue().toString())) {
                                            exist = true;
                                        }

                                        if (counter++ == childsNum - 1) {
                                            if (exist == true) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder
                                                        (VoucherCode.this);
                                                builder.setMessage("This code already exists.Do you want to override it?")
                                                        .setPositiveButton("override", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                            exist = false;
                                                                if (position2 == 0) {
                                                                    Code code = new Code(codeEdittxt.getText().toString(), Integer.valueOf(codevalue.getText().toString()), position, "Special");
                                                                    databaseReference.child("voucherCodes").child(codeEdittxt.getText().toString()).setValue(code).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(VoucherCode.this, "Code is applied", Toast.LENGTH_SHORT).show();
                                                                                codeEdittxt.setText("");
                                                                                codevalue.setText("");
                                                                            } else
                                                                                Toast.makeText(VoucherCode.this, "There is a problem.Try again..", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });

                                                                } else if (position2 == 1) {
                                                                    Code code = new Code(codeEdittxt.getText().toString(), Integer.valueOf(codevalue.getText().toString()), position, "Global");
                                                                    databaseReference.child("voucherCodes").child(codeEdittxt.getText().toString()).setValue(code).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(VoucherCode.this, "Code is applied", Toast.LENGTH_SHORT).show();
                                                                                codeEdittxt.setText("");
                                                                                codevalue.setText("");
                                                                            } else
                                                                                Toast.makeText(VoucherCode.this, "There is a problem.Try again..", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });

                                                                }
                                                            }
                                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        return;
                                                    }
                                                }).show();

                                            }
                                            if (exist == false)
                                            {
                                            if (position2 == 0) {
                                                Code code = new Code(codeEdittxt.getText().toString(), Integer.valueOf(codevalue.getText().toString()), position, "Special");
                                                databaseReference.child("voucherCodes").child(codeEdittxt.getText().toString()).setValue(code).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(VoucherCode.this, "Code is applied", Toast.LENGTH_SHORT).show();
                                                            codeEdittxt.setText("");
                                                            codevalue.setText("");
                                                        } else
                                                            Toast.makeText(VoucherCode.this, "There is a problem.Try again..", Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                            } else if (position2 == 1) {
                                                Code code = new Code(codeEdittxt.getText().toString(), Integer.valueOf(codevalue.getText().toString()), position, "Global");
                                                databaseReference.child("voucherCodes").child(codeEdittxt.getText().toString()).setValue(code).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(VoucherCode.this, "Code is applied", Toast.LENGTH_SHORT).show();
                                                            codeEdittxt.setText("");
                                                            codevalue.setText("");
                                                        } else
                                                            Toast.makeText(VoucherCode.this, "There is a problem.Try again..", Toast.LENGTH_SHORT).show();

                                                    }
                                                });

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
                            } else {
                                if (position2 == 0) {
                                    Code code = new Code(codeEdittxt.getText().toString(), Integer.valueOf(codevalue.getText().toString()), position, "Special");
                                    databaseReference.child("voucherCodes").child(codeEdittxt.getText().toString()).setValue(code).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(VoucherCode.this, "Code is applied", Toast.LENGTH_SHORT).show();
                                                codeEdittxt.setText("");
                                                codevalue.setText("");
                                            } else
                                                Toast.makeText(VoucherCode.this, "There is a problem.Try again..", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                } else if (position2 == 1) {
                                    Code code = new Code(codeEdittxt.getText().toString(), Integer.valueOf(codevalue.getText().toString()), position, "Global");
                                    databaseReference.child("voucherCodes").child(codeEdittxt.getText().toString()).setValue(code).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(VoucherCode.this, "Code is applied", Toast.LENGTH_SHORT).show();
                                                codeEdittxt.setText("");
                                                codevalue.setText("");
                                            } else
                                                Toast.makeText(VoucherCode.this, "There is a problem.Try again..", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


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
        else
            super.onBackPressed();
    }
}
