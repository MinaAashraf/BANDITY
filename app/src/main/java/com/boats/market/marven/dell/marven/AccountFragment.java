package com.boats.market.marven.dell.marven;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dell on 6/20/2019.
 */

public class AccountFragment extends Fragment {


    Fragment accountfragment2 = new AccountFragment2();
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    TextView namelabel, emaillabel, passlabel, conpasslabel, checktxt;
    EditText name_edittext, email_edittxt, pass_edittxt, confpass_edittxt;
    Button register_btn;
    CheckBox checkBox;
    boolean flag = true;
    ProgressBar progressBar6;
    ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_fragment, container, false);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "gotham.ttf");


        HomeActivity.currentFragment = "Fragment_4";
        HomeActivity.categoryname.setText("Register");
        scrollView = v.findViewById(R.id.scrollview);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        namelabel = v.findViewById(R.id.namelabel);
        emaillabel = v.findViewById(R.id.emaillabel);
        passlabel = v.findViewById(R.id.passlabel);
        conpasslabel = v.findViewById(R.id.confirmpasslabel);
        name_edittext = v.findViewById(R.id.name_edittext);
        email_edittxt = v.findViewById(R.id.emailadress_edittxt);
        pass_edittxt = v.findViewById(R.id.passedittxt);
        confpass_edittxt = v.findViewById(R.id.confirmpassedittxt);
        checkBox = v.findViewById(R.id.check_box);
        checktxt = v.findViewById(R.id.check_txt);
        register_btn = v.findViewById(R.id.register_btn);
        progressBar6 = v.findViewById(R.id.progressbar6);
        name_edittext.requestFocus();
        namelabel.setTextColor(Color.parseColor("#292561"));
        namelabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
        name_edittext.setTypeface(typeface);
        email_edittxt.setTypeface(typeface);
        pass_edittxt.setTypeface(typeface);
        confpass_edittxt.setTypeface(typeface);
        final TextView showpass = v.findViewById(R.id.show_pass);
        final TextView showpass2 = v.findViewById(R.id.show_pass2);
        namelabel.setTypeface(typeface);
        emaillabel.setTypeface(typeface);
        passlabel.setTypeface(typeface);
        conpasslabel.setTypeface(typeface);
        checktxt.setTypeface(typeface);
        register_btn.setTypeface(typeface);
        namelabel.setTypeface(typeface);
        showpass.setTypeface(typeface);
        showpass2.setTypeface(typeface);


        showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showpass.getText().toString().equals("SHOW"))
                {
                pass_edittxt.setTransformationMethod(null);
                pass_edittxt.setSelection(pass_edittxt.length());
                showpass.setText("HIDE");

                }
                else
                {
                    pass_edittxt.setTransformationMethod(new PasswordTransformationMethod());
                    pass_edittxt.setSelection(pass_edittxt.length());
                    showpass.setText("SHOW");
                }
            }
        });

        showpass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showpass2.getText().toString().equals("SHOW"))
                {
                    confpass_edittxt.setTransformationMethod(null);
                    confpass_edittxt.setSelection(confpass_edittxt.length());
                    showpass2.setText("HIDE");
                }
                else
                {
                    confpass_edittxt.setTransformationMethod(new PasswordTransformationMethod());
                    confpass_edittxt.setSelection(confpass_edittxt.length());
                    showpass2.setText("SHOW");
                }
            }
        });


        if (name_edittext.getText().toString().isEmpty() || email_edittxt.getText().toString().isEmpty() || pass_edittxt.getText().toString().isEmpty()
                || confpass_edittxt.getText().toString().isEmpty() || checkBox.isChecked() == false) {
            register_btn.setEnabled(false);
            register_btn.setAlpha(0.5f);
        }

        SpannableString spannableString = new SpannableString(checktxt.getText().toString());
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://sites.google.com/view/marven-store/home"));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };


        checktxt.setMovementMethod(LinkMovementMethod.getInstance());
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://sites.google.com/view/marvenapp-terms-of-use/home"));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        spannableString.setSpan(clickableSpan1, 29, 44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2, 48, checktxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        checktxt.setText(spannableString);
        checktxt.setMovementMethod(LinkMovementMethod.getInstance());

        TextView signtxt = v.findViewById(R.id.signtxt);
        SpannableString spannableString2 = new SpannableString(signtxt.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeActivity.class).putExtra("span", "register"));
            }
        };
        spannableString2.setSpan(clickableSpan, 25, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signtxt.setText(spannableString2);
        signtxt.setMovementMethod(LinkMovementMethod.getInstance());
        signtxt.setTypeface(typeface);

        name_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    namelabel.setTextColor(Color.parseColor("#292561"));
                    namelabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                } else {
                    namelabel.setTextColor(Color.parseColor("#707583"));
                    namelabel.animate().translationY(0).setDuration(50);
                }

                if (!pass_edittxt.getText().toString().isEmpty())
                    passlabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);

                if (!email_edittxt.getText().toString().isEmpty())
                    emaillabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);

                if (!confpass_edittxt.getText().toString().isEmpty())
                    conpasslabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
            }


        });


        email_edittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    emaillabel.setTextColor(Color.parseColor("#292561"));
                    emaillabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                } else {
                    emaillabel.setTextColor(Color.parseColor("#707583"));
                    emaillabel.animate().translationY(0).setDuration(50);
                }
                if (!pass_edittxt.getText().toString().isEmpty()) {
                    passlabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

                if (!name_edittext.getText().toString().isEmpty()) {
                    namelabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                            }

                if (!confpass_edittxt.getText().toString().isEmpty()) {
                    conpasslabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

            }
        });

        pass_edittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    passlabel.setTextColor(Color.parseColor("#292561"));
                    passlabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);

                } else {
                    passlabel.setTextColor(Color.parseColor("#707583"));
                    passlabel.animate().translationY(0).setDuration(50);
                }

                if (!name_edittext.getText().toString().isEmpty()) {
                    namelabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                            }

                if (!email_edittxt.getText().toString().isEmpty()) {
                    emaillabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

                if (!confpass_edittxt.getText().toString().isEmpty()) {
                    conpasslabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

            }
        });

        confpass_edittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    conpasslabel.setTextColor(Color.parseColor("#292561"));
                    conpasslabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);

                } else {
                    conpasslabel.setTextColor(Color.parseColor("#707583"));
                    conpasslabel.animate().translationY(0).setDuration(50);
                }

                if (!name_edittext.getText().toString().isEmpty()) {
                    namelabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                            }

                if (!email_edittxt.getText().toString().isEmpty()) {
                    emaillabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

                if (!pass_edittxt.getText().toString().isEmpty()) {
                    passlabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

            }
        });


        pass_edittxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                activeButton();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confpass_edittxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
     activeButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


