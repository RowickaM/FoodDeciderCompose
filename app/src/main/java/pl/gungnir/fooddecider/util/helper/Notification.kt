package pl.gungnir.fooddecider.util.helper

import android.app.NotificationManager
import android.app.PendingIntent
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat

interface Notification {

    companion object {

        const val NOTIFICATION_CHANNEL_INFO = "info_channel"
    }

    fun createChanel(
        channelId: String,
        @StringRes name: Int,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    )

    fun showNotification(
        notificationId: Int,
        intentAfterClick: PendingIntent,
        notificationBuilder: NotificationCompat.Builder
    )
}