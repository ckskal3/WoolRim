package org.woolrim.woolrim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.woolrim.woolrim.DataItems.RecordItem;
import org.woolrim.woolrim.Utils.NoAnimationViewPager;

import java.util.ArrayList;

public class PlayerFrameFragment extends Fragment {


    public  NoAnimationViewPager viewPager;
    private ArrayList<RecordItem> items;

    public static PlayerFrameFragment newInstance(Bundle bundle) {
        PlayerFrameFragment playerFrameFragment = new PlayerFrameFragment();
        playerFrameFragment.setArguments(bundle);
        return playerFrameFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        items = bundle.getParcelableArrayList("Data");
        return inflater.inflate(R.layout.fragment_frame_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        final ViewPagerAdapter adapterTemp = new ViewPagerAdapter(getChildFragmentManager());

        //Fragment 객체 생성 -> Bundle 객체 생성 -> Fragment 에 Bundle 실어서 Adapter 에 추가가
        for(RecordItem item : items){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Data",item);
            PlayerFragment playerFragment = PlayerFragment.newInstance(bundle);
            adapterTemp.addItem(playerFragment);
        }

//        adapterTemp.addItem(new PlayerFragment());
//        adapterTemp.addItem(new PlayerFragment());
//        adapterTemp.addItem(new PlayerFragment());
//        adapterTemp.addItem(new PlayerFragment());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PlayerFragmentInterface fragmentToShow = (PlayerFragmentInterface) adapterTemp.getItem(position);
                fragmentToShow.onResumeFragment();

                PlayerFragmentInterface fragmentToHide = (PlayerFragmentInterface) adapterTemp.getItem(currentPosition);
                fragmentToHide.onPauseFragment();

                currentPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(adapterTemp);


    }



    private void init(View view) {
        viewPager = view.findViewById(R.id.viewPager2);
    }
}
