package org.woolrim.woolrim;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.exception.ApolloException;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.apollographql.apollo.ApolloClient;

import org.woolrim.woolrim.DataItems.MyRecordItem;
import org.woolrim.woolrim.DataItems.PoemAndPoetItem;
import org.woolrim.woolrim.Utils.NetworkStatus;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class LoginFragment extends Fragment {
    private ConstraintLayout backgroundLayout;
    private Button loginBtn, registBtn;
    private EditText idEditText, passEditText;
    private Bundle bundle;

    private int requestCode;

    private InputMethodManager inputMethodManager;

    public static LoginFragment newInstance(Bundle bundle) {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bundle = getArguments();
        requestCode = bundle.getInt(getString(R.string.request_code));
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
        MainActivity.toolbarLabelTv.setText(R.string.login_kr);
    }


    private void init(View view) {
        backgroundLayout = view.findViewById(R.id.login_background_layout);
        loginBtn = view.findViewById(R.id.login_btn);
        registBtn = view.findViewById(R.id.regist_btn);
        idEditText = view.findViewById(R.id.id_edittext);
        passEditText = view.findViewById(R.id.password_edittext);

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void setOnClick() {

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyPadHide();

                String id = idEditText.getText().toString().trim();
                String pass = passEditText.getText().toString().trim();

//                request();
                if (id.length() == 0 && pass.length() == 0) { //나중에 삭제해야함 테스트용으로 남겨둠
                    WoolrimApplication.isLogin = true;
                    MainActivity.signInAndOutTv.setText(R.string.logout_kr);
                    MainActivity.profileChangeImageView.setVisibility(View.VISIBLE);
                    MainActivity.userNameTv.setText(R.string.user);
                    WoolrimApplication.loginedUserName = getString(R.string.user);
                    WoolrimApplication.loginedUserProfile = null;


                    if (requestCode == WoolrimApplication.REQUSET_RECORD_LIST_FRAGMENT) {
                        PoemListFragment poemListFragment = PoemListFragment.newInstance(bundle);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, poemListFragment)
                                .commit();
                        //                       .addToBackStack("LoginFagment")
                    } else if (requestCode == WoolrimApplication.REQUSET_MY_MENU) {
                        Bundle bundle = new Bundle();
                        bundle.putString("UserName", getString(R.string.user));
                        MyMenuFragment myMenuFragment = MyMenuFragment.newInstance(bundle);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, myMenuFragment)
                                .commit();
                    } else {
                        getActivity().getSupportFragmentManager()
                                .popBackStack();
                    }
                } else {
                    if (NetworkStatus.getConnectivityStatus(getContext()) != NetworkStatus.TYPE_NOT_CONNECTED) { // 인터넷 연결되어있다.
                        if (id.length() == 0) {
                            id = "0";
                        }
                        if (pass.length() == 0) {
                            pass = "0";
                        }
                        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
                        ApolloClient apolloClient = ApolloClient.builder().okHttpClient(okHttpClient).serverUrl(WoolrimApplication.BASE_URL).build();
                        final ArrayList<MyRecordItem> notificationItems = new ArrayList<>();
                        apolloClient.query(GetNotification.builder().stu_id(Integer.parseInt(id)).build()).enqueue(new ApolloCall.Callback<GetNotification.Data>() {
                            @Override
                            public void onResponse(@Nonnull com.apollographql.apollo.api.Response<GetNotification.Data> response) {
                                int cnt = response.data().getNotification().notification_list().size();
                                if (cnt > 0) {
                                    for (GetNotification.Notification_list item : response.data().getNotification().notification_list()) {
                                        notificationItems.add(new MyRecordItem(item.id(), item.content(), null, false));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {

                            }
                        });
                        apolloClient.mutate(GetLogin.builder().stu_id(Integer.parseInt(id)).passwd(pass).build()).enqueue(new ApolloCall.Callback<GetLogin.Data>() {
                            @Override
                            public void onResponse(@Nonnull com.apollographql.apollo.api.Response<GetLogin.Data> response) {
                                if (response.data().login().isSuccess()) { //로그인 성공시
                                    final String[] userData = new String[4];
                                    userData[0] = response.data().login().user().profile();
                                    userData[1] = response.data().login().user().name();
                                    userData[2] = response.data().login().user().id();
                                    userData[3] = String.valueOf(response.data().login().user().stu_id());

                                    WoolrimApplication.loginedUserName = userData[1];
                                    WoolrimApplication.loginedUserProfile = userData[0];
                                    WoolrimApplication.loginedUserId = Integer.parseInt(userData[3]);

                                    int size = response.data().login().recording_list().size();
                                    Log.d("Title", String.valueOf(size));
                                    WoolrimApplication.isLogin = true;

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MainActivity.signInAndOutTv.setText(R.string.logout_kr);
                                            MainActivity.profileChangeImageView.setVisibility(View.VISIBLE);
                                            MainActivity.userNameTv.setText(userData[1]);
                                            Glide.with(getContext()).load(userData[0]).into(MainActivity.profileImageView);
                                            Toast.makeText(getContext(), userData[0], Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    if (requestCode == WoolrimApplication.REQUSET_RECORD_LIST_FRAGMENT) {
                                        PoemListFragment poemListFragment = PoemListFragment.newInstance(bundle);
                                        getActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.container, poemListFragment)
                                                .commit();
                                        //                       .addToBackStack("LoginFagment")
                                    } else if (requestCode == WoolrimApplication.REQUSET_MY_MENU) {
                                        ArrayList<MyRecordItem> items = new ArrayList<>();
                                        for (GetLogin.Recording_list list : response.data().login().recording_list()) {
                                            items.add(new MyRecordItem(list.id(), list.poem().poet().name(), list.poem().name(), false));
                                        }
                                        Bundle bundle = new Bundle();
                                        bundle.putString("UserName", userData[1]);
                                        bundle.putString("UserPK", userData[2]);
                                        bundle.putParcelableArrayList("PoemList", items);
                                        bundle.putParcelableArrayList("NotificationList", notificationItems);
                                        MyMenuFragment myMenuFragment = MyMenuFragment.newInstance(bundle);
                                        getActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.container, myMenuFragment)
                                                .commit();
                                    } else {
                                        getActivity().getSupportFragmentManager()
                                                .popBackStack();
                                    }
                                } else { // 로그인 실패시
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {

                            }
                        });

                    } else { //인터넷 연결 안되어있다.
                        Toast.makeText(getContext(), "인터넷 연결을 해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                keyPadHide();

                SignUpFragment signUpFragment = SignUpFragment.newInstance(new Bundle());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("LoginFagment")
                        .replace(R.id.container, signUpFragment).commit();
            }
        });

        backgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyPadHide();
            }
        });
    }

    private void keyPadHide() {
        inputMethodManager.hideSoftInputFromWindow(idEditText.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(passEditText.getWindowToken(), 0);
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
        if (tempID.length() == 0 || insertPass.length() == 0) {
            Toast.makeText(getContext(), "입력하세요.", Toast.LENGTH_SHORT).show();
        } else {

            int insertID = Integer.parseInt(tempID);

            if (insertPass.equals(udd.passwd) && insertID == udd.stuId) {
                Toast.makeText(getContext(), "일치합니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "불일치합니다.", Toast.LENGTH_SHORT).show();
            }
        }


        Log.d("Result", String.valueOf(udd.stuId) + " " + udd.passwd);

    }


}
