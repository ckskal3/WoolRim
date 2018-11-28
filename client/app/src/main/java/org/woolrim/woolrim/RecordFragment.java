package org.woolrim.woolrim;

import android.content.Context;

import android.graphics.Canvas;
import android.media.AudioRecord;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.woolrim.woolrim.AudioMixUtils.AudioEncoder;
import org.woolrim.woolrim.DataItems.RecordItem;
import org.woolrim.woolrim.Utils.WaveLineViewTemp;

import java.io.File;
import java.io.IOException;

import jaygoo.widget.wlv.WaveLineView;
import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;

public class RecordFragment extends Fragment implements View.OnClickListener, MainActivity.OnKeyBackPressedListener {
    private Recorder recorder;
    private TextView poemTv;
    private ImageView startAndPauseIconIV;
    private LinearLayout startBtn, stopBtn, replayBtn;
    private Chronometer chronometer;

    private long totalDuration, tempDuration, startTime, pausedTime;

    private String mFileName = null, mFilePath = null, mFileNameAAC = null;
    private String poetName, poemName, poemContent;

    private boolean isRecording = false;
    private boolean isPaused = false;

//    private WaveLineView waveLineView;

    private WaveLineViewTemp waveLineView;

    public static RecordFragment newInstance(Bundle bundle) {
        RecordFragment recordFragment = new RecordFragment();
        recordFragment.setArguments(bundle);
        return recordFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;
        poemName = bundle.getString("PoemName");
        poetName = bundle.getString("PoetName");
        poemContent = bundle.getString("PoemContent");
//        handler = new Handler();
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        poemTv.setText(poemContent);

        setListener();



    }

    @Override
    public void onAttach(Context context) {
        Log.d("Time", "onAttach");

        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        Log.d("Time", "onDetach");
        if (isPaused) {
            //해당 녹음 파일 삭제 해야됨
            File file = new File(mFilePath);
            if (file.delete()) {
                Log.d("OnDetach", "FileDelete");
            }
        }
        recorder = null;
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        Log.d("Time", "onDestroyView");

        super.onDestroyView();
    }

    @Override
    public void onResume() {
        Log.d("Time", "onResume");
        waveLineView.onResume();
        super.onResume();
        MainActivity.toolbarLabelTv.setText(poetName + " - " + poemName);
    }

    private void init(View view) {
        waveLineView = view.findViewById(R.id.wave_line_view);
        poemTv = view.findViewById(R.id.poem_content_tv);
        startBtn = view.findViewById(R.id.record_layout);
        startAndPauseIconIV = view.findViewById(R.id.start_and_pause_icon_iv);
        stopBtn = view.findViewById(R.id.stop_layout);
        replayBtn = view.findViewById(R.id.replay_layout);
        chronometer = view.findViewById(R.id.recordingduration_chronometer);
    }

    private void setupRecorder() {

        File recordFile;
        recordFile = file();
        recorder = OmRecorder.pcm(
                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override
                    public void onAudioChunkPulled(AudioChunk audioChunk) {
                        animateVoice((float) (audioChunk.maxAmplitude()));
                    }
                }), recordFile);

    }


    private PullableSource mic() {
        PullableSource ps = new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_STEREO, 44100
                )
        );
