package org.woolrim.woolrim.DataItems;

import android.os.Parcel;
import android.os.Parcelable;

public class MyFavoritesItem implements Parcelable {
    public String recordingId, poemId, recordingStudentId, poemName, recordingStudentName, userName, error;

    public MyFavoritesItem(String recordingId, String poemId, String recordingStudentId, String poemName,String recordingStudentName, String userName) {
        this.recordingId = recordingId;
        this.poemId = poemId;
        this.recordingStudentId = recordingStudentId;
        this.poemName = poemName;
        this.recordingStudentName = recordingStudentName;
        this.userName = userName;
        this.error = "SUCCESS";
    }

    public MyFavoritesItem(String error) {
        this.error = error;
    }

    protected MyFavoritesItem(Parcel in) {
        recordingId = in.readString();
        poemId = in.readString();
        recordingStudentId = in.readString();
        poemName = in.readString();
        userName = in.readString();
        recordingStudentName = in.readString();
    }

    public static final Creator<MyFavoritesItem> CREATOR = new Creator<MyFavoritesItem>() {
        @Override
        public MyFavoritesItem createFromParcel(Parcel in) {
            return new MyFavoritesItem(in);
        }

        @Override
        public MyFavoritesItem[] newArray(int size) {
            return new MyFavoritesItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(recordingId);
        parcel.writeString(poemId);
        parcel.writeString(recordingStudentId);
        parcel.writeString(poemName);
        parcel.writeString(userName);
        parcel.writeString(recordingStudentName);
    }
}
