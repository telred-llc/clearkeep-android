package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractContactFragmentViewModel
import javax.inject.Inject

class ContactFragmentViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractContactFragmentViewModel() {
    private val _listType = MutableLiveData<Array<Int>>();
    private val listRoomByType = Transformations.switchMap(_listType) { input -> roomRepository.loadListRoomUserJoin(input) }
    override fun getListRoomByType(): LiveData<Resource<List<Room>>> {
        return listRoomByType;
    }

    override fun setListType(types: Array<Int>) {
        _listType.value = types;
    }
}