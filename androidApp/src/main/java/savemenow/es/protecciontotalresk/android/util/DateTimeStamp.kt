package savemenow.es.protecciontotalresk.android.util

import java.sql.Timestamp
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


/**
 ****Created by: Edison Martinez,
 ****Date:21,Sunday,2022,
 ****Proyect: Proteccion_Total_Resk.
 **/
class DateTimeStamp {

    private var timestampDateTime: Timestamp? = null
    val current = LocalDate.now()


    init {
        timestampDateTime = Timestamp(System.currentTimeMillis())
    }

    fun getDateTime() : Timestamp?
    {
        return timestampDateTime
    }

    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)

        return formatted
    }

}