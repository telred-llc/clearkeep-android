package vmodev.clearkeep.aes

import android.util.Base64
import vmodev.clearkeep.aes.interfaces.ICrypto
import vmodev.clearkeep.pbkdf2.interfaces.IGenerateKey
import java.security.MessageDigest
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class AESCrypto @Inject constructor(private val generateKey: IGenerateKey) : ICrypto {
    override fun encrypt(secretKey: String, rawData: String): String {
        val cipher: Cipher = Cipher.getInstance("AES");
        val keyAndSalt = generateKey.generate(secretKey).split(":")
        var key = keyAndSalt[1].toByteArray(Charsets.UTF_8);
        val sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);

        val secretKey = SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        val encryptedData = cipher.doFinal(rawData.toByteArray(Charsets.UTF_8));
        return keyAndSalt[0] + ":" + Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    override fun decrypt(secretKey: String, encryptedData: String): String {
        val cipher: Cipher = Cipher.getInstance("AES");
        val encryptedDatas = encryptedData.split(":");
        val keyAndSalt = generateKey.generate(secretKey, Base64.decode(encryptedDatas[0], Base64.DEFAULT)).split(":");
        var key = keyAndSalt[1].toByteArray(Charsets.UTF_8);
        val sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        val secretKey = SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        val decryptData = cipher.doFinal(Base64.decode(encryptedDatas[1], Base64.DEFAULT));
        return String(decryptData);
    }
}