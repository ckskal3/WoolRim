package org.woolrim.woolrim.SQLiteDAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.woolrim.woolrim.Utils.DBManagerHelper;
import org.woolrim.woolrim.DataItems.RecordItem;

import java.util.ArrayList;

public class RecordDAO {

    private SQLiteDatabase database;
    public static String[] recordAllColoumns = new String[]{
            DBManagerHelper.COLUMN_RECORD_PRIVATE_ID,
            DBManagerHelper.COLUMN_RECORD_NAME,
            DBManagerHelper.COLUMN_RECORD_PATH,
            DBManagerHelper.COLUMN_RECORD_STUDENT_ID,
            DBManagerHelper.COLUMN_RECORD_DURATION
    };

    public RecordDAO(SQLiteDatabase database) {
        this.database = database;
    }

    public void insertRecord(RecordItem recordItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBManagerHelper.COLUMN_RECORD_NAME, recordItem.fileName);
        contentValues.put(DBManagerHelper.COLUMN_RECORD_PATH, recordItem.filePath);
        contentValues.put(DBManagerHelper.COLUMN_RECORD_STUDENT_ID, recordItem.studentId);
        contentValues.put(DBManagerHelper.COLUMN_RECORD_DURATION, recordItem.duration);
        database.insert(DBManagerHelper.RECORD_TABLE_NAME, null, contentValues);
    }

    public ArrayList<RecordItem> selectRecord(String id) {
        ArrayList<RecordItem> recordItems = new ArrayList<>();
        Cursor cursor = database.query(
                DBManagerHelper.RECORD_TABLE_NAME,
                recordAllColoumns,
                DBManagerHelper.COLUMN_RECORD_STUDENT_ID,
                new String[]{id},
                null, null, null);

        if (cursor == null) {
            recordItems.add(new RecordItem("ERROR"));
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recordItems.add(processCursor((cursor)));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return recordItems;
    }

    public RecordItem processCursor(Cursor cursor) {
        return null;
//        return new RecordItem(
//                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_RECORD_NAME)),
//                cursor.getString(cursor.getColumnIndex(DBManagerHelper.COLUMN_RECORD_PATH)),
//                cursor.getInt(cursor.getColumnIndex(DBManagerHelper.COLUMN_RECORD_STUDENT_ID)),
//                cursor.getInt(cursor.getColumnIndex(DBManagerHelper.COLUMN_RECORD_DURATION)),
//        );
    }
}
