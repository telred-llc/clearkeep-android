package vmodev.clearkeep.repositories.wayloads

import android.support.annotation.MainThread
import android.util.Log
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject

abstract class AbstractNetworkBoundSourceReturnRx<T, V> @MainThread constructor() {
    private val asyncSubject: AsyncSubject<T> = AsyncSubject.create();

    init {
        loadFromDb().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : SingleObserver<T> {
            override fun onSuccess(t: T) {
                asyncSubject.onNext(t);
                asyncSubject.onComplete();
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                createCall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    Completable.fromAction {
                        saveCallResult(it);
                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                loadFromDb().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
        })
    }

    fun getObject() = asyncSubject as Observable<V>;
    abstract fun shouldFetch(data: T?): Boolean;
    abstract fun saveCallResult(item: V);
    abstract fun loadFromDb(): Single<T>;
    abstract fun createCall(): Observable<V>;
}