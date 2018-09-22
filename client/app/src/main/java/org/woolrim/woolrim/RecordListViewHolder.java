package org.woolrim.woolrim;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordListViewHolder extends RecyclerView.ViewHolder {
    public TextView poemTv, poetTv;
    public ImageView playIv, deleteIv;
    public RecordListViewHolder(View view) {
        super(view);
        poemTv = view.findViewById(R.id.mypoemtextview);
        poetTv = view.findViewById(R.id.mypoettextview);
        playIv = view.findViewById(R.id.playimageview);
        deleteIv = view.findViewById(R.id.deleteimageview);
    }
}
