package vmodev.clearkeep.aes.interfaces

interface ICrypto {
    fun encrypt(secretKey: String, rawData: String): String;
    fun decrypt(secretKey: String, encryptedData: String): String;
}