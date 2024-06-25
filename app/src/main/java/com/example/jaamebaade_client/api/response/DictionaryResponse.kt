package com.example.jaamebaade_client.api.response

data class DictionaryResponse(val result: DictionaryResult)

data class DictionaryResult(val name: String, val meaning: String)