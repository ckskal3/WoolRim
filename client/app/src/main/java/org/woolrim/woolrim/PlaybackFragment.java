package org.woolrim.woolrim;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PlaybackFragment extends DialogFragment implements View.OnClickListener{

    private static final String RECORDITEM = "RecordItem";
    private RecordItem recordItem;

    private Handler handler = new Handler();

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Button playBtn, completeBtn, deleteBtn;

    private boolean isPlaying = false;

    public static PlaybackFragment newInstance(RecordItem recordItem){
        PlaybackFragment playbackFragment = new PlaybackFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECORDITEM, recordItem);
        playbackFragment.setArguments(bundle);
        return playbackFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordItem = getArguments().getParcelable(RECORDITEM);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view  = getActivity().getLayoutInflater().inflate(R.layout.fragment_playback
        ,null);

        init(view);

        playBtn.setOnClickListener(this);
        completeBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer != null && b){
                    mediaPlayer.seekTo(i);
                    handler.removeCallbacks(runnable);

                    updateSeekBar();
                }else if(mediaPlayer == null && b ){
                    try {
                        prepareMediaPlayer(i);
                    }catch (IOException e){}
                    updateSeekBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer != null){
                    handler.removeCallbacks(runnable);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer !=null){
                    handler.removeCallbacks(runnable);
                    mediaPlayer.seekTo(seekBar.getProgress());

                    updateSeekBar();
                }
            }
        });

        builder.setView(view);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return builder.create();
    }

    private void init(View view){
        seekBar = view.findViewById(R.id.playbackseekBar);
        playBtn = view.findViewById(R.id.playbutton);
        completeBtn = view.findViewById(R.id.completebutton);
        deleteBtn = view.findViewById(R.id.deletebutton);

    }

    @Override
    public void onClick(View view){
        try {
            switch (view.getId()) {
                case R.id.playbutton:
                    onPlay(isPlaying);
                    Log.d("ttt", recordItem.path);
                    isPlaying = !isPlaying;
                    break;
                case R.id.deletebutton:
                    dismiss();
                    break;
                case R.id.completebutton:
                    dismiss();
                    break;
            }
        }catch (IOException e){}
    }


    @Override
    public void onStart() {
        super.onStart();

        Window window  = getDialog().getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);

        AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        alertDialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(false);
        alertDialog.getButton(Dialog.BUTTON_NEUTRAL).setEnabled(false);

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

    private void onPlay(boolean isPlaying) throws IOException{
        if(!isPlaying){
            if(mediaPlayer == null){
                startPlay();
            }else{
                resumePlay();
            }
        }else{
            pausePlay();
        }
    }

    private void startPlay() throws  IOException{
        playBtn.setText("정지");
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setDataSource(recordItem.path);
        mediaPlayer.prepare();
        seekBar.setMax(mediaPlayer.getDuration());

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

    private void prepareMediaPlayer(int progress) throws IOException{
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setDataSource(recordItem.path);
        mediaPlayer.prepare();
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.seekTo(progress);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlay();
            }
        });

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    private void resumePlay(){
        playBtn.setText("정지");
        handler.removeCallbacks(runnable);
        mediaPlayer.start();
        updateSeekBar();
    }

    private void pausePlay(){
        playBtn.setText("재생");
        handler.removeCallbacks(runnable);
        mediaPlayer.pause();
    }

    private void stopPlay(){
        playBtn.setText("재생");
        handler.removeCallbacks(runnable);
        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;

        seekBar.setProgress(seekBar.getMax());

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer != null){

                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);

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
