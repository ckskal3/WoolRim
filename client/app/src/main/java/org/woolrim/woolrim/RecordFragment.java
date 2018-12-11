package org.woolrim.woolrim;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;

import org.woolrim.woolrim.AudioMixUtils.AudioEncoder;
import org.woolrim.woolrim.DataItems.RecordItem;
import org.woolrim.woolrim.Utils.DialogDismissListener;
import org.woolrim.woolrim.Utils.WaveLineViewTemp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;

public class RecordFragment extends Fragment implements View.OnClickListener, MainActivity.OnKeyBackPressedListener {
    private Recorder recorder;
    private TextView poemTv, limitDurationWarningTv;
    private ImageView startAndPauseIconIV, progressCircleIV;
    private LinearLayout startBtn, stopBtn, replayBtn;
    private Chronometer chronometer;

    private long totalDuration, tempDuration, startTime, pausedTime, limitDuration;

    private String mFileName = null, mFilePath = null, mFileNameAAC = null, mFilePathAAC = null;
    private String poetName, poemName, poemContent;
    private static String RECORDITEM = "RecordItem";

    private boolean isRecording = false, isPaused = false;
    public static boolean isBGM = false;

    private Animation itemRotate;

    private WaveLineViewTemp waveLineView;


    public static RecordFragment newInstance(Bundle bundle) {
        RecordFragment recordFragment = new RecordFragment();
        recordFragment.setArguments(bundle);
        return recordFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Time", "onCreateView");
        Bundle bundle = getArguments();
        assert bundle != null;
        poemName = bundle.getString("PoemName");
        poetName = bundle.getString("PoetName");
        poemContent = bundle.getString("PoemContent");
        limitDuration = bundle.getInt("PoemLimitDuration");
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("Time", "onViewCreated");

        init(view);

        long itemDuration = limitDuration - 100;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);

        poemTv.setText(poemContent);
        limitDurationWarningTv.setText(String.format(getString(R.string.limit_duration_warning_kr), minutes, seconds));
        setListener();


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
        MainActivity.drawableControlImageView.setVisibility(View.INVISIBLE);
        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onPause() {
//        Log.d("Time",mFilePath);
        if (waveLineView.isRunning())
            waveLineView.onPause();
        MainActivity.drawableControlImageView.setVisibility(View.VISIBLE);
        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        //녹음멈추고 플레이버튼 변경
        super.onPause();
    }

    private void init(View view) {
        waveLineView = view.findViewById(R.id.wave_line_view);
        poemTv = view.findViewById(R.id.poem_content_tv);
        limitDurationWarningTv = view.findViewById(R.id.limit_duration_warning_text_view);
        startBtn = view.findViewById(R.id.record_layout);
        startAndPauseIconIV = view.findViewById(R.id.start_and_pause_icon_iv);
        stopBtn = view.findViewById(R.id.stop_layout);
        replayBtn = view.findViewById(R.id.replay_layout);
        chronometer = view.findViewById(R.id.recording_duration_chronometer);
        progressCircleIV = view.findViewById(R.id.progress_circular);
        itemRotate = AnimationUtils.loadAnimation(getContext(), R.anim.item_rotate);

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
        PullableSource ps = new PullableSource.NoiseSuppressor(
                new PullableSource.Default(
                        new AudioRecordConfig.Default(
                                MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                                AudioFormat.CHANNEL_IN_STEREO, 44100
                        )
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
//            String tempName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());

            mFileName =  new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());
            mFileName = new StringBuilder().append(mFileName).append(".pcm").toString();
            mFileNameAAC = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());;
            mFileNameAAC = new StringBuilder().append(mFileNameAAC).append(".aac").toString();
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePathAAC = Environment.getExternalStorageDirectory().getAbsolutePath() + getString(R.string.temp_folder_name) + mFileNameAAC;
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
                    if (isBGM) {
                        //서버연동후
                        isBGM = false;
                    }

                    totalDuration = 0;
                    tempDuration = 0;

                    if (mFilePath != null) { //이전 녹음 존재시 삭제
                        waveLineView.onResume();
                        removeFile(mFilePath, mFileName);
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

                        Log.d("Time", String.valueOf(totalDuration));
                        chronometer.setTextColor(getColor(R.color.timer_default_text_color));
                        chronometer.stop();
                        chronometer.setBase(SystemClock.elapsedRealtime());


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    isRecording = false;
                    isPaused = false;
                    Log.d("Time", String.valueOf(totalDuration) + " " + String.valueOf(limitDuration));
                    if (totalDuration > limitDuration) {
                        File file = new File(mFilePath);
                        file.delete();
                        mFilePath = null;
                        Toast.makeText(getContext(), "최대녹음시간을 초과하였습니다.\n재녹음 해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.replay_layout:
                if (mFilePath != null && !isPaused && !isRecording) {
                    if (!mFilePath.equals(mFilePathAAC)) {
                        /////////////////
                        final AudioEncoder accEncoder = AudioEncoder.createAccEncoder(mFilePath,
                                new AudioEncoder.OnEncodingStartListener() {
                                    @Override
                                    public void onEncodingStart() {
                                    }
                                },
                                new AudioEncoder.OnEncodingCompletedListener() {
                                    @Override
                                    public void onEncodingCompleted() {
                                        //프로그래스바 종료
//                                    progressCircleIV.setVisibility(View.INVISIBLE);

                                        File file = new File(mFilePath);
                                        file.delete();
//                                    Toast.makeText(getContext(), "인코딩 완료", Toast.LENGTH_SHORT).show();
                                        mFilePath = mFilePathAAC;
                                        mFileName = mFileNameAAC;

                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable(RECORDITEM,
                                                new RecordItem(
                                                        mFileName,
                                                        mFilePath,
                                                        WoolrimApplication.loginedUserId,
                                                        (int) totalDuration,
                                                        poemName,
                                                        poetName));
                                        PlaybackFragment playbackFragment = PlaybackFragment.newInstance(bundle);
                                        playbackFragment.setOnDialogDismissListener(new DialogDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {

                                            }

                                            @Override
                                            public void onDismissed(@Nullable String key, boolean flag) {
                                                super.onDismissed(key, flag);
                                                if(flag){
                                                    requestServerForFileUpload();
                                                }
                                            }
                                        });
                                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        playbackFragment.show(fragmentTransaction, "playback");

                                    }
                                });
                        new EncodingAndTemp().execute(accEncoder);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(RECORDITEM,
                                new RecordItem(
                                        mFileName,
                                        mFilePath,
                                        WoolrimApplication.loginedUserId,
                                        (int) totalDuration,
                                        poemName,
                                        poetName));
                        bundle.putLong("LimitDuration", limitDuration);
                        PlaybackFragment playbackFragment = PlaybackFragment.newInstance(bundle);
                        playbackFragment.setOnDialogDismissListener(new DialogDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {

                            }

                            @Override
                            public void onDismissed(@Nullable String key, boolean flag) {
                                super.onDismissed(key, flag);
                                if(flag){
                                    requestServerForFileUpload();
                                }
                            }
                        });
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        playbackFragment.show(fragmentTransaction, "playback");

                    }
                }
                break;
        }
    }