name_edittext.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
   activeButton();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
});
       email_edittxt.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            activeButton();
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              activeButton();
            }
        });


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(register_btn);
                namelabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                emaillabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                passlabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                conpasslabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                progressBar6.setVisibility(View.VISIBLE);
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                flag = true;
                final String email = email_edittxt.getText().toString();
                if (!email.contains("@gmail.com".trim()) && !email.contains("@yahoo.com".trim()) && !email.contains("@hotmail.com".trim())
                        && !email.contains("@outlook.com".trim())) {
                    email_edittxt.setError("Invalid Email");
                    email_edittxt.requestFocus();
                    flag = false;
                    progressBar6.setVisibility(View.GONE);
                }


                if (pass_edittxt.getText().toString().length() < 8) {
                    pass_edittxt.setError("This Password Is Very Weak");
                    pass_edittxt.requestFocus();
                    flag = false;
                    progressBar6.setVisibility(View.GONE);

                }

                if (!confpass_edittxt.getText().toString().equals(pass_edittxt.getText().toString())) {
                    confpass_edittxt.setError("Check This Password");
                    confpass_edittxt.requestFocus();
                    flag = false;
                    progressBar6.setVisibility(View.GONE);

                }

                if (flag) {


                    firebaseAuth.createUserWithEmailAndPassword(email_edittxt.getText().toString(), pass_edittxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Users users = new Users(name_edittext.getText().toString(), email_edittxt.getText().toString(), pass_edittxt.getText().toString(), 0);
                                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(R.id.placeholder2, accountfragment2);
                                            fragmentTransaction.commit();
                                            progressBar6.setVisibility(View.GONE);


                                        } else {
                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressBar6.setVisibility(View.GONE);

                                        }
                                    }
                                });
                                progressBar6.setVisibility(View.GONE);


                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar6.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

        return v;
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    public void activeButton ()
    {
        if (name_edittext.getText().toString().isEmpty() || email_edittxt.getText().toString().isEmpty() || pass_edittxt.getText().toString().isEmpty()
                || confpass_edittxt.getText().toString().isEmpty() || checkBox.isChecked() == false) {
            register_btn.setEnabled(false);
            register_btn.setAlpha(0.5f);
        } else {
            register_btn.setEnabled(true);
            register_btn.setAlpha(1);

        }
    }

}
