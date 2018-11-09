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

import org.woolrim.woolrim.DataItems.PoetItem;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class RecordListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private EditText searchEditText;

    private ArrayList<PoetItem> importedPoetItems;


    public static RecordListFragment newInstance(Bundle bundle){
        RecordListFragment recordFragment = new RecordListFragment();
        recordFragment.setArguments(bundle);
        return  recordFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        assert bundle != null;
        importedPoetItems = bundle.getParcelableArrayList("DataItems");

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



        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(sectionAdapter);

    }



    private void search(String searchKey){
        if(searchKey.length() == 0){
            // 모든 리스트 보여주는 코드
        }else{
            // 리스트 아이템 수정 하는 코드
        }
    }

    private void init(View view) {
        mRecyclerView = view.findViewById(R.id.poem_list_recyclerview);
        searchEditText = view.findViewById(R.id.search_record_edittext);
    }

}
