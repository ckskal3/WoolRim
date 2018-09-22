package org.woolrim.woolrim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class PoemListFragment extends Fragment {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private RecyclerView mRecyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;

    public static PoemListFragment newInstance(Bundle bundle){
        PoemListFragment poemListFragment = new PoemListFragment();
        poemListFragment.setArguments(bundle);
        return poemListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frament_poemlist,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        sectionAdapter = new SectionedRecyclerViewAdapter();
        mViewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        for (String contact : getResources().getStringArray(R.array.poets)) {
            List<String> items = getContactsWithLetter(contact);

            if (items.size() > 0) {
                SectionAddItem sectionAddItem = new SectionAddItem(contact, items, getContext(), sectionAdapter, 104);
                sectionAddItem.setOnItemClickListener(new SectionAddItem.OnItemClickListenr() {
                    @Override
                    public void onItemClick(SectionItemViewHolder holder, View view, int position) {
                        Intent itn = new Intent(getContext(),PlayerActivity.class);
                        getActivity().startActivity(itn);
                    }
                });
                sectionAdapter.addSection(sectionAddItem);
            }
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(sectionAdapter);
    }

    private void init(View view){
        mViewPager = view.findViewById(R.id.viewpager);
        mRecyclerView = view.findViewById(R.id.recyclerview);
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
