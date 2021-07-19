package pl.gungnir.fooddecider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import pl.gungnir.fooddecider.data.list
import pl.gungnir.fooddecider.screens.randomizeFood.RandomizeFood
import pl.gungnir.fooddecider.screens.savedFood.SavedFood
import pl.gungnir.fooddecider.screens.templates.FoodTemplate
import pl.gungnir.fooddecider.screens.templatesDetails.FoodTemplateDetails
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            FoodDeciderTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.background)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = NavigationItem.FoodTemplates.route
                    ) {
                        composable(route = NavigationItem.Random.route) {
                            RandomizeFood(navController)
                        }
                        composable(route = NavigationItem.RandomList.route) {
                            SavedFood()
                        }
                        composable(route = NavigationItem.FoodTemplates.route) {
                            FoodTemplate(navController)
                        }
                        composable(
                            route = NavigationItem.FoodTemplateDetails.route,
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            val id = it.arguments?.getInt("id")
                            id?.let {
                                val template = list[id]
                                FoodTemplateDetails(template)
                            } ?: navController.navigateUp()

                        }
                    }
                }
            }
        }
    }
}

sealed class NavigationItem(val route: String) {
    object Random : NavigationItem("random")
    object RandomList : NavigationItem("random_list")
    object FoodTemplates : NavigationItem("food_templates")
    object FoodTemplateDetails : NavigationItem("food_template_details/{id}")
}