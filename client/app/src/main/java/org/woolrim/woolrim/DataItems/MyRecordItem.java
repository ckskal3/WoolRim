package org.woolrim.woolrim.DataItems;


public class MyRecordItem {
    public String poem,poet;
    public boolean click_flag, auth_flag;
    public MyRecordItem(String poet, String poem, boolean auth_flag){
        this.poem = poem;
        this.poet = poet;
        this.click_flag = false;
        this.auth_flag = auth_flag;
    }

}