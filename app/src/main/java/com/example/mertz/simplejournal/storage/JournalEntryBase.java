package com.example.mertz.simplejournal.storage;

import java.util.Date;

/**
 * Base class for Journal data types.
 */

abstract class JournalEntryBase {

    JournalEntryBase(Date date, int number, String value) {
        m_date = date;
        m_number = number;
        m_value = value;
    }

    public Date Date() {
        return m_date;
    }

    public int Number() {
        return m_number;
    }

    public String Value() {
        return m_value;
    }

    private final Date m_date;

    private final int m_number;

    private final String m_value;
}
