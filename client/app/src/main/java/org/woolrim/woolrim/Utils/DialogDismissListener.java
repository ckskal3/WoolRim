package org.woolrim.woolrim.Utils;

import android.content.DialogInterface;
import android.support.annotation.Nullable;

public abstract  class DialogDismissListener implements DialogInterface.OnDismissListener {

    private String key;
    private boolean flag;

    public void onDismissed(@Nullable String key) {
        this.key = key;
    }
    public void onDismissed(@Nullable String key, boolean flag){
        this.onDismissed(key);
        this.flag = flag;
    }

    public String getKey(){
        return key;
    }
    public boolean getFlag(){
        return flag;
    }
}
