package pl.gungnir.fooddecider.util.navigation

import androidx.navigation.NavController
import pl.gungnir.fooddecider.model.data.NavigationItem
import pl.gungnir.fooddecider.util.KEY_TEMPLATE_ID

class Actions(navController: NavController?) {

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
}