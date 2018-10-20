package org.woolrim.woolrim;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/*
id: ID!
name: String!
stu_id: Int!
gender: String!
passwd: String!
created: String!
bongsa_time: Int
 */

public class UserDetail implements Parcelable{

    @SerializedName("stu_id")
    public int stuId;
    @SerializedName("passwd")
    public String passwd;
    @SerializedName("name")
    public String name;
    @SerializedName("university")
    public String university;
    @SerializedName("gender")
    public String gender;
    @SerializedName("crated")
    public String created;
    @SerializedName("bongsa_time")
    public int bongsaTime;

    public String error;

    public UserDetail(String error){
        this.error = error;
    }

    public UserDetail(int stuId, String passwd, String name, String university, String gender){
        this.stuId = stuId;
        this.passwd = passwd;
        this.name = name;
        this.university = university;
        this.gender = gender;
        this.bongsaTime = 0;
        this.error = "SUCCESS";
    }


    protected UserDetail(Parcel in) {
        stuId = in.readInt();
        passwd = in.readString();
        name = in.readString();
        gender = in.readString();
        university = in.readString();
        created = in.readString();
        bongsaTime = in.readInt();
        error = in.readString();
    }

    public static final Creator<UserDetail> CREATOR = new Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel in) {
            return new UserDetail(in);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(stuId);
        parcel.writeString(passwd);
        parcel.writeString(name);
        parcel.writeString(gender);
        parcel.writeString(created);
        parcel.writeString(university);
        parcel.writeInt(bongsaTime);
        parcel.writeString(error);
    }

}
