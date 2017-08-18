package io.ipoli.android.store

import dagger.Module
import dagger.Provides
import io.ipoli.android.player.PlayerRepository
import io.ipoli.android.player.RealmPlayerRepository

/**
 * Created by Polina Zhelyazkova <polina@ipoli.io>
 * on 8/18/17.
 */
@Module
class StoreModule {
    @Provides
    @StoreScope
    fun providePlayerRepository(): PlayerRepository = RealmPlayerRepository()

    @Provides
    @StoreScope
    fun provideDisplayCoinsUseCase(playerRepository: PlayerRepository): DisplayCoinsUseCase = DisplayCoinsUseCase(playerRepository)
}