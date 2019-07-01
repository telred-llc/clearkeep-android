package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractViewUserProfileActivityViewModel
import javax.inject.Inject

class ViewUserProfileActivityViewModel @Inject constructor(roomRepository: RoomRepository, userRepository: UserRepository) : AbstractViewUserProfileActivityViewModel() {

    private val _userIdForGetUser = MutableLiveData<String>();
    private val _userIdForCreateNewChat = MutableLiveData<String>();

    private val _getUserResult = Transformations.switchMap(_userIdForGetUser) { input -> userRepository.loadUser(input) }
    private val _createNewDirectChatResult = Transformations.switchMap(_userIdForCreateNewChat) { input -> roomRepository.createDirectChatRoom(input) }

    override fun getUserResult(): LiveData<Resource<User>> {
        return _getUserResult;
    }

    override fun createNewDirectChatResult(): LiveData<Resource<Room>> {
        return _createNewDirectChatResult;
    }

    override fun setGetUser(userId: String) {
        _userIdForGetUser.value = userId;
    }

    override fun setUserIdForCreateNewChat(userId: String) {
        _userIdForCreateNewChat.value = userId;
    }
}