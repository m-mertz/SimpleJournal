package com.example.mertz.simplejournal.storage;

import java.util.Date;

/**
 * Goal entry data type.
 */

public class GoalEntry extends JournalEntryBase {

    public GoalEntry(Date date, int number, String value) {
        super(date, number, value);
    }
}
