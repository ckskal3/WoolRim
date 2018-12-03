package org.woolrim.woolrim;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bumptech.glide.Glide;

import org.woolrim.woolrim.DataItems.MyFavoritesItem;
import org.woolrim.woolrim.DataItems.RecordItem;
import org.woolrim.woolrim.Utils.DBManagerHelper;
import org.woolrim.woolrim.type.CreateBookmarkInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import de.hdodenhof.circleimageview.CircleImageView;
import io.feeeei.circleseekbar.CircleSeekBar;

public class PlayerFragmentTemp extends Fragment implements View.OnTouchListener, View.OnClickListener {

    private ArrayList<RecordItem> items = new ArrayList<>();
    private RecordItem[] itemArray;

    private int itemSize, currentPage = 0;
    private int[] indicatorId = {R.id.indicator1, R.id.indicator2, R.id.indicator3, R.id.indicator4};

    private CircleSeekBar circleSeekBar;
    private CircleImageView userProfileIV;
    private ImageView playBtnBackIV, playIconBackIV, favoriteIconIV;
    private TextView fullTimeTextView, playingTimeTextView, userNameTextView;
    private View[] indicator;

    private ConstraintLayout playerBackgroundLayout;

    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false, isPause = false, isRestartAndResume = false;
    int bookmarkFlag, bookmarkPosition;

    public Handler seekBarHandler = new Handler(Looper.getMainLooper());
    public Handler timerHandler = new Handler(Looper.getMainLooper());

    public final int HORIZONTAL_MIN_DISTANCE = 150;
    private static final String logTag = "SwipeDetector";
    private float downX, downY, upX, upY;
    private Action mSwipeDetected = Action.None;

