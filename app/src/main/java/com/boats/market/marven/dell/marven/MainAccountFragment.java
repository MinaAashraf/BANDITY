package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.boats.market.marven.dell.marven.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by dell on 7/2/2019.
 */

public class MainAccountFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainaccount_fragment, container , false);


        return view;
    }



}
