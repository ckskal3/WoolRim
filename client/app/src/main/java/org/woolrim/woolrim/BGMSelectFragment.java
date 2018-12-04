package org.woolrim.woolrim;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.woolrim.woolrim.DataItems.BGMItem;
import org.woolrim.woolrim.DataItems.RecordItem;
import org.woolrim.woolrim.Utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.feeeei.circleseekbar.CircleSeekBar;

public class BGMSelectFragment extends Fragment {

    private RecyclerView bgmSelectRecyclerView;
    private Button selectCompleteBtn;
    private CircleSeekBar circleSeekBar;
    private TextView fullTImeTextView, playtimeTextView;
    private ImageView playButtonIconIV, progressCircleIV;

    private BGMSelectAdapter bgmSelectAdapter;
    private MediaPlayer mediaPlayer;
    private RecordItem recordItem;
    private Animation itemRotate;

    private int duration, mediaPlayerStatus, currentItemPosition = -1;
    private int[] bgms = {0, R.raw.bgm_1, R.raw.bgm_2, R.raw.bgm_3};
    private String fileName;

    public Handler seekBarHandler = new Handler(Looper.getMainLooper());
    public Handler timerHandler = new Handler(Looper.getMainLooper());

    private static final int INIT = 0, PLAYBACK = 1, PAUSE = 2, RESUME = 3;
    private static String RECORDITEM = "RecordItem";

    public static BGMSelectFragment newInstance(Bundle bundle) {
        BGMSelectFragment bgmSelectFragment = new BGMSelectFragment();
        bgmSelectFragment.setArguments(bundle);
        return bgmSelectFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        recordItem = bundle.getParcelable(RECORDITEM);
        return inflater.inflate(R.layout.fragment_bgmselect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        MainActivity.drawableControlImageView.setVisibility(View.INVISIBLE);
        duration = recordItem.duration;
        circleSeekBar.setMaxProcess(duration);


        long itemDuration = (long) duration;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);

        fullTImeTextView.setText(String.format(getString(R.string.timer_format), minutes, seconds));

        bgmSelectAdapter = new BGMSelectAdapter();
        bgmSelectAdapter.addItem(new BGMItem("없음"));
        bgmSelectAdapter.addItem(new BGMItem("잔잔한"));
        bgmSelectAdapter.addItem(new BGMItem("은은한"));
        bgmSelectAdapter.addItem(new BGMItem("몽환적인"));


        bgmSelectAdapter.setOnItemClickListener(new BGMSelectAdapter.OnItemClickListenr() {
            @Override
            public void onItemClick(BGMSelectAdapter.BGMListViewHolder holder, View view, final int position) {
                currentItemPosition = position;
                mediaPlayerStatus = INIT;
                stopItemSetting();
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                if (position != 0) {
                    mediaPlayer = MediaPlayer.create(getContext(), bgms[position]);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayerStatus = PLAYBACK;
                            stopItemSetting();
                        }
                    });
//                mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                }
//                    mediaPlayer.setDataSource(WoolrimApplication.FILE_BASE_URL+String.valueOf(WoolrimApplication.loginedUserId)+"/"+fileName+"_"+String.valueOf(position)+".mp3");

//                Log.d("Name", bgmSelectAdapter.getItem(position).bgmName);
            }
        });

        playButtonIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentItemPosition != -1) {
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
                        case PAUSE:
                            mediaPlayer.pause();
                            stopAndPauseItemSetting();
                            mediaPlayerStatus = RESUME;
                            break;
                        case RESUME:
                            mediaPlayer.start();
                            startItemSetting();
                            mediaPlayerStatus = PAUSE;
                            break;
                    }
                }
            }
        });

        selectCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkStatus.getConnectivityStatus(getContext()) != NetworkStatus.TYPE_NOT_CONNECTED) {
                    if (currentItemPosition == -1) {
                        Toast.makeText(getContext(), "BGM을 선택하여 주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            mediaPlayerStatus = INIT;
                            stopItemSetting();
                        }
                        requestServerForMix();
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.internet_connect_warning), Toast.LENGTH_SHORT).show();
                }
            }
        });
        bgmSelectRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        bgmSelectRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bgmSelectRecyclerView.setAdapter(bgmSelectAdapter);
    }

    @Override
    public void onResume() {
        MainActivity.toolbarLabelTv.setText(R.string.bgm_en);
        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        super.onResume();
    }

    @Override
    public void onDetach() {
        MainActivity.drawableControlImageView.setVisibility(View.VISIBLE);
        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        RecordFragment.isBGM = true;
        super.onDetach();
    }

    private void init(View view) {
        bgmSelectRecyclerView = view.findViewById(R.id.bgmrecyclerview);
        selectCompleteBtn = view.findViewById(R.id.complete_btn);
        playButtonIconIV = view.findViewById(R.id.bgm_play_button_iv);
        circleSeekBar = view.findViewById(R.id.bgm_seek_bar);
        fullTImeTextView = view.findViewById(R.id.bgm_full_time_tv);
        playtimeTextView = view.findViewById(R.id.bgm_playing_time_tv);
        progressCircleIV = view.findViewById(R.id.bgm_progress_circular);
        itemRotate = AnimationUtils.loadAnimation(getContext(), R.anim.item_rotate);
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

                playtimeTextView.setText(String.format(getString(R.string.timer_format), minutes, seconds));
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

    private void stopItemSetting() {
        stopAndPauseItemSetting();
        circleSeekBar.setCurProcess(0);
        playtimeTextView.setText(R.string.default_time);
    }

    private void stopAndPauseItemSetting() {

        seekBarHandler.removeCallbacks(seekBarRunnable);
        timerHandler.removeCallbacks(timeRunnable);

        playButtonIconIV.setImageResource(R.drawable.play_big_icon);
        playtimeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.timer_default_text_color));
    }


    private void startItemSetting() {
        updateSeekBar();
        updateTime();

        playButtonIconIV.setImageResource(R.drawable.record_pause_icon);
        playtimeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_sub_color));
    }

    private void requestServerForMix() {
        progressCircleIV.setVisibility(View.VISIBLE);
        progressCircleIV.setAnimation(itemRotate);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                WoolrimApplication.FILE_BASE_URL + "mix",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        progressCircleIV.setVisibility(View.INVISIBLE);
                        progressCircleIV.clearAnimation();
                        processResponse();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressCircleIV.setVisibility(View.INVISIBLE);
                        progressCircleIV.clearAnimation();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mix_num", String.valueOf(currentItemPosition));
                params.put("stu_id", "123456789");
                params.put("file_name", "WoolRim_1.aac");
                return params;
            }
        };
        WoolrimApplication.requestQueue.add(stringRequest);
    }

    private void processResponse() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECORDITEM, recordItem);
        bundle.putString("SelectedBGM", bgmSelectAdapter.getItem(currentItemPosition).bgmName);
        bundle.putString("SelectedBGMPosition", String.valueOf(currentItemPosition));
        MixesRecordPlayerFragment mixesRecordPlayerFragment = MixesRecordPlayerFragment.newInstance(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, mixesRecordPlayerFragment).addToBackStack("MainFragment").commit();
    }


}

