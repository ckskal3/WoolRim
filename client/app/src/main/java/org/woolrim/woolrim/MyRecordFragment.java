package org.woolrim.woolrim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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

import org.woolrim.woolrim.DataItems.MyRecordItem;
import org.woolrim.woolrim.DataItems.PoemAndPoetItem;
import org.woolrim.woolrim.Utils.EmptyRecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MyRecordFragment extends Fragment {

    //    private RecyclerView myRecordRecyclerView;
    private EmptyRecyclerView myRecordRecyclerView;
    private TextView myMenuTextView;
    private ImageView myMenuIconIv;
    private Button myBongsaButton;

    private int fragmentRequestCode = 0;
    private ArrayList<MyRecordItem> poemLists;
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
        return inflater.inflate(R.layout.fragment_my_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        if (fragmentRequestCode == 101) {
            myMenuTextView.setText("나의울림");
            myMenuIconIv.setImageResource(R.drawable.my_record_icon);
            myBongsaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"신청되었습니다.",Toast.LENGTH_SHORT).show();
                    //서버 전송 필요
                    for(MyRecordItem m : adapter.items){
                        if(m.click_flag){

                        }
                    }
                    adapter.updateItem();
                }
            });
        } else {
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
//            adapter.addItem(new MyRecordItem(null,"김소월", "시1", false));
//            adapter.addItem(new MyRecordItem(null,"김소월", "시2", false));
//            adapter.addItem(new MyRecordItem(null,"김소월", "시3", false));
//            adapter.addItem(new MyRecordItem(null,"김소월", "시4", true));
//            adapter.addItem(new MyRecordItem(null,"김소월", "시5", false));
        } else {
            adapter.addItem(new MyRecordItem(null,"이런 알람이 있습니다.", null, true));
            adapter.addItem(new MyRecordItem(null,"저런 알람도 있습니다.", null, true));
        }

        Log.d("Size", "" + adapter.items.size());

        myRecordRecyclerView.setAdapter(adapter);
        myRecordRecyclerView.setEmptyView(view.findViewById(R.id.my_no_item_view));



        adapter.setOnItemClickListener(new MyRecordAdapter.OnItemClickListener() {
            @Override
            public void onClick(MyMenuViewHolder myMenuViewHolder, View view, int position) {
                if (fragmentRequestCode == 101) {
                    switch (view.getId()) {
                        case R.id.playimageview:
                            MyRecordItem myRecordItem = adapter.getItem(position);
                            Toast.makeText(getContext(), String.valueOf(position) + "번째 플레이 버튼" + myRecordItem.poem + " " + myRecordItem.poet, Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.deleteimageview:
                            Bundle bundle = new Bundle();
                            bundle.putInt("FragmentRequestCode", CheckBottomFragment.MY_RECORD_DELETE_REQUEST);
                            bundle.putInt("ItemPosition", position);
                            bundle.putString("ItemPoem", adapter.getItem(position).poem);
                            bundle.putString("ItemPoet", adapter.getItem(position).poet);
                            CheckBottomFragment checkBottomFragment = CheckBottomFragment.newInstance(bundle);
                            checkBottomFragment.show(getActivity().getSupportFragmentManager(), "MyRecordFragment");
//                            Toast.makeText(getContext(), String.valueOf(position) + "번째 삭제 버튼", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.my_record_item_background_layout:
                            if(!adapter.getItem(position).auth_flag) {
                                if(adapter.getItem(position).click_flag){
                                    myMenuViewHolder.backgroundLayout.setBackgroundColor(android.R.color.white);
                                    adapter.getItem(position).click_flag = false;
                                }else{
                                    myMenuViewHolder.backgroundLayout.setBackgroundColor(R.color.gray_bar_color);
                                    adapter.getItem(position).click_flag = true;
                                }
                            }
                            break;
                    }
                } else {
                    switch (view.getId()) {
                        case R.id.deleteimageview:
                            adapter.deleteItem(position, null);
                            Toast.makeText(getContext(), String.valueOf(position) + "번째 삭제 버튼", Toast.LENGTH_SHORT).show();
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
}


class MyRecordAdapter extends RecyclerView.Adapter<MyMenuViewHolder> {

    public interface OnItemClickListener {
        void onClick(MyMenuViewHolder myMenuViewHolder, View view, int position);
    }

    public ArrayList<MyRecordItem> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;
    private int fragmentRequestCode = 0;

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
            holder.poetTv.setText(items.get(position).poet);
            holder.poemTv.setText(items.get(position).poem);
            if(items.get(position).auth_flag){
                holder.deleteIv.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.poetTv.setText(items.get(position).poet);
            holder.dashTv.setVisibility(View.GONE);
            holder.poemTv.setVisibility(View.GONE);
            holder.playIv.setVisibility(View.GONE);
        }

        holder.setOnItemClickListener(onItemClickListener);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
