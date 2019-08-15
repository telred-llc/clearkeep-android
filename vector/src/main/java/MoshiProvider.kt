//import com.squareup.moshi.Moshi
//import vmodev.clearkeep.*
//import vmodev.clearkeep.UriMoshiAdapter
//import vmodev.clearkeep.UserAccountData
//import vmodev.clearkeep.UserAccountDataDirectMessages
//import vmodev.clearkeep.UserAccountDataFallback
//import vmodev.clearkeep.jsonmodels.MessageContent
//
//object MoshiProvider {
//    private val moshi: Moshi = Moshi.Builder()
//            .add(UriMoshiAdapter())
//            .add(RuntimeJsonAdapterFactory.of(UserAccountData::class.java, "type", UserAccountDataFallback::class.java)
//                    .registerSubtype(UserAccountDataDirectMessages::class.java, UserAccountData.TYPE_DIRECT_MESSAGES)
//            )
//            .add(RuntimeJsonAdapterFactory.of(MessageContentEdit::class.java, "msgtype", MessageDefaultContent::class.java)
//                    .registerSubtype(MessageTextContent::class.java, MessageType.MSGTYPE_TEXT)
//                    .registerSubtype(MessageNoticeContent::class.java, MessageType.MSGTYPE_NOTICE)
//                    .registerSubtype(MessageEmoteContent::class.java, MessageType.MSGTYPE_EMOTE)
//            )
//            .add(SerializeNulls.JSON_ADAPTER_FACTORY)
//            .build()
//
//    fun providesMoshi(): Moshi {
//        return moshi
//    }
//}