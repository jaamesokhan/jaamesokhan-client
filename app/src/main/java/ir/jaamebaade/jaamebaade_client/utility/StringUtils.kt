package ir.jaamebaade.jaamebaade_client.utility

import java.text.Normalizer

fun String.toIntArray(): IntArray {
    return this.split(",").map { it.toInt() }.toIntArray()
}

private val arabicDiacriticsRegex = Regex("[\\u0610-\\u061A\\u064B-\\u065F\\u0670\\u06D6-\\u06ED]")

fun String.normalizedForSearch(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFKC)
        .replace(arabicDiacriticsRegex, "")
        .replace('ي', 'ی')
        .replace('ى', 'ی')
        .replace('ك', 'ک')
        .replace('ة', 'ه')
        .replace('ۀ', 'ه')
        .replace('ؤ', 'و')
        .replace('أ', 'ا')
        .replace('إ', 'ا')
        .replace('ٱ', 'ا')
        .replace('آ', 'ا')
        .replace("\u0640", "")
        .trim()
}
