package org.woolrim.woolrim.SectionRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.woolrim.woolrim.R;

public class SectionItemViewHolder extends RecyclerView.ViewHolder {

    public final View rootView;
    public final TextView tvItem;
    public SectionAddItem.OnItemClickListenr listener;

    public SectionItemViewHolder(View view) {
        super(view);
        rootView = view;
        tvItem = view.findViewById(R.id.tvItem);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if(listener != null){
                    listener.onItemClick(SectionItemViewHolder.this, view ,position);
                }
            }
        });

    }

    public void setOnItemClickListener(SectionAddItem.OnItemClickListenr listener) {
        this.listener = listener;
    }

}
