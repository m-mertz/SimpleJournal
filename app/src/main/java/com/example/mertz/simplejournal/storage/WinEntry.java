package com.example.mertz.simplejournal.storage;

import java.util.Date;

/**
 * Win entry data type.
 */

public class WinEntry extends JournalEntryBase {

    public WinEntry(Date date, int number, String value) {
        super(date, number, value);
    }
}
