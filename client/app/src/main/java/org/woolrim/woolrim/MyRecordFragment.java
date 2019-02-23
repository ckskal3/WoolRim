package org.woolrim.woolrim;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.woolrim.woolrim.DataItems.MyRecordItem;
import org.woolrim.woolrim.Utils.DialogDismissListener;
import org.woolrim.woolrim.Utils.EmptyRecyclerView;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.Nonnull;


public class MyRecordFragment extends Fragment {

    //    private RecyclerView myRecordRecyclerView;
    private EmptyRecyclerView myRecordRecyclerView;
    private TextView myMenuTextView;
    private ImageView myMenuIconIv;
    private Button myBongsaButton;

    private int fragmentRequestCode = 0;
    private String noItemString ="";
    private ArrayList<MyRecordItem> poemLists, notificationLists;
    public MyRecordAdapter adapter;


    public static MyRecordFragment newInstance(Bundle bundle) {
        MyRecordFragment myRecordFragment = new MyRecordFragment();
        myRecordFragment.setArguments(bundle);
        return myRecordFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;
        fragmentRequestCode = bundle.getInt("RequestCode");
        poemLists = bundle.getParcelableArrayList("PoemList");
        notificationLists = bundle.getParcelableArrayList("NotificationList");
        return inflater.inflate(R.layout.fragment_my_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);


        if (fragmentRequestCode == 101) {
            noItemString = "나의 울림이 없습니다" ;
            myMenuTextView.setText("나의울림");
            myMenuIconIv.setImageResource(R.drawable.my_record_icon);
            myBongsaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //서버 전송 필요
                    ArrayList<String> list = new ArrayList<>();
                    for(MyRecordItem m : adapter.items){
                        if(m.click_flag){
                            list.add(m._id);
                        }
                    }
                    if(list.size()>0) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("FragmentRequestCode", CheckBottomFragment.MY_VOLUNTEER_SCORE_SUBMIT_REQUEST);
                        bundle.putStringArrayList("ApplyRecordingId", list);
                        CheckBottomFragment checkBottomFragment = CheckBottomFragment.newInstance(bundle);
                        checkBottomFragment.setOnDismissListener(new DialogDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                Log.d("Time", "OnDismiss");
                            }

                            @Override
                            public void onDismissed(@Nullable String key, boolean flag) {
                                super.onDismissed(key, flag);

                                if (flag) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "신청되었습니다.", Toast.LENGTH_SHORT).show();
                                            adapter.updateItem();
                                        }
                                    });
                                }
                            }
                        });
                        checkBottomFragment.show(getActivity().getSupportFragmentManager(), "MyRecordFragment");
                    }

                }
            });
        } else {
            noItemString = "울림 알람이 없습니다" ;
            myMenuTextView.setText("울림알람");
            myMenuIconIv.setImageResource(R.drawable.my_alarm_icon);
            myBongsaButton.setVisibility(View.GONE);
        }

        myRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myRecordRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyRecordAdapter(fragmentRequestCode);
        if (fragmentRequestCode == 101) {

            for(MyRecordItem myRecordItem : poemLists){
                adapter.addItem(myRecordItem);
            }

        } else {
            for(MyRecordItem myRecordItem : notificationLists){
                adapter.addItem(myRecordItem);
            }

        }

        Log.d("Size", "" + adapter.items.size());

        myRecordRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        myRecordRecyclerView.setAdapter(adapter);

        myRecordRecyclerView.setEmptyView(view.findViewById(R.id.my_no_item_view),noItemString);


        adapter.setOnItemClickListener(new MyRecordAdapter.OnItemClickListener() {
            @Override
            public void onClick(MyMenuViewHolder myMenuViewHolder, View view, final int position) {
                if (fragmentRequestCode == 101) {
                    switch (view.getId()) {
                        case R.id.play_imageview:
                            MyRecordItem myRecordItem = adapter.getItem(position);
//                            Toast.makeText(getContext(), String.valueOf(position) + "번째 플레이 버튼" + myRecordItem.poem + " " + myRecordItem.poet, Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.delete_imageview:
                            Bundle bundle = new Bundle();
                            bundle.putInt("FragmentRequestCode", CheckBottomFragment.MY_RECORD_DELETE_REQUEST);
                            bundle.putInt("ItemPosition", position);
                            bundle.putString("ItemId",adapter.getItem(position)._id);
                            bundle.putString("ItemPoem", adapter.getItem(position).poem);
                            bundle.putString("ItemPoet", adapter.getItem(position).poet);
                            CheckBottomFragment checkBottomFragment = CheckBottomFragment.newInstance(bundle);
                            checkBottomFragment.setOnDismissListener(new DialogDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {

                                }

                                @Override
                                public void onDismissed(@Nullable String key, boolean flag) {
                                    super.onDismissed(key, flag);
                                    if(flag){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.deleteItem(position, null);
                                            }
                                        });
                                    }
                                }
                            });
                            checkBottomFragment.show(getActivity().getSupportFragmentManager(), "MyRecordFragment");
//                            Toast.makeText(getContext(), String.valueOf(position) + "번째 삭제 버튼", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.my_record_item_background_layout:
                            if(!adapter.getItem(position).auth_flag) {
                                if(adapter.getItem(position).click_flag){
                                    Log.d("Time","Here");
                                    myMenuViewHolder.backgroundLayout.setBackgroundColor(getColor(android.R.color.white));
                                    adapter.getItem(position).click_flag = false;
                                }else{
                                    Log.d("Time","There");
                                    myMenuViewHolder.backgroundLayout.setBackgroundColor(getColor(R.color.bright_gray_color));
                                    adapter.getItem(position).click_flag = true;
                                }
                            }
                            break;
                    }
                } else {
                    switch (view.getId()) {
                        case R.id.delete_imageview:
                            WoolrimApplication.apolloClient.mutate(DeleteNotification.builder().id(adapter.getItem(position)._id).build())
                                    .enqueue(new ApolloCall.Callback<DeleteNotification.Data>() {
                                @Override
                                public void onResponse(@Nonnull Response<DeleteNotification.Data> response) {
                                    if(response.data().deleteNotification()){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.deleteItem(position, null);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(@Nonnull ApolloException e) {

                                }
                            });

                            break;
                    }
                }

            }
        });
    }

    private void init(View view) {
        myMenuTextView = view.findViewById(R.id.my_menu_tv);
        myMenuIconIv = view.findViewById(R.id.my_menu_icon_iv);
        myRecordRecyclerView = view.findViewById(R.id.my_list_recycler);
        myBongsaButton = view.findViewById(R.id.my_bongsa_get_button);
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


class MyRecordAdapter extends RecyclerView.Adapter<MyMenuViewHolder> {

    public interface OnItemClickListener {
        void onClick(MyMenuViewHolder myMenuViewHolder, View view, int position);
    }

    public ArrayList<MyRecordItem> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;
    private int fragmentRequestCode = 0;
    public MyMenuViewHolder myMenuViewHolder;

    public MyRecordAdapter(int fragmentRequestCode) {
        super();
        this.fragmentRequestCode = fragmentRequestCode;
    }

    public void addItem(MyRecordItem data) {
        items.add(data);
    }

    public void deleteItem(int position, String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            file.delete();
        }

        items.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyRecordItem getItem(int position) {
        return items.get(position);
    }

    public void updateItem(){
        for(int i =0 ; i<items.size();i++) {
            if (items.get(i).click_flag) {
                Log.d("Time",String.valueOf(i));
                items.get(i).click_flag = false;
                items.get(i).auth_flag = true;
                notifyItemChanged(i);
            }
        }
    }


    @NonNull
    @Override
    public MyMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.my_record_recycler_item, parent, false);
        return new MyMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMenuViewHolder holder, int position) {
        if (fragmentRequestCode == 101) {
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.verticalBias = 50;
            layoutParams.bottomToBottom = 0;
            layoutParams.startToStart = 0;
            layoutParams.topToTop = 0;
            layoutParams.leftMargin = 20;

            holder.poetTv.setLayoutParams(layoutParams);
            holder.poetTv.setText(items.get(position).poet);
            holder.poemTv.setText(items.get(position).poem);
            if(items.get(position).auth_flag){
                holder.deleteIv.setVisibility(View.INVISIBLE);
                holder.backgroundLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),android.R.color.white));
            }
        } else {
            holder.poetTv.setText(items.get(position).poet);
            holder.dashTv.setVisibility(View.GONE);
            holder.poemTv.setVisibility(View.GONE);
            holder.playIv.setVisibility(View.GONE);
        }

        holder.setOnItemClickListener(onItemClickListener);

        myMenuViewHolder = holder;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
