package vmodev.clearkeep.ultis

class FormatString {
    companion object {
        public fun formatName(text: String): String {
            if (text.contains("Invite from")) {
                return text.replace("Invite from", "").trim()
            }
            if(text.contains("Call:")){
                return text.replace("Call:", "").trim()
            }
            return text

        }
    }

}