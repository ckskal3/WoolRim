package org.woolrim.woolrim;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

/*
    android media player with seek bar
    created by 수근
 */
public class PlayerFragment extends Fragment implements View.OnClickListener, PlayerFragmentInterface {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private SeekBar seekBar;
    Button infoBtn , playBtn, pauseBtn, stopBtn;
    public ViewPager v;

    @Override
    public void onPauseFragment() {
        mediaPlayer.stop();
        isPlaying = false;
        try {
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResumeFragment() {
        seekBar.setProgress(0);
    }

    class SeekBarThread extends Thread {
        @Override
        public void run() {
            while (isPlaying) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Tag","onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Tag","onDetach");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
//        PlayerActivity.naviTitleTv.setText("테스트 : "+new Random().nextInt());

        v = PlayerActivity.viewPager;
        v.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("Tag","Change"+position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("Tag","Selected"+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("Tag","ScrollStateChanged"+state);
            }
        });
        setOnClick();
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.voice_test);
        seekBar.setMax(mediaPlayer.getDuration());
    }

    @Override
    public void onPause() {
        super.onPause();
        isPlaying = false;
        if (mediaPlayer!=null) {
            mediaPlayer.release();
        }
    }

    private void init(View view){
        seekBar = view.findViewById(R.id.seekBar_progress);
        stopBtn = view.findViewById(R.id.btn_stop);
        pauseBtn = view.findViewById(R.id.btn_pause);
        playBtn = view.findViewById(R.id.btn_play);
    }

    private void setOnClick(){
        playBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
    }

    public void onStartBtnClick() {
        Toast.makeText(getContext(), "재생", Toast.LENGTH_LONG).show();
        Log.d("Tag","play");
        mediaPlayer.start();
        isPlaying = true;
        new SeekBarThread().start();
    }

    public void onPauseBtnClick() {
        Toast.makeText(getContext(), "일시정지", Toast.LENGTH_LONG).show();
        mediaPlayer.pause();
        isPlaying = false;
    }

    public void onStopBtnClick() {
        Toast.makeText(getContext(), "정지", Toast.LENGTH_LONG).show();
        mediaPlayer.stop();
        isPlaying = false;
        seekBar.setProgress(0);
        try {
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                onStartBtnClick();
                break;
            case R.id.btn_pause:
                onPauseBtnClick();
                break;
            case R.id.btn_stop:
                onStopBtnClick();
                break;
        }
    }
}
