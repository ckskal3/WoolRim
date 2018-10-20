package org.woolrim.woolrim;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordListViewHolder extends RecyclerView.ViewHolder {
    public TextView poemTv, poetTv,dashTv;
    public ImageView playIv, deleteIv;
    public MyMenuAdapter.OnItemClickListener onItemClickListener = null;

    public RecordListViewHolder(View view) {
        super(view);

        poemTv = view.findViewById(R.id.myrecord_poem_textview);
        poetTv = view.findViewById(R.id.myrecord_poet_textview);
        dashTv = view.findViewById(R.id.myrecord_item_dash_tecxtview);
        playIv = view.findViewById(R.id.playimageview);
        deleteIv = view.findViewById(R.id.deleteimageview);


        playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                onItemClickListener.onClick(RecordListViewHolder.this,view,position);
            }
        });

        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                onItemClickListener.onClick(RecordListViewHolder.this,view,position);
            }
        });
    }

    public void setOnItemClickListener(MyMenuAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


}
