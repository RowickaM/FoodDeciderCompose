package pl.gungnir.fooddecider

import android.app.Application
import org.koin.core.context.startKoin
import pl.gungnir.fooddecider.util.di.viewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        declareKoinModule()
    }

    private fun declareKoinModule() {
        startKoin {
            modules(
                modules = listOf(
                    viewModelModule
                )
            )
        }
    }
}