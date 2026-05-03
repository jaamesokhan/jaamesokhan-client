package ir.jaamebaade.jaamebaade_client.utility

import java.text.Normalizer

fun String.toIntArray(): IntArray {
    return this.split(",").map { it.toInt() }.toIntArray()
}

private val arabicDiacriticsRegex = Regex("[\\u0610-\\u061A\\u064B-\\u065F\\u0670\\u06D6-\\u06ED]")
private val punctuationRegex = Regex("[!«»\",،\\.(){}\\[\\]\\-_\\*%؛؟٬٫:“”‘’:/|\\\\…]")

private val charMap = mapOf(
    'ي' to 'ی',
    'ى' to 'ی',
    'ئ' to 'ی',
    'ك' to 'ک',
    'ة' to 'ه',
    'ۀ' to 'ه',
    'ؤ' to 'و',
    'أ' to 'ا',
    'إ' to 'ا',
    'ٱ' to 'ا',
    'آ' to 'ا',
    'ء' to '\u0000', // remove hamza if desired
    'ـ' to '\u0000'  // tatweel
)

fun String.normalizedForSearch(): String {
    var normalized = Normalizer.normalize(this, Normalizer.Form.NFKC)

    // 1. Remove diacritics
    normalized = normalized.replace(arabicDiacriticsRegex, "")

    // 2. Replace characters from charMap
    normalized = buildString {
        for (ch in normalized) {
            append(charMap[ch] ?: ch)
        }
    }

    // 3. Remove punctuation and strange marks
    normalized = normalized.replace(punctuationRegex, "")

    // 4. Remove invisible direction markers or spaces
    normalized = normalized
        .replace("\u200E", "") // LRM
        .replace("\u200F", "") // RLM
        .replace("\uFEFF", "") // BOM zero-width no-break space
        .replace("\u202A", "") // directional marks
        .replace("\u202B", "")
        .replace("\u202C", "")
        .replace("\u202D", "")
        .replace("\u202E", "")
        .replace(Regex("\\s+"), " ")
        .replace("‌", " ")
        .trim()

    return normalized
}
