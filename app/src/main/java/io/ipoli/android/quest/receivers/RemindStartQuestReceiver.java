package io.ipoli.android.quest.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.NotificationCompat;

import com.squareup.otto.Bus;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import io.ipoli.android.Constants;
import io.ipoli.android.R;
import io.ipoli.android.app.App;
import io.ipoli.android.app.navigation.ActivityIntentFactory;
import io.ipoli.android.app.utils.IntentUtils;
import io.ipoli.android.app.utils.StringUtils;
import io.ipoli.android.quest.activities.QuestActivity;
import io.ipoli.android.quest.data.Quest;
import io.ipoli.android.quest.persistence.QuestPersistenceService;
import io.ipoli.android.reminders.ReminderMinutesParser;
import io.ipoli.android.reminders.TimeOffsetType;
import io.ipoli.android.reminders.data.Reminder;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 3/17/16.
 */
public class RemindStartQuestReceiver extends BroadcastReceiver {

    public static final String ACTION_REMIND_START_QUEST = "io.ipoli.android.intent.action.REMIND_START_QUEST";

    @Inject
    Bus eventBus;

    @Inject
    QuestPersistenceService questPersistenceService;

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingResult result = goAsync();
        App.getAppComponent(context).inject(this);
        List<String> questIds = intent.getStringArrayListExtra(Constants.QUEST_IDS_EXTRA_KEY);
        long startTime = intent.getLongExtra(Constants.REMINDER_START_TIME, 0);
        new Thread() {
            @Override
            public void run() {
                CountDownLatch latch = new CountDownLatch(questIds.size());
                for (String questId : questIds) {
                    questPersistenceService.findById(questId, quest -> {
                        Reminder reminder = null;
                        for (Reminder r : quest.getReminders()) {
                            if (r.getStart() == startTime) {
                                reminder = r;
                                break;
                            }
                        }
                        if (reminder == null) {
                            latch.countDown();
                            return;
                        }
                        showNotification(context, quest, reminder);
                        latch.countDown();
                    });


                }
                try {
                    latch.await();
                    questPersistenceService.deleteRemindersAtTime(startTime, () -> {
                        context.sendBroadcast(new Intent(ScheduleNextRemindersReceiver.ACTION_SCHEDULE_REMINDERS));
                        result.finish();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    result.finish();
                }
            }
        }.start();
    }

    private void showNotification(Context context, Quest q, Reminder reminder) {
        Intent remindStartQuestIntent = new Intent(context, QuestActivity.class);
        remindStartQuestIntent.setAction(ACTION_REMIND_START_QUEST);
        remindStartQuestIntent.putExtra(Constants.QUEST_ID_EXTRA_KEY, q.getId());
        String name = q.getName();

        PendingIntent pendingNotificationIntent = ActivityIntentFactory.createWithParentStack(QuestActivity.class, remindStartQuestIntent, context, new Random().nextInt());

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        PendingIntent startQuestPI = getStartPendingIntent(q.getId(), reminder.getNotificationId(), context);
        PendingIntent snoozeQuestPI = getSnoozePendingIntent(q.getId(), reminder.getNotificationId(), context);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle(name)
                .setContentText(getContentText(context, reminder))
                .setContentIntent(pendingNotificationIntent)
                .setShowWhen(true)
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(largeIcon)
                .setOnlyAlertOnce(false)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_snooze_black_24dp, context.getString(R.string.snooze), snoozeQuestPI)
                .addAction(R.drawable.ic_play_arrow_black_24dp, context.getString(R.string.start).toUpperCase(), startQuestPI)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(reminder.getNotificationId(), builder.build());
    }

    private String getContentText(Context context, Reminder reminder) {
        if (StringUtils.isEmpty(reminder.getMessage())) {
            if (reminder.getMinutesFromStart() == 0) {
                return context.getString(R.string.ready_to_start);
            } else {
                Pair<Long, TimeOffsetType> parseResult = ReminderMinutesParser.parseCustomMinutes(Math.abs(reminder.getMinutesFromStart()));
                long timeValue = parseResult.first;
                TimeOffsetType timeOffsetType = parseResult.second;
                String type = timeOffsetType.name().toLowerCase();
                if (timeValue == 1) {
                    type = type.substring(0, type.length() - 1);
                }
                return "Starts in " + timeValue + " " + type;
            }
        }
        return reminder.getMessage();
    }


    private PendingIntent getStartPendingIntent(String questId, int notificationId, Context context) {
        Intent intent = new Intent(context, QuestActivity.class);
        intent.putExtra(Constants.QUEST_ID_EXTRA_KEY, questId);
        intent.putExtra(Constants.REMINDER_NOTIFICATION_ID_EXTRA_KEY, notificationId);
        intent.setAction(QuestActivity.ACTION_START_QUEST);

        return ActivityIntentFactory.createWithParentStack(QuestActivity.class, intent, context, new Random().nextInt());
    }

    private PendingIntent getSnoozePendingIntent(String questId, int notificationId, Context context) {
        Intent intent = new Intent(SnoozeQuestReceiver.ACTION_SNOOZE_QUEST);
        intent.putExtra(Constants.QUEST_ID_EXTRA_KEY, questId);
        intent.putExtra(Constants.REMINDER_NOTIFICATION_ID_EXTRA_KEY, notificationId);
        return IntentUtils.getBroadcastPendingIntent(context, intent, new Random().nextInt());
    }
}
