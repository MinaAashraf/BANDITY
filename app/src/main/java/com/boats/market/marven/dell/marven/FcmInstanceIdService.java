package com.boats.market.marven.dell.marven;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by dell on 8/31/2019.
 */

public class FcmInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String fcm_token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM_TOKEN",fcm_token);

    }
}
