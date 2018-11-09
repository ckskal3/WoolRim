package org.woolrim.woolrim.SectionRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.woolrim.woolrim.R;

public class SectionHeaderViewHolder  extends RecyclerView.ViewHolder {

    public final TextView tvTitle, charFirstTv;

    SectionHeaderViewHolder(View view) {
        super(view);
        tvTitle = view.findViewById(R.id.tvTitle);
        charFirstTv = view.findViewById(R.id.char_firstname_tv);
    }
}
