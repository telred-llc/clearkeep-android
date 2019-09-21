package vmodev.clearkeep.ultis

import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import im.vector.R
import im.vector.VectorApp
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorRoomActivity
import im.vector.util.PreferencesManager
import im.vector.widgets.WidgetManagerProvider
import im.vector.widgets.WidgetsManager
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.activities.RoomActivity
import java.util.HashMap

class SlashCommandsParser {
//    companion object {
//        const val LOG_TAG = SlashCommandsParser::class.java.simpleName
//    }

    enum class SlashCommand private constructor(val command: String, val param: String, @param:StringRes @field:StringRes
    val description: Int) {

        // defines the command line operations
        // the user can write theses messages to perform some actions
        // the list will be displayed in this order
        EMOTE("/me", "<message>", R.string.command_description_emote),
        BAN_USER("/ban", "<user-id> [reason]", R.string.command_description_ban_user),
        UNBAN_USER("/unban", "<user-id>", R.string.command_description_unban_user),
        SET_USER_POWER_LEVEL("/op", "<user-id> [<power-level>]", R.string.command_description_op_user),
        RESET_USER_POWER_LEVEL("/deop", "<user-id>", R.string.command_description_deop_user),
        INVITE("/invite", "<user-id>", R.string.command_description_invite_user),
        JOIN_ROOM("/join", "<room-alias>", R.string.command_description_join_room),
        PART("/part", "<room-alias>", R.string.command_description_part_room),
        TOPIC("/topic", "<topic>", R.string.command_description_topic),
        KICK_USER("/kick", "<user-id> [reason]", R.string.command_description_kick_user),
        CHANGE_DISPLAY_NAME("/nick", "<display-name>", R.string.command_description_nick),
        MARKDOWN("/markdown", "<on|off>", R.string.command_description_markdown),
        CLEAR_SCALAR_TOKEN("/clear_scalar_token", "", R.string.command_description_clear_scalar_token);


        companion object {

            private val lookup = HashMap<String, SlashCommand>()

            init {
                for (slashCommand in SlashCommand.values()) {
                    lookup[slashCommand.command] = slashCommand
                }
            }

            operator fun get(command: String): SlashCommand? {
                return lookup[command]
            }
        }
    }

