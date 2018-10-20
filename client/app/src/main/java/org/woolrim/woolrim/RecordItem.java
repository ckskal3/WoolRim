package org.woolrim.woolrim;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordItem implements Parcelable {
    public String name, path, duration;
    public int studentId;
    public String error;

    public RecordItem(String name, String path, int studentId, String duration) {
        this.name = name;
        this.path = path;
        this.studentId = studentId;
        this.duration = duration;
    }

    public RecordItem(String error) {
        this.error = error;
    }

    public RecordItem(Parcel src) {
        name = src.readString();
        path = src.readString();
        studentId = src.readInt();
        duration = src.readString();
        error = src.readString();
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
        parcel.writeString(name);
        parcel.writeString(path);
        parcel.writeInt(studentId);
        parcel.writeString(duration);
        parcel.writeString(error);
    }
}
