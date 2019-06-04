package vmodev.clearkeep.fragments.Interfaces

interface IListRoomOnFragmentInteractionListener {
    fun onClickItemJoin(roomId: String);
    fun onClickItemDecline(roomId: String);
    fun onClickItemPreview(roomId: String);
    fun onClickGoRoom(roomId: String);
}