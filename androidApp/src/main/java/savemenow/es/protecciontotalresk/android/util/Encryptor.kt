package savemenow.es.protecciontotalresk.android.util

import java.nio.charset.StandardCharsets
import java.security.MessageDigest


/**
 ****Created by: Edison Martinez,
 ****Date:21,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class Encryptor
{

    /*
        Function for encrpy the pass of the user
     */
    fun encodeData(passTemp : String) : String
    {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(passTemp.toByteArray(StandardCharsets.UTF_8))
            val hexString = StringBuilder()
            for (b in hash) {
                val hex = Integer.toHexString(0xff and b.toInt())
                if (hex.length == 1) hexString.append('0')
                hexString.append(hex)
            }
            hexString.toString()
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

}