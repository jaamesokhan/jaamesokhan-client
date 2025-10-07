package ir.jaamebaade.jaamebaade_client.audio

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.jaamebaade.jaamebaade_client.MainActivity
import ir.jaamebaade.jaamebaade_client.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioSessionManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    interface PlaybackController {
        fun onPlay()
        fun onPause()
        fun onStop()
    }

    companion object {
        const val CHANNEL_ID = "audio_playback_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_PLAY = "ir.jaamebaade.jaamebaade_client.action.PLAY"
        const val ACTION_PAUSE = "ir.jaamebaade.jaamebaade_client.action.PAUSE"
        const val ACTION_STOP = "ir.jaamebaade.jaamebaade_client.action.STOP"
    }

    private val notificationManager = NotificationManagerCompat.from(context)

    private val mediaSession = MediaSessionCompat(context, "JaameSokhanMediaSession").apply {
        setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )
        setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                playbackController?.onPlay()
            }

            override fun onPause() {
                playbackController?.onPause()
            }

            override fun onStop() {
                playbackController?.onStop()
            }
        })
    }

    private var playbackController: PlaybackController? = null
    private var metadataTitle: String? = null
    private var metadataSubtitle: String? = null
    private var metadataDuration: Long = 0L

    fun setPlaybackController(controller: PlaybackController) {
        playbackController = controller
    }

    fun updateMetadata(title: String?, subtitle: String?, duration: Long? = null) {
        metadataTitle = title ?: context.getString(R.string.app_name)
        metadataSubtitle = subtitle
        duration?.let { metadataDuration = it }

        val metadataBuilder = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, metadataTitle)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, metadataSubtitle ?: "")

        if (metadataDuration > 0) {
            metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, metadataDuration)
        }

        mediaSession.setMetadata(metadataBuilder.build())
    }

    fun onPlay(position: Long, duration: Long) {
        metadataDuration = duration
        updateMetadata(metadataTitle, metadataSubtitle, duration)
        updatePlaybackState(PlaybackStateCompat.STATE_PLAYING, position)
        mediaSession.isActive = true
        showNotification(isPlaying = true)
    }

    fun onPause(position: Long) {
        updatePlaybackState(PlaybackStateCompat.STATE_PAUSED, position)
        showNotification(isPlaying = false)
    }

    fun onStop() {
        updatePlaybackState(PlaybackStateCompat.STATE_STOPPED, 0L)
        mediaSession.isActive = false
        notificationManager.cancel(NOTIFICATION_ID)
    }

    fun handleNotificationAction(action: String?) {
        when (action) {
            ACTION_PLAY -> playbackController?.onPlay()
            ACTION_PAUSE -> playbackController?.onPause()
            ACTION_STOP -> playbackController?.onStop()
        }
    }

    fun release() {
        notificationManager.cancel(NOTIFICATION_ID)
        mediaSession.release()
    }

    private fun updatePlaybackState(state: Int, position: Long) {
        val playbackState = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat.ACTION_PAUSE or
                    PlaybackStateCompat.ACTION_STOP or
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
            )
            .setState(state, position, 1f)
            .build()
        mediaSession.setPlaybackState(playbackState)
    }

    private fun showNotification(isPlaying: Boolean) {
        val playPauseAction = if (isPlaying) buildPauseAction() else buildPlayAction()
        val stopAction = buildStopAction()

        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(metadataTitle ?: context.getString(R.string.app_name))
            .setContentText(metadataSubtitle ?: context.getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(contentIntent)
            .setOngoing(isPlaying)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setShowWhen(false)
            .addAction(playPauseAction)
            .addAction(stopAction)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0)
            )
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun buildPlayAction(): NotificationCompat.Action {
        val intent = Intent(context, AudioNotificationReceiver::class.java).apply {
            action = ACTION_PLAY
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Action(
            android.R.drawable.ic_media_play,
            context.getString(R.string.NOTIFICATION_PLAY),
            pendingIntent
        )
    }

    private fun buildPauseAction(): NotificationCompat.Action {
        val intent = Intent(context, AudioNotificationReceiver::class.java).apply {
            action = ACTION_PAUSE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Action(
            android.R.drawable.ic_media_pause,
            context.getString(R.string.NOTIFICATION_PAUSE),
            pendingIntent
        )
    }

    private fun buildStopAction(): NotificationCompat.Action {
        val intent = Intent(context, AudioNotificationReceiver::class.java).apply {
            action = ACTION_STOP
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            3,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Action(
            android.R.drawable.ic_menu_close_clear_cancel,
            context.getString(R.string.NOTIFICATION_STOP),
            pendingIntent
        )
    }
}
