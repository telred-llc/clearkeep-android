package vmodev.clearkeep.pbkdf2.interfaces

interface IGenerateKey {
    fun generate(passphrase: String): String;
    fun generate(passphrase: String, salt: ByteArray): String;
}