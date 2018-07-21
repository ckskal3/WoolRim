package org.woolrim.woolrim;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
/*
    android media player with seek bar
    created by 수근
 */
public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private SeekBar seekBar;

    class SeekBarThread extends Thread {
        @Override
        public void run() {
            while (isPlaying) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.seekBar_progress);
        mediaPlayer = MediaPlayer.create(this, R.raw.voice_test);
        seekBar.setMax(mediaPlayer.getDuration());
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPlaying = false;
        if (mediaPlayer!=null) {
            mediaPlayer.release();
        }
    }

    public void onStartBtnClick(View v) {
        Toast.makeText(getApplicationContext(), "재생", Toast.LENGTH_LONG).show();
        mediaPlayer.start();
        isPlaying = true;
        new SeekBarThread().start();
    }

    public void onPauseBtnClick(View v) {
        Toast.makeText(getApplicationContext(), "일시정지", Toast.LENGTH_LONG).show();
        mediaPlayer.pause();
        isPlaying = false;
    }

    public void onStopBtnClick(View v) {
        Toast.makeText(getApplicationContext(), "정지", Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {
        finish();
    }
}
