package com.example.mertz.simplejournal.storage;

/**
 * Storage contract for SimpleJournal storage.
 */
final class JournalStorageContract {
    private JournalStorageContract() {}

    /**
     * Table and columns names for journal entries.
     */
    static class JournalEntry {
        static final String TABLE_NAME_GRATEFULNESS = "gratefulness";
        static final String TABLE_NAME_GOALS = "goals";
        static final String TABLE_NAME_AFFIRMATIONS = "affirmations";
        static final String TABLE_NAME_WINS = "wins";
        static final String TABLE_NAME_IMPROVEMENT = "improvement";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_NUMBER = "number";
        static final String COLUMN_NAME_VALUE = "value";
    }
}
