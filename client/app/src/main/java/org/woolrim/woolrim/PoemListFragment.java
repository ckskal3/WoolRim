package org.woolrim.woolrim;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;

import org.woolrim.woolrim.DataItems.PoetItem;
import org.woolrim.woolrim.DataItems.PoemAndPoetItem;
import org.woolrim.woolrim.SectionRecyclerView.SectionAddItem;
import org.woolrim.woolrim.SectionRecyclerView.SectionItemViewHolder;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class PoemListFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private EditText searchPoemEditText;
    private View transparentView;

    private InputMethodManager inputMethodManager;

    private ArrayList<PoetItem> poetDataArrayList = new ArrayList<>();

    private int pageCode;
    private String searchKey = null;

    public static PoemListFragment newInstance(Bundle bundle) {
        PoemListFragment poemListFragment = new PoemListFragment();
        poemListFragment.setArguments(bundle);
        return poemListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;
        poetDataArrayList = bundle.getParcelableArrayList("DataItems");
        pageCode = bundle.getInt(getString(R.string.request_code));
        if(pageCode == WoolrimApplication.REQUSET_POEM_LIST_FRAGMENT){
            searchKey = bundle.getString("SearchKey");
        }
        return inflater.inflate(R.layout.frament_poemlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        for (PoetItem poetItem : poetDataArrayList) {

            if (poetItem.poemName.size() > 0) {

                String poetName = poetItem.poetName;
                ArrayList<PoemAndPoetItem> dataItems = poetItem.poemName;

                setHeaderAndItems(poetName, dataItems);
            }

        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(sectionAdapter);

        searchPoemEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchPoemEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View inview, boolean flag) {
                if (flag) {
                    transparentView.setVisibility(View.VISIBLE);
                }
            }
        });

        searchPoemEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        hide();
                        break;
                }
                return true;
            }
        });

        transparentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });

        searchPoemEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchKey = searchPoemEditText.getText().toString().trim();
                search(searchKey);
            }
        });

        if(searchKey != null){
            searchPoemEditText.setText(searchKey);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toolbarLabelTv.setText("시 목록");
    }

    private void init(View view) {
        mRecyclerView = view.findViewById(R.id.poem_list_recyclerview);
        searchPoemEditText = view.findViewById(R.id.search_poem_edittext);
        transparentView = view.findViewById(R.id.list_transparent_view);
    }

    private void search(String searchKey) {

        sectionAdapter.removeAllSections();

        if (searchKey.length() == 0) { //검색어 없음
            for (PoetItem poetItem : poetDataArrayList) {
                if (poetItem.poemName.size() > 0) {
                    String poetName = poetItem.poetName;
                    ArrayList<PoemAndPoetItem> dataItems = poetItem.poemName;

                    setHeaderAndItems(poetName, dataItems);
                }

            }
        } else { //검색어 존재함
            for (PoetItem poetItem : poetDataArrayList) {
                ArrayList<PoemAndPoetItem> poemAndPoetItems = new ArrayList<>();
                for(PoemAndPoetItem dataItem : poetItem.poemName){
                    if (dataItem.poem.toLowerCase().contains(searchKey)) {
                        poemAndPoetItems.add(dataItem);
                    }
                }
                if(poemAndPoetItems.size()>0){ // 무언가 들어가있다.
                    String poetName = poetItem.poetName;
                    ArrayList<PoemAndPoetItem> dataItems = poemAndPoetItems;

                    setHeaderAndItems(poetName, dataItems);

                }
            }
        }
    }

    private void setHeaderAndItems(final String poetName, final ArrayList<PoemAndPoetItem> poemNames){
        SectionAddItem sectionAddItem = new SectionAddItem(poetName, poemNames, pageCode);
        sectionAddItem.setOnItemClickListener(new SectionAddItem.OnItemClickListenr() {
            @Override
            public void onItemClick(SectionItemViewHolder holder, View view, int position) {
                requestServerForPoemMedia(poemNames.get(sectionAdapter.getPositionInSection(position))._id ,poetName,  poemNames.get(sectionAdapter.getPositionInSection(position)).poem);
            }
        });
        sectionAdapter.addSection(sectionAddItem);
    }

    private void requestServerForPoemMedia(int _id,String poetName, String poemName){
        Toast.makeText(getContext(),
                poetName+" "+poemName,
                Toast.LENGTH_SHORT).show();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        ApolloClient apolloClient = ApolloClient.builder().serverUrl(WoolrimApplication.BASE_URL).okHttpClient(okHttpClient)
                .build();

        final Bundle bundle = new Bundle();
        bundle.putString("PoetName",poetName);
        bundle.putString("PoemName",poemName);

        if(pageCode == WoolrimApplication.REQUSET_POEM_LIST_FRAGMENT){
            apolloClient.query(GetRecordingForPlay.builder().poem_id(String.valueOf(_id)).build()).enqueue(new ApolloCall.Callback<GetRecordingForPlay.Data>() {
                @Override
                public void onResponse(@Nonnull final Response<GetRecordingForPlay.Data> response) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), String.valueOf(response.data().getRecordingForPlay().size()), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Bundle bundle = new Bundle();
                    PlayerFrameFragment playerFrameFragment = PlayerFrameFragment.newInstance(new Bundle());
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("RecordListFragment")
                            .replace(R.id.container, playerFrameFragment).commit();
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {

                }
            });

        }else{
            apolloClient.query(GetPoemByName.builder().poem_name(poemName).poet_name(poetName).build())
                    .enqueue(new ApolloCall.Callback<GetPoemByName.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<GetPoemByName.Data> response) {
                            bundle.putString("PoemContent",response.data().getPoemByNames().content());
                            RecordFragment recordFragment = RecordFragment.newInstance(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("RecordListFragment")
                                    .replace(R.id.container, recordFragment).commit();
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {

                        }
                    });
        }


        /*
           ///////////서버 요청 부분 짜야함/////////////////////////
            String url = "http://stou2.cafe24.com/Woolrim/RecordSelect";
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            processServerResponse(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("poet(?)",poetName);
                    params.put("poem(?)",poemName);
                    return params;
                }
            };

            stringRequest.setShouldCache(false);
            WoolrimApplication.requestQueue.add(stringRequest);
        //////////////////////////////////////////////////////////////////
        */
//        if(pageCode == WoolrimApplication.REQUSET_POEM_LIST_FRAGMENT) {
//            PlayerFrameFragment playerFrameFragment = PlayerFrameFragment.newInstance(new Bundle());
//            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("RecordListFragment")
//                    .replace(R.id.container, playerFrameFragment).commit();
//        }else{
//            Bundle bundle = new Bundle();
//            bundle.putString("PoetName",poetName);
//            bundle.putString("PoemName",poemName);
//            RecordFragment recordFragment = RecordFragment.newInstance(bundle);
//            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("RecordListFragment")
//                    .replace(R.id.container, recordFragment).commit();
//        }
    }

    private void processServerResponse(String response){
        Gson gson = new Gson();
        /*
        데이터받을 클래스 = gson.fromJson(response, 클래스명.class);

         */
    }

    private void hide() {
        inputMethodManager.hideSoftInputFromWindow(searchPoemEditText.getWindowToken(), 0);
        searchPoemEditText.clearFocus();
        transparentView.setVisibility(View.INVISIBLE);
    }

}
