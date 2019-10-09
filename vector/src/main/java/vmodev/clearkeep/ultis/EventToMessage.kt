package vmodev.clearkeep.ultis

import org.matrix.androidsdk.rest.model.Event
import vmodev.clearkeep.viewmodelobjects.Message

fun Event.toMessage() : Message{
    return Message(this.eventId, this.contentJson.toString(), this.type, this.roomId, this.sender, this.originServerTs);
}