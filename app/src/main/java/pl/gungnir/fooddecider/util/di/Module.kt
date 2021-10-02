package pl.gungnir.fooddecider.util.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.gungnir.fooddecider.model.useCase.*
import pl.gungnir.fooddecider.ui.MainViewModel
import pl.gungnir.fooddecider.ui.screens.forgotPassword.ForgotPasswordViewModel
import pl.gungnir.fooddecider.ui.screens.login.LoginViewModel
import pl.gungnir.fooddecider.ui.screens.randomizeFood.SaveFoodShareViewModel
import pl.gungnir.fooddecider.ui.screens.registration.RegistrationViewModel
import pl.gungnir.fooddecider.ui.screens.templates.TemplatesViewModel
import pl.gungnir.fooddecider.ui.screens.templatesDetails.TemplateDetailsViewModel
import pl.gungnir.fooddecider.ui.screens.templates.FoodTemplatesSharedViewModel
import pl.gungnir.fooddecider.ui.screens.templatesDetails.FoodTemplateDetailsViewModel
import pl.gungnir.fooddecider.util.firebase.FirebaseAuthHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseAuthHelperImpl
import pl.gungnir.fooddecider.util.firebase.FirebaseHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseHelperImpl
import pl.gungnir.fooddecider.util.helper.ResourceProvider
import pl.gungnir.fooddecider.util.helper.ResourceProviderImpl
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.DatabaseRepoImpl

val viewModelModule = module {
    single { SaveFoodShareViewModel(get(), get()) }
    factory { FoodTemplatesSharedViewModel(get()) }
    single { FoodTemplateDetailsViewModel(get(), get()) }
    single { TemplatesViewModel(get()) }
    factory { TemplateDetailsViewModel(get(), get()) }
    factory { LoginViewModel(get(), get(), get(), get()) }
    factory { MainViewModel(get(), get()) }
    factory { ForgotPasswordViewModel(get(), get()) }
    factory { RegistrationViewModel(get(), get(), get(), get(), get()) }
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
    factory { GetTemplatesUseCase(get()) }
    factory { GetTemplateDetailsUseCase(get()) }
    factory { SetFoodListUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { SendRemindPasswordLinkUseCase(get()) }
    factory { SignUpUserUseCase(get()) }
    factory { CreateUserCollectionUseCase(get()) }
    factory { SendEmailVerificationUseCase(get()) }
    factory { SaveItemToListUseCase(get()) }
    factory { AddFoodToListUseCase(get()) }
    factory { SplitDishesTemplateByIdUseCase(get()) }
}

val appModule = module {
    single<ResourceProvider> { ResourceProviderImpl(androidContext()) }
}
