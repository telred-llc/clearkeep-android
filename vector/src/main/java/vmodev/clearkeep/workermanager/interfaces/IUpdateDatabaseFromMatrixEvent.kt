package vmodev.clearkeep.workermanager.interfaces

interface IUpdateDatabaseFromMatrixEvent {
    fun updateRoomName(roomId : String, roomName : String);
    fun insertMessage(id : String, content : String, type : String, roomId: String, userId : String);
}