package com.example.mertz.simplejournal.storage;

import java.util.Date;
import java.util.List;

/**
 * Interface for a journal storage service.
 */

public interface IJournalStorageService {
    void AddOrUpdateGratefulnessEntry(GratefulnessEntry entry);

    List<GratefulnessEntry> GetGratefulnessEntries(Date date);
}
