package org.woolrim.woolrim.DataItems;


import android.os.Parcel;
import android.os.Parcelable;

public class MyRecordItem implements Parcelable {
    public String _id,poem,poet;
    public boolean click_flag, auth_flag;
    public MyRecordItem(String _id,String poet, String poem, boolean auth_flag){
        this._id = _id;
        this.poem = poem;
        this.poet = poet;
        this.click_flag = false;
        this.auth_flag = auth_flag;
    }


    protected MyRecordItem(Parcel in) {
        _id = in.readString();
        poem = in.readString();
        poet = in.readString();
        click_flag = in.readByte() != 0;
        auth_flag = in.readByte() != 0;
    }

    public static final Creator<MyRecordItem> CREATOR = new Creator<MyRecordItem>() {
        @Override
        public MyRecordItem createFromParcel(Parcel in) {
            return new MyRecordItem(in);
        }

        @Override
        public MyRecordItem[] newArray(int size) {
            return new MyRecordItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(poem);
        parcel.writeString(poet);
        parcel.writeByte((byte) (click_flag ? 1 : 0));
        parcel.writeByte((byte) (auth_flag ? 1 : 0));
    }
}