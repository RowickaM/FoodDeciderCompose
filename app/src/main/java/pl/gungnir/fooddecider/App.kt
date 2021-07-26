package pl.gungnir.fooddecider

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.gungnir.fooddecider.util.di.appModule
import pl.gungnir.fooddecider.util.di.databaseModule
import pl.gungnir.fooddecider.util.di.useCaseModule
import pl.gungnir.fooddecider.util.di.viewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        declareKoinModule()
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