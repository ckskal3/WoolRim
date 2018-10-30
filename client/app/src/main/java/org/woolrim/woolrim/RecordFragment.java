package org.woolrim.woolrim;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;

import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;

public class RecordFragment extends Fragment implements View.OnClickListener, MainActivity.OnKeyBackPressedListener {
    private Recorder recorder;
    private TextView poemTv;
    private Button startBtn, stopBtn, replayBtn;
    private Chronometer chronometer;

    private long totalDuration, tempDuration , startTime, pausedTime;

    private String mFileName = null, mFilePath = null;
    private String poetName, poemName;

    private boolean isRecording = false;
    private boolean isPaused = false;

    private File recordFile;


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
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        setListener();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (isPaused) {
            //해당 녹음 파일 삭제 해야됨
            File file = new File(mFilePath);
            if(file.delete()){
                Log.d("OnDetach","FileDelete");
            }
        }
        recorder = null;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toolbarLabelTv.setText(poetName+" - "+poemName);
    }

    private void init(View view) {
        poemTv = view.findViewById(R.id.poemtv);
        startBtn = view.findViewById(R.id.record_btn);
        stopBtn = view.findViewById(R.id.stop_btn);
        replayBtn = view.findViewById(R.id.replay_btn);
        chronometer = view.findViewById(R.id.recordingduration_chronometer);
    }

    private void setupRecorder() {
        recordFile = file();
        recorder = OmRecorder.wav(
                new PullTransport.Default(mic()), recordFile);

    }


    private PullableSource mic() {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_STEREO, 44100
                )
        );
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
                    + "_" + +count + ".wav";//count부분 갯수 받아와서 수정해야됨
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath +=  getString(R.string.temp_folder_name) + mFileName;

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

                if (!isRecording && !isPaused) { //최초 실행 녹음
                    if(mFilePath != null){ //이전 녹음 존재시 삭제
                        File file = new File(mFilePath);
                        if(file.delete()){
                            Log.d("삭제","됨");
                        }else{
                            Log.d("삭제","안됨");
                        }
                        mFilePath = null;
                    }

                    setupRecorder();

                    startTime = System.currentTimeMillis();

                    recorder.startRecording();

                    chronometer.setTextColor(getResources().getColor(R.color.app_sub_color,null));
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();

                    isRecording = !isRecording;
                    startBtn.setText("일시정지");
                    Toast.makeText(getContext(), "Record Start!", Toast.LENGTH_SHORT).show();
                } else if (!isRecording && isPaused) { // 일시 정지 후 다시 녹음

                    startTime = System.currentTimeMillis();

                    recorder.resumeRecording();

                    chronometer.setTextColor(getResources().getColor(R.color.app_sub_color,null));
                    chronometer.setBase(SystemClock.elapsedRealtime() + pausedTime);
                    chronometer.start();

                    isRecording = !isRecording;
                    isPaused = !isPaused;
                    startBtn.setText("일시정지");
                    Toast.makeText(getContext(), "Record Resume!", Toast.LENGTH_SHORT).show();
                } else { // 일시정지

                    tempDuration = System.currentTimeMillis() - startTime + tempDuration;
                    totalDuration = tempDuration;

                    recorder.pauseRecording();

                    chronometer.setTextColor(getResources().getColor(R.color.timer_default_text_color,null));
                    pausedTime = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();

                    isRecording = !isRecording;
                    isPaused = !isPaused;
                    startBtn.setText("녹음하기");
                    Toast.makeText(getContext(), "Record Pause!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.stop_btn:
                if(mFilePath != null && isRecording) {
                    try {
                        if(!isPaused)
                            totalDuration = totalDuration + System.currentTimeMillis() - startTime;
                        startBtn.setText("녹음하기");
                        recorder.stopRecording();

                        chronometer.setTextColor(getResources().getColor(R.color.timer_default_text_color,null));
                        chronometer.stop();
                        chronometer.setBase(SystemClock.elapsedRealtime());

                        Toast.makeText(getContext(), "Record Stop!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isRecording = false;
                    isPaused = false;


                    Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/WoolrimTemp/"));
                    Log.d("Title",uri.toString());
                    Cursor cursor = getContext().getContentResolver().query(
                            uri,
                            new String[] { MediaStore.Audio.Media._ID,
                                    MediaStore.Audio.Media.DISPLAY_NAME,
                                    MediaStore.Audio.Media.TITLE,
                                    MediaStore.Audio.Media.DURATION,
                                    MediaStore.Audio.Media.ARTIST,
                                    MediaStore.Audio.Media.ALBUM,
                                    MediaStore.Audio.Media.YEAR,
                                    MediaStore.Audio.Media.MIME_TYPE,
                                    MediaStore.Audio.Media.SIZE,
                                    MediaStore.Audio.Media.DATA },
                            null,
                            null, null);

                    if(cursor != null){
                        Log.d("Title",String.valueOf(cursor.getCount()));
                    }
                }

                break;
            case R.id.replay_btn:
                if(mFilePath != null && !isPaused && !isRecording) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("RecordItem", new RecordItem(mFileName,mFilePath,0,"null"));
                    PlaybackFragment playbackFragment = PlaybackFragment.newInstance(bundle);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    playbackFragment.show(fragmentTransaction, "playback");
                }
                break;
        }
    }

    @Override
    public void onBack() {
            Bundle bundle = new Bundle();
            bundle.putInt("FragmentRequestCode", CheckBottomFragment.RECORDING_BACK_REQUEST);
            bundle.putString("FilePath",mFilePath);
            CheckBottomFragment checkBottomFragment = CheckBottomFragment.newInstance(bundle);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            checkBottomFragment.show(fragmentTransaction, "check");
    }
}
