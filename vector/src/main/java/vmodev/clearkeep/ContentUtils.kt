//package vmodev.clearkeep
//
//object ContentUtils {
//    fun extractUsefulTextFromReply(repliedBody: String): String {
//        val lines = repliedBody.lines()
//        var wellFormed = repliedBody.startsWith(">")
//        var endOfPreviousFound = false
//        val usefullines = ArrayList<String>()
//        lines.forEach {
//            if (it == "") {
//                endOfPreviousFound = true
//                return@forEach
//            }
//            if (!endOfPreviousFound) {
//                wellFormed = wellFormed && it.startsWith(">")
//            } else {
//                usefullines.add(it)
//            }
//        }
//        return usefullines.joinToString("\n").takeIf { wellFormed } ?: repliedBody
//    }
//
//    fun extractUsefulTextFromHtmlReply(repliedBody: String): String {
//        if (repliedBody.startsWith("<mx-reply>")) {
//            val closingTagIndex = repliedBody.lastIndexOf("</mx-reply>")
//            if (closingTagIndex != -1)
//                return repliedBody.substring(closingTagIndex + "</mx-reply>".length).trim()
//        }
//        return repliedBody
//    }
//}