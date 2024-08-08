package ir.jaamebaade.jaamebaade_client.utility

fun Int.toPersianNumber() : String{
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