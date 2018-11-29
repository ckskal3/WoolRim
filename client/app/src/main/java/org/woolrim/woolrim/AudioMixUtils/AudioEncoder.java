package org.woolrim.woolrim.AudioMixUtils;

public abstract class AudioEncoder {

    public interface OnEncodingCompletedListener{
        void onEndingCompleted();
    }

    public String rawAudioFile;
    public OnEncodingCompletedListener onEncodingCompletedListener;
    AudioEncoder(String rawAudioFile, OnEncodingCompletedListener onEncodingCompletedListener){
        this.rawAudioFile = rawAudioFile;
        this.onEncodingCompletedListener = onEncodingCompletedListener;
    }

    public static AudioEncoder createAccEncoder(String rawAudioFile, OnEncodingCompletedListener onEncodingCompletedListener){
        return new AACAudioEncoder(rawAudioFile, onEncodingCompletedListener );
    }

    public abstract void encodeToFile(String outEncodeFile);
}

