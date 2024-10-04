package ir.jaamebaade.jaamebaade_client.utility

import saman.zamani.persiandate.PersianDate
import java.util.Date

fun Date.convertToJalali(): PersianDate {
    return PersianDate(this)
}

fun PersianDate.toLocalFormat(): String {
    return "${shYear.toPersianNumber()}/${shMonth.toPersianNumber().toZeroPaddedString()}/${shDay.toPersianNumber().toZeroPaddedString()}"
}

fun PersianDate.toLocalFormatWithHour(): String {
    return "${hour.toPersianNumber().toZeroPaddedString()}:${minute.toPersianNumber().toZeroPaddedString()} ${shYear.toPersianNumber()}/${shMonth.toPersianNumber().toZeroPaddedString()}/${shDay.toPersianNumber().toZeroPaddedString()}"
}

fun String.toZeroPaddedString(): String {
    return this.padStart(2, '۰')
}