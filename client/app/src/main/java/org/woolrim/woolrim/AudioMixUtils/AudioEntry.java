package org.woolrim.woolrim.AudioMixUtils;

import java.io.Serializable;

public class AudioEntry implements Serializable {

    private static final long serialVersionUID = 2178420052691000209L;

    public long id;
    public String mime;
    public String fileUrl;

    public boolean isPlaying;

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        return obj instanceof AudioEntry && ((AudioEntry)obj).id == this.id;
    }
}
