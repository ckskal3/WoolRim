package org.woolrim.woolrim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyMenuFragment extends Fragment {
    private ImageView profileIv;
    private TextView userNameTv;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;

    public static MyRecordFragment myRecordFragment1;
    public static MyRecordFragment myRecordFragment2;


    public static MyMenuFragment newInstance(Bundle bundle){
        MyMenuFragment myMenuFragment = new MyMenuFragment();
        myMenuFragment.setArguments(bundle);
        return  myMenuFragment;
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
        bundle.putInt("RequestCode",101);

        myRecordFragment1 = MyRecordFragment.newInstance(bundle);

        bundle = new Bundle();
        bundle.putInt("RequestCode",102);
        myRecordFragment2 = MyRecordFragment.newInstance(bundle);

        adapterTemp.addItem(myRecordFragment1);
        adapterTemp.addItem(myRecordFragment2);



        myViewPager.setAdapter(adapterTemp);

        myViewPager.setCurrentItem(0,true);

        myTabLayout.setupWithViewPager(myViewPager, true);




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

}

