package vmodev.clearkeep.rests.models.responses

data class PassphraseResponse(val data: Data) {
    data class Data(val id: String, val passphrase: String)
};