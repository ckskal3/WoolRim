package org.woolrim.woolrim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PreviewPictureActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView previewPictureIv, drawableControlImageView;
    private TextView toolbarLabelTv, toolbarRightTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_picture);
        init();

        Intent intent = getIntent();
        Uri path = intent.getParcelableExtra("FilePath");
        previewPictureIv.setImageURI(path);

        drawableControlImageView.setImageResource(R.drawable.preview_back_icon);

        drawableControlImageView.setOnClickListener(this);
        toolbarRightTv.setOnClickListener(this);

    }

    private void init(){
        previewPictureIv  = findViewById(R.id.preview_iv);
        drawableControlImageView = findViewById(R.id.toolbar_menu_iv);
        toolbarLabelTv = findViewById(R.id.toolbar_label_tv);
        toolbarRightTv = findViewById(R.id.toolbar_right_tv);

        toolbarLabelTv.setVisibility(View.INVISIBLE);

    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_menu_iv:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.toolbar_right_tv:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
