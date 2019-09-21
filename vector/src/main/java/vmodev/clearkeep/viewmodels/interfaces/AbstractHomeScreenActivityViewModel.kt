package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import vmodev.clearkeep.rests.models.responses.PassphraseResponse
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractHomeScreenActivityViewModel : ViewModel() {
    abstract fun getUserById(): LiveData<Resource<User>>;
    abstract fun getListRoomByType(): LiveData<Resource<List<Room>>>;
    abstract fun setValueForUserById(userId: String);
    abstract fun setValueForListRoomType(filters: Array<Int>)
    abstract fun setValueForListRoomTypeFavourite(filters: Array<Int>);
    abstract fun getListRoomTypeFavouriteResult(): LiveData<Resource<List<Room>>>;
    abstract fun setValueForGetBackupStatus(time: Long);
    abstract fun getBackupKeyStatusResult(): LiveData<Resource<Int>>;
    abstract fun getPassphrase(): Observable<PassphraseResponse>;
    abstract fun createNewPassphrase(passphrase : String): Observable<PassphraseResponse>;
}