    /**
     * check if the text message is an IRC command.
     * If it is an IRC command, it is executed
     *
     * @param activity      the room activity
     * @param session       the session
     * @param room          the room
     * @param textMessage   the text message
     * @param formattedBody the formatted message
     * @param format        the message format
     * @return true if it is a splash command
     */
    companion object {
        fun manageSplashCommand(activity: RoomActivity?,
                                session: MXSession?,
                                room: Room?,
                                textMessage: String?,
                                formattedBody: String?,
                                format: String): Boolean {
            var isIRCCmd = false
            var isIRCCmdValid = false

            // sanity checks
            if (null == activity || null == session || null == room) {
//                Log.e(LOG_TAG, "manageSplashCommand : invalid parameters")
                return false
            }

            // check if it has the IRC marker
            if (null != textMessage && textMessage.startsWith("/")) {
//                Log.d(LOG_TAG, "manageSplashCommand : $textMessage")

                if (textMessage.length == 1) {
                    return false
                }

                if ("/" == textMessage.substring(1, 2)) {
                    return false
                }

                val callback = object : SimpleApiCallback<Void>(activity) {
                    override fun onSuccess(info: Void) {
//                        Log.d(LOG_TAG, "manageSplashCommand : $textMessage : the operation succeeded.")
                    }

                    override fun onMatrixError(e: MatrixError) {
                        if (MatrixError.FORBIDDEN == e.errcode) {
                            Toast.makeText(activity, e.error, Toast.LENGTH_LONG).show()
                        } else if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
                            activity.consentNotGivenHelper.displayDialog(e)
                        }
                    }
                }

                var messageParts: Array<String>? = null

                try {
                    messageParts = textMessage.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                } catch (e: Exception) {
//                    Log.e(LOG_TAG, "## manageSplashCommand() : split failed " + e.message, e)
                }

                // test if the string cut fails
                if (null == messageParts || 0 == messageParts.size) {
                    return false
                }

                val firstPart = messageParts[0]

                if (TextUtils.equals(firstPart, SlashCommand.CHANGE_DISPLAY_NAME.command)) {
                    isIRCCmd = true

                    val newDisplayname = textMessage.substring(SlashCommand.CHANGE_DISPLAY_NAME.command.length).trim { it <= ' ' }

                    if (newDisplayname.length > 0) {
                        isIRCCmdValid = true
                        val myUser = session.myUser

                        myUser.updateDisplayName(newDisplayname, callback)
                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.TOPIC.command)) {
                    isIRCCmd = true

                    val newTopic = textMessage.substring(SlashCommand.TOPIC.command.length).trim { it <= ' ' }

                    if (newTopic.length > 0) {
                        isIRCCmdValid = true
                        room.updateTopic(newTopic, callback)
                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.EMOTE.command)) {
                    isIRCCmd = true
                    isIRCCmdValid = true

                    val newMessage = textMessage.substring(SlashCommand.EMOTE.command.length).trim { it <= ' ' }

                    if (null != formattedBody && formattedBody.length > SlashCommand.EMOTE.command.length) {
                        activity.sendEmote(newMessage, formattedBody.substring(SlashCommand.EMOTE.command.length), format)
                    } else {
                        activity.sendEmote(newMessage, formattedBody!!, format)
                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.JOIN_ROOM.command)) {
                    isIRCCmd = true
                    val roomAlias = textMessage.substring(SlashCommand.JOIN_ROOM.command.length).trim { it <= ' ' }

                    if (roomAlias.length > 0) {
                        isIRCCmdValid = true
                        session.joinRoom(roomAlias, object : SimpleApiCallback<String>(activity) {

                            override fun onSuccess(roomId: String?) {
                                if (null != roomId) {
                                    val params = HashMap<String, Any>()
                                    params[VectorRoomActivity.EXTRA_MATRIX_ID] = session.myUserId
                                    params[VectorRoomActivity.EXTRA_ROOM_ID] = roomId

                                    CommonActivityUtils.goToRoomPage(activity, session, params)
                                }
                            }

                            override fun onMatrixError(e: MatrixError) {
                                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
                                    activity.consentNotGivenHelper.displayDialog(e)
                                } else {
                                    Toast.makeText(activity, e.error, Toast.LENGTH_LONG).show()
                                }
                            }
                        })
                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.PART.command)) {
                    isIRCCmd = true
                    val roomAlias = textMessage.substring(SlashCommand.PART.command.length).trim { it <= ' ' }

                    if (roomAlias.length > 0) {
                        isIRCCmdValid = true
                        var theRoom: Room? = null
                        val rooms = session.dataHandler.store.rooms

                        for (r in rooms) {
                            val state = r.state

                            if (null != state) {
                                if (TextUtils.equals(state.canonicalAlias, roomAlias)) {
                                    theRoom = r
                                    break
                                } else if (state.getAliases().indexOf(roomAlias) >= 0) {
                                    theRoom = r
                                    break
                                }
                            }
                        }

                        theRoom?.leave(callback)
                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.INVITE.command)) {
                    isIRCCmd = true

                    if (messageParts.size >= 2) {
                        isIRCCmdValid = true
                        room.invite(messageParts[1], callback)
                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.KICK_USER.command)) {
                    isIRCCmd = true

                    if (messageParts.size >= 2) {
                        isIRCCmdValid = true

                        val user = messageParts[1]
                        val reason = textMessage.substring(SlashCommand.BAN_USER.command.length
                                + 1
                                + user.length).trim { it <= ' ' }

                        room.kick(user, reason, callback)
                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.BAN_USER.command)) {
                    isIRCCmd = true

                    val params = textMessage.substring(SlashCommand.BAN_USER.command.length).trim { it <= ' ' }
                    val paramsList = params.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                    val bannedUserID = paramsList[0]
                    val reason = params.substring(bannedUserID.length).trim { it <= ' ' }

                    if (bannedUserID.length > 0) {
                        isIRCCmdValid = true
                        room.ban(bannedUserID, reason, callback)
                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.UNBAN_USER.command)) {
                    isIRCCmd = true

                    if (messageParts.size >= 2) {
                        isIRCCmdValid = true
                        room.unban(messageParts[1], callback)
                    }

                } else if (TextUtils.equals(firstPart, SlashCommand.SET_USER_POWER_LEVEL.command)) {
                    isIRCCmd = true

                    if (messageParts.size >= 3) {
                        isIRCCmdValid = true
                        val userID = messageParts[1]
                        val powerLevelsAsString = messageParts[2]

                        try {
                            if (userID.length > 0 && powerLevelsAsString.length > 0) {
                                room.updateUserPowerLevels(userID, Integer.parseInt(powerLevelsAsString), callback)
                            }
                        } catch (e: Exception) {
//                            Log.e(LOG_TAG, "mRoom.updateUserPowerLevels " + e.message, e)
                        }

                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.RESET_USER_POWER_LEVEL.command)) {
                    isIRCCmd = true

                    if (messageParts.size >= 2) {
                        isIRCCmdValid = true
                        room.updateUserPowerLevels(messageParts[1], 0, callback)
                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.MARKDOWN.command)) {
                    isIRCCmd = true

                    if (messageParts.size >= 2) {
                        if (TextUtils.equals(messageParts[1], "on")) {
                            isIRCCmdValid = true
                            PreferencesManager.setMarkdownEnabled(VectorApp.getInstance(), true)
                            Toast.makeText(activity, R.string.markdown_has_been_enabled, Toast.LENGTH_SHORT).show()
                        } else if (TextUtils.equals(messageParts[1], "off")) {
                            isIRCCmdValid = true
                            PreferencesManager.setMarkdownEnabled(VectorApp.getInstance(), false)
                            Toast.makeText(activity, R.string.markdown_has_been_disabled, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else if (TextUtils.equals(firstPart, SlashCommand.CLEAR_SCALAR_TOKEN.command)) {
                    isIRCCmd = true
                    isIRCCmdValid = true

                    val wm = WidgetManagerProvider.getWidgetManager(activity)
                    if (wm != null) {
                        wm.clearScalarToken(activity, session)
                        Toast.makeText(activity, "Scalar token cleared", Toast.LENGTH_SHORT).show()
                    }

                }

                if (!isIRCCmd) {
                    AlertDialog.Builder(activity)
                            .setTitle(R.string.command_error)
                            .setMessage(activity.getString(R.string.unrecognized_command, firstPart))
                            .setPositiveButton(R.string.ok, null)
                            .show()

                    // do not send the command as a message
                    isIRCCmd = true
                } else if (!isIRCCmdValid) {
                    AlertDialog.Builder(activity)
                            .setTitle(R.string.command_error)
                            .setMessage(activity.getString(R.string.command_problem_with_parameters, firstPart))
                            .setPositiveButton(R.string.ok, null)
                            .show()

                    // do not send the command as a message
                    isIRCCmd = true
                }
            }

            return isIRCCmd
        }
    }
}