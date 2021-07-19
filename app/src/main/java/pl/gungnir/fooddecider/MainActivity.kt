package pl.gungnir.fooddecider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import pl.gungnir.fooddecider.data.list
import pl.gungnir.fooddecider.mics.BottomBar
import pl.gungnir.fooddecider.mics.BottomBarItem
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
            val navBackStackEntry = navController.currentBackStackEntryAsState().value
            val currentDestination = navBackStackEntry?.destination

            FoodDeciderTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.background),
                    bottomBar = {
                        BottomBar(
                            currentDestination = currentDestination?.route,
                            onItemCLick = {
                                navigateTo(navController, it)
                            }
                        )
                    }
                ) {
                    NavHost(
                        modifier = Modifier.padding(bottom = 60.dp),
                        navController = navController,
                        startDestination = NavigationItem.Random.route
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

    private fun navigateTo(navController: NavHostController, item: BottomBarItem) {
        val navItem = when (item) {
            BottomBarItem.RandomFood -> NavigationItem.Random
            BottomBarItem.RandomFoodList -> NavigationItem.RandomList
            BottomBarItem.TemplateFood -> NavigationItem.FoodTemplates
        }
        navController.navigate(navItem.route)
    }
}

sealed class NavigationItem(val route: String) {
    object Random : NavigationItem("random")
    object RandomList : NavigationItem("random_list")
    object FoodTemplates : NavigationItem("food_templates")
    object FoodTemplateDetails : NavigationItem("food_template_details/{id}")
}