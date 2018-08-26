package org.woolrim.woolrim;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/*
    해당 위치 찾는 부분 구현 해야됨
 */

public class SectionAddItem extends StatelessSection {

    public String title;
    public List<String> list;
    public Context mContext;
    public int activityNumber;

    SectionAddItem(String title, List<String> list, Context context, int activityNumber) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.sectionlistview_item)
                .headerResourceId(R.layout.sectionlistview_header)
                .build());

        this.title = title;
        this.list = list;
        this.activityNumber = activityNumber;
        this.mContext = context;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new SectionItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SectionItemViewHolder itemHolder = (SectionItemViewHolder) holder;

        String name = list.get(position);

        itemHolder.tvItem.setText(name);

        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityNumber == 105) { //녹음 리스트 뷰일때 녹음액티비티 실행
//                    Intent itn = new Intent(mContext, VoiceRecordActivity.class);
//                    itn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(itn);
                } else {// 시 재생일떄는 해당 위치에 맞는 재생 액티비티 실행
                    Toast.makeText(mContext, "clicked",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
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
