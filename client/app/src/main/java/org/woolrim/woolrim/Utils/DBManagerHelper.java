package org.woolrim.woolrim.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.woolrim.woolrim.SQLiteDAO.*;

public class DBManagerHelper {

    private static final String DATABASE_NAME = "Woolrim.db";
    private static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE_NAME = "User";
    public static final String RECORD_TABLE_NAME = "Record";
    public static final String POEM_TABLE_NAME = "Poem";
    public static final String FAVORITE_TABLE_NAME = "Favorite";

    public static final String COLUMN_USER_PRIVATE_ID = "_id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_STUDENT_ID = "student_id";
    public static final String COLUMN_USER_GENDER = "gender";
    public static final String COLUMN_USER_UNIVERSITY = "university";
    public static final String COLUMN_USER_BONGSA_TIME = "bongsa_time";

    public static final String COLUMN_RECORD_PRIVATE_ID = "_id";
    public static final String COLUMN_RECORD_NAME = "name";
    public static final String COLUMN_RECORD_PATH = "path";
    public static final String COLUMN_RECORD_STUDENT_ID = "studentId";
    public static final String COLUMN_RECORD_DURATION = "duration";

    public static final String COLUMN_POEM_PRIVATE_ID = "_id";
    public static final String COLUMN_POEM_POEM  = "poem";
    public static final String COLUMN_POEM_POET  = "poet";
    public static final String COLUMN_POEM_FULL_COUNT  = "full_count";
    public static final String COLUMN_POEM_MAN_COUNT  = "man_count";
    public static final String COLUMN_POEM_WOMAN_COUNT  = "woman_count";

    public static final String COLUMN_FAVORITE_PRIVATE_ID = "_id";
    public static final String COLUMN_FAVORITE_POEM_NAME = "poem";
    public static final String COLUMN_FAVORITE_USER_NAME = "user";



    private SQLiteDatabaseHelper sqLiteDatabaseHelper;
    private final Context mContext;
    public static UserDAO userDAO;
    public static RecordDAO recordDAO;
    public static SQLiteDatabase database;


    public DBManagerHelper(Context context) {
        mContext = context;
    }

    public DBManagerHelper openDatabase() {
        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(mContext);
        database = sqLiteDatabaseHelper.getWritableDatabase();

        userDAO = new UserDAO(database);
        recordDAO = new RecordDAO(database);
        return this;
    }

    public void closeDatabase() {
        sqLiteDatabaseHelper.close();
    }

    public static class SQLiteDatabaseHelper extends SQLiteOpenHelper {

        public static SQLiteDatabase databaseTemp;

        public SQLiteDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            database = sqLiteDatabase;
            createUserTable(sqLiteDatabase);
            createRecordTable(sqLiteDatabase);
            createFavoriteTable(sqLiteDatabase);
        }


        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                dropTable(sqLiteDatabase, USER_TABLE_NAME);
                dropTable(sqLiteDatabase, RECORD_TABLE_NAME);
                dropTable(sqLiteDatabase, FAVORITE_TABLE_NAME);

                createUserTable(sqLiteDatabase);
                createRecordTable(sqLiteDatabase);
                createFavoriteTable(sqLiteDatabase);

            }
        }

        private static void createUserTable(SQLiteDatabase db) {
            String sql = "create table if not exists " + USER_TABLE_NAME + "(" +
                    COLUMN_USER_PRIVATE_ID + " integer PRIMARY KEY autoincrement, " +
                    COLUMN_USER_NAME + " text, " +
                    COLUMN_USER_PASSWORD + " text, " +
                    COLUMN_USER_STUDENT_ID + " integer, " +
                    COLUMN_USER_GENDER + " text, " +
                    COLUMN_USER_UNIVERSITY + " text, "+
                    COLUMN_USER_BONGSA_TIME + " integer )";
            db.execSQL(sql);
        }

        private static void createRecordTable(SQLiteDatabase db) {
            String sql = "create table if not exists " + RECORD_TABLE_NAME + "(" +
                    COLUMN_RECORD_PRIVATE_ID + " integer PRIMARY KEY autoincrement, " +
                    COLUMN_RECORD_NAME + " text, " +
                    COLUMN_RECORD_PATH + " text, " +
                    COLUMN_RECORD_STUDENT_ID + " integer, " +
                    COLUMN_RECORD_DURATION + " text )";

            db.execSQL(sql);
        }

        private static void createFavoriteTable(SQLiteDatabase db ){
            String sql = "create table if not exists "+ FAVORITE_TABLE_NAME+"("+
                    COLUMN_FAVORITE_PRIVATE_ID + " integer PRIMARY KEY autoincrement, "+
                    COLUMN_FAVORITE_POEM_NAME + " text, "+
                    COLUMN_FAVORITE_USER_NAME + " text )";

            db.execSQL(sql);
        }
        public static void createPoemTable(SQLiteDatabase db){
            String sql = "create table if not exists "+ POEM_TABLE_NAME+"("+
                    COLUMN_POEM_PRIVATE_ID + " integer PRIMARY KEY autoincrement,"+
                    COLUMN_POEM_POEM + " text, "+
                    COLUMN_POEM_POET + " text, "+
                    COLUMN_POEM_MAN_COUNT + " integer, "+
                    COLUMN_POEM_WOMAN_COUNT + " integer, "+
                    COLUMN_POEM_FULL_COUNT + " integer )";

            db.execSQL(sql);
        }


        public static void dropTable(SQLiteDatabase db, String tableName) {
            db.execSQL("drop table if exists " + tableName);
        }


        public static void recycleTable() {
            dropTable(database, USER_TABLE_NAME);
            createUserTable(database);
        }
    }
}
