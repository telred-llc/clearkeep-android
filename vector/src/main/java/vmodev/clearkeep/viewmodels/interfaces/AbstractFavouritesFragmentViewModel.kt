package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractFavouritesFragmentViewModel : ViewModel() {
    abstract fun getListTypeFavouritesDirectResult(): LiveData<Resource<List<Room>>>;
    abstract fun setListTypeFavouritesDirect(types: Array<Int>);
    abstract fun getRemoveFromFavouriteResult(): LiveData<Resource<Room>>;
    abstract fun setRemoveFromFavourite(roomId: String);
    abstract fun getLeaveRoom(): LiveData<Resource<String>>;
    abstract fun setLeaveRoom(roomId: String);
    abstract fun setListTypeFavouritesGroup(types: Array<Int>)
    abstract fun getListTypeFavouritesGroupResult(): LiveData<Resource<List<Room>>>
    abstract fun setIdForUpdateRoomNotify(roomId: String);
    abstract fun getUpdateRoomNotifyResult(): LiveData<Resource<Room>>;
}