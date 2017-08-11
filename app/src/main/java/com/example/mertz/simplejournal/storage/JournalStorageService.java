package com.example.mertz.simplejournal.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Storage service implementation for SimpleJournal.
 *
 * TODO intent service or something? what's the proper Android thing to use for a storage backend?
 * TODO interface
 */
public class JournalStorageService {
    public JournalStorageService(Context context) {
        // TODO is this expensive? lazy in background?
        m_storageHelper = new JournalStorageHelper(context);
    }

    public void Close() {
        m_storageHelper.close();
    }

    // TODO async method
    public void AddOrUpdateGratefulnessEntry(String date, int number, String value) {
        // TODO lazy and cached
        SQLiteDatabase db = m_storageHelper.getWritableDatabase();

        ContentValues entry = new ContentValues();
        entry.put(JournalStorageContract.GratefulnessEntry.COLUMN_NAME_DATE, date);
        entry.put(JournalStorageContract.GratefulnessEntry.COLUMN_NAME_NUMBER, number);
        entry.put(JournalStorageContract.GratefulnessEntry.COLUMN_NAME_VALUE, value);

        // TODO add or update
        db.insert(JournalStorageContract.GratefulnessEntry.TABLE_NAME, null, entry);
    }

    // TODO GratefulnessEntry class
    // TODO single query method for all entries for a date
    // TODO async method
    public String GetGratefulnessEntry(String date, int number) {
        // TODO lazy and cached
        SQLiteDatabase db = m_storageHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            JournalStorageContract.GratefulnessEntry.COLUMN_NAME_VALUE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = JournalStorageContract.GratefulnessEntry.COLUMN_NAME_DATE + " = ? AND " +
            JournalStorageContract.GratefulnessEntry.COLUMN_NAME_NUMBER + " = ?";

        String[] selectionArgs = {
            date,
            Integer.toString(number)
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
            JournalStorageContract.GratefulnessEntry._ID + " ASC";

        Cursor cursor = db.query(
            JournalStorageContract.GratefulnessEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder);

        String value = "<no entry found>";

        while (cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndexOrThrow(
                JournalStorageContract.GratefulnessEntry.COLUMN_NAME_VALUE));
            break;
        }

        cursor.close();
        return value;
    }

    private final JournalStorageHelper m_storageHelper;
}
