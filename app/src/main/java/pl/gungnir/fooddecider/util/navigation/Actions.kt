package pl.gungnir.fooddecider.util.navigation

import androidx.navigation.NavController
import pl.gungnir.fooddecider.model.data.NavigationItem
import pl.gungnir.fooddecider.ui.mics.BottomBarItem
import pl.gungnir.fooddecider.util.KEY_TEMPLATE_ID

class Actions(private val navController: NavController?) {

    val navToHome: () -> Unit = {
        navController?.navigate(NavigationItem.Random.route)
    }

    val navToRememberPassword: () -> Unit = {
        navController?.navigate(NavigationItem.ForgotPassword.route)
    }

    val navToRegistration: () -> Unit = {
        navController?.navigate(NavigationItem.Registration.route)
    }
    val navBack: () -> Unit = {
        navController?.navigateUp()
    }

    val navToTemplateDetails: (String) -> Unit = {
        navController?.navigate(
            NavigationItem.FoodTemplateDetails.route.replace(
                "{$KEY_TEMPLATE_ID}",
                it
            )
        )
    }

    val navToFoodList: () -> Unit = {
        navController?.navigate(NavigationItem.RandomList.route)
    }

    val navigateTo: (BottomBarItem) -> Unit = {
        navController?.navigate(it.navItem.route)
    }

    fun getActualNavigationItem(): NavigationItem {
        return when (navController?.currentDestination?.route) {
            NavigationItem.Login.route -> NavigationItem.Login
            NavigationItem.Random.route -> NavigationItem.Random
            NavigationItem.RandomList.route -> NavigationItem.RandomList
            NavigationItem.FoodTemplates.route -> NavigationItem.FoodTemplates
            NavigationItem.FoodTemplateDetails.route -> NavigationItem.FoodTemplateDetails
            else -> NavigationItem.Random
        }
    }
}