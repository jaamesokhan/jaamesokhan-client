package ir.jaamebaade.jaamebaade_client.utility

fun IntArray.toNavArgs(): String {
    return this.joinToString(",")
}
