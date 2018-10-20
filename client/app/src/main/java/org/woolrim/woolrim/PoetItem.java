package org.woolrim.woolrim;

import android.os.Parcel;
import android.os.Parcelable;

import org.woolrim.woolrim.Temp.TempDataItem;

import java.util.ArrayList;

public class PoetItem implements Parcelable{
    public String poetName;
    public ArrayList<TempDataItem> poemName = new ArrayList<>();
    public PoetItem(String poetName){
        this.poetName  = poetName;
    }


    protected PoetItem(Parcel in) {
        poetName = in.readString();
        poemName = in.createTypedArrayList(TempDataItem.CREATOR);
    }

    public static final Creator<PoetItem> CREATOR = new Creator<PoetItem>() {
        @Override
        public PoetItem createFromParcel(Parcel in) {
            return new PoetItem(in);
        }

        @Override
        public PoetItem[] newArray(int size) {
            return new PoetItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poetName);
        parcel.writeTypedList(poemName);
    }
}
