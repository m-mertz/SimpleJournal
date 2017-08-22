package com.example.mertz.simplejournal.storage;

import java.util.Date;
import java.util.List;

/**
 * Interface for a journal storage service.
 */

public interface IJournalStorageService {
    void AddOrUpdateGratefulnessEntry(GratefulnessEntry entry);

    List<GratefulnessEntry> GetGratefulnessEntries(Date date);

    void AddOrUpdateGoalEntry(GoalEntry entry);

    List<GoalEntry> GetGoalEntries(Date date);

    void AddOrUpdateAffirmationEntry(AffirmationEntry entry);

    List<AffirmationEntry> GetAffirmationEntries(Date date);

    void AddOrUpdateWinEntry(WinEntry entry);

    List<WinEntry> GetWinEntries(Date date);

    void AddOrUpdateImprovementEntry(ImprovementEntry entry);

    List<ImprovementEntry> GetImprovementEntries(Date date);
}
