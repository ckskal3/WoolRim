package org.woolrim.woolrim;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.woolrim.woolrim.DataItems.PoetItem;
import org.woolrim.woolrim.DataItems.PoemAndPoetItem;
import org.woolrim.woolrim.Utils.NetworkStatus;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainFragment extends Fragment implements View.OnClickListener {

    public static final int SHOW_LIST_LAYOUT_CODE = 105;
    public static final int SHOW_RECORD_LAYOUT_CODE = 106;

    private ConstraintLayout voiceSearchLayout;
    private LinearLayout showListLayout, recordPoemLayout;
    private ImageView imageView4, imageView5;

    public static TextView voiceRecognitionTv1, voiceRecognitionTv2;

    public static boolean isRecognitioning = false;

    public static MainFragment newInstance(Bundle bundle) {
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        setOnClick();


    }

    private void setOnClick() {
        showListLayout.setOnClickListener(this);
        recordPoemLayout.setOnClickListener(this);
        voiceSearchLayout.setOnClickListener(this);
    }

    private void init(View view) {
        showListLayout = view.findViewById(R.id.show_list_layout);
        recordPoemLayout = view.findViewById(R.id.show_record_layout);
        voiceSearchLayout = view.findViewById(R.id.search_voice_layout);

        voiceRecognitionTv1 = view.findViewById(R.id.voice_recognition_textview1);
        voiceRecognitionTv2 = view.findViewById(R.id.voice_recognition_textview2);

        imageView4 = view.findViewById(R.id.voice_recognition_icon_background);
        imageView5 = view.findViewById(R.id.voice_recognition_icon);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MainFragement", "onResume()");
        MainActivity.toolbarLabelTv.setText("울림");

    }

    @Override
    public void onClick(View view) {
        if(NetworkStatus.getConnectivityStatus(getContext()) != NetworkStatus.TYPE_NOT_CONNECTED) {//인터넷 연결시
            switch (view.getId()) {
                case R.id.show_list_layout: // 시 목록 보기
//                    requestServerForPoemList(WoolrimApplication.REQUSET_POEM_LIST_FRAGMENT);
                    requestServerForPoemListGraphQL(WoolrimApplication.REQUSET_POEM_LIST_FRAGMENT,null);
                    break;
                case R.id.show_record_layout: // 시 녹음 목록 보기
//                    requestServerForPoemList(WoolrimApplication.REQUSET_RECORD_LIST_FRAGMENT);
                    requestServerForPoemListGraphQL(WoolrimApplication.REQUSET_RECORD_LIST_FRAGMENT,null);
                    break;
                case R.id.search_voice_layout: // 음성 인식

                    VoiceRecognitionFragment testDialogFragment = VoiceRecognitionFragment.newInstance(new Bundle());
                    testDialogFragment.setDismissListener(new VoiceRecognitionFragment.DialogDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            voiceRecognitionTv1.setVisibility(View.VISIBLE);
                            voiceRecognitionTv2.setVisibility(View.VISIBLE);
                            requestServerForPoemListGraphQL(WoolrimApplication.REQUSET_POEM_LIST_FRAGMENT,key);
                        }

                        @Override
                        public void findSearchKey(String key) {
                            super.findSearchKey(key);
                            Log.d("Title", key);
                        }
                    });

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    testDialogFragment.show(fragmentTransaction, "playback");

                    Toast.makeText(getContext(), "곧 구현될 예정입니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else{
            Toast.makeText(getContext(),getString(R.string.internet_connect_warning),Toast.LENGTH_SHORT).show();
        }
    }

    public void requestServerForPoemListGraphQL(final int where, @Nullable final String searchKey){
        WoolrimApplication.apolloClient.query(GetAllPoem.builder().build()).enqueue(new ApolloCall.Callback<GetAllPoem.Data>() {
            @Override
            public void onResponse(@Nonnull com.apollographql.apollo.api.Response<GetAllPoem.Data> response) {
                ArrayList<PoemAndPoetItem> arrayList = new ArrayList<>();
                for(GetAllPoem.AllPoem allPoem :response.data().allPoem()){
                    arrayList.add(new PoemAndPoetItem(Integer.parseInt(allPoem.id()),allPoem.name(),allPoem.poet().name(),allPoem.auth_count_man(),0,allPoem.auth_count_man()));
                }

                if(arrayList != null){
                    String oldPoet = null;
                    ArrayList<PoetItem> poetItems = new ArrayList<>();
                    int i = -1 ;
                    for(PoemAndPoetItem poemAndPoetItem : arrayList){
                        String newPoet = poemAndPoetItem.poet;
                        if(!newPoet.equals(oldPoet)){
                            PoetItem poetItem = new PoetItem(newPoet);
                            poetItem.poemName.add(poemAndPoetItem);
                            poetItems.add(poetItem);
                            oldPoet = newPoet;
                            i++;
                        }else{
                            poetItems.get(i).poemName.add(poemAndPoetItem);
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("DataItems",poetItems);
                    bundle.putInt(getString(R.string.request_code),where);
                    if(searchKey != null){
                        bundle.putString("SearchKey",searchKey);
                    }
                    if(where == WoolrimApplication.REQUSET_POEM_LIST_FRAGMENT) {
                        PoemListFragment poemListFragment = PoemListFragment.newInstance(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().
                                replace(R.id.container, poemListFragment).addToBackStack("MainFragment").commit();
                    }else if(where == WoolrimApplication.REQUSET_RECORD_LIST_FRAGMENT){
                        if(WoolrimApplication.isLogin){// 로그인 되어있을경우
                            PoemListFragment poemListFragment = PoemListFragment.newInstance(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().
                                    replace(R.id.container, poemListFragment).addToBackStack("MainFragment").commit();
                        }else{// 로그인 해야할 경우
                            LoginFragment loginFragment = LoginFragment.newInstance(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().
                                    replace(R.id.container, loginFragment).addToBackStack("MainFragment").commit();
                        }

                    }
                }else{
                    Toast.makeText(getContext(),"오류가 발생했습니다. 다시 시도해 주세요",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }

    public  void requestServerForPoemList(final int where) {
        String url = "http://stou2.cafe24.com/Woolrim/DataSelect.php";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<PoemAndPoetItem> result = processServerRespose(response);
                        if(result != null){
                            String oldPoet = null;
                            ArrayList<PoetItem> poetItems = new ArrayList<>();
                            int i = -1 ;
                            for(PoemAndPoetItem tempDataItem : result){
                                String newPoet = tempDataItem.poet;
                                if(!newPoet.equals(oldPoet)){
                                    PoetItem poetItem = new PoetItem(newPoet);
                                    poetItem.poemName.add(tempDataItem);
                                    poetItems.add(poetItem);
                                    oldPoet = newPoet;
                                    i++;
                                }else{
                                    poetItems.get(i).poemName.add(tempDataItem);
                                }
                            }
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("DataItems",poetItems);
                            bundle.putInt(getString(R.string.request_code),where);
                            if(where == WoolrimApplication.REQUSET_POEM_LIST_FRAGMENT) {
//                                bundle.putString("SearchKey","먼 후일");
                                PoemListFragment poemListFragment = PoemListFragment.newInstance(bundle);
                                getActivity().getSupportFragmentManager().beginTransaction().
                                        replace(R.id.container, poemListFragment).addToBackStack("MainFragment").commit();
                            }else if(where == WoolrimApplication.REQUSET_RECORD_LIST_FRAGMENT){
                                if(WoolrimApplication.isLogin){// 로그인 되어있을경우
                                    PoemListFragment poemListFragment = PoemListFragment.newInstance(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction().
                                            replace(R.id.container, poemListFragment).addToBackStack("MainFragment").commit();
                                }else{// 로그인 해야할 경우
                                    LoginFragment loginFragment = LoginFragment.newInstance(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction().
                                            replace(R.id.container, loginFragment).addToBackStack("MainFragment").commit();
                                }
                            }
                        }else{
                            Toast.makeText(getContext(),"오류가 발생했습니다. 다시 시도해 주세요",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        stringRequest.setShouldCache(false);
        WoolrimApplication.requestQueue.add(stringRequest);
    }
    private ArrayList<PoemAndPoetItem> processServerRespose(String response){
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(response,RequestData.class);
        if(requestData.code == 200){
            Log.d("Code",String.valueOf(requestData.code));
            return requestData.result;
        }else return null;
    }
    public class RequestData {
        @SerializedName("data")
        public UserItem data;
        @SerializedName("Status")
        public String status;
        @SerializedName("Code")
        public int code;
        @SerializedName("message")
        public String message;
        @SerializedName("result")
        public ArrayList<PoemAndPoetItem> result;
    }

    public class UserItem {
        public UserDetail getUser;
    }


}
