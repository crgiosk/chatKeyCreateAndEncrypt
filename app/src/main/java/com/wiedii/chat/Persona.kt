package com.wiedii.chat

import android.util.Log
import java.security.Key
import java.security.KeyPairGenerator
import java.security.PublicKey
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.SecretKeySpec

class Persona(nombre: String) {
    val name = nombre
    private lateinit var privateKey: Key
    private lateinit var publicKey: PublicKey
    private lateinit var personaPublicKey: PublicKey
    var comunKey: ByteArray? = null
    var secretMessage: String? = null

    fun createKey() {
        val keyPairGenerator = KeyPairGenerator.getInstance("DH")
        keyPairGenerator.initialize(1024)

        val gp = keyPairGenerator.generateKeyPair()

        privateKey = gp.private
        publicKey = gp.public
    }

    fun createCommunSecretKey() {
        try {
            val keyAgreement = KeyAgreement.getInstance("DH")
            keyAgreement.init(privateKey)
            keyAgreement.doPhase(personaPublicKey, true)

            comunKey = shortenSecretKey(keyAgreement.generateSecret())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun receivePublicKeyFrom(person: Persona) {

        personaPublicKey = person.publicKey
    }

    fun encryptMessage(message: String, person: Persona): String {

        try {

            // You can use Blowfish or another symmetric algorithm but you must adjust the key size.
            val keySpec = SecretKeySpec(comunKey!!, "DES")
            val cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")

            cipher.init(Cipher.ENCRYPT_MODE, keySpec)

            val encryptedMessage = cipher.doFinal(message.toByteArray())
            Log.e(TAG, "Message encrypted $encryptedMessage")
            Log.e(TAG, "Message from $name to ${person.name} \nMessage encrypted $encryptedMessage")

            return person.receiveAndDecryptMessage(encryptedMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            return e.message.toString()
        }

    }

    fun receiveAndDecryptMessage(message: ByteArray): String {

        try {

            // You can use Blowfish or another symmetric algorithm but you must adjust the key size.
            val keySpec = SecretKeySpec(comunKey!!, "DES")
            val cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")

            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val decryptedMEssage = String(cipher.doFinal(message))
            Log.e(TAG, "Message decrypted $decryptedMEssage")
            return decryptedMEssage
        } catch (e: Exception) {
            e.printStackTrace()
            return e.message.toString()
        }

    }

    /**
     * 1024 bit symmetric key size is so big for DES so we must shorten the key size. You can get first 8 longKey of the
     * byte array or can use a key factory
     *
     * @param   longKey
     *
     * @return
     */
    private fun shortenSecretKey(longKey: ByteArray): ByteArray? {

        try {

            // Use 8 bytes (64 bits) for DES, 6 bytes (48 bits) for Blowfish
            val shortenedKey = ByteArray(8)

            System.arraycopy(longKey, 0, shortenedKey, 0, shortenedKey.size)

            return shortenedKey

            // Below lines can be more secure
            // final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // final DESKeySpec       desSpec    = new DESKeySpec(longKey);
            //
            // return keyFactory.generateSecret(desSpec).getEncoded();
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    companion object {
        const val TAG = "Persona"
        const val DH = "DH"

        const val AES = "AES"
    }


}