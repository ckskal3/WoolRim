package org.woolrim.woolrim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity {
    public static ViewPager viewPager;
    private TabLayout tabLayout;
    public static TextView naviTitleTv;
    private static ImageView ii;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings :
                Log.d("tag","tag");
                finish();
                break;
            case android.R.id.home:
                Log.d("tag","tag");
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
       actionBar.setDisplayHomeAsUpEnabled(true);



        final ViewPagerAdapter adapterTemp = new ViewPagerAdapter(getSupportFragmentManager());

        //Fragment 객체 생성 -> Bundle 객체 생성 -> Fragment 에 Bundle 실어서 Adapter 에 추가가
        adapterTemp.addItem(new PlayerFragment());
        adapterTemp.addItem(new PlayerFragment());
        adapterTemp.addItem(new PlayerFragment());
        adapterTemp.addItem(new PlayerFragment());
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

        viewPager.setCurrentItem(0,false);

        tabLayout.setupWithViewPager(viewPager, true);



    }

//    private void setNaviItem(View view) {
//        naviTitleTv = view.findViewById(R.id.navititletextview);
//        ii = view.findViewById(R.id.navibackimageview);
//
//    }

    private void init() {
        viewPager = findViewById(R.id.viewPager2);
        tabLayout = findViewById(R.id.tab_layout);
    }
}
