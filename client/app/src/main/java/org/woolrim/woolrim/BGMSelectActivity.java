package org.woolrim.woolrim;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BGMSelectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgmselect);

        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.bgmrecyclerview);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        final BGMSelectAdapter bgmSelectAdapter = new BGMSelectAdapter();
        bgmSelectAdapter.addItem(new BGM("없음"));
        bgmSelectAdapter.addItem(new BGM("잔잔한"));
        bgmSelectAdapter.addItem(new BGM("은은한"));
        bgmSelectAdapter.addItem(new BGM("몽환적인"));


        bgmSelectAdapter.setOnItemClickListener(new BGMSelectAdapter.OnItemClickListenr() {
            @Override
            public void onItemClick(BGMSelectAdapter.BGMListViewHolder holder, View view, int position) {
                Log.d("Name",bgmSelectAdapter.getItem(position).bgmName);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bgmSelectAdapter);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

class BGMSelectAdapter extends RecyclerView.Adapter<BGMSelectAdapter.BGMListViewHolder> {
    public List<BGM> bgms = new ArrayList<>();
    public int lastSelectedPositon = -1;

    OnItemClickListenr listener;

    public static interface OnItemClickListenr {
        public void onItemClick(BGMListViewHolder holder, View view, int position);
    }


    public BGMSelectAdapter() {
        super();
    }

    public void addItem(BGM data) {
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

    public BGM getItem(int position) {
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


