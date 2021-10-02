package pl.gungnir.fooddecider.util.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.data.NavigationItem
import pl.gungnir.fooddecider.ui.MainViewModel
import pl.gungnir.fooddecider.ui.screens.forgotPassword.ForgotPassword
import pl.gungnir.fooddecider.ui.screens.login.Login
import pl.gungnir.fooddecider.ui.screens.randomizeFood.RandomizeFood
import pl.gungnir.fooddecider.ui.screens.registration.Registration
import pl.gungnir.fooddecider.ui.screens.savedFood.SavedFood
import pl.gungnir.fooddecider.ui.screens.templates.FoodTemplate
import pl.gungnir.fooddecider.ui.screens.templatesDetails.FoodTemplateDetails
import pl.gungnir.fooddecider.util.KEY_TEMPLATE_ID

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun NavHostImpl(
    viewModel: MainViewModel,
    navController: NavHostController,
    navToHome: () -> Unit,
    navToRememberPassword: () -> Unit,
    navToRegistration: () -> Unit,
    navToTemplateDetails: (String) -> Unit,
    navBack: () -> Unit,
) {
    NavHost(
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.height_bottom_bar)),
        navController = navController,
        startDestination = NavigationItem.Login.route
    ) {
        composable(route = NavigationItem.Login.route) {
            CrashlyticsLog.log(NAV_LOGIN_LOG)

            viewModel.showBottomBar(false)
            viewModel.showToolbar(false)
            viewModel.setTitle("")
            viewModel.showFAB(false)

            Login(
                navToHome = navToHome,
                navToRegistration = navToRegistration,
                navToRememberPassword = navToRememberPassword
            )
        }
        composable(route = NavigationItem.Registration.route) {
            CrashlyticsLog.log(NAV_REGISTRATION_LOG)

            viewModel.showBottomBar(false)
            viewModel.showToolbar(false)
            viewModel.setTitle("")
            viewModel.showFAB(false)

            Registration(
                navBack = navBack
            )
        }
        composable(route = NavigationItem.ForgotPassword.route) {
            CrashlyticsLog.log(NAV_FORGOT_PASSWORD_LOG)
            viewModel.showBottomBar(false)
            viewModel.showToolbar(false)
            viewModel.setTitle("")
            viewModel.showFAB(false)

            ForgotPassword(
                navBack = navBack
            )
        }
        composable(route = NavigationItem.Random.route) {
            CrashlyticsLog.log(NAV_RANDOM_LOG)
            viewModel.showBottomBar(true)
            viewModel.showToolbar(true)
            viewModel.setTitle("")
            viewModel.showFAB(true)

            RandomizeFood(
                setSavedList = viewModel::setSavedList
            )
        }
        composable(route = NavigationItem.RandomList.route) {
            CrashlyticsLog.log(NAV_RANDOM_LIST_LOG)
            viewModel.showBottomBar(true)
            viewModel.showToolbar(true)
            viewModel.setTitle(stringResource(id = R.string.templates_title))
            viewModel.showFAB(true)

            SavedFood()
        }
        composable(route = NavigationItem.FoodTemplates.route) {
            CrashlyticsLog.log(NAV_FOOD_TEMPLATE_LOG)
            viewModel.showBottomBar(true)
            viewModel.showToolbar(true)
            viewModel.setTitle(stringResource(id = R.string.list_foods_title))
            viewModel.showFAB(true)

            FoodTemplate(
                navToTemplateDetails = navToTemplateDetails
            )
        }
        composable(
            route = NavigationItem.FoodTemplateDetails.route,
            arguments = listOf(
                navArgument(KEY_TEMPLATE_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            CrashlyticsLog.log(NAV_FOOD_TEMPLATE_DETAILS_LOG)
            viewModel.showBottomBar(false)
            viewModel.showToolbar(false)
            viewModel.setTitle("")
            viewModel.showFAB(true)

            val id = it.arguments?.getString(KEY_TEMPLATE_ID)
            id?.let {
                FoodTemplateDetails(id)
            } ?: navBack()

        }
    }
}