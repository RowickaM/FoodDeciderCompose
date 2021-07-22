package pl.gungnir.fooddecider.util.di

import org.koin.dsl.module
import pl.gungnir.fooddecider.model.useCase.GetAllSavedFoodUseCase
import pl.gungnir.fooddecider.ui.screens.randomizeFood.SaveFoodShareViewModel
import pl.gungnir.fooddecider.util.firebase.FirebaseHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseHelperImpl
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.DatabaseRepoImpl

val viewModelModule = module {
    single { SaveFoodShareViewModel(get()) }
}

val databaseModule = module {
    single<DatabaseRepo> { DatabaseRepoImpl(get()) }
    single<FirebaseHelper> { FirebaseHelperImpl() }
}

val useCaseModule = module {
    factory { GetAllSavedFoodUseCase(get()) }
}