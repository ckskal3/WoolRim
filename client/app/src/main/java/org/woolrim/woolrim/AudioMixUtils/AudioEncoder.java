package org.woolrim.woolrim.AudioMixUtils;

public abstract class AudioEncoder {

    public String rawAudioFile;

    AudioEncoder(String rawAudioFile){
        this.rawAudioFile = rawAudioFile;
    }

    public static AudioEncoder createAccEncoder(String rawAudioFile){
        return new AACAudioEncoder(rawAudioFile);
    }

    public abstract void encodeToFile(String outEncodeFile);
}
