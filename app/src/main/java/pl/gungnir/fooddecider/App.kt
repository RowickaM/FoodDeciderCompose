package pl.gungnir.fooddecider

import android.app.Application
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.FirebaseApp
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import pl.gungnir.fooddecider.util.di.appModule
import pl.gungnir.fooddecider.util.di.databaseModule
import pl.gungnir.fooddecider.util.di.useCaseModule
import pl.gungnir.fooddecider.util.di.viewModelModule
import pl.gungnir.fooddecider.util.helper.Notification

class App : Application() {

    private val notificationHelper by inject<Notification> {
        parametersOf(NotificationManagerCompat.from(this))
    }

    override fun onCreate() {
        super.onCreate()

        initialHawk()
        initialFirebase()
        declareKoinModule()
        createInfoChanel()
    }

    private fun initialHawk() {
        Hawk.init(this).build()
    }

    private fun initialFirebase() {
        FirebaseApp.initializeApp(this)
    }

    private fun declareKoinModule() {
        startKoin {
            androidContext(this@App)
            modules(
                modules = listOf(
                    viewModelModule,
                    databaseModule,
                    useCaseModule,
                    appModule,
                )
            )
        }
    }

    private fun createInfoChanel() {
        notificationHelper.createChanel(
            channelId = Notification.NOTIFICATION_CHANNEL_INFO,
            name = R.string.channel_notification_info,
            importance = NotificationManager.IMPORTANCE_HIGH
        )
    }
}