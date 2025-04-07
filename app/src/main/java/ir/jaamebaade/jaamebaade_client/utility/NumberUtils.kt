package ir.jaamebaade.jaamebaade_client.utility

fun Int.toPersianNumber(): String {
    return toPersianNumber(this.toString())
}

fun toPersianNumber(englishNumber: String): String {
    val persianDigits = "۰۱۲۳۴۵۶۷۸۹"
    return englishNumber.map { digit ->
        if (digit.isDigit()) {
            persianDigits[digit.digitToInt()]
        } else {
            digit
        }
    }.joinToString("")
}

fun Int.toJalaliMonthName(): String {
    return when (this) {
        1 -> "فروردین"
        2 -> "اردیبهشت"
        3 -> "خرداد"
        4 -> "تیر"
        5 -> "مرداد"
        6 -> "شهریور"
        7 -> "مهر"
        8 -> "آبان"
        9 -> "آذر"
        10 -> "دی"
        11 -> "بهمن"
        12 -> "اسفند"
        else -> ""
    }
}