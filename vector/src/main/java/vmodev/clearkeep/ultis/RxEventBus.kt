package vmodev.clearkeep.ultis

import io.reactivex.subjects.PublishSubject
import java.util.*


class RxEventBus<T> {
    private var subject: PublishSubject<T>? = PublishSubject.create<T>()


    companion object {
        private var instance: RxEventBus<*>? = null
        fun <T> instanceOf(): RxEventBus<T> {
            if(null== instance){
                instance = RxEventBus<T>()
            }
            return instance as RxEventBus<T>
        }
    }

    fun putData(objects:T ) {
        subject?.let {
            it.onNext(objects)
        }
    }

    fun getData(): PublishSubject<T>? {
        return subject

    }

}