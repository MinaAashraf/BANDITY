package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
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
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by dell on 7/2/2019.
 */

public class SigninFragment extends Fragment {
    TextView emaillabel, passwordlabel;
    EditText emailedit, passedit;
    Button login_btn;
    FirebaseAuth firebaseAuth;
    Fragment accountFragment2 = new AccountFragment2();
    ProgressBar progressbar5;
    ScrollView scrollView;
    Fragment registerFragment = new AccountFragment();
    boolean valid = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signin_fragment, container, false);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "gotham.ttf");


        firebaseAuth = FirebaseAuth.getInstance();

        HomeActivity.currentFragment = "Fragment_3";
        if (firebaseAuth.getCurrentUser() != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.placeholder2, accountFragment2);
            fragmentTransaction.commit();
        }

        HomeActivity.categoryname.setText("Login");
        emaillabel = view.findViewById(R.id.emaillabel2);
        passwordlabel = view.findViewById(R.id.passlabel2);
        emailedit = view.findViewById(R.id.emailadress_edittxt2);
        passedit = view.findViewById(R.id.passedittxt2);
        final TextView showPass = view.findViewById(R.id.show_pass);
        login_btn = view.findViewById(R.id.loging_btn);
        final CheckBox rememberMe_checkbox = view.findViewById(R.id.rememberMe);
        progressbar5 = view.findViewById(R.id.progressbar5);
        emailedit.requestFocus();
        final LinearLayout signin_layout = view.findViewById(R.id.signin_layout);
        final Dialog forgetPass_dialog = new Dialog(getActivity());
        forgetPass_dialog.setContentView(R.layout.forgetpassword_dialog);
        final ProgressBar progressBar = forgetPass_dialog.findViewById(R.id.progress_bar);
        forgetPass_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText email_edittxt = forgetPass_dialog.findViewById(R.id.email_edittxt);
        final TextView email_label = forgetPass_dialog.findViewById(R.id.email_label);
        final TextView confirm = forgetPass_dialog.findViewById(R.id.confirm);
        final TextView cancel = forgetPass_dialog.findViewById(R.id.cancel);
        scrollView = view.findViewById(R.id.scrollview);
        emaillabel.setTextColor(Color.parseColor("#292561"));
        emaillabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);

        rememberMe_checkbox.setTypeface(typeface);
        email_edittxt.setTypeface(typeface);
        email_label.setTypeface(typeface);
        emailedit.setTypeface(typeface);
        emaillabel.setTypeface(typeface);
        passedit.setTypeface(typeface);
        passwordlabel.setTypeface(typeface);
        login_btn.setTypeface(typeface);
        confirm.setTypeface(typeface);
        cancel.setTypeface(typeface);
        showPass.setTypeface(typeface);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("checkbox", 0);


        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPass.getText().toString().equals("SHOW")) {
                    passedit.setTransformationMethod(null);
                    passedit.setSelection(passedit.length());
                    showPass.setText("HIDE");
                } else {
                    passedit.setTransformationMethod(new PasswordTransformationMethod());
                    passedit.setSelection(passedit.length());
                    showPass.setText("SHOW");
                }
            }
        });


        boolean remeberMe = sharedPreferences.getBoolean("rememberMe", false);
        rememberMe_checkbox.setChecked(remeberMe);
        if (remeberMe) {
            String email = sharedPreferences.getString("email", null);
            String password = sharedPreferences.getString("password", null);
            emailedit.setText(email);
            emailedit.setSelection(emailedit.length());
            passedit.setText(password);
            passedit.setSelection(passedit.length());

            if (!emailedit.getText().toString().isEmpty()) {
                emaillabel.setTextColor(Color.parseColor("#292561"));
                emaillabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
            }
            if (!passedit.getText().toString().isEmpty()) {
                passwordlabel.setTextColor(Color.parseColor("#292561"));
                passwordlabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
            }

        }


        if (emailedit.getText().toString().isEmpty() || passedit.getText().toString().isEmpty()) {
            login_btn.setEnabled(false);
            login_btn.setAlpha(0.5f);
        } else {
            login_btn.setEnabled(true);
            login_btn.setAlpha(1);

        }

        email_edittxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    email_label.setTextColor(Color.parseColor("#292561"));
                    email_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);

                } else {
                    email_label.setTextColor(Color.parseColor("#707583"));
                    email_label.animate().translationY(0).setDuration(50);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(cancel);
                forgetPass_dialog.dismiss();
                email_edittxt.setText("");
                progressBar.setVisibility(View.GONE);
                signin_layout.setVisibility(View.VISIBLE);

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(confirm);
                if (!email_edittxt.getText().toString().isEmpty()) {
                    email_label.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email_edittxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Email sent", Toast.LENGTH_SHORT).show();
                                forgetPass_dialog.dismiss();
                                email_edittxt.setText("");
                                progressBar.setVisibility(View.GONE);
                                signin_layout.setVisibility(View.VISIBLE);


                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    });

                } else if (email_edittxt.getText().toString().isEmpty()) {
                    email_edittxt.setError("Enter Your Email Address");
                    email_edittxt.requestFocus();
                }


            }
        });


        rememberMe_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("rememberMe", b).apply();
                if (b == false) {
                    editor.putString("email", null);
                    editor.putString("password", null);
                    editor.apply();
                }

            }
        });


        TextView forgotPassword = view.findViewById(R.id.forgotPassword);
        forgotPassword.setTypeface(typeface);
        SpannableString spannableString = new SpannableString(forgotPassword.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                forgetPass_dialog.show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);

            }
        };

        spannableString.setSpan(clickableSpan, 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgotPassword.setText(spannableString);
        forgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

        TextView register_txt = view.findViewById(R.id.register_txt);
        register_txt.setTypeface(typeface);
        SpannableString spannableString1 = new SpannableString(register_txt.getText().toString());
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.placeholder2, registerFragment);
                fragmentTransaction.commit();
            }
        };

        spannableString1.setSpan(clickableSpan1, 22, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        register_txt.setText(spannableString1);
        register_txt.setMovementMethod(LinkMovementMethod.getInstance());

        emailedit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    emaillabel.setTextColor(Color.parseColor("#292561"));
                    emaillabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);

                } else {
                    emaillabel.setTextColor(Color.parseColor("#707583"));
                    emaillabel.animate().translationY(0).setDuration(50);
                }

                if (!passedit.getText().toString().isEmpty()) {
                    passwordlabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }


            }
        });

        emailedit.addTextChangedListener(new TextWatcher() {
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

        passedit.addTextChangedListener(new TextWatcher() {
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

        passedit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    passwordlabel.setTextColor(Color.parseColor("#292561"));
                    passwordlabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                } else {
                    passwordlabel.setTextColor(Color.parseColor("#707583"));
                    passwordlabel.animate().translationY(0).setDuration(50);
                }

                if (!emailedit.getText().toString().isEmpty()) {
                    emaillabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid = true;
                hideKeyboard(login_btn);
                progressbar5.setVisibility(View.VISIBLE);
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                if (!passedit.getText().toString().isEmpty()) {
                    passwordlabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }
                if (!emailedit.getText().toString().isEmpty()) {
                    emaillabel.animate().translationY(-30 * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)).setDuration(50);
                }

                if (!(emailedit.getText().toString().contains("@gmail.com".trim()) || emailedit.getText().toString().contains("@yahoo.com".trim()) || emailedit.getText().toString().contains("@hotmail.com".trim()) ||
                        emailedit.getText().toString().contains("@outlook.com".trim()))) {
                    emailedit.setError("invalid email");
                    emailedit.requestFocus();
                    valid = false;
                    progressbar5.setVisibility(View.GONE);
                }
                if (passedit.length() < 8) {
                    passedit.setError("invalid password");
                    passedit.requestFocus();
                    valid = false;
                    progressbar5.setVisibility(View.GONE);
                }
                if (valid) {
                    firebaseAuth.signInWithEmailAndPassword(emailedit.getText().toString(), passedit.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.placeholder2, accountFragment2);
                                fragmentTransaction.commit();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("rememberMe", rememberMe_checkbox.isChecked()).apply();
                                if (rememberMe_checkbox.isChecked()) {
                                    editor.putString("email", emailedit.getText().toString());
                                    editor.putString("password", passedit.getText().toString());
                                    editor.apply();
                                }

                                progressbar5.setVisibility(View.GONE);


                            } else {
                                Toast.makeText(getActivity(), "This Account Not Exist.Check Email And Password", Toast.LENGTH_SHORT).show();
                                progressbar5.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
        getActivity().getFragmentManager().popBackStack();

        return view;
    }


    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    public  void activeButton ()
    {
        if (emailedit.getText().toString().isEmpty() || passedit.getText().toString().isEmpty()) {
            login_btn.setEnabled(false);
            login_btn.setAlpha(0.5f);
        } else {
            login_btn.setEnabled(true);
            login_btn.setAlpha(1);

        }
    }

}
