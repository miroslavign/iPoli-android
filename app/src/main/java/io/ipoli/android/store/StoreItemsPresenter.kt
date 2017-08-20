package io.ipoli.android.store

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by Polina Zhelyazkova <polina@ipoli.io>
 * on 8/20/17.
 */
class StoreItemsPresenter @Inject constructor(private val displayCoinsUseCase: DisplayCoinsUseCase) :
    MviBasePresenter<StoreItemsController, StoreViewState>() {

    override fun bindIntents() {
        val observables = ArrayList<Observable<StoreStatePartialChange>>()
        observables.add(
            intent { it.showCoinsIntent() }.switchMap {
                displayCoinsUseCase.execute(Unit)
            })

        val allIntents: Observable<StoreStatePartialChange> = Observable.merge(observables)
        val initialState: StoreViewState = StoreLoadingState()

        val stateObservable = allIntents.scan(initialState, this::viewStateReducer)
            .observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(stateObservable, StoreItemsController::render)
    }

    private fun viewStateReducer(previousState: StoreViewState, statePartialChange: StoreStatePartialChange): StoreViewState {
        return statePartialChange.computeNewState(previousState)
    }
}