//        audioRecord = ps.audioRecord();
        return ps;
    }

    @NonNull
    private File file() {
        int count = 0;
        File f;

        File folder = new File(Environment.getExternalStorageDirectory() + getString(R.string.temp_folder_name));
        if (!folder.exists()) {
            //folder /SoundRecorder doesn't exist, create the folder
            folder.mkdir();
        }

        do {
            count++;

            mFileName = getString(R.string.app_name)
                    + "_" + +count + ".pcm";//count부분 갯수 받아와서 수정해야됨
            mFileNameAAC = getString(R.string.app_name)
                    + "_" + +count + ".aac";
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileNameAAC = Environment.getExternalStorageDirectory().getAbsolutePath() + getString(R.string.temp_folder_name) + mFileNameAAC;
            mFilePath += getString(R.string.temp_folder_name) + mFileName;


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
            case R.id.record_layout:
                if (!isRecording && !isPaused) { //최초 실행 녹음
                    totalDuration = 0;
                    tempDuration =0;

                    if (mFilePath != null) { //이전 녹음 존재시 삭제
                        waveLineView.onResume();
                        File file = new File(mFilePath);
                        if (file.delete()) {
                            Log.d("삭제", "됨");
                        } else {
                            Log.d("삭제", "안됨");
                        }
                        mFilePath = null;
                    }
                    ///////////이부분 수정함
                    waveLineView.startAnim();
                    setupRecorder();

                    startTime = System.currentTimeMillis();

                    startAndPauseIconIV.setImageResource(R.drawable.record_pause_icon);

                    recorder.startRecording();

                    chronometer.setTextColor(getColor(R.color.app_sub_color));
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();

                    isRecording = !isRecording;
                    Toast.makeText(getContext(), "Record Start!", Toast.LENGTH_SHORT).show();
                } else if (!isRecording && isPaused) { // 일시 정지 후 다시 녹음

                    startAndPauseIconIV.setImageResource(R.drawable.record_pause_icon);

                    startTime = System.currentTimeMillis();

                    recorder.resumeRecording();
                    waveLineView.startAnim();

                    chronometer.setTextColor(getColor(R.color.app_sub_color));
                    chronometer.setBase(SystemClock.elapsedRealtime() + pausedTime);
                    chronometer.start();

                    isRecording = !isRecording;
                    isPaused = !isPaused;
                    Toast.makeText(getContext(), "Record Resume!", Toast.LENGTH_SHORT).show();
                } else { // 일시정지

                    tempDuration = System.currentTimeMillis() - startTime + tempDuration;
                    totalDuration = tempDuration;
                    startAndPauseIconIV.setImageResource(R.drawable.record_start_circle);

                    recorder.pauseRecording();
                    waveLineView.stopAnim();

                    chronometer.setTextColor(getColor(R.color.timer_default_text_color));
                    pausedTime = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();

                    isRecording = !isRecording;
                    isPaused = !isPaused;
                    Toast.makeText(getContext(), "Record Pause!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.stop_layout:
                if (mFilePath != null && isRecording) {
                    try {
                        if (!isPaused)
                            totalDuration = totalDuration + System.currentTimeMillis() - startTime;
                        recorder.stopRecording();

                        recorder = null;
                        startAndPauseIconIV.setImageResource(R.drawable.record_start_circle);
                        waveLineView.clearDraw();



//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                waveLineView.clearDraw();
//                            }
//                        });
                        int timer = (int)chronometer.getBase();
                        Log.d("Time",String.valueOf(timer)+" "+String.valueOf(totalDuration));
                        chronometer.setTextColor(getColor(R.color.timer_default_text_color));
                        chronometer.stop();
                        chronometer.setBase(SystemClock.elapsedRealtime());


                        Toast.makeText(getContext(), "Record Stop!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /////////////////
                    AudioEncoder accEncoder = AudioEncoder.createAccEncoder(mFilePath);
                    String finalMixPath = mFileNameAAC;
                    accEncoder.encodeToFile(finalMixPath);

                    mFilePath = mFileNameAAC;
                    ///////////////////


                    isRecording = false;
                    isPaused = false;

                }

                break;
            case R.id.replay_layout:
                if (mFilePath != null && !isPaused && !isRecording) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("RecordItem", new RecordItem(mFileName, mFilePath, 0, (int)totalDuration));
                    PlaybackFragment playbackFragment = PlaybackFragment.newInstance(bundle);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    playbackFragment.show(fragmentTransaction, "playback");
                }
                break;
        }
    }
///////////////이부분 수정함///////////////////////
    private void animateVoice(final float maxPeak) {
        waveLineView.setVolume((int)(maxPeak));

        Log.d("Volume",String.valueOf(maxPeak));
//        recordButton.animate().scaleX(1 + maxPeak).scaleY(1 + maxPeak).setDuration(10).start();
    }
/////////////////////////////////////////////////////
    @Override
    public void onBack(int requestCode) {
        Bundle bundle = new Bundle();
//        if(requestCode == 0)Log.d("RequestCode",String.valueOf(requestCode));
        bundle.putInt("FragmentRequestCode", CheckBottomFragment.RECORDING_BACK_REQUEST);
        bundle.putInt("RequestCode",requestCode);
        bundle.putString("FilePath", mFilePath);
        CheckBottomFragment checkBottomFragment = CheckBottomFragment.newInstance(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        checkBottomFragment.show(fragmentTransaction, "check");
    }

    private int getColor(int colorId){
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return getResources().getColor(colorId, null);
        } else {
            return getResources().getColor(colorId);
        }
    }
}

