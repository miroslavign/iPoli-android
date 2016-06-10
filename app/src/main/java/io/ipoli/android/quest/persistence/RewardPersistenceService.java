package io.ipoli.android.quest.persistence;

import io.ipoli.android.app.persistence.PersistenceService;
import io.ipoli.android.reward.data.Reward;

/**
 * Created by Polina Zhelyazkova <polina@ipoli.io>
 * on 5/30/16.
 */
public interface RewardPersistenceService extends PersistenceService<Reward> {

    void findAll(OnDatabaseChangedListener<Reward> listener);

    void close();
}
