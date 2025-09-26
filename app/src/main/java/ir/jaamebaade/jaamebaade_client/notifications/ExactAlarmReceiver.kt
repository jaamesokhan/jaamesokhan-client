package ir.jaamebaade.jaamebaade_client.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import ir.jaamebaade.jaamebaade_client.R
import ir.jaamebaade.jaamebaade_client.constants.AppRoutes
import ir.jaamebaade.jaamebaade_client.model.PoemWithPoet
import ir.jaamebaade.jaamebaade_client.model.Verse
import ir.jaamebaade.jaamebaade_client.repository.PoemRepository
import ir.jaamebaade.jaamebaade_client.repository.VerseRepository
import ir.jaamebaade.jaamebaade_client.utility.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ExactAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var poemRepository: PoemRepository

    @Inject
    lateinit var verseRepository: VerseRepository

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    override fun onReceive(context: Context, intent: Intent) {
        if (!sharedPrefManager.getIsScheduledNotificationsEnabled()) return
        val pending = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val poemWithPoet = poemRepository.getRandomPoem()
                poemWithPoet?.let {
                    val first4Verses = verseRepository.getFirst4VersesByPoemId(it.poem.id)
                    NotificationService.showNotification(
                        context = context,
                        title = context.getString(R.string.DAILY_RANDOM_POEM_TITLE),
                        message = buildNotificationMessage(poemWithPoet, first4Verses),
                        destinationRoute = "${AppRoutes.POEM}/${poemWithPoet.poet.id}/${poemWithPoet.poem.id}/-1",
                    )
                }

                Log.d("ExactAlarm", "Alarm fired at ${System.currentTimeMillis()}")

                val localTime = sharedPrefManager.getScheduledNotificationTime()
                ExactAlarmScheduler.scheduleExact(context, localTime)
            } finally {
                pending.finish()
            }
        }
    }

    private fun buildNotificationMessage(poem: PoemWithPoet, verses: List<Verse>): String {
        val selectedVerses = if (verses.size >= 2) {
            verses.take(2)
        } else {
            verses
        }
        return "${selectedVerses.joinToString("/") { verse -> verse.text }}\n${poem.poem.title} - ${poem.poet.name}"
    }

}
