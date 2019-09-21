package vmodev.clearkeep.repositories.wayloads

import androidx.annotation.MainThread
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject

abstract class AbstractNetworkCallAndSaveToDBReturnRx<T, V> @MainThread constructor() {
    private val asyncSubject: AsyncSubject<T> = AsyncSubject.create();

    init {
        createCall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Completable.fromAction {
                saveCallResult(it);
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        loadFromDb(it).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : SingleObserver<T> {
                                    override fun onSuccess(t: T) {
                                        asyncSubject.onNext(t);
                                        asyncSubject.onComplete();
                                    }

                                    override fun onSubscribe(d: Disposable) {

                                    }

                                    override fun onError(e: Throwable) {
                                        asyncSubject.onError(e);
                                        asyncSubject.onComplete();
                                    }
                                })
                    }, {
                        asyncSubject.onError(it);
                        asyncSubject.onComplete();
                    })
        }, {
            asyncSubject.onError(it);
            asyncSubject.onComplete();
        })
    }

    fun getObject() = asyncSubject as Observable<V>;
    abstract fun saveCallResult(item: V);
    abstract fun loadFromDb(item: V): Single<T>;
    abstract fun createCall(): Observable<V>;
}