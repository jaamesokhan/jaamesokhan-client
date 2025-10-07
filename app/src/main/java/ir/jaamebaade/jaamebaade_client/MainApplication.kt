package ir.jaamebaade.jaamebaade_client

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp
import ir.jaamebaade.jaamebaade_client.audio.AudioSessionManager

/*
    Hilt application for dependency injection
 */
@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createAudioChannel()
    }

    private fun createAudioChannel() {
        val channel = NotificationChannel(
            AudioSessionManager.CHANNEL_ID,
            getString(R.string.AUDIO_NOTIFICATION_CHANNEL_NAME),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.AUDIO_NOTIFICATION_CHANNEL_DESCRIPTION)
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

}
