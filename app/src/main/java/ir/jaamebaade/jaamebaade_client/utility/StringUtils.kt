package ir.jaamebaade.jaamebaade_client.utility

fun String.toIntArray(): IntArray {
    return this.split(",").map { it.toInt() }.toIntArray()
}