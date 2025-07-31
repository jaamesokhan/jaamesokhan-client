package ir.jaamebaade.jaamebaade_client.ui.theme

enum class PoemFontSize(val displayName: String) {
    SMALL("ریز"),
    MEDIUM("متوسط"),
    LARGE("درشت");

    companion object {
        fun fromOrdinal(ordinal: Int): PoemFontSize = entries.toTypedArray().getOrElse(ordinal) { MEDIUM }
    }
}