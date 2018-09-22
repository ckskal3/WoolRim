package org.woolrim.woolrim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyMenuActivity extends AppCompatActivity {
    private ImageView profileIv;
    private TextView userNameTv;
    private RecyclerView myRecordRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymenu);
        init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        myRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyMenuAdapter adapter = new MyMenuAdapter();
        adapter.addItem(new MyRecord("김소월","시1"));
        adapter.addItem(new MyRecord("김소월","시2"));
        adapter.addItem(new MyRecord("김소월","시3"));
        adapter.addItem(new MyRecord("김소월","시4"));
        Log.d("Size",""+adapter.records.size());
        myRecordRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        profileIv = findViewById(R.id.myprofileimageview);
        userNameTv = findViewById(R.id.mynametextview);
        myRecordRecyclerView = findViewById(R.id.my_list_recycler);
    }

}
class MyMenuAdapter extends RecyclerView.Adapter<RecordListViewHolder>{
    public List<MyRecord> records= new ArrayList<>();

    public MyMenuAdapter() {
        super();
    }

    public void addItem(MyRecord data){
        records.add(data);
    }
    @NonNull
    @Override
    public RecordListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup test = (ViewGroup) inflater.inflate(R.layout.myrecord_recycler_item,parent,false);
        return new RecordListViewHolder(test);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListViewHolder holder, int position) {
        holder.poetTv.setText(records.get(position).poet);
        holder.poemTv.setText(records.get(position).poem);

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

}
