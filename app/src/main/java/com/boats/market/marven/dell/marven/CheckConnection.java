package com.boats.market.marven.dell.marven;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dell on 8/20/2019.
 */

public class CheckConnection {
    Context context ;

    public CheckConnection(Context context) {
        this.context = context;
    }

    public boolean isConnecting ()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null)
        {
          NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();
          if (networkInfo!=null && networkInfo.isConnected())
          return true;
        }
        return false;
    }
}
