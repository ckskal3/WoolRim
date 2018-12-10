package org.woolrim.woolrim.SectionRecyclerView;


import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.woolrim.woolrim.DataItems.PoemAndPoetItem;
import org.woolrim.woolrim.R;
import org.woolrim.woolrim.WoolrimApplication;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/*
    해당 위치 찾는 부분 구현 해야됨
 */

public class SectionAddItem extends StatelessSection {

    public String title;
    public ArrayList<PoemAndPoetItem> list;
    public OnItemClickListenr listener;
    public int pageCode;

    private static final char HANGUL_BEGIN_UNICODE = 44032; // 가
    private static final char HANGUL_LAST_UNICODE = 55203; // 힣
    private static final char HANGUL_BASE_UNIT = 588;//각자음 마다 가지는 글자수
    private static final char[] INITIAL_SOUND = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
            'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    public interface OnItemClickListenr {
        void onItemClick(SectionItemViewHolder holder, View view, int position);
    }

    public SectionAddItem(String title, ArrayList<PoemAndPoetItem> list, int pageCode) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.sectionlistview_item)
                .headerResourceId(R.layout.sectionlistview_header)
                .build());

        this.title = title;
        this.list = list;
        this.pageCode = pageCode;

    }

    public void setOnItemClickListener(OnItemClickListenr listener) {
        this.listener = listener;
    }


    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new SectionItemViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SectionItemViewHolder itemHolder = (SectionItemViewHolder) holder;

        String name = list.get(position).poem;

        itemHolder.tvItem.setText(name);
//        Log.d("TIme",list.get(position).poem+" "+String.valueOf(list.get(position).full_count)+" "+String.valueOf(list.get(position).man_count)+" "+String.valueOf(list.get(position).woman_count));
        if (pageCode == WoolrimApplication.REQUSET_POEM_LIST_FRAGMENT) { //들을수 있는곡 없을시 클릭 불가.
            if ((list.get(position).man_count + list.get(position).woman_count) == 0) {
                itemHolder.tvItem.setTextColor(R.color.gray_bar_color);
            } else {
                itemHolder.setOnItemClickListener(listener);
            }
        } else { //녹음 할 수 있는 곡 없을시 클릭 불가.
            int standardAuthCount = list.get(position).full_count/2;
            boolean flag;
            if(WoolrimApplication.loginedUserGender.equals("여자")){
                flag = list.get(position).woman_count >= standardAuthCount;
            }else{
                flag = list.get(position).man_count >= standardAuthCount;
            }
            if (flag) {
                itemHolder.tvItem.setTextColor(R.color.gray_bar_color);
            } else {
                itemHolder.setOnItemClickListener(listener);
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new SectionHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        SectionHeaderViewHolder headerHolder = (SectionHeaderViewHolder) holder;

        String chosung = getInitialSound(title.charAt(0)) + "";

        headerHolder.tvTitle.setText(title);
        headerHolder.charFirstTv.setText(chosung);
    }

    private char getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin / HANGUL_BASE_UNIT;
        return INITIAL_SOUND[index];
    }

}
