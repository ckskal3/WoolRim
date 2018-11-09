package org.woolrim.woolrim;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.woolrim.woolrim.Utils.PermissionDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements FragmentInteraction, View.OnClickListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private LinearLayout homeLayout, favoritesLayout, myPageLayout, signInAndOutLayout;
    private Toolbar toolbar;

    private TextView toolbarRightTv;
    public static TextView toolbarLabelTv, signInAndOutTv, userNameTv;

    private ImageView drawableControlImageView, drawableCloseImageView;
    public static ImageView profileImageView, profileChangeImageView;

    public static String userName;

    public String imageFilePath = null;

    private boolean isLogined = false;

    private Uri photoUri;

    public static int requestCode = 0;

    public interface OnKeyBackPressedListener { //녹음프래그먼트에서 뒤로가기 눌렀을시 다이올로그 띄우기 위한 Listener
        void onBack(int requestCode);
    }


    @Override
    public void onFragmentInteraction(Fragment fragment, String name) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(name).commit();
    }

    @Override
    public void onDialogFragmentInteraction(DialogFragment fragment, String name) {
        fragment.show(getSupportFragmentManager().beginTransaction(), name);
    }

    @Override
    public void onIntentAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 1111);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("state", "OnCreate");


        setContentView(R.layout.activity_main);

        //////////퍼미션 다이얼로그 띄우는 부분////////////////
        PermissionUtils.permission(PermissionConstants.MICROPHONE, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        PermissionDialog.showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        LogUtils.d(permissionsGranted);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            PermissionDialog.showOpenAppSettingDialog();
                        }
                        LogUtils.d(permissionsDeniedForever, permissionsDenied);
                    }
                })
                .request();
        //////////////////////////////////////////////////////

        init();


        setItemClickListener();

        setSupportActionBar(toolbar);

        if(!WoolrimApplication.isLogin) {
            userNameTv.setText(R.string.guest);
            userName = userNameTv.getText().toString();

            profileImageView.setImageResource(R.drawable.profile_icon);
            profileChangeImageView.setVisibility(View.INVISIBLE);
        }




        MainFragment mainFragment = MainFragment.newInstance(new Bundle());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mainFragment)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        WoolrimApplication.isLogin = false;
        super.onDestroy();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        drawableControlImageView = findViewById(R.id.toolbar_menu_iv);
        toolbarLabelTv = findViewById(R.id.toolbar_label_tv);
        toolbarRightTv = findViewById(R.id.toolbar_right_tv);
        toolbarRightTv.setVisibility(View.GONE);

        navigationView = findViewById(R.id.navi_view);
        profileImageView = findViewById(R.id.navi_profile_iv);
        userNameTv = findViewById(R.id.user_name);
        profileChangeImageView = findViewById(R.id.profile_modify_iv);

        homeLayout = findViewById(R.id.navi_home);
        favoritesLayout = findViewById(R.id.navi_favorite);
        myPageLayout = findViewById(R.id.navi_my_page);
        signInAndOutLayout = findViewById(R.id.navi_sign_in_out);
        signInAndOutTv = findViewById(R.id.navi_item_sign_in_out_textview);
        drawableCloseImageView = findViewById(R.id.navi_x_imageview);


    }

    void setItemClickListener() {
        drawableControlImageView.setOnClickListener(this);
        homeLayout.setOnClickListener(this);
        favoritesLayout.setOnClickListener(this);
        myPageLayout.setOnClickListener(this);
        signInAndOutLayout.setOnClickListener(this);
        profileChangeImageView.setOnClickListener(this);
        drawableCloseImageView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int itemId = view.getId();
        if (itemId == R.id.toolbar_menu_iv) {
            if (drawer.isDrawerOpen(Gravity.START)) {
                drawer.closeDrawer(Gravity.START);
            } else {
                drawer.openDrawer(Gravity.START);
            }
        } else {
            switch (view.getId()) {
                case R.id.profile_modify_iv:
                    GallerySelectFragment gallerySelectFragment = GallerySelectFragment.newInstance(new Bundle());
                    gallerySelectFragment.show(getSupportFragmentManager(), "Main");
                    return;
                case R.id.navi_home:
                    if ((getSupportFragmentManager().findFragmentById(R.id.container) instanceof RecordFragment)) {
                        requestCode = WoolrimApplication.REQUSET_HOME;
                        onBackPressed();
                    } else if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                        getSupportFragmentManager().popBackStack("MainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    break;
                case R.id.navi_favorite:
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                    if (fragment instanceof RecordFragment) {
                        requestCode = WoolrimApplication.REQUSET_FAVORITE;
                        onBackPressed();
                    }else if (!(fragment instanceof  MyFavoritesFragment)) {
                        if (WoolrimApplication.isLogin) { // 서버 가져오기

                        }else{ // 내부디비

                        }

                        if(fragment instanceof MyMenuFragment || fragment instanceof LoginFragment){
                            Log.d("Title","here");
                            MyFavoritesFragment myFavoritesFragment = MyFavoritesFragment.newInstance(new Bundle());
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, myFavoritesFragment).commit();
                        }else{
                            MyFavoritesFragment myFavoritesFragment = MyFavoritesFragment.newInstance(new Bundle());
                            getSupportFragmentManager().beginTransaction().addToBackStack("MainFragment").replace(R.id.container, myFavoritesFragment).commit();
                        }
                    }
                    break;
                case R.id.navi_my_page:
                    if ((getSupportFragmentManager().findFragmentById(R.id.container) instanceof RecordFragment)) { // 레코드 프레그먼트일 경우 다이얼로그 띄움
                        requestCode = WoolrimApplication.REQUSET_MY_MENU;
                        onBackPressed();
                    }
                    else if (!WoolrimApplication.isLogin) { // 로그인 안되어있을 경우 로그인 프래그먼트로
                        Bundle bundle = new Bundle();
                        bundle.putInt(getString(R.string.request_code), WoolrimApplication.REQUSET_MY_MENU);
                        LoginFragment loginFragment = LoginFragment.newInstance(bundle);
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction().replace(R.id.container,loginFragment);
                        if(fm.findFragmentById(R.id.container) instanceof  LoginFragment || fm.findFragmentById(R.id.container) instanceof  MyFavoritesFragment){
                            ft.commit();
                        }else{
                            ft.addToBackStack("MainFragment").commit();
                        }
                    } else if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof MyMenuFragment)) { //마이 메뉴인경우 다시 안띄움
                        //서버로 부터 알람내역이랑 자신이 올린 녹음파일에 대한 데이터 가져와야함
//                        getSupportFragmentManager().popBackStack("MainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        Bundle bundle = new Bundle();
                        bundle.putString("UserName",userNameTv.getText().toString().trim());
                        MyMenuFragment myMenuFragment = MyMenuFragment.newInstance(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myMenuFragment)
                                .addToBackStack("MainFragment")
                                .commit();
                    }
                    break;
                case R.id.navi_sign_in_out:
                    if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof LoginFragment)) { //로그인 프래그먼트가 아닐경우
                        if(getSupportFragmentManager().findFragmentById(R.id.container) instanceof RecordFragment){
                            requestCode = WoolrimApplication.REQUSET_RECORD_LOGOUT;
                            onBackPressed();
                        }
                        else if (WoolrimApplication.isLogin) { //로그인 상태인 경우 -> 로그아웃해야됨 홈으로 보냄
                            signInAndOutTv.setText(R.string.login_kr);
                            profileImageView.setImageResource(R.drawable.profile_icon);
                            userNameTv.setText(R.string.guest);
                            profileChangeImageView.setVisibility(View.INVISIBLE);
                            getSupportFragmentManager().popBackStack("MainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            WoolrimApplication.isLogin = false;
                        } else { //로그아웃 상태인경우
                            Bundle bundle = new Bundle();
                            bundle.putInt(getString(R.string.request_code), WoolrimApplication.REQUSET_MAIN_ACTIVITY);
                            LoginFragment loginFragment = LoginFragment.newInstance(bundle);
                            if(getSupportFragmentManager().findFragmentById(R.id.container) instanceof SignUpFragment){
                                getSupportFragmentManager().popBackStack();
                            }else{
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, loginFragment)
                                        .addToBackStack("MainFragment")
                                        .commit();
                            }

                        }
                    }
                    break;

                case R.id.navi_x_imageview:
                    break;
            }
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1111:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        File tempFile = null;
                        tempFile = createGalleryPicture();

                        photoUri = data.getData();
                        Uri tempUri = Uri.fromFile(tempFile);

                        Log.d("photoUri : " + photoUri.toString(), " tempUri : " + tempUri.toString());
                        //미리보기 화면/////
                        previewPicture(photoUri);
                        ////////////////////
                    }
                }
                break;
            case 2222:
                if (resultCode == RESULT_OK) {
                    String realPath = getRealPathFromURI(photoUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(realPath);
                    if (bitmap.getHeight() > 300 || bitmap.getWidth() > 300) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    }
                    MainActivity.profileImageView.setImageBitmap(bitmap);
                } else {
                    onIntentAction();
                }
                break;
        }
    }

    private void previewPicture(Uri realPath) {
        Intent intent = new Intent(this, PreviewPictureActivity.class);
        intent.putExtra("FilePath", realPath);
        startActivityForResult(intent, 2222);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }


    private File createGalleryPicture() {
        String tempName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());
        String fileName = "IMG" + tempName + ".jpg";
        File tempFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "Temp");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        tempFile = new File(storageDir, fileName);
        imageFilePath = tempFile.getAbsolutePath();

        return tempFile;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Fragment checkRecordFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        boolean flag1 = checkRecordFragment instanceof RecordFragment;
        boolean flag2 = drawer.isDrawerOpen(GravityCompat.START);
        if (flag1 && flag2) {
            drawer.closeDrawer(GravityCompat.START);
            ((RecordFragment) checkRecordFragment).onBack(requestCode);
        } else if (flag1) {
            ((RecordFragment) checkRecordFragment).onBack(requestCode);
        } else if (flag2) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void request(String urlString) {
        /*
        query getAllUserQuery { getAllUser{idnamestu_idgenderpasswdcreatedbongsa_time}}
         */
        final String body = "query { getAllUser{ id name stu_id gender passwd created bongsa_time } }";
        final String body1 = "mutation { createUser(input: {name:\"조수근\" stu_id:201201548 gender:\"남\" passwd:\"123456\"})}";


        StringRequest request = new StringRequest(
                Request.Method.POST,
                urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        println("에러 -> " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("query", body1);
                return params;
            }
        };

        request.setShouldCache(false);
        WoolrimApplication.requestQueue.add(request);
    }


}
