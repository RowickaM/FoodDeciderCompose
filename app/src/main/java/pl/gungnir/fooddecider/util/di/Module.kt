package pl.gungnir.fooddecider.util.di

import org.koin.dsl.module
import pl.gungnir.fooddecider.model.useCase.GetAllSavedFood
import pl.gungnir.fooddecider.ui.screens.randomizeFood.RandomizeFoodViewModel
import pl.gungnir.fooddecider.ui.screens.savedFood.SavedFoodViewModel
import pl.gungnir.fooddecider.util.firebase.FirebaseHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseHelperImpl
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.DatabaseRepoImpl

val viewModelModule = module {
    factory { SavedFoodViewModel() }
    factory { RandomizeFoodViewModel() }
}

val databaseModule = module {
    single<DatabaseRepo> { DatabaseRepoImpl() }
    single<FirebaseHelper> { FirebaseHelperImpl() }
}

val useCaseModule = module {
    factory { GetAllSavedFood(get()) }
}