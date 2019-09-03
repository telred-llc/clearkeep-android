package vmodev.clearkeep.repositories.wayloads

import android.annotation.SuppressLint
import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject

abstract class AbstractNetworkCreateAndUpdateSourceReturnRx<T, V> @MainThread constructor() {
    private val asyncSubject: AsyncSubject<V> = AsyncSubject.create();

    init {
        loadFromDb().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<V> {
                    @SuppressLint("CheckResult")
                    override fun onSuccess(t: V) {
                        createCall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ remote ->
                                    val one = Observable.create<Int> {
                                        insertResult(getInsertItem(remote, null));
                                        it.onNext(1);
                                        it.onComplete();
                                    };
                                    val two = Observable.create<Int> {
                                        updateResult(getUpdateItem(remote, null));
                                        it.onNext(2);
                                        it.onComplete();
                                    };

                                    one.zipWith(two, BiFunction<Int, Int, Int> { t1, t2 -> t1 + t2; }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
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
                                })
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        createCall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ remote ->
                                    val one = Observable.create<Int> {
                                        insertResult(getInsertItem(remote, null));
                                        it.onNext(1);
                                        it.onComplete();
                                    };
                                    val two = Observable.create<Int> {
                                        updateResult(getUpdateItem(remote, null));
                                        it.onNext(2);
                                        it.onComplete();
                                    };

                                    one.zipWith(two, BiFunction<Int, Int, Int> { t1, t2 -> t1 + t2; }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
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
                                })
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