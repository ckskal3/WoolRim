package org.woolrim.woolrim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class RecordListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private EditText searchEditText;

    public static RecordListFragment newInstance(Bundle bundle){
        RecordListFragment recordFragment = new RecordListFragment();
        recordFragment.setArguments(bundle);
        return  recordFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recordlist,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchKeyword  = searchEditText.getText().toString();
                search(searchKeyword);
            }
        });


        sectionAdapter = new SectionedRecyclerViewAdapter();

        for (String contact : getResources().getStringArray(R.array.poets)) {
            List<String> items = getContactsWithLetter(contact);

            if (items.size() > 0) {
                SectionAddItem sectionAddItem = new SectionAddItem(contact,items,getContext(),sectionAdapter,105);
                sectionAddItem.setOnItemClickListener(new SectionAddItem.OnItemClickListenr() {
                    @Override
                    public void onItemClick(SectionItemViewHolder holder, View view, int position) {
                        RecordFragment recordFragment = RecordFragment.newInstance(new Bundle());
                        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("RecordListFragment")
                                .replace(R.id.container,recordFragment).commit();
                    }
                });
                sectionAdapter.addSection(sectionAddItem);
            }
        }



        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(sectionAdapter);

    }


    private void setNaviItem(View view){
//        backIv = view.findViewById(R.id.navibackimageview);
//        searchEditText = view.findViewById(R.id.searchpoemedittext);
//        searchIv = view.findViewById(R.id.searchimageview);
    }

    private void search(String searchKey){
        if(searchKey.length() == 0){
            // 모든 리스트 보여주는 코드
        }else{
            // 리스트 아이템 수정 하는 코드
        }
    }

    private void init(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
        searchEditText = view.findViewById(R.id.search_edittext);
    }

    private List<String> getContactsWithLetter(String name) {
        List<String> items = new ArrayList<>();
        int id =0;
        if(name.equals("김소월")) id = R.array.김소월;
        else if(name.equals("이상")) id = R.array.이상;
        else if(name.equals("한용운")) id = R.array.한용운;
        else if(name.equals("윤동주")) id = R.array.윤동주;
        else if(name.equals("신동엽")) id = R.array.신동엽;
        else id = R.array.작자미상;
        for (String contact : getResources().getStringArray(id) ){
            items.add(contact);
        }
        return items;
    }
}
