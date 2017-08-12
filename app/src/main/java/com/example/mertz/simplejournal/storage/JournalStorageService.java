package com.example.mertz.simplejournal.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
    public void AddOrUpdateGratefulnessEntry(GratefulnessEntry entry) {
        // TODO lazy and cached
        SQLiteDatabase db = m_storageHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(JournalStorageContract.GratefulnessEntry.COLUMN_NAME_DATE, entry.Date());
        values.put(JournalStorageContract.GratefulnessEntry.COLUMN_NAME_NUMBER, entry.Number());
        values.put(JournalStorageContract.GratefulnessEntry.COLUMN_NAME_VALUE, entry.Value());

        // TODO add or update
        db.insert(JournalStorageContract.GratefulnessEntry.TABLE_NAME, null, values);
    }

    // TODO GratefulnessEntry class
    // TODO single query method for all entries for a date
    // TODO async method
    public List<GratefulnessEntry> GetGratefulnessEntries(String date) {
        // TODO lazy and cached
        SQLiteDatabase db = m_storageHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            JournalStorageContract.GratefulnessEntry.COLUMN_NAME_NUMBER,
            JournalStorageContract.GratefulnessEntry.COLUMN_NAME_VALUE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = JournalStorageContract.GratefulnessEntry.COLUMN_NAME_DATE + " = ?";

        String[] selectionArgs = {
            date
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

        List<GratefulnessEntry> results = new ArrayList<GratefulnessEntry>();

        while (cursor.moveToNext()) {
            int number = cursor.getInt(cursor.getColumnIndexOrThrow(
                JournalStorageContract.GratefulnessEntry.COLUMN_NAME_NUMBER));
            String value = cursor.getString(cursor.getColumnIndexOrThrow(
                JournalStorageContract.GratefulnessEntry.COLUMN_NAME_VALUE));

            results.add(new GratefulnessEntry(date, number, value));
        }

        cursor.close();
        return results;
    }

    private final JournalStorageHelper m_storageHelper;
}
