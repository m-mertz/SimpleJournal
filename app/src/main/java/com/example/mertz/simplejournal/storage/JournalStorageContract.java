package com.example.mertz.simplejournal.storage;

/**
 * Storage contract for SimpleJournal storage.
 */
final class JournalStorageContract {
    private JournalStorageContract() {}

    /**
     * Table and columns names for gratefulness entries.
     */
    static class GratefulnessEntry {
        static final String TABLE_NAME = "gratefulness";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_NUMBER = "number";
        static final String COLUMN_NAME_VALUE = "value";
    }
}
