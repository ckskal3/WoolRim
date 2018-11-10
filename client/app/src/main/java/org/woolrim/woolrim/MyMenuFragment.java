package org.woolrim.woolrim;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.woolrim.woolrim.DataItems.MyRecordItem;
import org.woolrim.woolrim.DataItems.PoemAndPoetItem;
import org.woolrim.woolrim.Utils.BadgeTabLayout;
import org.woolrim.woolrim.Utils.SetTabIndicatorWidth;

import java.util.ArrayList;
import java.util.Objects;


public class MyMenuFragment extends Fragment {
    private ImageView profileIv;
    private TextView userNameTv;
    private ViewPager myViewPager;
    private BadgeTabLayout myTabLayout;

    public static MyRecordFragment myRecordFragment1;
    public MyRecordFragment myRecordFragment2;

    private String userName;
    private ArrayList<MyRecordItem> poemLists;

    public static MyMenuFragment newInstance(Bundle bundle) {
        MyMenuFragment myMenuFragment = new MyMenuFragment();
        myMenuFragment.setArguments(bundle);
        return myMenuFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;
        userName = bundle.getString("UserName");
        poemLists = bundle.getParcelableArrayList("PoemList");

        Log.d("Title",userName+" "+ String.valueOf(poemLists.size()));
        return inflater.inflate(R.layout.fragment_my_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        setView();

        ViewPagerAdapter adapterTemp = new ViewPagerAdapter(getChildFragmentManager());


        //Fragment 객체 생성 -> Bundle 객체 생성 -> Fragment 에 Bundle 실어서 Adapter 에 추가가
        Bundle bundle = new Bundle();
        bundle.putInt("RequestCode", 101);
        bundle.putParcelableArrayList("PoemList",poemLists);
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
                        myTabLayout.getTabBuilderItem(0).setTabTitleColor(getColor(R.color.app_main_color)).build();
                        myTabLayout.getTabBuilderItem(1).setTabTitleColor(getColor(android.R.color.black)).build();
                    } else {
                        myTabLayout.getTabBuilderItem(1).setTabTitleColor(getColor(R.color.app_main_color)).build();
                        myTabLayout.getTabBuilderItem(0).setTabTitleColor(getColor(android.R.color.black)).build();
                    }
                }
            }
        });


        myTabLayout.with(0).init();
        myTabLayout.getTabBuilderItem(0)
                .setTabTitle("나의울림")
                .setTabTitleColor(getColor(R.color.app_main_color))
                .noBadge()
                .build();


        myTabLayout.with(1).init();
        myTabLayout.getTabBuilderItem(1)
                .setTabTitle("울림알람")
                .setTabTitleColor(getColor(android.R.color.black))
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

    private void setView(){
        userNameTv.setText(userName);

    }

    private int getColor(int colorId){
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return getResources().getColor(colorId, null);
        } else {
            return getResources().getColor(colorId);
        }
    }

}

