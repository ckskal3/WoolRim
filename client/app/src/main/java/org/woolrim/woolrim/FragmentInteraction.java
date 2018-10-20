package org.woolrim.woolrim;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

public interface FragmentInteraction {
    void onFragmentInteraction(Fragment fragment,String name);
    void onDialogFragmentInteraction(DialogFragment fragment, String name);
    void onIntentAction();
}
