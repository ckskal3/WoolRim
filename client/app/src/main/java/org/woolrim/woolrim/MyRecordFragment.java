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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.woolrim.woolrim.Utils.EmptyRecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MyRecordFragment extends Fragment {

//    private RecyclerView myRecordRecyclerView;
    private EmptyRecyclerView myRecordRecyclerView;
    private TextView myMenuTextView;
    private ImageView myMeneIconIv;

    private int fragmentRequestCode = 0;
    public MyMenuAdapter adapter;

    public static MyRecordFragment newInstance(Bundle bundle) {
        MyRecordFragment myRecordFragment = new MyRecordFragment();
        myRecordFragment.setArguments(bundle);
        return myRecordFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        fragmentRequestCode = bundle.getInt("RequestCode");

        return inflater.inflate(R.layout.fragment_my_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        if (fragmentRequestCode == 101) {
            myMenuTextView.setText("나의울림");
            myMeneIconIv.setImageResource(R.drawable.my_record_icon);
        } else {
            myMenuTextView.setText("울림알람");
            myMeneIconIv.setImageResource(R.drawable.my_alarm_icon);
        }

        myRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myRecordRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyMenuAdapter(fragmentRequestCode);
        if (fragmentRequestCode == 101) {
            adapter.addItem(new MyRecordItem("김소월", "시1"));
            adapter.addItem(new MyRecordItem("김소월", "시2"));
            adapter.addItem(new MyRecordItem("김소월", "시3"));
            adapter.addItem(new MyRecordItem("김소월", "시4"));
            adapter.addItem(new MyRecordItem("김소월", "시5"));
        } else {
            adapter.addItem(new MyRecordItem("이런 알람이 있습니다.", null));
            adapter.addItem(new MyRecordItem("저런 알람도 있습니다.", null));
        }

        Log.d("Size", "" + adapter.items.size());

        myRecordRecyclerView.setAdapter(adapter);
        myRecordRecyclerView.setEmptyView(view.findViewById(R.id.my_no_item_view));

        adapter.setOnItemClickListener(new MyMenuAdapter.OnItemClickListener() {
            @Override
            public void onClick(RecordListViewHolder recordListViewHolder, View view, int position) {
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
                            bundle.putString("ItemPoem",adapter.getItem(position).poem);
                            bundle.putString("ItemPoet",adapter.getItem(position).poet);
                            CheckBottomFragment checkBottomFragment = CheckBottomFragment.newInstance(bundle);
                            checkBottomFragment.show(getActivity().getSupportFragmentManager(), "MyRecordFragment");
//                            Toast.makeText(getContext(), String.valueOf(position) + "번째 삭제 버튼", Toast.LENGTH_SHORT).show();
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
        myMeneIconIv = view.findViewById(R.id.my_menu_icon_iv);
        myRecordRecyclerView = view.findViewById(R.id.my_list_recycler);
    }
}


class MyMenuAdapter extends RecyclerView.Adapter<RecordListViewHolder> {

    public interface OnItemClickListener {
        void onClick(RecordListViewHolder recordListViewHolder, View view, int position);
    }

    public ArrayList<MyRecordItem> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;
    private int fragmentRequestCode = 0;

    public MyMenuAdapter(int fragmentRequestCode) {
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


    @NonNull
    @Override
    public RecordListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup test = (ViewGroup) inflater.inflate(R.layout.myrecord_recycler_item, parent, false);
        return new RecordListViewHolder(test);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListViewHolder holder, int position) {
        if (fragmentRequestCode == 101) {
            holder.poetTv.setText(items.get(position).poet);
            holder.poemTv.setText(items.get(position).poem);
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
