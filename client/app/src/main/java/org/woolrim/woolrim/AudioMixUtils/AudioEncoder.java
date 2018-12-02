package org.woolrim.woolrim.AudioMixUtils;

public abstract class AudioEncoder {

    public interface OnEncodingCompletedListener{
        void onEncodingCompleted();
    }

    public interface  OnEncodingStartListener{
        void onEncodingStart();
    }

    public String rawAudioFile;
    public OnEncodingStartListener onEncodingStartListener;
    public OnEncodingCompletedListener onEncodingCompletedListener;
    AudioEncoder(String rawAudioFile,OnEncodingStartListener onEncodingStartListener, OnEncodingCompletedListener onEncodingCompletedListener){
        this.rawAudioFile = rawAudioFile;
        this.onEncodingCompletedListener = onEncodingCompletedListener;
        this.onEncodingStartListener = onEncodingStartListener;
    }

    public static AudioEncoder createAccEncoder(String rawAudioFile,OnEncodingStartListener onEncodingStartListener, OnEncodingCompletedListener onEncodingCompletedListener){
        return new AACAudioEncoder(rawAudioFile,onEncodingStartListener ,onEncodingCompletedListener );
    }

    public abstract void encodeToFile(String outEncodeFile);
}

