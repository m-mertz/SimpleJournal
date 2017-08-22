package com.example.mertz.simplejournal.storage;

import java.util.Date;

/**
 * Gratefulness entry data type.
 */

public class GratefulnessEntry extends JournalEntryBase {

    public GratefulnessEntry(Date date, int number, String value) {
        super(date, number, value);
    }
}
