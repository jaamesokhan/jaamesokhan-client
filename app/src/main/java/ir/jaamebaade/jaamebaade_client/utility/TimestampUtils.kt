package ir.jaamebaade.jaamebaade_client.utility

import saman.zamani.persiandate.PersianDate
import java.util.Date

fun Date.convertToJalali(): PersianDate {
    return PersianDate(this)
}

fun PersianDate.toLocalFormatWithHour(): String {
    return "${shDay.toPersianNumber().toZeroPaddedString()}  ${shMonth.toJalaliMonthName()}  ${shYear.toPersianNumber()} - ${hour.toPersianNumber().toZeroPaddedString()}:${minute.toPersianNumber().toZeroPaddedString()}"
}

fun String.toZeroPaddedString(): String {
    return this.padStart(2, '۰')
}