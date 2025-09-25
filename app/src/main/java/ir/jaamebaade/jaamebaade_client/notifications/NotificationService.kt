package ir.jaamebaade.jaamebaade_client.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ir.jaamebaade.jaamebaade_client.MainActivity
import ir.jaamebaade.jaamebaade_client.R

object NotificationService {
    const val CHANNEL_ID = "jaamesokhan_channel"
    const val CHANNEL_NAME = "jaamesokhan"
    private var counter = 1


    @SuppressLint("MissingPermission")
    fun showNotification(
        context: Context,
        title: String,
        message: String,
        destinationRoute: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("destination_route", destinationRoute)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(counter, builder.build())
            counter++
        }
    }
}