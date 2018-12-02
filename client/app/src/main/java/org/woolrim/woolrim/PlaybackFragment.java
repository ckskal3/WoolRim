package org.woolrim.woolrim;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.google.gson.Gson;

import org.woolrim.woolrim.DataItems.RecordItem;
import org.woolrim.woolrim.Utils.DBManagerHelper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.feeeei.circleseekbar.CircleSeekBar;

public class PlaybackFragment extends BottomSheetDialogFragment implements View.OnClickListener {


    private static final String RECORDITEM = "RecordItem";
    private RecordItem recordItem;

//    private Handler handler = new Handler();

    private MediaPlayer mediaPlayer;
    private CircleSeekBar circleSeekBar;
    private ImageView playBtn;
    private TextView completeTv, deleteTv, fullTimeTv, playingTimeTv;

    private boolean isPlaying = false;

    public Handler seekBarHandler = new Handler(Looper.getMainLooper());
    public Handler timerHandler = new Handler(Looper.getMainLooper());

    public static PlaybackFragment newInstance(Bundle bundle) {
        PlaybackFragment playbackFragment = new PlaybackFragment();
        playbackFragment.setArguments(bundle);
        return playbackFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("here", "here");
        recordItem = getArguments().getParcelable(RECORDITEM);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        setUpPlayer();

        playBtn.setOnClickListener(this);
        completeTv.setOnClickListener(this);
        deleteTv.setOnClickListener(this);

    }


    private void init(View view) {
        circleSeekBar = view.findViewById(R.id.playback_circle_seek_bar);
        playBtn = view.findViewById(R.id.playback_button_icon_imageview);
        completeTv = view.findViewById(R.id.playback_complete_textview);
        playingTimeTv = view.findViewById(R.id.playback_playing_time_textview);
        deleteTv = view.findViewById(R.id.playback_delete_textview);
        fullTimeTv = view.findViewById(R.id.playback_full_time_textview);

    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.playback_button_icon_imageview:
                    onPlay(isPlaying);
                    Log.d("ttt", recordItem.filePath);
                    isPlaying = !isPlaying;
                    break;
                case R.id.playback_delete_textview:
                    File file = new File(recordItem.filePath);
                    if (file.delete())
                        Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    dismiss();
                    break;
                case R.id.playback_complete_textview:
                    recordItem.duration = mediaPlayer.getDuration();
                    if(mediaPlayer != null){
                        stopAndResetPlayer();
                    }
                    DBManagerHelper.recordDAO.insertRecord(recordItem);

                    //////////////////서버로 파일 보내는 코드//////////////////
                    requestServerForFileUpload();
                    ////////////////////////////////////////////////////////

                    break;
            }
        } catch (IOException e) {
        }
    }

    private void requestServerForFileUpload(){
        String url = "http://stou2.cafe24.com/Woolrim/FileUpload.php";

//        String url = "http://192.168.1.252:3000/upload";
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(
                Request.Method.POST,
                WoolrimApplication.FILE_BASE_URL+"upload",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processServerResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        simpleMultiPartRequest.addStringParam("stu_id",String.valueOf(WoolrimApplication.loginedUserId));
        simpleMultiPartRequest.addFile("user_recording", recordItem.filePath);

        WoolrimApplication.requestQueue.add(simpleMultiPartRequest);
    }

    private void processServerResponse(String response){
        Gson gson = new Gson();
        Log.d("Time",response);
        Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();

        ///성공일떄와 오류일떄 나눠서 처리해야함////////

        ////////////////////////////////////////////
        BGMSelectFragment bgmSelectFragment = BGMSelectFragment.newInstance(new Bundle());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, bgmSelectFragment).addToBackStack("BGNSelectFragment")
                .commit();
        dismiss();

    }


    @Override
    public void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            stopAndResetPlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mediaPlayer != null) {
            stopAndResetPlayer();
        }
    }

    private void onPlay(boolean isPlaying) throws IOException {
        if (!isPlaying) {
            if (mediaPlayer == null) {
                startPlay();
            } else {
                resumePlay();
            }
        } else {
            pausePlay();
        }
    }

    private void setUpPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(recordItem.filePath);
            mediaPlayer.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            circleSeekBar.setMaxProcess(mediaPlayer.getDuration());

            long itemDuration = mediaPlayer.getDuration();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                    - TimeUnit.MINUTES.toSeconds(minutes);

            fullTimeTv.setText(String.format(getString(R.string.timer_format), minutes, seconds));

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    completePlay();
                }
            });
        }

    }

    private void startPlay()  {
        startItemSetting();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        updateSeekBar();
        updateTime();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }



    private void resumePlay() {
        startItemSetting();

        updateSeekBar();
        updateTime();
        mediaPlayer.start();
    }

    private void pausePlay() {
        stopAndPauseItemSetting();

        seekBarHandler.removeCallbacks(seekBarRunnable);
        timerHandler.removeCallbacks(timeRunnable);
        mediaPlayer.pause();
    }

    private void completePlay(){
        stopAndPauseItemSetting();

        circleSeekBar.setCurProcess(circleSeekBar.getMaxProcess());
        seekBarHandler.removeCallbacks(seekBarRunnable);
        timerHandler.removeCallbacks(timeRunnable);
        isPlaying = false;
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void stopPlay() {
        Log.d("Time","stopPlay");
        circleSeekBar.setCurProcess(0);
        seekBarHandler.removeCallbacks(seekBarRunnable);
        timerHandler.removeCallbacks(timeRunnable);
        isPlaying = false;
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mediaPlayer.stop();
    }

    private void stopAndResetPlayer(){
        stopPlay();
        mediaPlayer.release();
        mediaPlayer = null;
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
                Log.d("Time",String.valueOf(mCurrentPosition));
                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);

                playingTimeTv.setText(String.format(getString(R.string.timer_format), minutes, seconds));
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

    private void stopAndPauseItemSetting(){
        playBtn.setImageResource(R.drawable.play_big_icon);
        playingTimeTv.setTextColor(ContextCompat.getColor(getContext(),R.color.timer_default_text_color));
    }


    private void startItemSetting(){
        playBtn.setImageResource(R.drawable.pause_icon);
        playingTimeTv.setTextColor(ContextCompat.getColor(getContext(),R.color.app_sub_color));
    }

}
