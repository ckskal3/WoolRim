package org.woolrim.woolrim.SQLiteDAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.woolrim.woolrim.DataItems.MyFavoritesItem;
import org.woolrim.woolrim.Utils.DBManagerHelper;

import java.util.ArrayList;

public class FavoriteDAO {
    private SQLiteDatabase database;
    public static String[] favoriteAllColumns = new String[]{
            DBManagerHelper.COLUMN_FAVORITE_PRIVATE_ID,
            DBManagerHelper.COLUMN_FAVORITE_RECORDING_ID,
            DBManagerHelper.COLUMN_FAVORITE_POEM_ID,
            DBManagerHelper.COLUMN_FAVORITE_RECORDING_STUDENT_ID,
            DBManagerHelper.COLUMN_FAVORITE_POEM_NAME,
            DBManagerHelper.COLUMN_FAVORITE_RECORDING_STUDENT_NAME,
            DBManagerHelper.COLUMN_FAVORITE_USER_NAME
    };

    public FavoriteDAO(SQLiteDatabase database) {
        this.database = database;
    }

    public void insertFavorite(MyFavoritesItem favoritesItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBManagerHelper.COLUMN_FAVORITE_RECORDING_ID,favoritesItem.recordingId);
        contentValues.put(DBManagerHelper.COLUMN_FAVORITE_POEM_ID,favoritesItem.poemId);
        contentValues.put(DBManagerHelper.COLUMN_FAVORITE_RECORDING_STUDENT_ID,favoritesItem.recordingStudentId);
        contentValues.put(DBManagerHelper.COLUMN_FAVORITE_POEM_NAME, favoritesItem.poemName);
        contentValues.put(DBManagerHelper.COLUMN_FAVORITE_RECORDING_STUDENT_NAME,favoritesItem.recordingStudentName);
        contentValues.put(DBManagerHelper.COLUMN_FAVORITE_USER_NAME, favoritesItem.userName);

        database.insert(DBManagerHelper.FAVORITE_TABLE_NAME,null,contentValues);

    }

    public void updateFavorite(MyFavoritesItem myFavoritesItem, int bookmarkFlag){
        if(bookmarkFlag == 1){ // 추가
            insertFavorite(myFavoritesItem);
        }else{ // 삭제
            deleteFavorite(myFavoritesItem);
        }
    }

    public void deleteFavorite(MyFavoritesItem myFavoritesItem){
        database.delete(DBManagerHelper.FAVORITE_TABLE_NAME,
                DBManagerHelper.COLUMN_FAVORITE_RECORDING_STUDENT_NAME + " = ? AND "+
                DBManagerHelper.COLUMN_FAVORITE_POEM_NAME+ " = ? ",
                new String[]{myFavoritesItem.recordingStudentName, myFavoritesItem.poemName});
    }

    public ArrayList<MyFavoritesItem> selectFavorite(String userName) {
        ArrayList<MyFavoritesItem> items = new ArrayList<>();
        Cursor cursor = database.query(
                DBManagerHelper.FAVORITE_TABLE_NAME,
                favoriteAllColumns, DBManagerHelper.COLUMN_FAVORITE_USER_NAME + " = ? ",
                new String[]{userName}, null, null, null
        );

        if (cursor.getCount() == 0) {
            items.add(new MyFavoritesItem("ERROR"));
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items.add(processCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return items;
    }

    public MyFavoritesItem selectFavorite(String recordingStudentName, String poemName){
        MyFavoritesItem favoritesItem = null;
        Cursor cursor = database.query(
                DBManagerHelper.FAVORITE_TABLE_NAME,
                favoriteAllColumns,
                DBManagerHelper.COLUMN_FAVORITE_RECORDING_STUDENT_NAME +" = ? AND " +DBManagerHelper.COLUMN_FAVORITE_POEM_NAME+ " = ? ",
                new String[]{recordingStudentName,poemName},
                null,null,null
        );
        if(cursor.getCount() == 0){
            favoritesItem = new MyFavoritesItem("ERROR");
        }else{
            favoritesItem = new MyFavoritesItem("SUCCESS");
        }
        return favoritesItem;
    }

    public MyFavoritesItem processCursor(Cursor cursor) {
        return new MyFavoritesItem(
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_FAVORITE_RECORDING_ID)),
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_FAVORITE_POEM_ID)),
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_FAVORITE_RECORDING_STUDENT_ID)),
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_FAVORITE_POEM_NAME)),
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_FAVORITE_RECORDING_STUDENT_NAME)),
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_FAVORITE_USER_NAME))
        );
    }
}
