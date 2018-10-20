package org.woolrim.woolrim.Temp;

import android.os.Parcel;
import android.os.Parcelable;

public class TempDataItem implements Parcelable {
    public String poem;
    public String poet;
    public int man_count;
    public int woman_count;
    public int full_count;

    protected TempDataItem(Parcel in) {
        poem = in.readString();
        poet = in.readString();
        man_count = in.readInt();
        woman_count = in.readInt();
        full_count = in.readInt();
    }

    public static final Creator<TempDataItem> CREATOR = new Creator<TempDataItem>() {
        @Override
        public TempDataItem createFromParcel(Parcel in) {
            return new TempDataItem(in);
        }

        @Override
        public TempDataItem[] newArray(int size) {
            return new TempDataItem[size];
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
