package org.woolrim.woolrim;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.feeeei.circleseekbar.CircleSeekBar;

/*
    android media player with seek bar
    created by 수근
 */
public class PlayerFragment extends Fragment implements View.OnClickListener, PlayerFragmentInterface {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    //    private HoloCircleSeekBar holoCircleSeekBar;
    private CircleSeekBar circleSeekBar;
    private ImageView playBtnBackIV, playIconBackIV;
    private TextView fullTimeTextView, playingTimaTextView;
    private ViewPager v;

    public Handler handler = new Handler();
    public Handler handler2 = new Handler();

    @Override
    public void onPauseFragment() {
        mediaPlayer.stop();
        handler.removeCallbacks(runnable);
        handler2.removeCallbacks(timeRunnable);

        isPlaying = false;
        try {
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
            playIconBackIV.setImageResource(R.drawable.play_big_icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResumeFragment() {
//        holoCircleSeekBar.setInitPosition(0);
        circleSeekBar.setCurProcess(0);
        playingTimaTextView.setText("00 : 00");
        playingTimaTextView.setTextColor(getResources().getColor(R.color.timer_default_text_color, null));

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                int mCurrentPosition = mediaPlayer.getCurrentPosition();
//                holoCircleSeekBar.setValue(mCurrentPosition);
                circleSeekBar.setCurProcess(mCurrentPosition);
                updateSeekBar();
            }
        }
    };

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                int mCurrentPosition = mediaPlayer.getCurrentPosition();

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);

                playingTimaTextView.setText(String.format("%02d : %02d", minutes, seconds + 1));
                updateTime();
            }

        }
    };

    private void updateSeekBar() {
        handler.postDelayed(runnable, 20);
    }

    private void updateTime() {
        handler2.postDelayed(timeRunnable, 1000);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
//        PlayerFrameFragment.naviTitleTv.setText("테스트 : "+new Random().nextInt());

        v = PlayerFrameFragment.viewPager;
        v.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("Tag", "Change" + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("Tag", "Selected" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("Tag", "ScrollStateChanged" + state);
            }
        });
        setOnClick();
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.voice_test);


        long itemDuration = mediaPlayer.getDuration();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);

        fullTimeTextView.setText(String.format("%02d : %02d", minutes, seconds));

        Log.d("Time", String.valueOf(minutes) + " " + String.valueOf(seconds));

//        holoCircleSeekBar.setMax(mediaPlayer.getDuration());
        circleSeekBar.setMaxProcess(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playIconBackIV.setImageResource(R.drawable.play_big_icon);
                isPlaying = false;

                handler.removeCallbacks(runnable);
                handler2.removeCallbacks(timeRunnable);

//                holoCircleSeekBar.setValue(mediaPlayer.getDuration());
                circleSeekBar.setCurProcess(mediaPlayer.getDuration());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        isPlaying = false;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    private void init(View view) {
        playBtnBackIV = view.findViewById(R.id.play_button_background_imageview);
        playIconBackIV = view.findViewById(R.id.play_button_icon_imageview);
//        holoCircleSeekBar = view.findViewById(R.id.circle_seek_bar);
        circleSeekBar = view.findViewById(R.id.player_circle_seek_bar);
        fullTimeTextView = view.findViewById(R.id.player_full_time_textview);
        playingTimaTextView = view.findViewById(R.id.player_playing_time_textview);
    }

    private void setOnClick() {
        playBtnBackIV.setOnClickListener(this);
        playIconBackIV.setOnClickListener(this);
    }

    public void onStartBtnClick() {
        Toast.makeText(getContext(), "재생", Toast.LENGTH_LONG).show();
        Log.d("Tag", "play");
        mediaPlayer.start();
        isPlaying = true;
        playIconBackIV.setImageResource(R.drawable.replay_icon);
        playingTimaTextView.setText("00 : 00");
        playingTimaTextView.setTextColor(getResources().getColor(R.color.app_sub_color, null));

        updateSeekBar();
        updateTime();
    }

    public void onPauseBtnClick() {
        Toast.makeText(getContext(), "일시정지", Toast.LENGTH_LONG).show();
        mediaPlayer.pause();
        playIconBackIV.setImageResource(R.drawable.play_big_icon);

        playingTimaTextView.setTextColor(getResources().getColor(R.color.timer_default_text_color, null));
        isPlaying = false;

        handler.removeCallbacks(runnable);
        handler2.removeCallbacks(timeRunnable);

    }


    @Override
    public void onClick(View v) {
        if (isPlaying) {
            onPauseBtnClick();
        } else {
            onStartBtnClick();
        }
    }
}
