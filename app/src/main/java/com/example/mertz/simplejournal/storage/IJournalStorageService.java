package com.example.mertz.simplejournal.storage;

import java.util.Date;
import java.util.List;

/**
 * Interface for a journal storage service.
 */

public interface IJournalStorageService {
    void AddOrUpdateGratefulnessEntry(JournalEntry entry);

    List<JournalEntry> GetGratefulnessEntries(Date date);

    void AddOrUpdateGoalEntry(JournalEntry entry);

    List<JournalEntry> GetGoalEntries(Date date);

    void AddOrUpdateAffirmationEntry(JournalEntry entry);

    List<JournalEntry> GetAffirmationEntries(Date date);

    void AddOrUpdateWinEntry(JournalEntry entry);

    List<JournalEntry> GetWinEntries(Date date);

    void AddOrUpdateImprovementEntry(JournalEntry entry);

    List<JournalEntry> GetImprovementEntries(Date date);
}
