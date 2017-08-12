package com.example.mertz.simplejournal.storage;

/**
 * Gratefulness entry data type.
 */

public class GratefulnessEntry {
    public GratefulnessEntry(String date, int number, String value) {
        m_date = date;
        m_number = number;
        m_value = value;
    }

    public String Date() {
        return m_date;
    }

    public int Number() {
        return m_number;
    }

    public String Value() {
        return m_value;
    }

    private final String m_date;

    private final int m_number;

    private final String m_value;
}
