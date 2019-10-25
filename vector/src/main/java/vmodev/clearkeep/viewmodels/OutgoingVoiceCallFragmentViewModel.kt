package vmodev.clearkeep.viewmodels

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractOutgoingVoiceCallFragmentViewModel
import javax.inject.Inject

class OutgoingVoiceCallFragmentViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractOutgoingVoiceCallFragmentViewModel() {
    private val _roomId = MutableLiveData<String>();
    private val _roomResult = Transformations.switchMap(_roomId) { input -> roomRepository.loadRoom(input) }
    override fun setRoomId(roomId: String) {
        if (!TextUtils.equals(_roomId.value, roomId))
            _roomId.value = roomId;
    }

    override fun getRoomResult(): LiveData<Resource<Room>> {
        return _roomResult;
    }
}