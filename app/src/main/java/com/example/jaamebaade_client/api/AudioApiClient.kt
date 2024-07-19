package com.example.jaamebaade_client.api

import android.util.Log
import com.example.jaamebaade_client.api.response.AudioData
import javax.inject.Inject

class AudioApiClient @Inject constructor(
    private val audioApiService: AudioApiService,
) {
    suspend fun getAllRecitations(
        poemId: Int,
        successCallBack: () -> Unit,
        failCallBack: () -> Unit
    ): List<AudioData> {
        try {
            val res = audioApiService.getAllRecitations(poemId).body()
            val audioLinks = mutableListOf<AudioData>()
            res?.let {
                val recitationsMap = it["recitations"] as? List<Map<String, String>>
                recitationsMap?.forEach { recitation ->
                    recitation["mp3Url"].let { mp3Url ->
                        audioLinks.add(
                            AudioData(
                                artist = recitation["audioArtist"]!!,
                                url = mp3Url!!
                            )
                        )
                    }
                }
            }
            successCallBack()
            return audioLinks
        } catch (e: Exception) {
            Log.e("AudioApiClient", "error: ${e.message}")
            failCallBack()
            return listOf()
        }
    }
}