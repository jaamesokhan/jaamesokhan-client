package ir.jaamebaade.jaamebaade_client.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalTime
import java.util.Calendar

object ExactAlarmScheduler {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleExact(
        context: Context,
        localTime: LocalTime
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, ExactAlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerAtMillis = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, localTime.hour)
            set(Calendar.MINUTE, localTime.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }.timeInMillis

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
    }

    fun cancel(context: Context) {
        val pi = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, ExactAlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) ?: return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pi)
    }
}
