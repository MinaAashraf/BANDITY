package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {
    boolean flag = false;
    String email;
    ScrollView scrollView;

    int cart_counter;
    android.support.v7.widget.Toolbar toolbar;
    TextView cart_counter_txt;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");

        final TextView oldPass_label = findViewById(R.id.oldPassword_label);
        final TextView newpass_label = findViewById(R.id.newPassword_label);
        final TextView confirm_label = findViewById(R.id.confirmPassword_label);
        final EditText oldPass_edittxt = findViewById(R.id.oldPassword_edittxt);
        final EditText newpass_edittxt = findViewById(R.id.newPassword_edittxt);
        final EditText confirm_edittxt = findViewById(R.id.confirmPassword_edittxt);
        final Button changePass_btn = findViewById(R.id.changePassword_btn);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final ProgressBar progressBar = findViewById(R.id.progressbar4);
        scrollView = findViewById(R.id.scrollview);

        oldPass_edittxt.setTypeface(typeface);
        oldPass_label.setTypeface(typeface);
        newpass_edittxt.setTypeface(typeface);
        newpass_label.setTypeface(typeface);
        confirm_edittxt.setTypeface(typeface);
        confirm_label.setTypeface(typeface);
        changePass_btn.setTypeface(typeface);


        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

         toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        TextView categoryname = toolbar.findViewById(R.id.customcategort_name);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);

         cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this, CartActivity.class));
            }
        });
        categoryname.setText("Change Password");
        ImageView search_icon = toolbar.findViewById(R.id.search_icon);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this, SearchActivity.class));
            }
        });
        categoryname.setTypeface(typeface);

        if (oldPass_edittxt.getText().toString().isEmpty() || newpass_edittxt.getText().toString().isEmpty() || confirm_edittxt.getText().toString().isEmpty()) {
            changePass_btn.setBackgroundResource(R.drawable.btn);
            changePass_btn.setAlpha(0.5f);
            changePass_btn.setEnabled(false);
        } else {
            changePass_btn.setBackgroundResource(R.drawable.btn);
            changePass_btn.setAlpha(1);
            changePass_btn.setEnabled(true);
        }


        oldPass_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
        oldPass_label.setTextColor(Color.parseColor("#292561"));
        databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        oldPass_edittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    oldPass_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                    oldPass_label.setTextColor(Color.parseColor("#292561"));
                } else {
                    oldPass_label.animate().translationY(0).setDuration(50);
                    oldPass_label.setTextColor(Color.parseColor("#707583"));
                }
                if (!newpass_edittxt.getText().toString().isEmpty()) {
                    newpass_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }
                if (!confirm_edittxt.getText().toString().isEmpty()) {
                    confirm_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

            }
        });

        newpass_edittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    newpass_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                    newpass_label.setTextColor(Color.parseColor("#292561"));
                } else {
                    newpass_label.animate().translationY(0).setDuration(50);
                    newpass_label.setTextColor(Color.parseColor("#707583"));
                }
                if (!oldPass_edittxt.getText().toString().isEmpty()) {
                    oldPass_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }
                if (!confirm_edittxt.getText().toString().isEmpty()) {
                    confirm_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }
            }
        });

        confirm_edittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    confirm_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                    confirm_label.setTextColor(Color.parseColor("#292561"));
                } else {
                    confirm_label.animate().translationY(0).setDuration(50);
                    confirm_label.setTextColor(Color.parseColor("#707583"));
                }
                if (!newpass_edittxt.getText().toString().isEmpty()) {
                    newpass_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }
                if (!oldPass_edittxt.getText().toString().isEmpty()) {
                    oldPass_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }
            }
        });

        oldPass_edittxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (oldPass_edittxt.getText().toString().isEmpty() || newpass_edittxt.getText().toString().isEmpty() || confirm_edittxt.getText().toString().isEmpty()) {
                    changePass_btn.setBackgroundResource(R.drawable.btn);
                    changePass_btn.setAlpha(0.5f);
                    changePass_btn.setEnabled(false);
                } else {
                    changePass_btn.setBackgroundResource(R.drawable.btn);
                    changePass_btn.setAlpha(1);
                    changePass_btn.setEnabled(true);
                }


                return false;
            }
        });


        newpass_edittxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (oldPass_edittxt.getText().toString().isEmpty() || newpass_edittxt.getText().toString().isEmpty() || confirm_edittxt.getText().toString().isEmpty()) {
                    changePass_btn.setBackgroundResource(R.drawable.btn);
                    changePass_btn.setAlpha(0.5f);
                    changePass_btn.setEnabled(false);
                } else {
                    changePass_btn.setBackgroundResource(R.drawable.btn);
                    changePass_btn.setAlpha(1);
                    changePass_btn.setEnabled(true);
                }


                return false;
            }
        });


        confirm_edittxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (oldPass_edittxt.getText().toString().isEmpty() || newpass_edittxt.getText().toString().isEmpty() || confirm_edittxt.getText().toString().isEmpty()) {
                    changePass_btn.setBackgroundResource(R.drawable.btn);
                    changePass_btn.setAlpha(0.5f);
                    changePass_btn.setEnabled(false);
                } else {
                    changePass_btn.setBackgroundResource(R.drawable.btn);
                    changePass_btn.setAlpha(1);
                    changePass_btn.setEnabled(true);
                }


                return false;
            }
        });

        changePass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(changePass_btn);
                flag = true;

                if (!newpass_edittxt.getText().toString().isEmpty()) {
                    newpass_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }
                if (!oldPass_edittxt.getText().toString().isEmpty()) {
                    oldPass_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

                if (!confirm_edittxt.getText().toString().isEmpty()) {
                    confirm_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

                if (newpass_edittxt.getText().toString().length() < 8) {
                    flag = false;
                    newpass_edittxt.setError("This Password Is Very Weak");
                    newpass_edittxt.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }
                if (!confirm_edittxt.getText().toString().equals(newpass_edittxt.getText().toString())) {
                    flag = false;
                    confirm_edittxt.setError("Check Your Password");
                    confirm_edittxt.requestFocus();
                    progressBar.setVisibility(View.GONE);

                }

                if (flag) {
                    progressBar.setVisibility(View.VISIBLE);
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    AuthCredential credential = EmailAuthProvider.getCredential(email, oldPass_edittxt.getText().toString());
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseUser.updatePassword(newpass_edittxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangePassword.this, "Password is updated successfully", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            finish();
                                        } else {
                                            Toast.makeText(ChangePassword.this, "Error.Password is not updated", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(ChangePassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
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
