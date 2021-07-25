package pl.gungnir.fooddecider.util.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.gungnir.fooddecider.model.useCase.GetAllSavedFoodUseCase
import pl.gungnir.fooddecider.model.useCase.IsUserLoggedUseCase
import pl.gungnir.fooddecider.model.useCase.LoginUseCase
import pl.gungnir.fooddecider.ui.screens.login.LoginViewModel
import pl.gungnir.fooddecider.ui.screens.randomizeFood.SaveFoodShareViewModel
import pl.gungnir.fooddecider.util.firebase.FirebaseAuthHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseAuthHelperImpl
import pl.gungnir.fooddecider.util.firebase.FirebaseHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseHelperImpl
import pl.gungnir.fooddecider.util.helper.ResourceProvider
import pl.gungnir.fooddecider.util.helper.ResourceProviderImpl
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.DatabaseRepoImpl

val viewModelModule = module {
    single { SaveFoodShareViewModel(get()) }
    factory { LoginViewModel(get(), get(), get()) }
}

val databaseModule = module {
    single<DatabaseRepo> { DatabaseRepoImpl(get(), get()) }
    single<FirebaseHelper> { FirebaseHelperImpl() }
    single<FirebaseAuthHelper> { FirebaseAuthHelperImpl() }
}

val useCaseModule = module {
    factory { GetAllSavedFoodUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { IsUserLoggedUseCase(get()) }
}

val appModule = module {
    single<ResourceProvider> { ResourceProviderImpl(androidContext()) }
}
