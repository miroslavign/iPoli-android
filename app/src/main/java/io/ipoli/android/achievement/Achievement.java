package io.ipoli.android.achievement;

import android.support.annotation.StringRes;

import io.ipoli.android.R;

import static io.ipoli.android.R.string.achievemet_first_quest_completed_name;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 7/24/17.
 */
public enum Achievement {

    FIRST_QUEST_COMPLETED(1,
            Category.BRONZE,
            achievemet_first_quest_completed_name,
            R.string.achievemet_first_quest_completed_desc,
            0, 0),
    COMPLETE_10_QUESTS_IN_A_DAY(2,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100),
    GAIN_100_XP_IN_A_DAY(3,
            Category.GOLD,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            100, 500),
    COMPLETE_QUEST_FOR_100_DAYS_IN_A_ROW(4,
            Category.GOLD,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            100, 500),
    COMPLETE_DAILY_CHALLENGE_FOR_5_DAYS_IN_A_ROW(5,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100),
    LEVEL_15TH(6,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100),
    LEVEL_20TH(7,
            Category.GOLD,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            100, 500),
    FIRST_REPEATING_QUEST_CREATED(8,
            Category.BRONZE,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            0, 0),
    FIRST_CHALLENGE_ACCEPTED(9,
            Category.BRONZE,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            0, 0),
    FIRST_DAILY_CHALLENGE_COMPLETED(10,
            Category.BRONZE,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            0, 0),
    FIRST_POST_ADDED(11,
            Category.BRONZE,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            0, 0),
    FIRST_AVATAR_CHANGED(12,
            Category.BRONZE,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            0, 0),
    FIRST_REWARD_USED(13,
            Category.BRONZE,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            0, 0),
    FIVE_POSTS_ADDED(14,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100),
    FIRST_POWER_UP(15,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100),
    HAVE_1K_COINS(16,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100),
    INVITE_FRIEND(17,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100),
    CHANGE_PET(18,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100),
    PET_DIED(19,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100),
    FIRST_FOLLOW(20,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100),
    FIRST_FOLLOWER(21,
            Category.SILVER,
            achievemet_first_quest_completed_name,
            achievemet_first_quest_completed_name,
            15, 100);

    public final int code;

    public final Achievement.Category category;

    @StringRes
    public final int name;

    @StringRes
    public final int description;

    public final int coins;

    public final int experience;

    public enum Category {
        BRONZE, SILVER, GOLD;
    }

    Achievement(int code, Category category, int name, int description, int coins, int experience) {
        this.code = code;
        this.category = category;
        this.name = name;
        this.description = description;
        this.coins = coins;
        this.experience = experience;
    }

    public static Achievement get(int code) {
        for (Achievement achievement : values()) {
            if (achievement.code == code) {
                return achievement;
            }
        }
        return null;
    }
}
