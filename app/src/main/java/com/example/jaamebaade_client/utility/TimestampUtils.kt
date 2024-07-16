package com.example.jaamebaade_client.utility

import saman.zamani.persiandate.PersianDate
import java.util.Date

fun Date.convertToJalali(): PersianDate {
    return PersianDate(this)
}

fun PersianDate.toLocalFormat(): String {
    return "${shYear.toPersianNumber()}/${shMonth.toPersianNumber()}/${shDay.toPersianNumber()}"
}