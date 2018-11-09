package org.woolrim.woolrim;

import com.google.gson.annotations.SerializedName;

import org.woolrim.woolrim.DataItems.PoemAndPoetItem;

import java.util.ArrayList;

public class RequestData {
    @SerializedName("data")
    public UserItem data;
    @SerializedName("Status")
    public String status;
    @SerializedName("Code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public ArrayList<PoemAndPoetItem> result;
}
