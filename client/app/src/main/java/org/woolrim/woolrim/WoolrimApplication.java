package org.woolrim.woolrim;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class WoolrimApplication extends Application {
    public static RequestQueue requestQueue;
    public static DBManagerHelper dbManagerHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        dbManagerHelper = new DBManagerHelper(WoolrimApplication.this);
        dbManagerHelper.openDatabase();
    }

    @Override
    public void onTerminate() {
        requestQueue.stop();
        requestQueue = null;
        dbManagerHelper.closeDatabase();
        super.onTerminate();
    }

}
