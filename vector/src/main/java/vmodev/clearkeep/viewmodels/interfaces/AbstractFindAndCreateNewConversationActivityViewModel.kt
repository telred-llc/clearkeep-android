package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractFindAndCreateNewConversationActivityViewModel : ViewModel() {
    abstract fun getUsers() : LiveData<Resource<List<User>>>;
    abstract fun getInviteUserToDirectChat() : LiveData<Resource<Room>>;
    abstract fun setQuery(query : String);
    abstract fun setInviteUserToDirectChat(userId : String);
    abstract fun joinRoomResult() : LiveData<Resource<Room>>;
    abstract fun setJoinRoom(roomId : String);
}