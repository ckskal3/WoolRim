package org.woolrim.woolrim;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.exception.ApolloException;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


import org.woolrim.woolrim.DataItems.MyRecordItem;
import org.woolrim.woolrim.Utils.NetworkStatus;
import org.woolrim.woolrim.type.Status;

import javax.annotation.Nonnull;


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

                    MainActivity.signInAndOutTv.setText(R.string.logout_kr);
                    MainActivity.profileChangeImageView.setVisibility(View.VISIBLE);
                    MainActivity.userNameTv.setText(R.string.android_kr);

                    WoolrimApplication.isLogin = true;
                    WoolrimApplication.loginedUserName = getString(R.string.android_kr);
                    WoolrimApplication.loginedUserPK = "7";
                    WoolrimApplication.loginedUserId = 123456789;
                    WoolrimApplication.loginedUserProfile = getString(R.string.no_profile_en);
                    WoolrimApplication.isTest =true;


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
                        WoolrimApplication.apolloClient.mutate(GetLogin.builder().stu_id(Integer.parseInt(id)).passwd(pass).build()).
                                enqueue(new ApolloCall.Callback<GetLogin.Data>() {
                                    @Override
                                    public void onResponse(@Nonnull com.apollographql.apollo.api.Response<GetLogin.Data> response) {
                                        if (response.data().login().isSuccess()) {
                                            final String[] userData = new String[5];
                                            userData[0] = response.data().login().user().profile();
                                            userData[1] = response.data().login().user().name();
                                            userData[2] = response.data().login().user().id();
                                            userData[3] = String.valueOf(response.data().login().user().stu_id());
                                            userData[4] = response.data().login().user().gender();

                                            WoolrimApplication.isLogin = true;
                                            WoolrimApplication.loginedUserProfile = WoolrimApplication.FILE_BASE_URL+userData[0];
                                            WoolrimApplication.loginedUserName = userData[1];
                                            WoolrimApplication.loginedUserPK = userData[2];
                                            WoolrimApplication.loginedUserId = Integer.parseInt(userData[3]);
                                            WoolrimApplication.loginedUserGender = userData[4];

                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    MainActivity.signInAndOutTv.setText(R.string.logout_kr);
                                                    MainActivity.profileChangeImageView.setVisibility(View.VISIBLE);
                                                    MainActivity.userNameTv.setText(userData[1]);
                                                    if (userData[0] == null || userData[0].equals(getString(R.string.no_profile_en))) {
                                                        Glide.with(getContext()).load(R.drawable.profile_icon).into(MainActivity.profileImageView);
                                                    } else {
                                                        Glide.with(getContext()).load(WoolrimApplication.FILE_BASE_URL+userData[0]).into(MainActivity.profileImageView);
                                                    }
                                                }
                                            });

                                            if (requestCode == WoolrimApplication.REQUSET_RECORD_LIST_FRAGMENT) {//녹음 목록
                                                PoemListFragment poemListFragment = PoemListFragment.newInstance(bundle);
                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.container, poemListFragment)
                                                        .commit();
                                            } else if (requestCode == WoolrimApplication.REQUSET_MY_MENU) {
                                                Bundle bundle = new Bundle();
                                                ArrayList<MyRecordItem> myRecordItem = new ArrayList<>();
                                                for (GetLogin.Recording_list item : response.data().login().recording_list()) {
                                                    boolean auth_flag;
                                                    auth_flag = item.auth_flag() != Status.ACCEPTED;
                                                    myRecordItem.add(new MyRecordItem(
                                                            item.id(),
                                                            item.poem().poet().name(),
                                                            item.poem().name(),
                                                            auth_flag
                                                    ));
                                                }
                                                ArrayList<MyRecordItem> notificationItems = new ArrayList<>();
                                                for (GetLogin.Notification_list item : response.data().login().notification_list()) {
                                                    notificationItems.add(new MyRecordItem(item.id(), item.content(), null, false));
                                                }
                                                bundle.putParcelableArrayList("PoemList", myRecordItem);
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
                                        }
                                    }

                                    @Override
                                    public void onFailure(@Nonnull ApolloException e) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getContext(), "정보가 잘못되었습니다", Toast.LENGTH_SHORT).show();
                                            }
                                        });
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

}
