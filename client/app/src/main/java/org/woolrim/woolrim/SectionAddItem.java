package org.woolrim.woolrim;


import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.woolrim.woolrim.Temp.TempDataItem;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/*
    해당 위치 찾는 부분 구현 해야됨
 */

public class SectionAddItem extends StatelessSection {

    public String title;
    public ArrayList<TempDataItem> list;
    public OnItemClickListenr listener;
    public int pageCode;


    public interface OnItemClickListenr {
        void onItemClick(SectionItemViewHolder holder, View view, int position);
    }

    SectionAddItem(String title, ArrayList<TempDataItem> list, int pageCode) {
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


        if(pageCode == MainFragment.SHOW_LIST_LAYOUT_CODE){ //들을수 있는곡 없을시 클릭 불가.
            if(list.get(position).full_count == 0){
                itemHolder.tvItem.setTextColor(R.color.gray_bar_color);
            }else{
                itemHolder.setOnItemClickListener(listener);
            }
        }else{ //녹음 할 수 있는 곡 없을시 클릭 불가.
            if(list.get(position).full_count == 4){
                itemHolder.tvItem.setTextColor(R.color.gray_bar_color);
            }else{
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

        headerHolder.tvTitle.setText(title);
    }

}
