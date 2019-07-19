package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractFavouritesFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractListRoomFragmentViewModel
import javax.inject.Inject

class FavouritesFragmentViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractFavouritesFragmentViewModel() {
    private val _listType = MutableLiveData<Array<Int>>();
    private val listRoomByType = Transformations.switchMap(_listType) { input -> roomRepository.loadListRoomUserJoin(input) }
    private val _removeFromFavourite = MutableLiveData<String>();
    private val removeFromFavouriteResult = Transformations.switchMap(_removeFromFavourite) { input -> roomRepository.removeFromFavourite(input) }
    private val _leaveRoom = MutableLiveData<String>();
    private val leaveRoomResult = Transformations.switchMap(_leaveRoom) { input -> roomRepository.leaveRoom(input) }
    private val _listGroupFavourites = MutableLiveData<Array<Int>>();
    private val _getListGroupFavouritesResult = Transformations.switchMap(_listGroupFavourites) { input -> roomRepository.loadListRoomUserJoin(input) }
    private val _roomIdForUpdateNotify = MutableLiveData<String>();
    private val _updateRoomNotifyResult = Transformations.switchMap(_roomIdForUpdateNotify) { input -> roomRepository.setRoomNotify(input) }
    private val _setChangeNotificationState = MutableLiveData<ChangeNotificationStateObject>();
    private val _changeNotificationStateResult = Transformations.switchMap(_setChangeNotificationState) { input -> roomRepository.changeNotificationState(input.roomId, input.state) }
    override fun getListTypeFavouritesDirectResult(): LiveData<Resource<List<Room>>> {
        return listRoomByType;
    }

    override fun setListTypeFavouritesDirect(types: Array<Int>) {
        _listType.value = types;
    }

    override fun getRemoveFromFavouriteResult(): LiveData<Resource<Room>> {
        return removeFromFavouriteResult;
    }

    override fun setRemoveFromFavourite(roomId: String) {
        _removeFromFavourite.value = roomId;
    }

    override fun getLeaveRoom(): LiveData<Resource<String>> {
        return leaveRoomResult;
    }

    override fun setLeaveRoom(roomId: String) {
        _leaveRoom.value = roomId;
    }

    override fun setListTypeFavouritesGroup(types: Array<Int>) {
        _listGroupFavourites.value = types;
    }

    override fun getListTypeFavouritesGroupResult(): LiveData<Resource<List<Room>>> {
        return _getListGroupFavouritesResult;
    }

    override fun setIdForUpdateRoomNotify(roomId: String) {
        _roomIdForUpdateNotify.value = roomId;
    }

    override fun getUpdateRoomNotifyResult(): LiveData<Resource<Room>> {
        return _updateRoomNotifyResult;
    }
    override fun setChangeNotificationState(roomId: String, state: Byte) {
        _setChangeNotificationState.value = ChangeNotificationStateObject(roomId, state);
    }

    override fun getChangeNotificationStateResult(): LiveData<Resource<Room>> {
        return _changeNotificationStateResult;
    }
}