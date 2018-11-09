package org.woolrim.woolrim.DataItems;

import android.os.Parcel;
import android.os.Parcelable;

public class PoemAndPoetItem implements Parcelable {
    public String poem;
    public String poet;
    public int man_count;
    public int woman_count;
    public int full_count;

    public PoemAndPoetItem(String poem , String poet, int man_count, int woman_count, int full_count){
        this.poem = poem;
        this.poet = poet;
        this.man_count = man_count;
        this.woman_count = woman_count;
        this.full_count = full_count;
    }

    protected PoemAndPoetItem(Parcel in) {
        poem = in.readString();
        poet = in.readString();
        man_count = in.readInt();
        woman_count = in.readInt();
        full_count = in.readInt();
    }

    public static final Creator<PoemAndPoetItem> CREATOR = new Creator<PoemAndPoetItem>() {
        @Override
        public PoemAndPoetItem createFromParcel(Parcel in) {
            return new PoemAndPoetItem(in);
        }

        @Override
        public PoemAndPoetItem[] newArray(int size) {
            return new PoemAndPoetItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poem);
        parcel.writeString(poet);
        parcel.writeInt(man_count);
        parcel.writeInt(woman_count);
        parcel.writeInt(full_count);
    }
}
