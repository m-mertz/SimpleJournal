package com.example.mertz.simplejournal.storage;

import java.util.Date;

/**
 * Affirmation entry data type.
 */

public class AffirmationEntry extends JournalEntryBase {

    public AffirmationEntry(Date date, int number, String value) {
        super(date, number, value);
    }
}
