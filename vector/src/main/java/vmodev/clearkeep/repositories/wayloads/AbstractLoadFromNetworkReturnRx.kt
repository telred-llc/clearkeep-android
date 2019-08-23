package vmodev.clearkeep.repositories.wayloads

import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

abstract class AbstractLoadFromNetworkReturnRx<T> @MainThread constructor() {
    private val result: PublishSubject<T> = PublishSubject.create();

    init {
        createCall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ t ->
            Completable.fromAction {
                saveCallResult(t);
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                result.onNext(t);
                result.onComplete();
            };
        }, { e ->
            result.onError(e);
            result.onComplete();
        })
    }

    fun getObject() = result as Observable<T>;

    @MainThread
    abstract fun createCall(): Observable<T>;

    @WorkerThread
    abstract fun saveCallResult(item: T);
}