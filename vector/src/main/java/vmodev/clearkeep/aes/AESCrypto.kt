package vmodev.clearkeep.aes

import android.util.Base64
import vmodev.clearkeep.aes.interfaces.ICrypto
import vmodev.clearkeep.pbkdf2.interfaces.IGenerateKey
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class AESCrypto @Inject constructor(private val generateKey: IGenerateKey) : ICrypto {
    override fun encrypt(secretKey: String, rawData: String): String {
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        val keyAndSalt = generateKey.generate(secretKey).split(":")
        var key = Base64.decode(keyAndSalt[1], Base64.NO_WRAP);
        val secretKey = SecretKeySpec(key, "AES");
        val iv = IvParameterSpec(ByteArray(16))
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        val encryptedData = cipher.doFinal(rawData.toByteArray(Charsets.UTF_8));
        return keyAndSalt[0] + ":" + Base64.encodeToString(encryptedData, Base64.NO_WRAP);
    }

    override fun decrypt(secretKey: String, encryptedData: String): String {
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        val encryptedDatas = encryptedData.split(":");
        val keyAndSalt = generateKey.generate(secretKey, Base64.decode(encryptedDatas[0], Base64.NO_WRAP)).split(":");
        var key = Base64.decode(keyAndSalt[1], Base64.NO_WRAP);
        val secretKey = SecretKeySpec(key, "AES");
        val iv = IvParameterSpec(ByteArray(16))
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        val decryptData = cipher.doFinal(Base64.decode(encryptedDatas[1], Base64.NO_WRAP));
        return String(decryptData);
    }
}