    public enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }


    public static PlayerFragmentTemp newInstance(Bundle bundle) {
        PlayerFragmentTemp playerFragmentTemp = new PlayerFragmentTemp();
        playerFragmentTemp.setArguments(bundle);
        return playerFragmentTemp;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;
        items = bundle.getParcelableArrayList("Data");
        bookmarkPosition = bundle.getInt("BookmarkPosition", -1);
        assert items != null;
        itemSize = items.size();
        itemArray = new RecordItem[itemSize];
        indicator = new View[itemSize];
        int i = 0;
        for (RecordItem item : items) {
            itemArray[i] = item;
            i++;
        }
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        init(view);

        setItem(currentPage);

        setOnClick();

        playerBackgroundLayout.setOnTouchListener(this);

//        circleSeekBar.setMaxProcess(10000);


    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.toolbarLabelTv.setText(items.get(0).poemName);
    }

    @Override
    public void onDetach() {//Fragment stack에서 빠질때(?)
        Log.d("Time1", "onDetach");
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
//        updateBookmark();

        super.onDetach();
    }

    @Override
    public void onPause() {//다른 화면 전환시
        Log.d("Time1", "onPause");
        updateBookmark();
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.player_favorite_icon_iv:
                if (bookmarkFlag == 1) {
                    favoriteIconIV.setImageResource(R.drawable.favorite_middle_empty_color_icon);
                    bookmarkFlag = 0;
                } else {
                    favoriteIconIV.setImageResource(R.drawable.favorite_middle_color_icon);
                    bookmarkFlag = 1;
                }
                break;
            default:
                if (isPlaying) {
                    onPauseBtnClick();
                } else {
                    onStartBtnClick();
                }
                break;
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = motionEvent.getX();
                downY = motionEvent.getY();
                mSwipeDetected = Action.None;
                return false; // allow other events like Click to be processed
            }
            case MotionEvent.ACTION_MOVE: {
                upX = motionEvent.getX();
                upY = motionEvent.getY();

                float deltaX = downX - upX;
                // horizontal swipe detection
                if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        mSwipeDetected = Action.LR;
                        return true;
                    }
                    if (deltaX > 0) {
                        mSwipeDetected = Action.RL;
                        return true;
                    }
                }
                mSwipeDetected = Action.None;
                return true;
            }
        }
        if (itemSize > 1) {
            if (mSwipeDetected == Action.RL && currentPage != (itemSize - 1)) {
                Log.i(logTag, "우에서 좌");
                stopMediaAndTimer();
                updateBookmark();
                indicator[currentPage].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.default_indicator));
                itemArray[currentPage].bookmarkFlag = bookmarkFlag;
                currentPage = ++currentPage % itemSize;
                setItem(currentPage);
            } else if (mSwipeDetected == Action.LR && currentPage != 0) {
                Log.i(logTag, "좌에서 우");
                stopMediaAndTimer();
                updateBookmark();
                indicator[currentPage].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.default_indicator));
                itemArray[currentPage].bookmarkFlag = bookmarkFlag;
                --currentPage;
                currentPage = currentPage < 0 ? itemSize - 1 : currentPage;
                setItem(currentPage);
            }
        }

        return false;
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
                Log.d("Time", String.valueOf(mCurrentPosition));
                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);

                playingTimeTextView.setText(String.format(getString(R.string.timer_format), minutes, seconds));
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

    private void init(View view) {
        playerBackgroundLayout = view.findViewById(R.id.player_background_layout);
        userNameTextView = view.findViewById(R.id.player_user_name_tv);
        userProfileIV = view.findViewById(R.id.player_user_profile_iv);
        favoriteIconIV = view.findViewById(R.id.player_favorite_icon_iv);
        playBtnBackIV = view.findViewById(R.id.play_button_background_imageview);
        playIconBackIV = view.findViewById(R.id.play_button_icon_imageview);
        circleSeekBar = view.findViewById(R.id.player_circle_seek_bar);
        fullTimeTextView = view.findViewById(R.id.player_full_time_textview);
        playingTimeTextView = view.findViewById(R.id.player_playing_time_textview);

        for (int i = 0; i < itemSize; i++) {
            indicator[i] = view.findViewById(indicatorId[i]);
            indicator[i].setVisibility(View.VISIBLE);
        }

    }

    private void setItem(int currentPage) {
        bookmarkFlag = itemArray[currentPage].bookmarkFlag;
        userNameTextView.setText(itemArray[currentPage].studentName);
        if (itemArray[currentPage].studentProfilePath == null || itemArray[currentPage].studentProfilePath.equals(getString(R.string.no_profile_en))) {
            Glide.with(this).load(R.drawable.profile_icon).into(userProfileIV);
        } else {
            Glide.with(this).load(WoolrimApplication.FILE_BASE_URL + itemArray[currentPage].studentProfilePath).into(userProfileIV);
        }
        if (itemArray[currentPage].bookmarkFlag == 1) {
            favoriteIconIV.setImageResource(R.drawable.favorite_middle_color_icon);
        } else {
            favoriteIconIV.setImageResource(R.drawable.favorite_middle_empty_color_icon);
        }
        indicator[currentPage].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.selected_indicator));
        setDuration(itemArray[currentPage].duration);
        circleSeekBar.setMaxProcess(itemArray[currentPage].duration);
        circleSeekBar.setCurProcess(0);
        playingTimeTextView.setText(getString(R.string.default_time));
        playingTimeTextView.setTextColor(getColor(R.color.timer_default_text_color));
        playIconBackIV.setImageResource(R.drawable.play_big_icon);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    private void stopMediaAndTimer() {
        isPlaying = false;
        isRestartAndResume = false;
        if (mediaPlayer != null || mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        timerHandler.removeCallbacks(timeRunnable);
        seekBarHandler.removeCallbacks(seekBarRunnable);
    }

    private void setDuration(int duration) {
//        Log.d("Time",String.valueOf(mediaPlayer.getDuration()));
        long itemDuration = duration;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(itemDuration)
                - TimeUnit.MINUTES.toSeconds(minutes);
        fullTimeTextView.setText(String.format(getString(R.string.timer_format), minutes, seconds));
    }

    private void updateBookmark() {
        Log.d("Flag", String.valueOf(bookmarkFlag) + " " + String.valueOf(itemArray[currentPage].bookmarkFlag));
        if (bookmarkFlag != itemArray[currentPage].bookmarkFlag) { //이전상태와 지금상태가 다를때
            if (WoolrimApplication.isLogin) {//로그인시
                requestServerForBookmark(bookmarkFlag);
            } else {//비로그인시
                itemArray[currentPage].bookmarkFlag = bookmarkFlag;
                MyFavoritesItem myFavoritesItem = new MyFavoritesItem(
                        String.valueOf(itemArray[currentPage].mediaId),
                        String.valueOf(itemArray[currentPage].poemId),
                        String.valueOf(itemArray[currentPage].studentId),
                        itemArray[currentPage].poemName,
                        itemArray[currentPage].studentName,
                        "Guest"
                );
                DBManagerHelper.favoriteDAO.updateFavorite(myFavoritesItem, bookmarkFlag);
                if (bookmarkFlag == 1 && bookmarkPosition != -1) {
                    MyFavoritesFragment.myFavoritesAdapter.addItem(myFavoritesItem);
                } else if (bookmarkFlag == 0 && bookmarkPosition != -1) {
                    MyFavoritesFragment.myFavoritesAdapter.deleteItem(bookmarkPosition);
                }
            }
        }
    }

    private void requestServerForBookmark(int bookmarkFlag) {
        if (bookmarkFlag == 1) {
            WoolrimApplication.apolloClient.mutate(CreateBookMark
                    .builder()
                    .input(CreateBookmarkInput
                            .builder()
                            .user_id(String.valueOf(WoolrimApplication.loginedUserPK))
                            .recording_id(String.valueOf(itemArray[currentPage].mediaId))
                            .build())
                    .build())
                    .enqueue(new ApolloCall.Callback<CreateBookMark.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<CreateBookMark.Data> response) {
                            if (response.data().createBookmark().isSuccess()) {
                                if (bookmarkPosition != -1) {
                                    MyFavoritesFragment.myFavoritesAdapter.addItem(new MyFavoritesItem(
                                            String.valueOf(itemArray[currentPage].mediaId),
                                            String.valueOf(itemArray[currentPage].poemId),
                                            String.valueOf(itemArray[currentPage].studentId),
                                            itemArray[currentPage].poemName,
                                            itemArray[currentPage].studentName,
                                            WoolrimApplication.loginedUserName
                                    ));
                                }
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {

                        }
                    });
        } else {
            WoolrimApplication.apolloClient.mutate(DeleteBookMark.builder()
                    .recording_id(String.valueOf(itemArray[currentPage].mediaId))
                    .user_id(String.valueOf(itemArray[currentPage].studentId))
                    .build())
                    .enqueue(new ApolloCall.Callback<DeleteBookMark.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<DeleteBookMark.Data> response) {
                            if (response.data().deleteBookmark().isSuccess()) {
                                if (bookmarkPosition != -1) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MyFavoritesFragment.myFavoritesAdapter.deleteItem(bookmarkPosition);

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {

                        }
                    });
        }
    }

    private void setOnClick() {
        playBtnBackIV.setOnClickListener(this);
        playIconBackIV.setOnClickListener(this);
        favoriteIconIV.setOnClickListener(this);
    }

    public void onStartBtnClick() {
        if (!isRestartAndResume) {
            try {
                mediaPlayer.setDataSource(itemArray[currentPage].filePath);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                playIconBackIV.setImageResource(R.drawable.play_big_icon);
                                isPlaying = false;
                                isPause = false;
                                isRestartAndResume = true;
                                seekBarHandler.removeCallbacks(seekBarRunnable);
                                timerHandler.removeCallbacks(timeRunnable);

                                circleSeekBar.setCurProcess(mediaPlayer.getDuration());
                            }
                        });

                        updateTime();
                        updateSeekBar();

                        setDuration(mediaPlayer.getDuration());

                        mediaPlayer.start();

                        if (!isPause) {
                            playingTimeTextView.setText(getString(R.string.default_time));
                        }
                        isPlaying = true;
                        isPause = false;
                        playIconBackIV.setImageResource(R.drawable.pause_icon);
                        playingTimeTextView.setTextColor(getColor(R.color.app_sub_color));

                        Toast.makeText(getContext(), "재생", Toast.LENGTH_LONG).show();

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            updateTime();
            updateSeekBar();

            mediaPlayer.start();

            if (!isPause) {
                playingTimeTextView.setText(getString(R.string.default_time));
            }
            isPlaying = true;
            isPause = false;
            playIconBackIV.setImageResource(R.drawable.pause_icon);
            playingTimeTextView.setTextColor(getColor(R.color.app_sub_color));

            Toast.makeText(getContext(), "재생", Toast.LENGTH_LONG).show();
        }


    }

    public void onPauseBtnClick() {
        seekBarHandler.removeCallbacks(seekBarRunnable);
        timerHandler.removeCallbacks(timeRunnable);
        Toast.makeText(getContext(), "일시정지", Toast.LENGTH_LONG).show();
        mediaPlayer.pause();

        playIconBackIV.setImageResource(R.drawable.play_big_icon);

        playingTimeTextView.setTextColor(getColor(R.color.timer_default_text_color));
        isPlaying = false;
        isPause = true;
        isRestartAndResume = true;


    }

    private int getColor(int colorId) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return getResources().getColor(colorId, null);
        } else {
            return getResources().getColor(colorId);
        }
    }

}
