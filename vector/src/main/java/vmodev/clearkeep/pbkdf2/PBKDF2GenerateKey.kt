package vmodev.clearkeep.pbkdf2

import android.util.Base64
import vmodev.clearkeep.pbkdf2.interfaces.IGenerateKey
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PBKDF2GenerateKey : IGenerateKey {
    val secretKeyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    override fun generate(passphrase: String): String {
        var salt: ByteArray = ByteArray(128);
        val secureRandom: SecureRandom = SecureRandom();
        secureRandom.nextBytes(salt);
        val spec: KeySpec = PBEKeySpec(passphrase.toCharArray(), salt, 10000, 512);
        val secretKey = secretKeyFactory.generateSecret(spec);
        val saltEncode = Base64.encodeToString(salt, Base64.DEFAULT);
        val keyEncode = Base64.encodeToString(secretKey.encoded, Base64.DEFAULT);
        return "$saltEncode:$keyEncode";
    }

    override fun generate(passphrase: String, salt: ByteArray): String {
        val spec: KeySpec = PBEKeySpec(passphrase.toCharArray(), salt, 10000, 512);
        val secretKey = secretKeyFactory.generateSecret(spec);
        val saltEncode = Base64.encodeToString(salt, Base64.DEFAULT);
        val keyEncode = Base64.encodeToString(secretKey.encoded, Base64.DEFAULT);
        return "$saltEncode:$keyEncode";
    }
}