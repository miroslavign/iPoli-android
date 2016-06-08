package io.ipoli.android.quest.persistence;

import org.joda.time.LocalDate;

import java.util.List;

import io.ipoli.android.app.persistence.PersistenceService;
import io.ipoli.android.quest.data.Quest;
import io.ipoli.android.quest.data.RepeatingQuest;
import rx.Observable;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 1/7/16.
 */
public interface QuestPersistenceService extends PersistenceService<Quest> {
    Observable<Quest> findById(String id);

    Observable<List<Quest>> findAllUnplanned();

    Observable<List<Quest>> findPlannedNonAllDayBetween(LocalDate startDate, LocalDate endDate);

    Observable<List<Quest>> findAllCompletedNonAllDayBetween(LocalDate startDate, LocalDate endDate);

    Observable<List<Quest>> findAllPlannedAndStartedToday();

    Observable<List<Quest>> findAllIncompleteToDosBefore(LocalDate localDate);

    Observable<List<Quest>> findPlannedQuestsStartingAfter(LocalDate localDate);

    long countCompletedQuests(RepeatingQuest repeatingQuest, LocalDate fromDate, LocalDate toDate);

    Observable<List<Quest>> findAllNonAllDayForDate(LocalDate currentDate);

    Observable<List<Quest>> findAllNonAllDayCompletedForDate(LocalDate currentDate);

    Observable<List<Quest>> findAllNonAllDayIncompleteForDate(LocalDate currentDate);

    Observable<List<Quest>> findAllForRepeatingQuest(RepeatingQuest repeatingQuest);

    long countAllForRepeatingQuest(RepeatingQuest repeatingQuest, LocalDate startDate, LocalDate endDate);

    List<Quest> findAllNonAllDayIncompleteForDateSync(LocalDate currentDate);

    Quest findByExternalSourceMappingIdSync(String source, String sourceId);

    Observable<Quest> findByExternalSourceMappingId(String source, String sourceId);
}
