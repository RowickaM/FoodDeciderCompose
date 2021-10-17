package pl.gungnir.fooddecider.util.helper

import android.app.NotificationChannel
import android.app.PendingIntent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pl.gungnir.fooddecider.R

class NotificationImpl(
    private val resourceProvider: ResourceProvider,
    private val notificationManager: NotificationManagerCompat,
) : Notification {

    override fun createChanel(channelId: String, @StringRes name: Int, importance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createNotificationChanel(
                channelId = channelId,
                name = resourceProvider.getString(name),
                importance = importance
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showNotification(
        notificationId: Int,
        intentAfterClick: PendingIntent,
        notificationBuilder: NotificationCompat.Builder
    ) {
        val builder = notificationBuilder
            .setSmallIcon(R.mipmap.ic_logo_round)
            .setAutoCancel(true)
            .setContentIntent(intentAfterClick)

        notificationManager.notify(notificationId, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel(
        channelId: String,
        name: String,
        importance: Int
    ) = NotificationChannel(channelId, name, importance)
}