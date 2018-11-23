package org.woolrim.woolrim;

import android.app.Application;
import android.os.Build;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.apollographql.apollo.ApolloClient;
import com.tsengvn.typekit.Typekit;

import org.woolrim.woolrim.Utils.DBManagerHelper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class WoolrimApplication extends Application {
    public final static int REQUSET_MAIN_ACTIVITY = 100;
    public final static int REQUSET_MAIN_FRAGMENT = 101;
    public final static int REQUSET_POEM_LIST_FRAGMENT = 102;
    public final static int REQUSET_RECORD_LIST_FRAGMENT = 103;
    public final static int REQUSET_LOGIN_FRAGMENT = 104;

    public final static int REQUSET_HOME = 110;
    public final static int REQUSET_MY_MENU = 111;
    public final static int REQUSET_FAVORITE = 112;
    public final static int REQUSET_RECORD_LOGOUT = 113;

    public final static String BASE_URL = "http://13.125.221.121:3000/graphql";
    public final static String FILE_BASE_URL = "http://13.125.221.121:4000/";

    public static String loginedUserName,loginedUserProfile , loginedUserPK;
    public static int loginedUserId;


    public static RequestQueue requestQueue;
    public static DBManagerHelper dbManagerHelper;

    public static HttpLoggingInterceptor httpLoggingInterceptor;
    public static OkHttpClient okHttpClient;
    public static ApolloClient apolloClient;

    public static boolean isLogin = false, isTest = false;

    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addBold(Typekit.createFromAsset(this, "font/nanumsquare_eb.ttf"))
                .addNormal(Typekit.createFromAsset(this, "font/nanumsquare_b.ttf"));

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        dbManagerHelper = new DBManagerHelper(WoolrimApplication.this);
        dbManagerHelper.openDatabase();

        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        apolloClient = ApolloClient.builder().okHttpClient(okHttpClient).serverUrl(BASE_URL).build();

    }



    @Override
    public void onTerminate() {

        requestQueue.stop();
        requestQueue = null;
        dbManagerHelper.closeDatabase();
        super.onTerminate();
    }

    public static void userInfoReset(){
        isLogin = false;
        isTest = false;
        loginedUserId = 0;
        loginedUserProfile = null;
        loginedUserPK = null;
        loginedUserName = null;
    }

}
