package vmodev.clearkeep.repositories.wayloads

import android.support.annotation.MainThread
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject

abstract class AbstractNetworkCreateOrUpdateSourceReturnRx<T, V> @MainThread constructor() {
    private val asyncSubject: AsyncSubject<V> = AsyncSubject.create();

    init {
        loadFromDb().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(object : SingleObserver<V> {
            override fun onSuccess(t: V) {
                createCall().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe({ remote ->
                    Completable.fromAction {
                        updateItem(remote);
                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                        loadFromDb().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : SingleObserver<V>{
                                    override fun onSuccess(t: V) {
                                        asyncSubject.onNext(t);
                                        asyncSubject.onComplete();
                                    }

                                    override fun onSubscribe(d: Disposable) {

                                    }

                                    override fun onError(e: Throwable) {
                                        asyncSubject.onError(e);
                                        asyncSubject.onComplete();
                                    }
                                });
                    }, {
                        asyncSubject.onError(it);
                        asyncSubject.onComplete();
                    })
                }, {
                    asyncSubject.onError(it);
                    asyncSubject.onComplete();
                })
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                createCall().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe({ remote ->
                    Completable.fromAction {
                        createNewItem(remote);
                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                        loadFromDb().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : SingleObserver<V>{
                                    override fun onSuccess(t: V) {
                                        asyncSubject.onNext(t);
                                        asyncSubject.onComplete();
                                    }

                                    override fun onSubscribe(d: Disposable) {

                                    }

                                    override fun onError(e: Throwable) {
                                        asyncSubject.onError(e);
                                        asyncSubject.onComplete();
                                    }
                                });
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
    abstract fun loadFromDb(): Single<V>;
    abstract fun updateOrCreate(item: V): Boolean;
    abstract fun createNewItem(item: T);
    abstract fun updateItem(item: T);
    abstract fun createCall(): Observable<T>;
}