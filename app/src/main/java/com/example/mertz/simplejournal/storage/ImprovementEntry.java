package com.example.mertz.simplejournal.storage;

import java.util.Date;

/**
 * Improvement entry data type.
 */

public class ImprovementEntry extends JournalEntryBase {

    public ImprovementEntry(Date date, int number, String value) {
        super(date, number, value);
    }
}
