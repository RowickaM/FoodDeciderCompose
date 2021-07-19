package pl.gungnir.fooddecider.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import pl.gungnir.fooddecider.model.data.list
import pl.gungnir.fooddecider.ui.mics.BottomBar
import pl.gungnir.fooddecider.ui.mics.BottomBarItem
import pl.gungnir.fooddecider.ui.screens.randomizeFood.RandomizeFood
import pl.gungnir.fooddecider.ui.screens.savedFood.SavedFood
import pl.gungnir.fooddecider.ui.screens.templates.FoodTemplate
import pl.gungnir.fooddecider.ui.screens.templatesDetails.FoodTemplateDetails
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val bottomNavigationList = arrayListOf(
                BottomBarItem.RandomFoodList,
                BottomBarItem.RandomFood,
                BottomBarItem.TemplateFood,
            )

            FoodDeciderTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.background),
                    bottomBar = {
                        BottomBar(
                            navigationList = bottomNavigationList,
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