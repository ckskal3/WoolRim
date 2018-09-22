package org.woolrim.woolrim;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class SectionHeaderViewHolder  extends RecyclerView.ViewHolder {

    public final TextView tvTitle;

    SectionHeaderViewHolder(View view) {
        super(view);
        tvTitle = view.findViewById(R.id.tvTitle);

    }
}
