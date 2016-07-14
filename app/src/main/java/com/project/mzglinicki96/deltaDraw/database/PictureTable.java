package com.project.mzglinicki96.deltaDraw.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mzglinicki.96 on 01.04.2016.
 */
public final class PictureTable {

    //Define table name
    public static final String TABLE_NAME = "draws_table";

    //Define table columns
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_AUTHOR = "AUTHOR";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_POINTS = "POINTS";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_AUTHOR + " text not null, "
            + COLUMN_POINTS + " text, "
            + COLUMN_DATE + " text "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}