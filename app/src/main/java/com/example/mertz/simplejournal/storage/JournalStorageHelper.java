package com.example.mertz.simplejournal.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SimpleJournal storage helper for database management.
 */
public class JournalStorageHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SimpleJournal.db";

    public JournalStorageHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_GRATEFULNESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != DATABASE_VERSION || newVersion != DATABASE_VERSION) {
            throw new IllegalArgumentException("Database upgrades not supported, should not happen");
        }
    }

    private static final String SQL_CREATE_GRATEFULNESS_TABLE =
        "CREATE TABLE " + JournalStorageContract.GratefulnessEntry.TABLE_NAME + " (" +
            JournalStorageContract.GratefulnessEntry._ID + " INTEGER PRIMARY KEY," +
            JournalStorageContract.GratefulnessEntry.COLUMN_NAME_DATE + " TEXT," + // TODO proper date type? // TODO index for fast access?
            JournalStorageContract.GratefulnessEntry.COLUMN_NAME_NUMBER + " INTEGER," +
            JournalStorageContract.GratefulnessEntry.COLUMN_NAME_VALUE + " TEXT)";
}
