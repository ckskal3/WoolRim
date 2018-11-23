package org.woolrim.woolrim;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.woolrim.woolrim.DataItems.MyFavoritesItem;
import org.woolrim.woolrim.DataItems.RecordItem;
import org.woolrim.woolrim.Utils.DBManagerHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.feeeei.circleseekbar.CircleSeekBar;

/*
    android media player with seek bar
    created by 수근
 */
public class PlayerFragment extends Fragment implements View.OnClickListener, PlayerFragmentInterface, View.OnTouchListener {

    private CircleSeekBar circleSeekBar;
    private CircleImageView userProfileIV;
    private ImageView playBtnBackIV, playIconBackIV, favoriteIconIV;
    private TextView fullTimeTextView, playingTimeTextView, userNameTextView;

    private ConstraintLayout playerBackgroundLayout;

    private RecordItem recordData;
    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false;
    int bookmarkFlag;

    public Handler seekBarHandler = new Handler(Looper.getMainLooper());
    public Handler timerHandler = new Handler(Looper.getMainLooper());

    public static PlayerFragment newInstance(Bundle bundle) {
        PlayerFragment playerFragment = new PlayerFragment();
        playerFragment.setArguments(bundle);
        return playerFragment;
    }

    @Override
    public void onPauseFragment() {
        mediaPlayer.stop();
        seekBarHandler.removeCallbacks(seekBarRunnable);
        timerHandler.removeCallbacks(timeRunnable);

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
        circleSeekBar.setCurProcess(0);
        playingTimeTextView.setText("00 : 00");
        playingTimeTextView.setTextColor(getColor(R.color.timer_default_text_color));
    }

    @Override
    public void onDetach() {
        if(recordData.bookmarkFlag != bookmarkFlag){
            DBManagerHelper.favoriteDAO.updateFavorite(
                    new MyFavoritesItem(
                            String.valueOf(recordData.mediaId),
                            String.valueOf(recordData.poemId),
                            String.valueOf(recordData.studentId),
                            recordData.poemName,
                            recordData.studentName,
                            "Guest"
                    ),
                    bookmarkFlag);
        }
        super.onDetach();
        Log.d("OnDetach",recordData.studentName);
    }

    private Runnable seekBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                int mCurrentPosition = mediaPlayer.getCurrentPosition();
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

                playingTimeTextView.setText(String.format("%02d : %02d", minutes, seconds + 1));
                updateTime();
            }

        }
    };

    private void updateSeekBar() {
        seekBarHandler.postDelayed(seekBarRunnable, 20);
    }

    private void updateTime() {
        timerHandler.postDelayed(timeRunnable, 1000);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;
        recordData = bundle.getParcelable("Data");
        assert recordData != null;
        bookmarkFlag = recordData.bookmarkFlag;
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @SuppressLint({"DefaultLocale", "ClickableViewAccessibility"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        setItem();
//        PlayerFrameFragment.naviTitleTv.setText("테스트 : "+new Random().nextInt());

        setOnClick();

        playerBackgroundLayout.setOnTouchListener(this);

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.voice_test);


        long itemDuration = mediaPlayer.getDuration();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);

        fullTimeTextView.setText(String.format("%02d : %02d", minutes, seconds));

        Log.d("Time", String.valueOf(minutes) + " " + String.valueOf(seconds));

        circleSeekBar.setMaxProcess(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playIconBackIV.setImageResource(R.drawable.play_big_icon);
                isPlaying = false;

                seekBarHandler.removeCallbacks(seekBarRunnable);
                timerHandler.removeCallbacks(timeRunnable);

                circleSeekBar.setCurProcess(mediaPlayer.getDuration());
            }
        });
    }

    ///////////////////////////////////////////
    public final int HORIZONTAL_MIN_DISTANCE = 150;

    public static enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }

    private static final String logTag = "SwipeDetector";
    private float downX, downY, upX, upY;
    private Action mSwipeDetected = Action.None;
    public Action getAction() {
        return mSwipeDetected;
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                mSwipeDetected = Action.None;
                return false; // allow other events like Click to be processed
            }
            case MotionEvent.ACTION_MOVE: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                // horizontal swipe detection
                if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        mSwipeDetected = Action.LR;
                        return true;
                        }
                    if (deltaX > 0) {
                        mSwipeDetected = Action.RL;
                        return true;
                    }
                }
                mSwipeDetected = Action.None;
                return true;
            }
        }
        if(mSwipeDetected == Action.RL){
            Log.i(logTag, "우에서 좌");

        }else if(mSwipeDetected == Action.LR) {
            Log.i(logTag, "좌에서 우");

        }

        return false;
    }

