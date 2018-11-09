package org.woolrim.woolrim;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class GallerySelectFragment extends DialogFragment implements View.OnClickListener {

    private Button gallerySelectBtn, cancelBtn, defaultImageSelectBtn;
    private FragmentInteraction fragmentInteraction;

    public static GallerySelectFragment newInstance(Bundle bundle) {
        GallerySelectFragment gallerySelectFragment = new GallerySelectFragment();
        gallerySelectFragment.setArguments(bundle);
        return gallerySelectFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteraction) {
            //init the listener
            fragmentInteraction = (FragmentInteraction) context;


        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_gallery_select, null);
        init(view);

        builder.setView(view);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();

        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);

        AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        alertDialog.getButton(Dialog.BUTTON_NEGATIVE).setEnabled(false);
        alertDialog.getButton(Dialog.BUTTON_NEUTRAL).setEnabled(false);

        setItemsClickListener();

    }

    @Override
    public void onPause() {
        fragmentInteraction = null;
        dismiss();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        fragmentInteraction = null;
        super.onDestroyView();
    }


    private void init(View view) {
        cancelBtn = view.findViewById(R.id.gallery_select_cancel_button);
        gallerySelectBtn = view.findViewById(R.id.gallery_select_gallery_button);
        defaultImageSelectBtn = view.findViewById(R.id.gallery_select_default_button);
    }

    private void setItemsClickListener() {
        cancelBtn.setOnClickListener(this);
        gallerySelectBtn.setOnClickListener(this);
        defaultImageSelectBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gallery_select_cancel_button:
                dismiss();
                break;
            case R.id.gallery_select_default_button:
                MainActivity.profileImageView.setImageResource(R.drawable.profile_icon);
                dismiss();
                break;
            case R.id.gallery_select_gallery_button:
                fragmentInteraction.onIntentAction();
                dismiss();
                break;
        }
    }
}
