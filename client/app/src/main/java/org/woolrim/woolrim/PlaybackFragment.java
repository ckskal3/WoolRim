package org.woolrim.woolrim;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
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

    private Handler handler = new Handler();

    private MediaPlayer mediaPlayer;
    private CircleSeekBar seekBar;
    private ImageView playBtn;
    private TextView completeTv, deleteTv, fullTimeTv;

    private boolean isPlaying = false;

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

        seekBar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onChanged(CircleSeekBar circleSeekBar, int i) {

            }

//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                if (mediaPlayer != null && b) {
//                    mediaPlayer.seekTo(i);
//                    handler.removeCallbacks(runnable);
//
//                    updateSeekBar();
//                } else if (mediaPlayer == null && b) {
//                    try {
//                        prepareMediaPlayer(i);
//                    } catch (IOException e) {
//                    }
//                    updateSeekBar();
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                if (mediaPlayer != null) {
//                    handler.removeCallbacks(runnable);
//                }
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if (mediaPlayer != null) {
//                    handler.removeCallbacks(runnable);
//                    mediaPlayer.seekTo(seekBar.getProgress());
//
//                    updateSeekBar();
//                }
//            }
        });

    }


    private void init(View view) {
        seekBar = view.findViewById(R.id.playback_circle_seek_bar);
        playBtn = view.findViewById(R.id.playback_button_icon_imageview);
        completeTv = view.findViewById(R.id.playback_complete_textview);
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
                    recordItem.duration = String.valueOf(mediaPlayer.getDuration());
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
                url,
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
        simpleMultiPartRequest.addFile("file",recordItem.filePath);

        WoolrimApplication.requestQueue.add(simpleMultiPartRequest);
    }

    private void processServerResponse(String response){
        Gson gson = new Gson();
//        RequestData result = gson.fromJson(response,RequestData.class);
        Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();

        ///성공일떄와 오류일떄 나눠서 처리해야함////////

        ////////////////////////////////////////////
        BGMSelectFragment bgmSelectFragment = BGMSelectFragment.newInstance(new Bundle());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, bgmSelectFragment).addToBackStack("BGNSelectFragment")
                .commit();
        dismiss();

//        Toast.makeText(getContext(),result.status+" "+String.valueOf(result.code)+result.message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            stopPlay();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mediaPlayer != null) {
            stopPlay();
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
        }catch (IOException e){}


        seekBar.setMaxProcess(mediaPlayer.getDuration());

        long itemDuration = mediaPlayer.getDuration();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);

        fullTimeTv.setText(String.format("%02d : %02d", minutes, seconds));

    }

    private void startPlay()  {
        playBtn.setImageResource(R.drawable.replay_icon);


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlay();
            }
        });

        updateSeekBar();

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void prepareMediaPlayer(int progress) throws IOException {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setDataSource(recordItem.filePath);
        mediaPlayer.prepare();
        seekBar.setMaxProcess(mediaPlayer.getDuration());
        mediaPlayer.seekTo(progress);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlay();
            }
        });

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    private void resumePlay() {
        playBtn.setImageResource(R.drawable.replay_icon);
        handler.removeCallbacks(runnable);
        mediaPlayer.start();
        updateSeekBar();
    }

    private void pausePlay() {
        playBtn.setImageResource(R.drawable.play_big_icon);
        handler.removeCallbacks(runnable);
        mediaPlayer.pause();
    }

    private void stopPlay() {
        playBtn.setImageResource(R.drawable.play_big_icon);
        handler.removeCallbacks(runnable);
        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;

        seekBar.setCurProcess(seekBar.getMaxProcess());

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {

                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setCurProcess(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);

                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        handler.postDelayed(runnable, 100);
    }

}
