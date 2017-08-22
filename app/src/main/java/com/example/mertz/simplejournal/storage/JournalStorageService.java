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
    public void AddOrUpdateGratefulnessEntry(GratefulnessEntry entry) {
        AddOrUpdateEntry(JournalStorageContract.JournalEntry.TABLE_NAME_GRATEFULNESS, entry);
    }

    @Override
    public List<GratefulnessEntry> GetGratefulnessEntries(Date date) {

        // This could be a simple lambda to implement the functional factory interface, but requires Java 8.
        JournalEntryFactory<GratefulnessEntry> factory = new JournalEntryFactory<GratefulnessEntry>() {
            @Override
            public GratefulnessEntry Create(Date dateTmp, int number, String value) {
                return new GratefulnessEntry(dateTmp, number, value);
            }
        };

        return GetEntries(JournalStorageContract.JournalEntry.TABLE_NAME_GRATEFULNESS, date, factory);
    }

    @Override
    public void AddOrUpdateGoalEntry(GoalEntry entry) {
        AddOrUpdateEntry(JournalStorageContract.JournalEntry.TABLE_NAME_GOALS, entry);
    }

    @Override
    public List<GoalEntry> GetGoalEntries(Date date) {

        // This could be a simple lambda to implement the functional factory interface, but requires Java 8.
        JournalEntryFactory<GoalEntry> factory = new JournalEntryFactory<GoalEntry>() {
            @Override
            public GoalEntry Create(Date dateTmp, int number, String value) {
                return new GoalEntry(dateTmp, number, value);
            }
        };

        return GetEntries(JournalStorageContract.JournalEntry.TABLE_NAME_GOALS, date, factory);
    }

    @Override
    public void AddOrUpdateAffirmationEntry(AffirmationEntry entry) {
        AddOrUpdateEntry(JournalStorageContract.JournalEntry.TABLE_NAME_AFFIRMATIONS, entry);
    }

    @Override
    public List<AffirmationEntry> GetAffirmationEntries(Date date) {

        // This could be a simple lambda to implement the functional factory interface, but requires Java 8.
        JournalEntryFactory<AffirmationEntry> factory = new JournalEntryFactory<AffirmationEntry>() {
            @Override
            public AffirmationEntry Create(Date dateTmp, int number, String value) {
                return new AffirmationEntry(dateTmp, number, value);
            }
        };

        return GetEntries(JournalStorageContract.JournalEntry.TABLE_NAME_AFFIRMATIONS, date, factory);
    }

    @Override
    public void AddOrUpdateWinEntry(WinEntry entry) {
        AddOrUpdateEntry(JournalStorageContract.JournalEntry.TABLE_NAME_WINS, entry);
    }

    @Override
    public List<WinEntry> GetWinEntries(Date date) {

        // This could be a simple lambda to implement the functional factory interface, but requires Java 8.
        JournalEntryFactory<WinEntry> factory = new JournalEntryFactory<WinEntry>() {
            @Override
            public WinEntry Create(Date dateTmp, int number, String value) {
                return new WinEntry(dateTmp, number, value);
            }
        };

        return GetEntries(JournalStorageContract.JournalEntry.TABLE_NAME_WINS, date, factory);
    }

    @Override
    public void AddOrUpdateImprovementEntry(ImprovementEntry entry) {
        AddOrUpdateEntry(JournalStorageContract.JournalEntry.TABLE_NAME_IMPROVEMENT, entry);
    }

    @Override
    public List<ImprovementEntry> GetImprovementEntries(Date date) {

        // This could be a simple lambda to implement the functional factory interface, but requires Java 8.
        JournalEntryFactory<ImprovementEntry> factory = new JournalEntryFactory<ImprovementEntry>() {
            @Override
            public ImprovementEntry Create(Date dateTmp, int number, String value) {
                return new ImprovementEntry(dateTmp, number, value);
            }
        };

        return GetEntries(JournalStorageContract.JournalEntry.TABLE_NAME_IMPROVEMENT, date, factory);
    }

    private void AddOrUpdateEntry(String tableName, JournalEntryBase entry) {

        SQLiteDatabase db = m_storageHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(JournalStorageContract.JournalEntry.COLUMN_NAME_DATE, FormatDate(entry.Date()));
        values.put(JournalStorageContract.JournalEntry.COLUMN_NAME_NUMBER, entry.Number());
        values.put(JournalStorageContract.JournalEntry.COLUMN_NAME_VALUE, entry.Value());

        db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private <T extends JournalEntryBase> List<T> GetEntries(String tableName, Date date, JournalEntryFactory<T> entryFactory) {

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

        List<T> results = new ArrayList<>();

        while (cursor.moveToNext()) {
            int number = cursor.getInt(cursor.getColumnIndexOrThrow(
                JournalStorageContract.JournalEntry.COLUMN_NAME_NUMBER));
            String value = cursor.getString(cursor.getColumnIndexOrThrow(
                JournalStorageContract.JournalEntry.COLUMN_NAME_VALUE));

            Log.v("GetEntries", tableName + "," + FormatDate(date) + "," + number + "," + value);
            results.add(entryFactory.Create(date, number, value));
        }

        cursor.close();
        return results;
    }

    private static String FormatDate(Date date) {
        return DateFormat.format(date);
    }

    private static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

    private final JournalStorageHelper m_storageHelper;

    private interface JournalEntryFactory<T> {
        T Create(Date date, int number, String value);
    }
}
