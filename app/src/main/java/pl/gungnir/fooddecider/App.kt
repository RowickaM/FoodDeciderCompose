package pl.gungnir.fooddecider

import android.app.Application
import com.google.firebase.FirebaseApp
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.gungnir.fooddecider.util.di.appModule
import pl.gungnir.fooddecider.util.di.databaseModule
import pl.gungnir.fooddecider.util.di.useCaseModule
import pl.gungnir.fooddecider.util.di.viewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initialHawk()
        initialFirebase()
        declareKoinModule()
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
}