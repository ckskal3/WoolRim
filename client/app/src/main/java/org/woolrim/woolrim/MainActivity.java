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
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.exception.ApolloException;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.bumptech.glide.Glide;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.woolrim.woolrim.DataItems.MyRecordItem;
import org.woolrim.woolrim.Utils.PermissionDialog;
import org.woolrim.woolrim.type.Status;
import org.woolrim.woolrim.type.UpdateUserInput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import static com.android.volley.Request.Method.POST;


public class MainActivity extends AppCompatActivity implements FragmentInteraction, View.OnClickListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private LinearLayout homeLayout, favoritesLayout, myPageLayout, signInAndOutLayout;
    private Toolbar toolbar;

    private TextView toolbarRightTv;
    public static TextView toolbarLabelTv, signInAndOutTv, userNameTv;

    private ImageView  drawableCloseImageView;
    public static ImageView profileImageView, profileChangeImageView, drawableControlImageView;

    public static String userName;

    public String imageFilePath = null, imageFileName = null;

    private boolean isLogined = false, profileUploadFlag = false;

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

//       DBManagerHelper.SQLiteDatabaseHelper.recycleTable();

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

        if (!WoolrimApplication.isLogin) {
            userNameTv.setText(R.string.guest);
            userName = userNameTv.getText().toString();

            Glide.with(this).load(R.drawable.profile_icon).into(profileImageView);
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
            FragmentManager currentFragmentManager = getSupportFragmentManager();
            final FragmentTransaction currentFragmentTransaction = currentFragmentManager.beginTransaction();
            Fragment currentFragment = currentFragmentManager.findFragmentById(R.id.container);

            switch (view.getId()) {
                // currentFragment 가  RecordFragment 일때는 onBackPressed
                // 자기자신, PlayerFragment, 메뉴에 있는 Fragment 일때는 addToBackStack 안함
                case R.id.profile_modify_iv:
                    GallerySelectFragment gallerySelectFragment = GallerySelectFragment.newInstance(new Bundle());
                    gallerySelectFragment.show(currentFragmentManager, "Main");
                    return;

                case R.id.navi_home:
                    if (currentFragment instanceof RecordFragment) {
                        requestCode = WoolrimApplication.REQUSET_HOME;
                        onBackPressed();
                    } else if (currentFragmentManager.getBackStackEntryCount() != 0) {
                        currentFragmentManager.popBackStack("MainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    break;

                case R.id.navi_favorite:
                    if (currentFragment instanceof RecordFragment) {
                        requestCode = WoolrimApplication.REQUSET_FAVORITE;
                        onBackPressed();
                    } else if (!(currentFragment instanceof MyFavoritesFragment)) {
                        processFavoriteFragmentChange(currentFragment, new Bundle());
                    }
                    break;

                case R.id.navi_my_page:
                    if (currentFragment instanceof RecordFragment) { // 레코드 프레그먼트일 경우 다이얼로그 띄움
                        requestCode = WoolrimApplication.REQUSET_MY_MENU;
                        onBackPressed();
                    } else if (!WoolrimApplication.isLogin) { // 로그인 안되어있을 경우 로그인 프래그먼트로
                        Bundle bundle = new Bundle();
                        bundle.putInt(getString(R.string.request_code), WoolrimApplication.REQUSET_MY_MENU);
                        LoginFragment loginFragment = LoginFragment.newInstance(bundle);
                        FragmentTransaction ft = currentFragmentTransaction.replace(R.id.container, loginFragment);

                        if (currentFragment instanceof LoginFragment || currentFragment instanceof MyFavoritesFragment || currentFragment instanceof PlayerFragmentTemp) {
                            ft.commit();
                        } else {
                            ft.addToBackStack("MainFragment").commit();
                        }
                    } else if (!(currentFragment instanceof MyMenuFragment)) { //마이 메뉴인경우 다시 안띄움
                        //서버로 부터 알람내역이랑 자신이 올린 녹음파일에 대한 데이터 가져와야함
                        if (WoolrimApplication.isTest) {//테스트용 지워야함
                            testMyMenuFragmentChange(currentFragment, currentFragmentTransaction);
                        } else {
                            processMyMenuFragmentChange(currentFragment, currentFragmentTransaction);
                        }
                    }
                    break;
                case R.id.navi_sign_in_out:
                    if (!(currentFragment instanceof LoginFragment)) { //로그인 프래그먼트가 아닐경우
                        if (currentFragment instanceof RecordFragment) {
                            requestCode = WoolrimApplication.REQUSET_RECORD_LOGOUT;
                            onBackPressed();
                        } else if (WoolrimApplication.isLogin) { //로그인 상태인 경우 -> 로그아웃해야됨 홈으로 보냄
                            signInAndOutTv.setText(R.string.login_kr);
                            profileImageView.setImageResource(R.drawable.profile_icon);
                            userNameTv.setText(R.string.guest);
                            profileChangeImageView.setVisibility(View.INVISIBLE);
                            currentFragmentManager.popBackStack("MainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            WoolrimApplication.userInfoReset();

                        } else { //로그아웃 상태인경우
                            Bundle bundle = new Bundle();
                            bundle.putInt(getString(R.string.request_code), WoolrimApplication.REQUSET_MAIN_ACTIVITY);
                            LoginFragment loginFragment = LoginFragment.newInstance(bundle);
                            if (currentFragment instanceof SignUpFragment) {
                                currentFragmentManager.popBackStack();
                            } else if (currentFragment instanceof PlayerFragmentTemp) {
                                currentFragmentTransaction.replace(R.id.container, loginFragment)
                                        .commit();
                            } else {
                                currentFragmentTransaction.replace(R.id.container, loginFragment)
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
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1111:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        photoUri = data.getData();

                        //미리보기 화면/////
                        previewPicture(photoUri);
                        ////////////////////
                    }
                }
                break;
            case 2222:
                if (resultCode == RESULT_OK) {
                    final String realPath = getRealPathFromURI(photoUri);
                    final Bitmap[] bitmap = new Bitmap[1];
                    bitmap[0] = BitmapFactory.decodeFile(realPath);
                    if (bitmap[0].getHeight() > 300 || bitmap[0].getWidth() > 300) {
                        bitmap[0] = Bitmap.createScaledBitmap(bitmap[0], 300, 300, true);
                    }
                    File fixedBitmapFile = createGalleryPicture();
                    OutputStream out = null;

                    try {
                        fixedBitmapFile.createNewFile();
                        out = new FileOutputStream(fixedBitmapFile);

                        bitmap[0].compress(Bitmap.CompressFormat.JPEG, 100, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    SimpleMultiPartRequest smr = new SimpleMultiPartRequest(
                            POST,
                            WoolrimApplication.FILE_BASE_URL + getString(R.string.upload_en),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Time", "onResponse " + response);

                                    if (response.equals("success")) {
                                        requestUpdateUserProfile(bitmap[0]);
                                    }
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    smr.addStringParam("stu_id", String.valueOf(WoolrimApplication.loginedUserId));
                    smr.addFile("user_recording", imageFilePath);

                    WoolrimApplication.requestQueue.add(smr);

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
        imageFileName = fileName;
        File tempFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "Temp");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        tempFile = new File(storageDir, fileName);
        imageFilePath = tempFile.getAbsolutePath();

        return tempFile;
    }

    private void requestUpdateUserProfile(final Bitmap bitmap) {
        WoolrimApplication.apolloClient.mutate(UpdateUserProfile.builder().input(
                UpdateUserInput.builder()
                        .id(WoolrimApplication.loginedUserPK)
                        .name(WoolrimApplication.loginedUserName)
                        .profile(String.valueOf(WoolrimApplication.loginedUserId) + "/" + imageFileName)
                        .build())
                .build())
                .enqueue(new ApolloCall.Callback<UpdateUserProfile.Data>() {
                    @Override
                    public void onResponse(@Nonnull com.apollographql.apollo.api.Response<UpdateUserProfile.Data> response) {
                        if (response.data().modifyUser().isSuccess()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("Time", "here");
                                    Glide.with(getApplicationContext()).load(WoolrimApplication.FILE_BASE_URL + String.valueOf(WoolrimApplication.loginedUserId) + "/" + imageFileName).into(profileImageView);
                                }
                            });
                            File file = new File(imageFilePath);
                            file.delete();
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
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

    private void processFavoriteFragmentChange(Fragment fragment, Bundle bundle) {
        if (fragment instanceof MyMenuFragment || fragment instanceof LoginFragment || fragment instanceof PlayerFragmentTemp) {
            Log.d("Title", "here");
            MyFavoritesFragment myFavoritesFragment = MyFavoritesFragment.newInstance(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, myFavoritesFragment).commit();
        } else {
            MyFavoritesFragment myFavoritesFragment = MyFavoritesFragment.newInstance(bundle);
            getSupportFragmentManager().beginTransaction().addToBackStack("MainFragment").replace(R.id.container, myFavoritesFragment).commit();
        }
    }

    private void testMyMenuFragmentChange(Fragment currentFragment, FragmentTransaction currentFragmentTransaction) {
        Bundle bundle = new Bundle();

        ArrayList<MyRecordItem> myRecordItem = new ArrayList<>();
        myRecordItem.add(new MyRecordItem("1", "시인", "시1", false));
        myRecordItem.add(new MyRecordItem("2", "시인", "시2", false));
        myRecordItem.add(new MyRecordItem("3", "시인", "시3", false));
        myRecordItem.add(new MyRecordItem("4", "시인", "시4", false));
        myRecordItem.add(new MyRecordItem("5", "시인", "시5", false));
        myRecordItem.add(new MyRecordItem("6", "시인", "시6", false));
        myRecordItem.add(new MyRecordItem("7", "시인", "시7", false));
        myRecordItem.add(new MyRecordItem("8", "시인", "시8", false));


        ArrayList<MyRecordItem> notificationItems = new ArrayList<>();
        notificationItems.add(new MyRecordItem("1", "알림1", null, false));
        notificationItems.add(new MyRecordItem("2", "알림2", null, false));
        notificationItems.add(new MyRecordItem("3", "알림3", null, false));
        notificationItems.add(new MyRecordItem("4", "알림4", null, false));

        bundle.putParcelableArrayList("PoemList", myRecordItem);
        bundle.putParcelableArrayList("NotificationList", notificationItems);

        MyMenuFragment myMenuFragment = MyMenuFragment.newInstance(bundle);

        if (currentFragment instanceof MyFavoritesFragment || currentFragment instanceof LoginFragment || currentFragment instanceof PlayerFragmentTemp) {
            currentFragmentTransaction
                    .replace(R.id.container, myMenuFragment)
                    .commit();
        } else {
            currentFragmentTransaction
                    .replace(R.id.container, myMenuFragment)
                    .addToBackStack("MainFragment")
                    .commit();
        }


    }

    private void processMyMenuFragmentChange(final Fragment currentFragment, final FragmentTransaction currentFragmentTransaction) {
        WoolrimApplication.apolloClient
                .query(GetMyMenu.builder().stu_id(WoolrimApplication.loginedUserId).build())
                .enqueue(new ApolloCall.Callback<GetMyMenu.Data>() {
                    @Override
                    public void onResponse(@Nonnull com.apollographql.apollo.api.Response<GetMyMenu.Data> response) {
                        if (response.data().getMainInfo().isSuccess()) {
                            Bundle bundle = new Bundle();
                            ArrayList<MyRecordItem> myRecordItem = new ArrayList<>();
                            for (GetMyMenu.Recording_list item : response.data().getMainInfo().recording_list()) {
                                boolean auth_flag;
                                auth_flag = item.auth_flag() != Status.ACCEPTED;
                                myRecordItem.add(new MyRecordItem(
                                        item.id(),
                                        item.poem().poet().name(),
                                        item.poem().name(),
                                        auth_flag
                                ));
                            }
                            ArrayList<MyRecordItem> notificationItems = new ArrayList<>();
                            for (GetMyMenu.Notification_list item : response.data().getMainInfo().notification_list()) {
                                notificationItems.add(new MyRecordItem(item.id(), item.content(), null, false));
                            }
                            bundle.putParcelableArrayList("PoemList", myRecordItem);
                            bundle.putParcelableArrayList("NotificationList", notificationItems);

                            MyMenuFragment myMenuFragment = MyMenuFragment.newInstance(bundle);

                            if (currentFragment instanceof MyFavoritesFragment || currentFragment instanceof PlayerFragmentTemp) {
                                currentFragmentTransaction
                                        .replace(R.id.container, myMenuFragment)
                                        .commit();
                            } else {
                                currentFragmentTransaction
                                        .replace(R.id.container, myMenuFragment)
                                        .addToBackStack("MainFragment")
                                        .commit();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }

                });

    }

}
