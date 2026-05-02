package ir.jaamebaade.jaamebaade_client.api

import android.util.Log
import ir.jaamebaade.jaamebaade_client.api.response.AudioData
import javax.inject.Inject

class GanjoorApiClient @Inject constructor(
    private val ganjoorAudioApiService: GanjoorAudioApiService,
) {
    suspend fun getAllRecitations(
        poemId: Int,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ): List<AudioData> {
        try {
            val res = ganjoorAudioApiService.getAllRecitations(poemId).body()
            val audioLinks = mutableListOf<AudioData>()
            res?.let {
                val recitationsMap = it["recitations"] as? List<Map<String, String>>
                recitationsMap?.forEach { recitation ->
                    recitation["mp3Url"]?.let { mp3Url ->
                        audioLinks.add(
                            AudioData(
                                artistName = recitation["audioArtist"] ?: "",
                                poemId = poemId,
                                syncFileUrl = recitation["xmlText"],
                                audioFileUrl = mp3Url
                            )
                        )
                    }
                }
            }
            onSuccess()
            return audioLinks
        } catch (e: Exception) {
            Log.e("GanjoorApiClient", "error: ${e.message}")
            onFailure()
            return listOf()
        }
    }
}
