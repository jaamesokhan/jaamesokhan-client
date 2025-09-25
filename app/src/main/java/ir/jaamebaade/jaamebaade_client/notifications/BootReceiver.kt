package ir.jaamebaade.jaamebaade_client.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_TIME_CHANGED -> {
                Log.d("BootReceiver", "System event: ${intent.action}")

                if (sharedPrefManager.getIsScheduledNotificationsEnabled()) {
                    val localTime = sharedPrefManager.getScheduledNotificationTime()
                    ExactAlarmScheduler.cancel(context)
                    ExactAlarmScheduler.scheduleExact(context, localTime)
                    sharedPrefManager.setIsScheduledNotificationsSetUp(true)
                } else {
                    ExactAlarmScheduler.cancel(context)
                    sharedPrefManager.setIsScheduledNotificationsSetUp(false)
                }
            }
        }
    }
}
