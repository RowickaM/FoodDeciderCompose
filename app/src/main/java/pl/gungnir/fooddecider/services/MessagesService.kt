package pl.gungnir.fooddecider.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.MainActivity
import pl.gungnir.fooddecider.util.helper.Notification

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class MessagesService : FirebaseMessagingService() {

    private val notificationHelper by inject<Notification> {
        parametersOf(NotificationManagerCompat.from(this))
    }

    private val notificationId = 1

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val intent = Intent(this, MainActivity::class.java)

        remoteMessage.data.forEach {
            intent.putExtra(it.key, it.value)
        }

        remoteMessage.notification?.let {
            notificationHelper.showNotification(
                notificationId = notificationId,
                intentAfterClick = PendingIntent.getActivity(this, 0, intent, 0),
                notificationBuilder = createNotification(it.title, it.body)
            )
        }
    }

    private fun createNotification(title: String?, body: String?) =
        NotificationCompat.Builder(this, Notification.NOTIFICATION_CHANNEL_INFO)
            .setContentTitle(title ?: getString(R.string.app_name))
            .setContentText(body ?: getString(R.string.new_actions_in_app))
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
}