package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractRoomFragmentViewModel : ViewModel() {
    abstract fun getListRoomByType(): LiveData<Resource<List<Room>>>;
    abstract fun setListType(types: Array<Int>);
    abstract fun getAddToFavouriteResult(): LiveData<Resource<Room>>;
    abstract fun setAddToFavourite(roomId: String);
    abstract fun getLeaveRoom(): LiveData<Resource<String>>;
    abstract fun setLeaveRoom(roomId: String);
}