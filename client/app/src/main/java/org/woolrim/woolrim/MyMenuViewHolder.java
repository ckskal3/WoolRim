package org.woolrim.woolrim;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyMenuViewHolder extends RecyclerView.ViewHolder {
    public ConstraintLayout backgroundLayout;
    public TextView poemTv, poetTv, dashTv;
    public ImageView playIv, deleteIv;
    public MyRecordAdapter.OnItemClickListener onItemClickListener = null;

    public MyMenuViewHolder(View view) {
        super(view);

        init(view);

        playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                onItemClickListener.onClick(MyMenuViewHolder.this, view, position);
            }
        });

        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                onItemClickListener.onClick(MyMenuViewHolder.this, view, position);
            }
        });

        backgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                onItemClickListener.onClick(MyMenuViewHolder.this, view, position);
            }
        });

    }

    private void init(View view) {
        backgroundLayout = view.findViewById(R.id.my_record_item_background_layout);
        poemTv = view.findViewById(R.id.myrecord_poem_textview);
        poetTv = view.findViewById(R.id.myrecord_poet_textview);
        dashTv = view.findViewById(R.id.myrecord_item_dash_tecxtview);
        playIv = view.findViewById(R.id.playimageview);
        deleteIv = view.findViewById(R.id.deleteimageview);

    }

    public void setOnItemClickListener(MyRecordAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
