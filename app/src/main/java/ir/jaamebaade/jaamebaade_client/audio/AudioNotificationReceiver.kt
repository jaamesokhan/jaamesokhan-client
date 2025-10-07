package ir.jaamebaade.jaamebaade_client.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AudioNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var audioSessionManager: AudioSessionManager

    override fun onReceive(context: Context?, intent: Intent?) {
        audioSessionManager.handleNotificationAction(intent?.action)
    }
}
