package com.example.mertz.simplejournal.storage;

import android.provider.BaseColumns;

/**
 * Storage contract for SimpleJournal storage.
 *
 *
 * TODO: this should be internal to storage implementation, no need to expose this because nobody
 * should compose their own SQL queries in random code, all access should be through storage adapter.
 */
public final class JournalStorageContract {
    private JournalStorageContract() {}

    /**
     * Table and columns names for gratefulness entries.
     */
    public static class GratefulnessEntry {
        public static final String TABLE_NAME = "gratefulness";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_NUMBER = "number";
        public static final String COLUMN_NAME_VALUE = "value";
    }
}