    ///////////////이부분 수정함///////////////////////
    private void animateVoice(final float maxPeak) {
        waveLineView.setVolume((int) (maxPeak));

        Log.d("Volume", String.valueOf(maxPeak));
//        recordButton.animate().scaleX(1 + maxPeak).scaleY(1 + maxPeak).setDuration(10).start();
    }

    /////////////////////////////////////////////////////
    @Override
    public void onBack(int requestCode) {
        if (mFilePath != null) {
            Bundle bundle = new Bundle();
//        if(requestCode == 0)Log.d("RequestCode",String.valueOf(requestCode));FT
            bundle.putInt("FragmentRequestCode", CheckBottomFragment.RECORDING_BACK_REQUEST);
            bundle.putInt("RequestCode", requestCode);
            bundle.putString("FilePath", mFilePath);
            bundle.putString("FileName",mFileName);
            CheckBottomFragment checkBottomFragment = CheckBottomFragment.newInstance(bundle);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            checkBottomFragment.show(fragmentTransaction, "check");
        } else {//이거 고쳐야된다
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void requestServerForFileUpload(){
        progressCircleIV.setVisibility(View.VISIBLE);
        progressCircleIV.setAnimation(itemRotate);
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(
                Request.Method.POST,
                WoolrimApplication.FILE_BASE_URL+"upload",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressCircleIV.setVisibility(View.INVISIBLE);
                        progressCircleIV.clearAnimation();
                        processServerResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressCircleIV.setVisibility(View.INVISIBLE);
                        progressCircleIV.clearAnimation();
                    }
                }
        );
        simpleMultiPartRequest.addStringParam("stu_id",String.valueOf(WoolrimApplication.loginedUserId));
        simpleMultiPartRequest.addFile("user_recording", mFilePath);

        WoolrimApplication.requestQueue.add(simpleMultiPartRequest);
    }

    private void processServerResponse(String response){
        Gson gson = new Gson();
        Log.d("Time",response);
        Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();

        ///성공일떄와 오류일떄 나눠서 처리해야함////////

        ////////////////////////////////////////////
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECORDITEM, new RecordItem(
                mFileName,
                mFilePath,
                WoolrimApplication.loginedUserId,
                (int)totalDuration,
                poemName,
                poetName
        ));
        BGMSelectFragment bgmSelectFragment = BGMSelectFragment.newInstance(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, bgmSelectFragment).addToBackStack("BGNSelectFragment")
                .commit();

    }

    private void removeFile(final String mFilePath, final String mFileName){

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                WoolrimApplication.FILE_BASE_URL + "remove_record",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        File file = new File(mFilePath);
                        file.delete();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("stu_id",String.valueOf(WoolrimApplication.loginedUserId));
                params.put("file_name",mFileName);
                return params;
            }
        };

        WoolrimApplication.requestQueue.add(stringRequest);
    }

    private int getColor(int colorId) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return getResources().getColor(colorId, null);
        } else {
            return getResources().getColor(colorId);
        }
    }

    private class EncodingAndTemp extends AsyncTask<AudioEncoder, Boolean, Boolean> {
        @Override
        protected Boolean doInBackground(AudioEncoder... audioEncoders) {
            audioEncoders[0].encodeToFile(mFilePathAAC);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressUpdate(false);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            onProgressUpdate(true);
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            if (values[0]) {
                progressCircleIV.clearAnimation();
                progressCircleIV.setVisibility(View.INVISIBLE);
            } else {
                progressCircleIV.setVisibility(View.VISIBLE);
                progressCircleIV.setAnimation(itemRotate);
            }
        }

    }
}