//////////////////////////////////
    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
        seekBarHandler.removeCallbacks(seekBarRunnable);
        timerHandler.removeCallbacks(timeRunnable);

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
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            circleSeekBar.setCurProcess(0);
            playingTimeTextView.setText("00 : 00");
            playingTimeTextView.setTextColor(getColor(R.color.timer_default_text_color));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_favorite_icon_iv:
                if(bookmarkFlag == 1){
                    favoriteIconIV.setImageResource(R.drawable.favorite_middle_empty_color_icon);
                    bookmarkFlag = 0;
                }else{
                    favoriteIconIV.setImageResource(R.drawable.favorite_middle_color_icon);
                    bookmarkFlag = 1;
                }
                break;
            default:
                if (isPlaying) {
                    onPauseBtnClick();
                } else {
                    onStartBtnClick();
                }
                break;
        }

    }

    private void init(View view) {
        playerBackgroundLayout = view.findViewById(R.id.player_background_layout);
        userNameTextView = view.findViewById(R.id.player_user_name_tv);
        userProfileIV = view.findViewById(R.id.player_user_profile_iv);
        favoriteIconIV = view.findViewById(R.id.player_favorite_icon_iv);
        playBtnBackIV = view.findViewById(R.id.play_button_background_imageview);
        playIconBackIV = view.findViewById(R.id.play_button_icon_imageview);
        circleSeekBar = view.findViewById(R.id.player_circle_seek_bar);
        fullTimeTextView = view.findViewById(R.id.player_full_time_textview);
        playingTimeTextView = view.findViewById(R.id.player_playing_time_textview);
    }

    private void setItem() {
        userNameTextView.setText(recordData.studentName);
        if (recordData.studentProfilePath == null|| recordData.studentProfilePath.equals(getString(R.string.no_profile_en))) {
            Glide.with(this).load(R.drawable.profile_icon).into(userProfileIV);
        } else {
            Glide.with(this).load(recordData.studentProfilePath).into(userProfileIV);
        }
        if (recordData.bookmarkFlag == 1) {
            favoriteIconIV.setImageResource(R.drawable.favorite_middle_color_icon);
        } else {
            favoriteIconIV.setImageResource(R.drawable.favorite_middle_empty_color_icon);
        }
    }

    private void setOnClick() {
        playBtnBackIV.setOnClickListener(this);
        playIconBackIV.setOnClickListener(this);
        favoriteIconIV.setOnClickListener(this);
    }

    public void onStartBtnClick() {
        Toast.makeText(getContext(), "재생", Toast.LENGTH_LONG).show();
        Log.d("Tag", "play");
        mediaPlayer.start();
        isPlaying = true;
        playIconBackIV.setImageResource(R.drawable.replay_icon);
        playingTimeTextView.setText("00 : 00");
        playingTimeTextView.setTextColor(getColor(R.color.app_sub_color));

        updateSeekBar();
        updateTime();
    }

    public void onPauseBtnClick() {
        Toast.makeText(getContext(), "일시정지", Toast.LENGTH_LONG).show();
        mediaPlayer.pause();
        playIconBackIV.setImageResource(R.drawable.play_big_icon);

        playingTimeTextView.setTextColor(getColor(R.color.timer_default_text_color));
        isPlaying = false;

        seekBarHandler.removeCallbacks(seekBarRunnable);
        timerHandler.removeCallbacks(timeRunnable);

    }

    private int getColor(int colorId) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return getResources().getColor(colorId, null);
        } else {
            return getResources().getColor(colorId);
        }
    }

}
