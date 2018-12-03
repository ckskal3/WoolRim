package org.woolrim.woolrim;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;

import org.woolrim.woolrim.DataItems.RecordItem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.feeeei.circleseekbar.CircleSeekBar;

public class MixesRecordPlayerFragment extends Fragment implements View.OnClickListener {

    private String bgmName, bgmPosition;
    private int mediaPlayerStatus = INIT, duration;
    private static final int INIT = 0, PLAYBACK = 1, PAUSE = 2, RESUME = 3;
    private static String RECORDITEM = "RecordItem";


    private CircleSeekBar circleSeekBar;
    private CircleImageView userProfileIV;
    private ImageView playBtnBackIV, playIconBackIV;
    private TextView fullTimeTextView, playingTimeTextView, userNameTextView;
    private Button completeButton;

    private MediaPlayer mediaPlayer;
    private RecordItem recordItem;

    public Handler seekBarHandler = new Handler(Looper.getMainLooper());
    public Handler timerHandler = new Handler(Looper.getMainLooper());

    public static MixesRecordPlayerFragment newInstance(Bundle bundle) {
        MixesRecordPlayerFragment mixesRecordPlayerFragment = new MixesRecordPlayerFragment();
        mixesRecordPlayerFragment.setArguments(bundle);
        return mixesRecordPlayerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        recordItem = bundle.getParcelable(RECORDITEM);
        bgmName = bundle.getString("SelectedBGM");
        bgmPosition = bundle.getString("SelectedBGMPosition");
        duration = recordItem.duration;
        Log.d("Time",recordItem.fileName+" ");
        return inflater.inflate(R.layout.fragment_mixed_record_preview_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setItem();
    }

    @Override
    public void onPause() { //음악재생 pause
        mediaPlayer.pause();
        stopAndPauseItemSetting();
        mediaPlayerStatus = RESUME;
        super.onPause();
    }

    @Override
    public void onResume() { //
        MainActivity.toolbarLabelTv.setText(bgmName);
        super.onResume();
    }

    private void init(View view) {
        userProfileIV = view.findViewById(R.id.mixed_player_user_profile_iv);
        playIconBackIV = view.findViewById(R.id.mixed_play_button_icon_imageview);
        playBtnBackIV = view.findViewById(R.id.mixed_play_button_background_imageview);
        fullTimeTextView = view.findViewById(R.id.mixed_player_full_time_textview);
        playingTimeTextView = view.findViewById(R.id.mixed_player_playing_time_textview);
        userNameTextView = view.findViewById(R.id.mixed_player_user_name_tv);
        circleSeekBar = view.findViewById(R.id.mixed_player_circle_seek_bar);
        completeButton = view.findViewById(R.id.mixed_player_complete_btn);
    }

    private void setItem() {
        mediaPlayer = new MediaPlayer();
        Glide.with(this).load(WoolrimApplication.loginedUserProfile).into(userProfileIV);

        long[] timer = calculateTimer(duration);
        fullTimeTextView.setText(String.format(getString(R.string.timer_format), timer[0], timer[1]));
        fullTimeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.timer_default_text_color));
        playingTimeTextView.setText(getString(R.string.default_time));
        playingTimeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.timer_default_text_color));
        userNameTextView.setText(WoolrimApplication.loginedUserName);
        circleSeekBar.setMaxProcess(duration);

        setOnClick();
    }

    private void setOnClick() {
        playBtnBackIV.setOnClickListener(this);
        playIconBackIV.setOnClickListener(this);
        completeButton.setOnClickListener(this);
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

                long[] timer = calculateTimer(mCurrentPosition);
                Log.d("Time", String.valueOf(mCurrentPosition));

                playingTimeTextView.setText(String.format(getString(R.string.timer_format), timer[0], timer[1]));
                updateTime();

            }

        }
    };

    private void updateSeekBar() {
        seekBarHandler.postDelayed(seekBarRunnable, 20);
    }

    private void updateTime() {
        timerHandler.postDelayed(timeRunnable, 5);
    }

    private long[] calculateTimer(int time) {
        long[] timeResult = new long[2];
        timeResult[0] = TimeUnit.MILLISECONDS.toMinutes(time);
        timeResult[1] = TimeUnit.MILLISECONDS.toSeconds(time)
                - TimeUnit.MINUTES.toSeconds(timeResult[0]);

        return timeResult;
    }
    private void stopItemSetting() {
        stopAndPauseItemSetting();
        circleSeekBar.setCurProcess(0);
        playingTimeTextView.setText(R.string.default_time);
    }

    private void stopAndPauseItemSetting() {

        seekBarHandler.removeCallbacks(seekBarRunnable);
        timerHandler.removeCallbacks(timeRunnable);

        playIconBackIV.setImageResource(R.drawable.play_big_icon);
        playingTimeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.timer_default_text_color));
    }


    private void startItemSetting() {
        updateSeekBar();
        updateTime();

        playIconBackIV.setImageResource(R.drawable.record_pause_icon);
        playingTimeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_sub_color));
    }

    private void requestServerForComplete(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                WoolrimApplication.FILE_BASE_URL + "mix_complete",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("FragmentRequestCode", CheckBottomFragment.MY_RECORD_SUBMIT_REQUEST);
                        CheckBottomFragment checkBottomFragment = CheckBottomFragment.newInstance(bundle);
                        checkBottomFragment.show(getActivity().getSupportFragmentManager(), "BGMSelectFragment");
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mix_num",bgmPosition);
                params.put("stu_id",String.valueOf(WoolrimApplication.loginedUserId));
                params.put("file_name","1");
                params.put("duration",String.valueOf(recordItem.duration));
                params.put("poem_name",recordItem.poemName);
                params.put("poet_name",recordItem.poetName);
                return params;
            }
        };

        WoolrimApplication.requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mixed_player_complete_btn:
                stopItemSetting();
                requestServerForComplete();
                break;
            default:
                switch (mediaPlayerStatus) {
                    case INIT:
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                                startItemSetting();
                            }
                        });
                        mediaPlayerStatus = PAUSE;
                        break;
                    case PLAYBACK:
                        mediaPlayer.start();
                        startItemSetting();
                        mediaPlayerStatus = PAUSE;
                        break;
                    case RESUME:
                        mediaPlayer.start();
                        startItemSetting();
                        mediaPlayerStatus = PAUSE;
                        break;
                    case PAUSE:
                        mediaPlayer.pause();
                        stopAndPauseItemSetting();
                        mediaPlayerStatus = RESUME;
                        break;
                }
                break;

        }

    }
}