class BGMSelectAdapter extends RecyclerView.Adapter<BGMSelectAdapter.BGMListViewHolder> {
    public List<BGMItem> bgms = new ArrayList<>();
    public int lastSelectedPositon = -1;


    OnItemClickListenr listener;

    public static interface OnItemClickListenr {
        public void onItemClick(BGMListViewHolder holder, View view, int position);
    }


    public BGMSelectAdapter() {
        super();
    }

    public void addItem(BGMItem data) {
        bgms.add(data);
    }

    @NonNull
    @Override
    public BGMListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup test = (ViewGroup) inflater.inflate(R.layout.bgmselect_recyler_item, parent, false);
        return new BGMListViewHolder(test);
    }

    @Override
    public void onBindViewHolder(@NonNull BGMListViewHolder holder, int position) {
        holder.bgmNameTv.setText(bgms.get(position).bgmName);
        holder.setOnItemClickListener(listener);
        holder.checkRadioButton.setChecked(position == lastSelectedPositon);
    }

    @Override
    public int getItemCount() {
        return bgms.size();
    }

    public BGMItem getItem(int position) {
        return bgms.get(position);
    }

    public void setOnItemClickListener(OnItemClickListenr listener) {
        this.listener = listener;
    }


    class BGMListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView bgmNameTv;
        public RadioButton checkRadioButton;
        OnItemClickListenr listener;

        public BGMListViewHolder(View itemView) {
            super(itemView);
            bgmNameTv = itemView.findViewById(R.id.bgmnametextview);
            checkRadioButton = itemView.findViewById(R.id.checkradiobutton);
            checkRadioButton.setClickable(false);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (listener != null) {
                listener.onItemClick(BGMListViewHolder.this, view, position);
                lastSelectedPositon = position;
                notifyDataSetChanged();
            }
        }

        public void setOnItemClickListener(OnItemClickListenr listener) {
            this.listener = listener;
        }

    }

}


