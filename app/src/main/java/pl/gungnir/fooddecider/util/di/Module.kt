package pl.gungnir.fooddecider.util.di

import org.koin.dsl.module
import pl.gungnir.fooddecider.ui.screens.randomizeFood.RandomizeFoodViewModel
import pl.gungnir.fooddecider.ui.screens.savedFood.SavedFoodViewModel

val viewModelModule = module {
    factory { SavedFoodViewModel() }
    factory { RandomizeFoodViewModel() }
}