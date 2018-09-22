package org.woolrim.woolrim;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordItem implements Parcelable{
    String name;
    String path;
    int duration;
    public RecordItem(String name , String path , int duration){
        this.name = name;
        this.path = path;
        this.duration = duration;
    }

    public RecordItem(Parcel src){
        name = src.readString();
        path = src.readString();
        duration = src.readInt();
    }

    public static final RecordItem.Creator<RecordItem> CREATOR = new Parcelable.Creator<RecordItem>(){
        @Override
        public RecordItem createFromParcel(Parcel parcel) {
            return new RecordItem(parcel);
        }
        @Override
        public RecordItem[] newArray(int i) {
            return new RecordItem[i];
        }
    } ;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(path);
        parcel.writeInt(duration);
    }
}
