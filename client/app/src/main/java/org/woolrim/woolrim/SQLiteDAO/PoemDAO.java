package org.woolrim.woolrim.SQLiteDAO;

import android.database.sqlite.SQLiteDatabase;

import org.woolrim.woolrim.DBManagerHelper;

public class PoemDAO {

    private SQLiteDatabase database;
    public static String[] poemAllColoumns = new String[]{
            DBManagerHelper.COLUMN_POEM_PRIVATE_ID,
            DBManagerHelper.COLUMN_POEM_POEM,
            DBManagerHelper.COLUMN_POEM_POET,
            DBManagerHelper.COLUMN_POEM_MAN_COUNT,
            DBManagerHelper.COLUMN_POEM_WOMAN_COUNT,
            DBManagerHelper.COLUMN_POEM_FULL_COUNT
    };


}
