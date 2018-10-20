package org.woolrim.woolrim;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;

import org.woolrim.woolrim.Temp.TempDataItem;

import java.util.ArrayList;
import java.util.Map;

public class MainFragment extends Fragment implements View.OnClickListener {

    public static final int SHOW_LIST_LAYOUT_CODE = 105;
    public static final int SHOW_RECORD_LAYOUT_CODE = 106;


    private ConstraintLayout voiceSearchLayout;
    private LinearLayout showListLayout, recordPoemLayout;
    private ImageView imageView4, imageView5;
    private View tranparentView;

    public static TextView voiceRecognitionTv1, voiceRecognitionTv2;

    private Animation itemRotate;

    public static boolean isRecognitioning = false;

    public abstract class DialogDismissListener implements DialogInterface.OnDismissListener {

    }

    public static MainFragment newInstance(Bundle bundle) {
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        setOnClick();
    }

    private void setOnClick() {
        showListLayout.setOnClickListener(this);
        recordPoemLayout.setOnClickListener(this);
        voiceSearchLayout.setOnClickListener(this);
    }

    private void init(View view) {
        showListLayout = view.findViewById(R.id.show_list_layout);
        recordPoemLayout = view.findViewById(R.id.show_record_layout);
        voiceSearchLayout = view.findViewById(R.id.search_voice_layout);

        voiceRecognitionTv1 = view.findViewById(R.id.voice_recognition_textview1);
        voiceRecognitionTv2 = view.findViewById(R.id.voice_recognition_textview2);

        imageView4 = view.findViewById(R.id.voice_recognition_icon_background);
        imageView5 = view.findViewById(R.id.voice_recognition_icon);
        tranparentView = view.findViewById(R.id.transparent_view);
        itemRotate = AnimationUtils.loadAnimation(getContext(), R.anim.item_rotate);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MainFragement", "onResume()");
        MainActivity.toolbarLabelTv.setText("울림");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_list_layout: // 시 목록 보기
                requestServerForPoemList(SHOW_LIST_LAYOUT_CODE);
                break;
            case R.id.show_record_layout: // 시 녹음 목록 보기
                requestServerForPoemList(SHOW_RECORD_LAYOUT_CODE);
                break;
            case R.id.search_voice_layout: // 음성 인식

                VoiceRecognitionFragment testDialogFragment = VoiceRecognitionFragment.newInstance(new Bundle());
                testDialogFragment.setDismissListener(new DialogDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        voiceRecognitionTv1.setVisibility(View.VISIBLE);
                        voiceRecognitionTv2.setVisibility(View.VISIBLE);
                    }
                });

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                testDialogFragment.show(fragmentTransaction, "playback");

                Toast.makeText(getContext(), "곧 구현될 예정입니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void requestServerForPoemList(final int where) {
        String url = "http://stou2.cafe24.com/Woolrim/DataSelect.php";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<TempDataItem> result = processServerRespose(response);
                        if(result != null){
                            String oldPoet = null;
                            ArrayList<PoetItem> poetItems = new ArrayList<>();
                            int i = -1 ;
                            for(TempDataItem tempDataItem : result){
                                String newPoet = tempDataItem.poet;
                                if(!newPoet.equals(oldPoet)){
                                    PoetItem poetItem = new PoetItem(newPoet);
                                    poetItem.poemName.add(tempDataItem);
                                    poetItems.add(poetItem);
                                    oldPoet = newPoet;
                                    i++;
                                }else{
                                    poetItems.get(i).poemName.add(tempDataItem);
                                }
                            }

                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("DataItems",poetItems);

                            if(where == SHOW_LIST_LAYOUT_CODE) {
                                bundle.putInt("RequestPageCode",SHOW_LIST_LAYOUT_CODE);
                                PoemListFragment poemListFragment = PoemListFragment.newInstance(bundle);
                                getActivity().getSupportFragmentManager().beginTransaction().
                                        replace(R.id.container, poemListFragment).addToBackStack("MainFragment").commit();
                            }else if(where == SHOW_RECORD_LAYOUT_CODE){
                                bundle.putInt("RequestPageCode",SHOW_RECORD_LAYOUT_CODE);
                                LoginFragment loginFragment = LoginFragment.newInstance(bundle);
                                getActivity().getSupportFragmentManager().beginTransaction().
                                        replace(R.id.container, loginFragment).addToBackStack("MainFragment").commit();
                            }
                        }else{
                            Toast.makeText(getContext(),"오류가 발생했습니다. 다시 시도해 주세요",Toast.LENGTH_SHORT).show();
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
                return super.getParams();
            }
        };

        stringRequest.setShouldCache(false);
        WoolrimApplication.requestQueue.add(stringRequest);
    }
    private ArrayList<TempDataItem> processServerRespose(String response){
        Gson gson = new Gson();
        RequestData requestData = gson.fromJson(response,RequestData.class);
        if(requestData.code == 200){
            Log.d("Code",String.valueOf(requestData.code));
            return requestData.result;
        }else return null;
    }
}
