package org.woolrim.woolrim.DataItems;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordItem implements Parcelable {
    public String fileName, filePath, duration, studentName, studentProfilePath, poemName, poetName;
    public int studentId, mediaId, poemId, bookmarkFlag;
    public String error;

    public RecordItem(String fileName, String filePath, int studentId, String duration) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.studentId = studentId;
        this.duration = duration;
    }

    public RecordItem(String filePath, String studentName, String studentProfilePath, String poemName, String poetName, int studentId, int mediaId, int poemId, int bookmarkFlag) {
        this.filePath = filePath;
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

    public RecordItem(Parcel src) {
        fileName = src.readString();
        filePath = src.readString();
        studentName = src.readString();
        studentProfilePath = src.readString();
        poemName = src.readString();
        poetName = src.readString();
        mediaId = src.readInt();
        studentId = src.readInt();
        poemId = src.readInt();
        duration = src.readString();
        error = src.readString();
        bookmarkFlag = src.readInt();
    }

    public static final RecordItem.Creator<RecordItem> CREATOR = new Parcelable.Creator<RecordItem>() {
        @Override
        public RecordItem createFromParcel(Parcel parcel) {
            return new RecordItem(parcel);
        }

        @Override
        public RecordItem[] newArray(int i) {
            return new RecordItem[i];
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
        parcel.writeString(poetName);
        parcel.writeString(poemName);
        parcel.writeInt(mediaId);
        parcel.writeInt(studentId);
        parcel.writeInt(poemId);
        parcel.writeString(duration);
        parcel.writeString(error);
        parcel.writeInt(bookmarkFlag);
    }
}
