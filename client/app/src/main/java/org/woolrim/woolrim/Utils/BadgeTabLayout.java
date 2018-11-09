package org.woolrim.woolrim.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.woolrim.woolrim.R;


public class BadgeTabLayout extends TabLayout{

    private SparseArray<Builder> mTabBuilders = new SparseArray<>();

    private static final int INVAILD_VALUE = Integer.MIN_VALUE;

    public BadgeTabLayout(Context context) {
        super(context);
    }

    public BadgeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BadgeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Builder with(int position){
        Tab tab = getTabAt(position);
        return with(tab);
    }

    public Builder with(Tab tab){
        if(tab == null){
            throw new IllegalArgumentException("Tab must not be null");
        }
        Builder builder = mTabBuilders.get(tab.getPosition());

        if(builder == null){
            builder = new Builder(this, tab);
            mTabBuilders.put(tab.getPosition(),builder);
        }

        return builder;
    }

    public Builder getTabBuilderItem(int position){
        return mTabBuilders.get(position);
    }




    public class Builder{
        public TabLayout parent;
        private  Tab mTab;

        private View mView;
        private Context mContext;
        private TextView mBadgeTextView, mTitleTextView;
        private ImageView mIconView;
        private Drawable mIconDrawable;
        private int mIconColorFilter = Integer.MIN_VALUE;
        private int mBadgeCount = Integer.MIN_VALUE;
        private boolean mHasBadge = false;

        public Builder(TabLayout parent, Tab mTab){
            this.parent = parent;
            this.mTab = mTab;
            this.mContext = this.parent.getContext();
        }

        public void init() {
            if (mTab.getCustomView() != null) {
                this.mView = mTab.getCustomView();
            } else {
                this.mView = LayoutInflater.from(mContext).
                        inflate(R.layout.tab_custom_item, parent, false);
            }
            if (mView != null) {
                this.mIconView = mView.findViewById(R.id.tab_icon);
                this.mBadgeTextView = mView.findViewById(R.id.tab_badge);
                this.mTitleTextView = mView.findViewById(R.id.tab_title);
            }


            if (this.mIconView != null) {
                this.mIconDrawable = mIconView.getDrawable();
            }
        }

        public Builder imageContentDescription(String contentDescription){
            if(this.mIconView != null)
                this.mIconView.setContentDescription(contentDescription);
            return this;
        }

        public Builder hasBadge(){
            mHasBadge = true;
            return this;
        }

        public Builder noBadge(){
            mHasBadge = false;
            return this;
        }

        public Builder badge(boolean hasBadge){
            mHasBadge = hasBadge;
            return this;
        }
        public Builder icon(int drawableRes){
            mIconDrawable = getDrawableCompat(mContext, drawableRes);
            return this;
        }
        public Builder icon(@Nullable Drawable drawable){
            mIconDrawable = drawable;
            return this;
        }

        public Builder iconColor(@Nullable int color){
            mIconColorFilter = color;
            return this;
        }

        public Builder increase(){
            mBadgeCount = mBadgeTextView == null ? INVAILD_VALUE : Integer.parseInt(mBadgeTextView.getText().toString()) +1;
            return this;
        }

        public Builder decrease(){
            mBadgeCount = mBadgeTextView == null ? INVAILD_VALUE : Integer.parseInt(mBadgeTextView.getText().toString()) - 1;
            return this;
        }

        public Builder badgeCount(int count){
            mBadgeCount = count;
            return this;
        }

        public Builder setTabTitle(String title){
            mTitleTextView.setText(title);
            return this;
        }

        public Builder setTabTitleColor(int color){
            mTitleTextView.setTextColor(color);
            return this;
        }

        public void build(){
            if(mView == null){
                return ;
            }

            if(mBadgeTextView !=null){

                if(mHasBadge && mBadgeCount >0){
                    mBadgeTextView.setText(formatBadgeNumber(mBadgeCount));
                    mBadgeTextView.setVisibility(VISIBLE);
                }else{
                    mBadgeTextView.setVisibility(INVISIBLE);
                }
            }

            if(mIconView != null && mIconDrawable != null){
                mIconView.setImageDrawable(mIconDrawable.mutate());

                if(mIconColorFilter != Integer.MIN_VALUE){
                    mIconDrawable.setColorFilter(mIconColorFilter, PorterDuff.Mode.MULTIPLY);
                }
            }

            mTab.setCustomView(mView);
        }

    }
    @Nullable
    private static Drawable getDrawableCompat(Context context, int drawableRes){
        @Nullable
        Drawable drawable = null;
        try{
            drawable = getDrawable(context, drawableRes, context.getTheme());
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return drawable;
    }

    private static String formatBadgeNumber(int value){
        if (value <= 0) {
            return "-"+formatBadgeNumber(-value);
        }
        if(value <=10){
            return Integer.toString(value);
        }else {
            return "10+";
        }
    }

    public static final Drawable getDrawable(Context context, int id, @Nullable Resources.Theme theme) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return context.getResources().getDrawable(id,theme);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

}
