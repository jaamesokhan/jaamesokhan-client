package ir.jaamebaade.jaamebaade_client.utility

import android.content.Context
import android.util.Log
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.jaamebaade.jaamebaade_client.api.JaameSokhanApiClient
import ir.jaamebaade.jaamebaade_client.repository.PoetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/*
    Updates the image urls of downloaded poets when they change on the server
 */
@Singleton
class PoetImageSyncer @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val poetRepository: PoetRepository,
    private val jaameSokhanApiClient: JaameSokhanApiClient,
) {
    private val hasRun = AtomicBoolean(false)

    suspend fun syncOnce() {
        if (!hasRun.compareAndSet(false, true)) return
        withContext(Dispatchers.IO) {
            try {
                sync()
            } catch (e: Exception) {
                Log.e("PoetImageSyncer", "Failed to sync poet images: ${e.message}")
            }
        }
    }

    private suspend fun sync() {
        val poets = poetRepository.getAllPoets()
        if (poets.isEmpty()) return
        val remoteImages = jaameSokhanApiClient.getPoetImages(poets.map { it.id }) ?: return
        val remoteUrlById = remoteImages.associate { it.id to it.imageUrl }

        poets.forEach { poet ->
            if (!remoteUrlById.containsKey(poet.id)) return@forEach
            val newUrl = remoteUrlById[poet.id]
            if (newUrl == poet.imageUrl) return@forEach

            poetRepository.updatePoetImageUrl(poet.id, newUrl)
            poet.imageUrl?.let { evictFromCache(it) }
            newUrl?.takeIf { it.isNotEmpty() }?.let { prefetch(it) }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    private fun evictFromCache(url: String) {
        val imageLoader = context.imageLoader
        imageLoader.diskCache?.remove(url)
        imageLoader.memoryCache?.let { cache ->
            cache.keys.filter { it.key == url }.forEach { cache.remove(it) }
        }
    }

    // Same request options as SquareImage so the cached image is reused offline
    private fun prefetch(url: String) {
        val request = ImageRequest.Builder(context)
            .data(url)
            .size(coil.size.Size.ORIGINAL)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
        context.imageLoader.enqueue(request)
    }
}
