package org.woolrim.woolrim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.woolrim.woolrim.DataItems.BGMItem;

import java.util.ArrayList;
import java.util.List;

public class BGMSelectFragment extends Fragment {

    private RecyclerView bgmSelectRecyclerView;
    private Button selectCompleteBtn;
    private BGMSelectAdapter bgmSelectAdapter;


    public static BGMSelectFragment newInstance(Bundle bundle) {
        BGMSelectFragment bgmSelectFragment = new BGMSelectFragment();
        bgmSelectFragment.setArguments(bundle);
        return bgmSelectFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bgmselect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        bgmSelectAdapter = new BGMSelectAdapter();
        bgmSelectAdapter.addItem(new BGMItem("없음"));
        bgmSelectAdapter.addItem(new BGMItem("잔잔한"));
        bgmSelectAdapter.addItem(new BGMItem("은은한"));
        bgmSelectAdapter.addItem(new BGMItem("몽환적인"));


        bgmSelectAdapter.setOnItemClickListener(new BGMSelectAdapter.OnItemClickListenr() {
            @Override
            public void onItemClick(BGMSelectAdapter.BGMListViewHolder holder, View view, int position) {
                Log.d("Name", bgmSelectAdapter.getItem(position).bgmName);
            }
        });
        selectCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("FragmentRequestCode",CheckBottomFragment.MY_RECORD_SUBMIT_REQUEST);
                CheckBottomFragment checkBottomFragment = CheckBottomFragment.newInstance(bundle);
                checkBottomFragment.show(getActivity().getSupportFragmentManager(),"BGMSelectFragment");
            }
        });
        bgmSelectRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        bgmSelectRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bgmSelectRecyclerView.setAdapter(bgmSelectAdapter);
    }


    private void init(View view) {
        bgmSelectRecyclerView = view.findViewById(R.id.bgmrecyclerview);
        selectCompleteBtn = view.findViewById(R.id.complete_btn);
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


