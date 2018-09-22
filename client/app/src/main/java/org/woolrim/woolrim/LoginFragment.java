package org.woolrim.woolrim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginFragment extends Fragment {
    private Button loginBtn, registBtn;
    private EditText idEditText, passEditText;

    public static LoginFragment newInstance(Bundle bundle){
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        setOnClick();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                onDetach();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(View view){
        loginBtn = view.findViewById(R.id.login_btn);
        registBtn = view.findViewById(R.id.regist_btn);
        idEditText = view.findViewById(R.id.id_edittext);
        passEditText = view.findViewById(R.id.password_edittext);
    }

    private void setOnClick(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordListFragment recordListFagment = RecordListFragment.newInstance(new Bundle());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("LoginFagment")
                        .replace(R.id.container,recordListFagment).commit();

            }
        });
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request("http://54.180.81.190:9788/graphql");
            }
        });
    }


    public void request(String urlString) {
        /*
        query getAllUserQuery { getAllUser{idnamestu_idgenderpasswdcreatedbongsa_time}}
         */
        final String body = "query { getAllUser{ id name stu_id gender passwd created bongsa_time } }";
        final String body1 = "mutation { createUser(input: {name:\"조수근\" stu_id:201201548 gender:\"남\" passwd:\"123456\"})}";

        final String body2 = "query { getUser ( id:1 ){ stu_id, passwd } }";


        StringRequest request = new StringRequest(
                Request.Method.POST,
                urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
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
                params.put("query",body2);
                return params;
            }
        };

        request.setShouldCache(false);
        WoolrimApplication.requestQueue.add(request);
    }

    private void processResponse(String response){
        Log.d("Result",response);
        Gson gson = new Gson();
        RequestData rd = gson.fromJson(response,RequestData.class);

       User ud = rd.data;
       UserDetail udd = ud.getUser;

        Log.d("Result",String.valueOf(udd.stuId)+" "+udd.passwd);

    }

}
