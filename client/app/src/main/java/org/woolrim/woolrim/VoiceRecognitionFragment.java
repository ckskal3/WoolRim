package org.woolrim.woolrim;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class VoiceRecognitionFragment extends DialogFragment {

    private ImageView voiceRecognitionBgIv, voiceRecognitionIconIv;
    private MainFragment.DialogDismissListener mResultListener;

    private Animation itemRotate;


    public static VoiceRecognitionFragment newInstance(Bundle bundle){
        VoiceRecognitionFragment testDialogFragment = new VoiceRecognitionFragment();
        testDialogFragment.setArguments(bundle);
        return testDialogFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainFragment.voiceRecognitionTv1.setVisibility(View.INVISIBLE);
        MainFragment.voiceRecognitionTv2.setVisibility(View.INVISIBLE);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view  = getActivity().getLayoutInflater().inflate(R.layout.fragment_voice_recognition
                ,null);

        init(view);

        builder.setView(view);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        return builder.create();
    }


    private void init(View view){
        voiceRecognitionBgIv = view.findViewById(R.id.voice_recognition_icon_background);
        voiceRecognitionIconIv = view.findViewById(R.id.voice_recognition_icon);
        itemRotate = AnimationUtils.loadAnimation(getContext(),R.anim.item_rotate);
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window  = getDialog().getWindow();

        assert window != null;
        window.setGravity(Gravity.TOP);


//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        int sourceY = location[1];

        WindowManager.LayoutParams params = window.getAttributes();
        params.y = dpToPx(74f); // above source view

        window.setAttributes(params);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        alertDialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(false);
        alertDialog.getButton(Dialog.BUTTON_NEUTRAL).setEnabled(false);

        alertDialog.setOnDismissListener(mResultListener);

        if(!MainFragment.isRecognitioning){
            voiceRecognitionBgIv.startAnimation(itemRotate);
            MainFragment.isRecognitioning = true;
        }
    }
    public int dpToPx(float valueInDp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    public void onDestroyView() {
        voiceRecognitionBgIv.clearAnimation();
        MainFragment.isRecognitioning =false;
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

    public void setDismissListener(MainFragment.DialogDismissListener listener) {
        mResultListener = listener;
    }


}
