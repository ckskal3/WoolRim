package org.woolrim.woolrim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView userEmailTv, userNameTv;
    public static String userName;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("state","OnCreate");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);


        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.END)) {
                    drawer.closeDrawer(Gravity.END);
                } else {
                    drawer.openDrawer(Gravity.END);
                }
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);


        userNameTv = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userNameTv.setText(R.string.ex_navi_header_user_name);
        userName = userNameTv.getText().toString();
        userEmailTv = navigationView.getHeaderView(0).findViewById(R.id.user_email);
        userEmailTv.setText(R.string.ex_navi_header_user_email);

        navigationView.setNavigationItemSelectedListener(this);

        MainFragment mainFragment = MainFragment.newInstance(new Bundle());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,mainFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_0) {
            getSupportFragmentManager()
                    .popBackStack();
        } else if (id == R.id.nav_1) {

        } else if (id == R.id.nav_2) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }



    public void request(String urlString) {
        /*
        query getAllUserQuery { getAllUser{idnamestu_idgenderpasswdcreatedbongsa_time}}
         */
        final String body = "query { getAllUser{ id name stu_id gender passwd created bongsa_time } }";
        final String body1 = "mutation { createUser(input: {name:\"조수근\" stu_id:201201548 gender:\"남\" passwd:\"123456\"})}";



        StringRequest request = new StringRequest(
                Request.Method.POST,
                urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        println("에러 -> " + error.getMessage());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("query",body1);
                return params;
            }
        };

        request.setShouldCache(false);
        WoolrimApplication.requestQueue.add(request);
    }


}
