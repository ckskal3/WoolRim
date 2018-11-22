package org.woolrim.woolrim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.woolrim.woolrim.DataItems.RecordItem;

import java.util.ArrayList;

public class SinglePlayerActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView  drawableControlImageView;
    private TextView toolbarLabelTv, toolbarRightTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        init();
        Intent intent = getIntent();
        ArrayList<RecordItem> items = intent.getParcelableArrayListExtra("Data");

        toolbarLabelTv.setText(items.get(0).poemName);

        toolbarRightTv.setVisibility(View.INVISIBLE);
        drawableControlImageView.setImageResource(R.drawable.preview_back_icon);
        drawableControlImageView.setOnClickListener(this);


        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Data",items);
        PlayerFragmentTemp playerFragmentTemp = PlayerFragmentTemp.newInstance(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.single_player_container,playerFragmentTemp).commit();

    }

    private void init(){
        drawableControlImageView = findViewById(R.id.toolbar_menu_iv);
        toolbarLabelTv = findViewById(R.id.toolbar_label_tv);
        toolbarRightTv = findViewById(R.id.toolbar_right_tv);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
