package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Observable
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.rests.models.responses.PassphraseResponse
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenActivityViewModel
import javax.inject.Inject

class HomeScreenActivityViewModel @Inject constructor(userRepository: UserRepository, roomRepository: RoomRepository, private val keyBackupRepository: KeyBackupRepository) : AbstractHomeScreenActivityViewModel() {
    private val _userId = MutableLiveData<String>();
    private val _filters = MutableLiveData<Array<Int>>();
    private val _filtersFavourite = MutableLiveData<Array<Int>>();
    private val _setValueForGetBackupKeyStatus = MutableLiveData<Long>();

    private val userById = Transformations.switchMap(_userId) { input -> userRepository.loadUser(input) }
    private val listRoomByType = Transformations.switchMap(_filters) { input -> roomRepository.loadListRoom(input) }
    private val _getListFavouriteResult = Transformations.switchMap(_filtersFavourite) { input -> roomRepository.loadListRoom(input) }
    private val _getBackupStatusResult = Transformations.switchMap(_setValueForGetBackupKeyStatus) { input -> keyBackupRepository.getBackupKeyStatusWhenSignIn() }

    override fun getUserById(): LiveData<Resource<User>> {
        return userById;
    }

    override fun getListRoomByType(): LiveData<Resource<List<Room>>> {
        return listRoomByType;
    }

    override fun setValueForUserById(userId: String) {
        _userId.value = userId;
    }

    override fun setValueForListRoomType(filters: Array<Int>) {
        _filters.value = filters;
    }

    override fun setValueForListRoomTypeFavourite(filters: Array<Int>) {
        _filtersFavourite.value = filters;
    }

    override fun getListRoomTypeFavouriteResult(): LiveData<Resource<List<Room>>> {
        return _getListFavouriteResult;
    }

    override fun setValueForGetBackupStatus(time: Long) {
        if (_setValueForGetBackupKeyStatus.value != time)
            _setValueForGetBackupKeyStatus.value = time;
    }

    override fun getBackupKeyStatusResult(): LiveData<Resource<Int>> {
        return _getBackupStatusResult;
    }

    override fun getPassphrase(): Observable<PassphraseResponse> {
        return keyBackupRepository.getPassphrase();
    }

    override fun createNewPassphrase(passphrase: String): Observable<PassphraseResponse> {
        return keyBackupRepository.createPassphrase(passphrase);
    }
}