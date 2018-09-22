package org.woolrim.woolrim;

import android.app.FragmentTransaction;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;
import omrecorder.WriteAction;

public class RecordFragment extends Fragment implements View.OnClickListener {
    private Recorder recorder;
    private TextView poemTv;
    private Button startBtn, stopBtn, replayBtn;

    private String mFileName, mFilePath;
    private MediaPlayer mediaPlayer;

    private boolean isRecording = false;
    private boolean isPaused = false;

    public static RecordFragment newInstance(Bundle bundle){
        RecordFragment recordFragment = new RecordFragment();
        recordFragment.setArguments(bundle);
        return recordFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            init(view);

        setupRecord();
        setListener();
        poemTv.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(isPaused){
            //해당 녹음 파일 삭제 해야됨
        }
        recorder = null;
    }


    private void init(View view) {
        poemTv = view.findViewById(R.id.poemtv);
        startBtn = view.findViewById(R.id.record_btn);
        stopBtn = view.findViewById(R.id.stop_btn);
        replayBtn = view.findViewById(R.id.replay_btn);
    }

    private void setupRecorder() {
        recorder = OmRecorder.wav(
                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override
                    public void onAudioChunkPulled(AudioChunk audioChunk) {
                        animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
                    }
                }), file());
    }

    private void setupRecord() {
        recorder = OmRecorder.wav(
                new PullTransport.Noise(mic(),
                        new PullTransport.OnAudioChunkPulledListener() {
                            @Override
                            public void onAudioChunkPulled(AudioChunk audioChunk) {
                                animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
                            }
                        },
                        new WriteAction.Default(),
                        new Recorder.OnSilenceListener() {
                            @Override
                            public void onSilence(long silenceTime) {
                                Log.e("silenceTime", String.valueOf(silenceTime));
                                Toast.makeText(getContext(), "silence of " + silenceTime + " detected",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, 200
                ), file()
        );
    }

    private void animateVoice(final float maxPeak) {
//        startBtn.animate().scaleX(1 + maxPeak).scaleY(1 + maxPeak).setDuration(10).start();
    }

    private PullableSource mic() {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 44100
                )
        );
    }

    @NonNull
    private File file() {
        int count = 0;
        File f;

        File folder = new File(Environment.getExternalStorageDirectory() + "/WoolrimTemp");
        if (!folder.exists()) {
            //folder /SoundRecorder doesn't exist, create the folder
            folder.mkdir();
        }

        do {
            count++;

            mFileName = getString(R.string.app_name)
                    + "_" + +count + ".wav";//count부분 갯수 받아와서 수정해야됨
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += "/WoolrimTemp/" + mFileName;

            f = new File(mFilePath);
        } while (f.exists() && !f.isDirectory());

        return f;
    }

    private void setListener() {
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        replayBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_btn:
                if (!isRecording && !isPaused) {
                    setupRecorder();
                    recorder.startRecording();
                    isRecording = !isRecording;
                    startBtn.setText("일시정지");
                    Toast.makeText(getContext(), "Record Start!", Toast.LENGTH_SHORT).show();
                } else if (!isRecording && isPaused) {
                    recorder.resumeRecording();
                    isRecording = !isRecording;
                    isPaused = !isPaused;
                    startBtn.setText("일시정지");
                    Toast.makeText(getContext(), "Record Resume!", Toast.LENGTH_SHORT).show();
                } else {
                    recorder.pauseRecording();
                    isRecording = !isRecording;
                    isPaused = !isPaused;
                    startBtn.setText("녹음하기");
                    Toast.makeText(getContext(), "Record Pause!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.stop_btn:
                try {
                    startBtn.setText("녹음하기");
                    recorder.stopRecording();
                    Toast.makeText(getContext(), "Record Stop!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateVoice(0);
                    }
                }, 100);
                isRecording = false;
                isPaused = false;

                break;
            case R.id.replay_btn:
                PlaybackFragment playbackFragment = PlaybackFragment.newInstance(new RecordItem(mFileName, mFilePath, 0));
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                playbackFragment.show(fragmentTransaction, "playback");
                break;
        }
    }
}
