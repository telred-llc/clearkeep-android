package vmodev.clearkeep.repositories.wayloads

import android.support.annotation.MainThread
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject

abstract class AbstractNetworkCreateAndUpdateSourceReturnRx<T, V> @MainThread constructor() {
    private val asyncSubject: AsyncSubject<V> = AsyncSubject.create();

    init {
        loadFromDb().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<V> {
                    override fun onSuccess(t: V) {
                        createCall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ remote ->
                                    Completable.fromAction {
                                        insertResult(getInsertItem(remote, t));
                                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                loadFromDb().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(object : SingleObserver<V> {
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
                                            });
                                    Completable.fromAction {
                                        updateResult(getUpdateItem(remote, t));
                                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                loadFromDb().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(object : SingleObserver<V> {
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
                                            });
                                }, {
                                    asyncSubject.onError(it);
                                    asyncSubject.onComplete();
                                });
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        createCall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ remote ->
                                    Completable.fromAction {
                                        insertResult(getInsertItem(remote, null));
                                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                loadFromDb().toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe({
                                                            asyncSubject.onNext(it);
                                                            asyncSubject.onComplete();
                                                        }, {
                                                            asyncSubject.onError(it);
                                                            asyncSubject.onComplete();
                                                        });
                                            }, {
                                                asyncSubject.onError(it);
                                                asyncSubject.onComplete();
                                            });
                                    Completable.fromAction {
                                        updateResult(getUpdateItem(remote, null));
                                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                loadFromDb().toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe({
                                                            asyncSubject.onNext(it);
                                                            asyncSubject.onComplete();
                                                        }, {
                                                            asyncSubject.onError(it);
                                                            asyncSubject.onComplete();
                                                        });
                                            }, {
                                                asyncSubject.onError(it);
                                                asyncSubject.onComplete();
                                            });
                                }, {
                                    asyncSubject.onError(it);
                                    asyncSubject.onComplete();
                                });
                    }
                });
    }

    fun getObject() = asyncSubject as Observable<V>;

    abstract fun insertResult(item: T);
    abstract fun updateResult(item: T);
    abstract fun loadFromDb(): Single<V>;
    abstract fun createCall(): Observable<T>;
    abstract fun getInsertItem(remoteItem: T, localItem: V?): T;
    abstract fun getUpdateItem(remoteItem: T, localItem: V?): T;
}