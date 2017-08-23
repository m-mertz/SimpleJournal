package com.example.mertz.simplejournal.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Storage service implementation for SimpleJournal.
 */
public class JournalStorageService implements IJournalStorageService {

    public JournalStorageService(Context context) {
        m_storageHelper = JournalStorageHelper.GetInstance(context);
    }

    @Override
    public void AddOrUpdateGratefulnessEntry(JournalEntry entry) {
        AddOrUpdateEntry(JournalStorageContract.JournalEntry.TABLE_NAME_GRATEFULNESS, entry);
    }

    @Override
    public List<JournalEntry> GetGratefulnessEntries(Date date) {
        return GetEntries(JournalStorageContract.JournalEntry.TABLE_NAME_GRATEFULNESS, date);
    }

    @Override
    public void AddOrUpdateGoalEntry(JournalEntry entry) {
        AddOrUpdateEntry(JournalStorageContract.JournalEntry.TABLE_NAME_GOALS, entry);
    }

    @Override
    public List<JournalEntry> GetGoalEntries(Date date) {
        return GetEntries(JournalStorageContract.JournalEntry.TABLE_NAME_GOALS, date);
    }

    @Override
    public void AddOrUpdateAffirmationEntry(JournalEntry entry) {
        AddOrUpdateEntry(JournalStorageContract.JournalEntry.TABLE_NAME_AFFIRMATIONS, entry);
    }

    @Override
    public List<JournalEntry> GetAffirmationEntries(Date date) {
        return GetEntries(JournalStorageContract.JournalEntry.TABLE_NAME_AFFIRMATIONS, date);
    }

    @Override
    public void AddOrUpdateWinEntry(JournalEntry entry) {
        AddOrUpdateEntry(JournalStorageContract.JournalEntry.TABLE_NAME_WINS, entry);
    }

    @Override
    public List<JournalEntry> GetWinEntries(Date date) {
        return GetEntries(JournalStorageContract.JournalEntry.TABLE_NAME_WINS, date);
    }

    @Override
    public void AddOrUpdateImprovementEntry(JournalEntry entry) {
        AddOrUpdateEntry(JournalStorageContract.JournalEntry.TABLE_NAME_IMPROVEMENT, entry);
    }

    @Override
    public List<JournalEntry> GetImprovementEntries(Date date) {
        return GetEntries(JournalStorageContract.JournalEntry.TABLE_NAME_IMPROVEMENT, date);
    }

    private void AddOrUpdateEntry(String tableName, JournalEntry entry) {

        SQLiteDatabase db = m_storageHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(JournalStorageContract.JournalEntry.COLUMN_NAME_DATE, FormatDate(entry.Date()));
        values.put(JournalStorageContract.JournalEntry.COLUMN_NAME_NUMBER, entry.Number());
        values.put(JournalStorageContract.JournalEntry.COLUMN_NAME_VALUE, entry.Value());

        db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private List<JournalEntry> GetEntries(String tableName, Date date) {

        SQLiteDatabase db = m_storageHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            JournalStorageContract.JournalEntry.COLUMN_NAME_NUMBER,
            JournalStorageContract.JournalEntry.COLUMN_NAME_VALUE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = JournalStorageContract.JournalEntry.COLUMN_NAME_DATE + " = ?";

        String[] selectionArgs = {
            FormatDate(date)
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
            JournalStorageContract.JournalEntry.COLUMN_NAME_NUMBER + " ASC";

        Cursor cursor = db.query(
            tableName,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder);

        List<JournalEntry> results = new ArrayList<>();

        while (cursor.moveToNext()) {
            int number = cursor.getInt(cursor.getColumnIndexOrThrow(
                JournalStorageContract.JournalEntry.COLUMN_NAME_NUMBER));
            String value = cursor.getString(cursor.getColumnIndexOrThrow(
                JournalStorageContract.JournalEntry.COLUMN_NAME_VALUE));

            Log.v("GetEntries", tableName + "," + FormatDate(date) + "," + number + "," + value);
            results.add(new JournalEntry(date, number, value));
        }

        cursor.close();
        return results;
    }

    private static String FormatDate(Date date) {
        return DateFormat.format(date);
    }

    private static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

    private final JournalStorageHelper m_storageHelper;
}
