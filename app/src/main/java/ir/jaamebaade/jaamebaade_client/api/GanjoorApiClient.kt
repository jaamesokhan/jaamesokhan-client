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
                val recitations = (it["recitations"] as? List<*>)?.filterIsInstance<Map<*, *>>()
                recitations?.forEach { recitation ->
                    (recitation["mp3Url"] as? String)?.let { mp3Url ->
                        audioLinks.add(
                            AudioData(
                                artistName = recitation["audioArtist"] as? String ?: "",
                                poemId = poemId,
                                syncFileUrl = recitation["xmlText"] as? String,
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
