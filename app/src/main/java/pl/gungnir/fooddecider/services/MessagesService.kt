package pl.gungnir.fooddecider.services

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagesService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("MRMRMR", "MessagesService.kt onMessageReceived: ?")

        remoteMessage.notification?.let {
            Log.d("MRMRMR", "message title: ${it.title} body: ${it.body}")
        }

        remoteMessage.data.forEach {
            Log.d("MRMRMR", "message ${it.key}: ${it.value} ")
        }
    }
}