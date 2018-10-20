package org.woolrim.woolrim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment implements View.OnClickListener, TextWatcher {
    private EditText userIdEditText, userPassEditText, userNameEditText;
    private TextView userUniversityTextView;
    private Button manButton, womanButton, completeButton, selectCompleteButton;
    private ImageView checkImageView1, checkImageView2, checkImageView3, checkImageView4;
    private Animation translateUp, translateDown;
    private ConstraintLayout selectLayout, userUniversityInputLayout;
    private NumberPicker universityListPicker;
    private UniversityRecyclerViewAdapter universityRecyclerViewAdapter;


    private String userId, userName, userPass , userUnversity, userGender;

    private boolean schoolSelectLayoutIsOpen = false;
    private boolean manSelectFlag = false, womanSelectFlag = false, userIdOkFlag = false, userNameOkFlag = false, userPassOkFlag = false, userUniversityOkFlag = false;
    private boolean serverResonseFlag = false;

    public static SignUpFragment newInstance(Bundle bundle) {
        SignUpFragment signUpFragment = new SignUpFragment();
        signUpFragment.setArguments(bundle);
        return signUpFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);


        UniversityItem[] universityItems = new UniversityItem[3];
        universityItems[0] = new UniversityItem("인천대학교");
        universityItems[1] = new UniversityItem("인하대학교");
        universityItems[2] = new UniversityItem("재능대학교");

        String[] data = new String[]{universityItems[0].universityName, universityItems[1].universityName, universityItems[2].universityName};


        universityListPicker.setMinValue(0);
        universityListPicker.setMaxValue(data.length - 1);
        universityListPicker.setDisplayedValues(data);
        universityListPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        setItemListener();
    }

    private void init(View view) {
        userIdEditText = view.findViewById(R.id.signup_stuid_edittext);
        userPassEditText = view.findViewById(R.id.signup_pass_edittext);
        userNameEditText = view.findViewById(R.id.signup_username_edittext);
        userUniversityTextView = view.findViewById(R.id.signup_university_edittext);
        manButton = view.findViewById(R.id.signup_man_button);
        womanButton = view.findViewById(R.id.signup_woman_button);
        completeButton = view.findViewById(R.id.signup_complete_button);
        selectCompleteButton = view.findViewById(R.id.university_complete_button);
        selectLayout = view.findViewById(R.id.university_select_layout);
        userUniversityInputLayout = view.findViewById(R.id.signup_university_input_layout);

        checkImageView1 = view.findViewById(R.id.signup_ok_sign_iv1);
        checkImageView2 = view.findViewById(R.id.signup_ok_sign_iv2);
        checkImageView3 = view.findViewById(R.id.signup_ok_sign_iv3);
        checkImageView4 = view.findViewById(R.id.signup_ok_sign_iv4);

        universityListPicker = view.findViewById(R.id.university_list_picker);

        translateDown = AnimationUtils.loadAnimation(getContext(), R.anim.translate_down);
        translateUp = AnimationUtils.loadAnimation(getContext(), R.anim.translate_up);

    }

    private void setItemListener() {
        selectCompleteButton.setOnClickListener(this);
        userUniversityTextView.setOnClickListener(this);
        manButton.setOnClickListener(this);
        womanButton.setOnClickListener(this);
        completeButton.setOnClickListener(this);
        userUniversityInputLayout.setOnClickListener(this);

        userIdEditText.addTextChangedListener(this);
        userNameEditText.addTextChangedListener(this);
        userPassEditText.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_university_input_layout: //대학 선택 창 띄우는 레이아웃
                if (!schoolSelectLayoutIsOpen) {
                    selectLayout.setVisibility(View.VISIBLE);
                    selectLayout.startAnimation(translateUp);
                    schoolSelectLayoutIsOpen = true;
                }
                break;
            case R.id.university_complete_button:// 대학선택 완료 버튼
                checkImageView2.setImageResource(R.drawable.ok_icon);
                selectLayout.startAnimation(translateDown);
                selectLayout.setVisibility(View.INVISIBLE);
                schoolSelectLayoutIsOpen = false;
                userUnversity = universityListPicker.getDisplayedValues()[universityListPicker.getValue()];
                userUniversityTextView.setText(userUnversity);
                userUniversityOkFlag = true;
                break;
            case R.id.signup_man_button: // 남자 성별 버튼
                manButton.setBackgroundResource(R.drawable.gender_selected_background);
                manSelectFlag = true;
                userGender = getString(R.string.man_kr);
                if (womanSelectFlag) {
                    womanButton.setBackgroundResource(R.drawable.gender_select_background);
                    womanSelectFlag = false;
                }
                break;
            case R.id.signup_woman_button: // 여자 성별 버튼
                womanButton.setBackgroundResource(R.drawable.gender_selected_background);
                womanSelectFlag = true;
                userGender = getString(R.string.woman_kr);
                if (manSelectFlag) {
                    manButton.setBackgroundResource(R.drawable.gender_select_background);
                    manSelectFlag = false;
                }
                break;

            case R.id.signup_complete_button: // 가입 완료 버튼
                if (userPassOkFlag && userIdOkFlag && userNameOkFlag && userUniversityOkFlag && (womanSelectFlag || manSelectFlag)) {

                    //중복 검사 해봐야 함
                    UserDetail userDetail = DBManagerHelper.userDAO.selectUserDetail(userId);
                    if(userDetail.error.equals("ERROR")) {
                        int userIdInteger = Integer.parseInt(userId);
                        DBManagerHelper.userDAO.insertUserDetail(new UserDetail(
                                userIdInteger,
                                userPass,
                                userName,
                                userUnversity,
                                userGender));
                        requestServerForUserData();

                    }else{
                        Toast.makeText(getContext(), "이미 가입하셨습니다.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getContext(), "정보 입력이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        userId = userIdEditText.getText().toString().trim();
        userName = userNameEditText.getText().toString().trim();
        userPass = userPassEditText.getText().toString().trim();

        if (userName.length() > 2) {
            checkImageView1.setImageResource(R.drawable.ok_icon);
            userNameOkFlag = true;
        } else {
            checkImageView1.setImageResource(R.drawable.x_icon2);
            userNameOkFlag = false;
        }
        if (userId.length() == 9) {
            checkImageView3.setImageResource(R.drawable.ok_icon);
            userIdOkFlag = true;
        } else {
            checkImageView3.setImageResource(R.drawable.x_icon2);
            userIdOkFlag = false;
        }
        if (userPass.length() >= 4) {
            checkImageView4.setImageResource(R.drawable.ok_icon);
            userPassOkFlag = true;
        } else {
            checkImageView4.setImageResource(R.drawable.x_icon2);
            userPassOkFlag = false;
        }
    }

    private void requestServerForUserData(){
        String url1 = "http://stou2.cafe24.com/Woolrim/UserSelect.php";
        String url2 = "http://stou2.cafe24.com/Woolrim/UserInsert.php";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(processServerResponse(response)){
                            Toast.makeText(getContext(), "회원가입 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                            assert getFragmentManager() != null;
                            getFragmentManager().popBackStack();
                        }else{
                            Toast.makeText(getContext(),"오류가 발생했습니다. 다시 가입해주세요",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                /*
                 $name = $_POST['name'];
                 $password = $_POST['password'];
                 $student_id = $_POST['student_id'];
                 $gender = $_POST['gender'];
                 $university = $_POST['university'];
                 */
                params.put("name",userName);
                params.put("password", userPass);
                params.put("student_id",userId);
                params.put("gender",userGender);
                params.put("university",userUnversity);
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        WoolrimApplication.requestQueue.add(stringRequest);
    }

    private boolean processServerResponse(String response){
        Gson gson = new Gson();
        RequestData result = gson.fromJson(response,RequestData.class);
        Log.d("Code",String.valueOf(result.code));
        if(result.code == 200) return true;
        else return false;
    }
}
