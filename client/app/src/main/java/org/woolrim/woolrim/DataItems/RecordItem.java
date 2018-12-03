package org.woolrim.woolrim.DataItems;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordItem implements Parcelable {
    public String fileName, filePath, studentName, studentProfilePath, poemName, poetName;
    public int studentId, mediaId, poemId, bookmarkFlag, duration;
    public String error;

    public RecordItem(String fileName, String filePath, int studentId, int duration, String poemName, String poetName) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.studentId = studentId;
        this.duration = duration;
        this.poemName = poemName;
        this.poetName = poetName;
    }

    public RecordItem(String filePath,int duration, String studentName, String studentProfilePath, String poemName, String poetName, int studentId, int mediaId, int poemId, int bookmarkFlag) {
        this.filePath = filePath;
        this.duration = duration;
        this.studentName = studentName;
        this.studentProfilePath = studentProfilePath;
        this.poetName = poetName;
        this.poemName = poemName;
        this.studentId = studentId;
        this.mediaId = mediaId;
        this.poemId = poemId;
        this.bookmarkFlag = bookmarkFlag;
    }

    public RecordItem(String error) {
        this.error = error;
    }


    protected RecordItem(Parcel in) {
        fileName = in.readString();
        filePath = in.readString();
        studentName = in.readString();
        studentProfilePath = in.readString();
        poemName = in.readString();
        poetName = in.readString();
        studentId = in.readInt();
        mediaId = in.readInt();
        poemId = in.readInt();
        bookmarkFlag = in.readInt();
        duration = in.readInt();
        error = in.readString();
    }

    public static final Creator<RecordItem> CREATOR = new Creator<RecordItem>() {
        @Override
        public RecordItem createFromParcel(Parcel in) {
            return new RecordItem(in);
        }

        @Override
        public RecordItem[] newArray(int size) {
            return new RecordItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fileName);
        parcel.writeString(filePath);
        parcel.writeString(studentName);
        parcel.writeString(studentProfilePath);
        parcel.writeString(poemName);
        parcel.writeString(poetName);
        parcel.writeInt(studentId);
        parcel.writeInt(mediaId);
        parcel.writeInt(poemId);
        parcel.writeInt(bookmarkFlag);
        parcel.writeInt(duration);
        parcel.writeString(error);
    }
}
