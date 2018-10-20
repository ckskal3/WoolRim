package org.woolrim.woolrim.Temp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.woolrim.woolrim.R;

import java.util.List;

public class AdapterTemp extends PagerAdapter {
    Context context;
    List<Drawable> obj;

    AdapterTemp(List<Drawable> res, Context context) {
        obj = res;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.pager_adapter, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageDrawable(obj.get(position));
        container.addView(view);

        return view;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }


    public int getCount() {
        return obj.size();
    }
}

