package org.woolrim.woolrim;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class SectionItemViewHolder extends RecyclerView.ViewHolder {

public final View rootView;
public final TextView tvItem;

    SectionItemViewHolder(View view) {
        super(view);
        rootView = view;
        tvItem = (TextView) view.findViewById(R.id.tvItem);
        }
}
