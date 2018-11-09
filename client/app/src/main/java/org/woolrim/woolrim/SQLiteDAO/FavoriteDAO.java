package org.woolrim.woolrim.SQLiteDAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.woolrim.woolrim.DataItems.MyFavoritesItem;
import org.woolrim.woolrim.Utils.DBManagerHelper;

import static org.woolrim.woolrim.SQLiteDAO.UserDAO.userAllColumns;

public class FavoriteDAO {
    private SQLiteDatabase database;
    public static String[] favoriteAllColoumns = new String[]{
            DBManagerHelper.COLUMN_FAVORITE_PRIVATE_ID,
            DBManagerHelper.COLUMN_FAVORITE_POEM_NAME,
            DBManagerHelper.COLUMN_FAVORITE_USER_NAME
    };

    public FavoriteDAO(SQLiteDatabase database) {
        this.database = database;
    }

    public long insertFavorite(MyFavoritesItem favoritesItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBManagerHelper.COLUMN_FAVORITE_POEM_NAME, favoritesItem.poemName);
        contentValues.put(DBManagerHelper.COLUMN_FAVORITE_USER_NAME, favoritesItem.userName);

        long result = database.insert(DBManagerHelper.FAVORITE_TABLE_NAME,null,contentValues);
        return result;

    }

    public MyFavoritesItem selectFavorite(String id) {
        MyFavoritesItem favoritesItem = null;
        Cursor cursor = database.query(
                DBManagerHelper.USER_TABLE_NAME,
                userAllColumns, DBManagerHelper.COLUMN_USER_STUDENT_ID + " = ? ",
                new String[]{id}, null, null, null
        );

        if (cursor.getCount() == 0) {
            favoritesItem = new MyFavoritesItem("ERROR");
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                favoritesItem = processCursor(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return favoritesItem;
    }

    public MyFavoritesItem processCursor(Cursor cursor) {
        return new MyFavoritesItem(
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_FAVORITE_POEM_NAME)),
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_FAVORITE_USER_NAME))
        );
    }
}
