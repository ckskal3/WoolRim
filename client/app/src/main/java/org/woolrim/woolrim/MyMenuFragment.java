package org.woolrim.woolrim;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.woolrim.woolrim.Utils.BadgeTabLayout;
import org.woolrim.woolrim.Utils.SetTabIndicatorWidth;

import java.util.Objects;


public class MyMenuFragment extends Fragment {
    private ImageView profileIv;
    private TextView userNameTv;
    private ViewPager myViewPager;
    private BadgeTabLayout myTabLayout;

    public static MyRecordFragment myRecordFragment1;
    public MyRecordFragment myRecordFragment2;


    public static MyMenuFragment newInstance(Bundle bundle) {
        MyMenuFragment myMenuFragment = new MyMenuFragment();
        myMenuFragment.setArguments(bundle);
        return myMenuFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mymenu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        ViewPagerAdapter adapterTemp = new ViewPagerAdapter(getChildFragmentManager());


        //Fragment 객체 생성 -> Bundle 객체 생성 -> Fragment 에 Bundle 실어서 Adapter 에 추가가
        Bundle bundle = new Bundle();
        bundle.putInt("RequestCode", 101);

        myRecordFragment1 = MyRecordFragment.newInstance(bundle);

        bundle = new Bundle();
        bundle.putInt("RequestCode", 102);
        myRecordFragment2 = MyRecordFragment.newInstance(bundle);

        adapterTemp.addItem(myRecordFragment1);
        adapterTemp.addItem(myRecordFragment2);

        myViewPager.setAdapter(adapterTemp);
        myViewPager.setCurrentItem(0, true);
        myTabLayout.setupWithViewPager(myViewPager);

        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                myTabLayout.getTabBuilderItem(position).noBadge().build();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
                    if (Objects.requireNonNull(myTabLayout.getTabAt(0)).isSelected()) {
                        Log.d("state",String.valueOf(state));
                        myTabLayout.getTabBuilderItem(0).setTabTitleColor(getResources().getColor(R.color.app_main_color,null)).build();
                        myTabLayout.getTabBuilderItem(1).setTabTitleColor(getResources().getColor(android.R.color.black,null)).build();
                    } else {
                        myTabLayout.getTabBuilderItem(1).setTabTitleColor(getResources().getColor(R.color.app_main_color,null)).build();
                        myTabLayout.getTabBuilderItem(0).setTabTitleColor(getResources().getColor(android.R.color.black,null)).build();
                    }
                }
            }
        });


        myTabLayout.with(0).init();
        myTabLayout.getTabBuilderItem(0)
                .setTabTitle("나의울림")
                .setTabTitleColor(getResources().getColor(R.color.app_main_color, null))
                .noBadge()
                .build();


        myTabLayout.with(1).init();
        myTabLayout.getTabBuilderItem(1)
                .setTabTitle("울림알람")
                .setTabTitleColor(getResources().getColor(android.R.color.black, null))
                .badge(true)
                .badgeCount(5)
                .build();
        SetTabIndicatorWidth setTabIndicatorWidth = new SetTabIndicatorWidth();
        setTabIndicatorWidth.wrapTabIndicatorToTitle(myTabLayout,30,30);


    }


    @Override
    public void onResume() {
        super.onResume();

        MainActivity.toolbarLabelTv.setText("마이울림");
    }

    private void init(View view) {
        profileIv = view.findViewById(R.id.myprofileimageview);
        userNameTv = view.findViewById(R.id.mynametextview);
        myViewPager = view.findViewById(R.id.my_container);
        myTabLayout = view.findViewById(R.id.my_tabLayout);

    }

    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.setMinimumWidth(0);
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                // setting custom margin between tabs
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        // left
                        settingMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        settingMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        settingMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }

    private void settingMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }


}

