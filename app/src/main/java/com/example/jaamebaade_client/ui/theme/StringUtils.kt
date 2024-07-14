package com.example.jaamebaade_client.ui.theme

fun String.toIntArray(): IntArray {
    return this.split(",").map { it.toInt() }.toIntArray()
}