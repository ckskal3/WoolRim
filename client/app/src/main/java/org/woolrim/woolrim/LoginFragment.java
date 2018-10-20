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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    private Button loginBtn, registBtn;
    private EditText idEditText, passEditText;
    private Bundle bundle;

    public static LoginFragment newInstance(Bundle bundle) {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bundle = getArguments();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        setOnClick();

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toolbarLabelTv.setText("로그인");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onDetach();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(View view) {
        loginBtn = view.findViewById(R.id.login_btn);
        registBtn = view.findViewById(R.id.regist_btn);
        idEditText = view.findViewById(R.id.id_edittext);
        passEditText = view.findViewById(R.id.password_edittext);
    }

    private void setOnClick() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PoemListFragment poemListFragment = PoemListFragment.newInstance(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("LoginFagment")
                        .replace(R.id.container, poemListFragment).commit();

            }
        });
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment signUpFragment = SignUpFragment.newInstance(new Bundle());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("LoginFagment")
                        .replace(R.id.container, signUpFragment).commit();
//                request("http://54.180.81.190:9788/graphql");
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("query", body2);
                return params;
            }
        };

        request.setShouldCache(false);
        WoolrimApplication.requestQueue.add(request);
    }

    private void processResponse(String response) {
        Log.d("Result", response);
        Gson gson = new Gson();
        RequestData rd = gson.fromJson(response, RequestData.class);

        UserItem ud = rd.data;
        UserDetail udd = ud.getUser;


        String tempID = idEditText.getText().toString();
        String insertPass = passEditText.getText().toString();
        if (tempID.length() ==0 || insertPass.length() == 0) {
            Toast.makeText(getContext(), "입력하세요.", Toast.LENGTH_SHORT).show();
        } else {

            int insertID = Integer.parseInt(tempID);

            if (insertPass.equals(udd.passwd) && insertID == udd.stuId) {
                Toast.makeText(getContext(), "일치합니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "불일치합니다.", Toast.LENGTH_SHORT).show();
            }
        }


        Log.d("Result", String.valueOf(udd.stuId) + " " + udd.passwd);

    }

}
