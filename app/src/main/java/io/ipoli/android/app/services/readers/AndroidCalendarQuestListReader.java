package io.ipoli.android.app.services.readers;

import android.text.TextUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.ipoli.android.Constants;
import io.ipoli.android.app.utils.Time;
import io.ipoli.android.quest.data.Quest;
import io.ipoli.android.quest.data.RepeatingQuest;
import io.ipoli.android.quest.data.SourceMapping;
import io.ipoli.android.quest.persistence.QuestPersistenceService;
import io.ipoli.android.quest.persistence.RepeatingQuestPersistenceService;
import me.everything.providers.android.calendar.Event;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 5/11/16.
 */
public class AndroidCalendarQuestListReader implements AndroidCalendarListReader<Quest> {

    private final QuestPersistenceService questPersistenceService;
    private final RepeatingQuestPersistenceService repeatingQuestPersistenceService;

    public AndroidCalendarQuestListReader(QuestPersistenceService questPersistenceService, RepeatingQuestPersistenceService repeatingQuestPersistenceService) {
        this.questPersistenceService = questPersistenceService;
        this.repeatingQuestPersistenceService = repeatingQuestPersistenceService;
    }

    @Override
    public List<Quest> read(List<Event> events) {
        List<Quest> res = new ArrayList<>();
        for (Event e : events) {
            if (e.allDay) {
                continue;
            }
            Quest q = new Quest(e.title);
            Quest foundQuest = questPersistenceService.findByExternalSourceMappingIdSync(Constants.EXTERNAL_SOURCE_ANDROID_CALENDAR, String.valueOf(e.id));
            if (foundQuest != null) {
                q.setId(foundQuest.getId());
                q.setCreatedAt(foundQuest.getCreatedAt());
                q.setRemoteId(foundQuest.getRemoteId());
            }
            res.add(q);
            DateTime startDateTime = new DateTime(e.dTStart, DateTimeZone.forID(e.eventTimeZone));
            DateTime endDateTime = new DateTime(e.dTend, DateTimeZone.forID(e.eventTimeZone));
            q.setAllDay(e.allDay);
//            if (e.allDay) {
//                q.setDuration((int) TimeUnit.DAYS.toMinutes(1));
//                q.setStartMinute(0);
//                startDateTime = startDateTime.plusDays(1);
//                endDateTime = endDateTime.minusMillis(1);
//            } else {
            q.setDuration(Minutes.minutesBetween(startDateTime, endDateTime).getMinutes());
            q.setStartMinute(startDateTime.getMinuteOfDay());
//            }
            q.setStartDateFromLocal(startDateTime.toLocalDate().toDate());
            q.setEndDateFromLocal(endDateTime.toLocalDate().toDate());
            q.setSource(Constants.SOURCE_ANDROID_CALENDAR);
            q.setSourceMapping(SourceMapping.fromGoogleCalendar(e.id));

            if (endDateTime.toLocalDate().isBefore(new LocalDate())) {
                q.setCompletedAt(new Date(e.dTend));
                q.setCompletedAtMinute(Time.of(q.getCompletedAt()).toMinutesAfterMidnight());
            }
            if (TextUtils.isEmpty(e.originalId)) {
                continue;
            }
            RepeatingQuest h = repeatingQuestPersistenceService.findByExternalSourceMappingIdSync(Constants.EXTERNAL_SOURCE_ANDROID_CALENDAR, e.originalId);
            q.setRepeatingQuest(h);
        }
        return res;
    }

}
