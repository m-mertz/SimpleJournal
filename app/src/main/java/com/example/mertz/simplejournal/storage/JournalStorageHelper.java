package com.example.mertz.simplejournal.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SimpleJournal storage helper for database management.
 */
class JournalStorageHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SimpleJournal.db";

    private static JournalStorageHelper s_instance;

    static synchronized JournalStorageHelper GetInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (s_instance == null) {
            s_instance = new JournalStorageHelper(context.getApplicationContext());
        }
        return s_instance;
    }

    private JournalStorageHelper(Context context) {
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
        "CREATE TABLE " + JournalStorageContract.JournalEntry.TABLE_NAME_GRATEFULNESS + " (" +
            JournalStorageContract.JournalEntry.COLUMN_NAME_DATE + " TEXT," +
            JournalStorageContract.JournalEntry.COLUMN_NAME_NUMBER + " INTEGER," +
            JournalStorageContract.JournalEntry.COLUMN_NAME_VALUE + " TEXT," +
            "PRIMARY KEY (" +
            JournalStorageContract.JournalEntry.COLUMN_NAME_DATE + "," +
            JournalStorageContract.JournalEntry.COLUMN_NAME_NUMBER + "));";
}
