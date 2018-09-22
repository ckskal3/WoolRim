package org.woolrim.woolrim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment implements View.OnClickListener {


    private Button showListBtn,recordPoemBtn, voiceSearchBtn;

    public static MainFragment newInstance(Bundle bundle){
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        setOnClick();
    }

    private void setOnClick(){
        showListBtn.setOnClickListener(this);
        recordPoemBtn.setOnClickListener(this);
        voiceSearchBtn.setOnClickListener(this);
    }

    private void init(View view){
        showListBtn = view.findViewById(R.id.show_list_btn);
        recordPoemBtn = view.findViewById(R.id.record_poem_btn);
        voiceSearchBtn = view.findViewById(R.id.search_voice_btn);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.show_list_btn:
                PoemListFragment poemListFragment = PoemListFragment.newInstance(new Bundle());
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container,poemListFragment).addToBackStack("MainFragment").commit();
                break;
            case R.id.record_poem_btn:
                LoginFragment loginFragment = LoginFragment.newInstance(new Bundle());
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container,loginFragment).addToBackStack("MainFragment").commit();
                break;
            case R.id.search_voice_btn:
                Intent itn3 =new Intent(getContext(),MyMenuActivity.class);
                itn3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(itn3,100);
                break;
        }
    }
}
