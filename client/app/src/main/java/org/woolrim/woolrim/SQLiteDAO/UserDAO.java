package org.woolrim.woolrim.SQLiteDAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.woolrim.woolrim.DBManagerHelper;
import org.woolrim.woolrim.UserDetail;


public class UserDAO {
    public SQLiteDatabase database;
    public static String[] userAllColumns = new String[]{
            DBManagerHelper.COLUMN_USER_NAME,
            DBManagerHelper.COLUMN_USER_STUDENT_ID,
            DBManagerHelper.COLUMN_USER_PASSWORD,
            DBManagerHelper.COLUMN_USER_GENDER,
            DBManagerHelper.COLUMN_USER_UNIVERSITY
    };

    public UserDAO(SQLiteDatabase database) {
        this.database = database;
    }

    public long insertUserDetail(UserDetail userDetail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBManagerHelper.COLUMN_USER_STUDENT_ID, userDetail.stuId);
        contentValues.put(DBManagerHelper.COLUMN_USER_NAME, userDetail.name);
        contentValues.put(DBManagerHelper.COLUMN_USER_PASSWORD, userDetail.passwd);
        contentValues.put(DBManagerHelper.COLUMN_USER_UNIVERSITY, userDetail.university);
        contentValues.put(DBManagerHelper.COLUMN_USER_GENDER, userDetail.gender);
        contentValues.put(DBManagerHelper.COLUMN_USER_BONGSA_TIME, userDetail.bongsaTime);

        long result = database.insert(DBManagerHelper.USER_TABLE_NAME,null,contentValues);
        return result;

    }

    public UserDetail selectUserDetail(String id) {
        UserDetail userDetail = null;
        Cursor cursor = database.query(
                DBManagerHelper.USER_TABLE_NAME,
                userAllColumns, DBManagerHelper.COLUMN_USER_STUDENT_ID + " = ? ",
                new String[]{id}, null, null, null
        );

        if (cursor.getCount() == 0) {
            userDetail = new UserDetail("ERROR");
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                userDetail = processCursor(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return userDetail;
    }

    public UserDetail processCursor(Cursor cursor) {
        return new UserDetail(
                cursor.getInt(cursor.getColumnIndex(DBManagerHelper.COLUMN_USER_STUDENT_ID)),
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_USER_PASSWORD)),
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_USER_NAME)),
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_USER_UNIVERSITY)),
                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_USER_GENDER))
        );
    }
}
