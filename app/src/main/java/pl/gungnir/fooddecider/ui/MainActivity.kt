package pl.gungnir.fooddecider.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.data.NavigationItem
import pl.gungnir.fooddecider.ui.mics.BottomBar
import pl.gungnir.fooddecider.ui.mics.BottomBarItem
import pl.gungnir.fooddecider.ui.mics.Toolbar
import pl.gungnir.fooddecider.ui.screens.forgotPassword.ForgotPassword
import pl.gungnir.fooddecider.ui.screens.login.Login
import pl.gungnir.fooddecider.ui.screens.randomizeFood.RandomizeFood
import pl.gungnir.fooddecider.ui.screens.registration.Registration
import pl.gungnir.fooddecider.ui.screens.savedFood.SavedFood
import pl.gungnir.fooddecider.ui.screens.templates.FoodTemplate
import pl.gungnir.fooddecider.ui.screens.templatesDetails.FoodTemplateDetails
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme
import pl.gungnir.fooddecider.util.KEY_TEMPLATE_ID
import pl.gungnir.fooddecider.util.navigation.Actions

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null
    private val viewModel by inject<MainViewModel>(MainViewModel::class.java)

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val action = remember(navController) { Actions(navController) }

            navController = rememberNavController()
            val bottomNavigationList = arrayListOf(
                BottomBarItem.RandomFoodList(stringResource(id = R.string.bottom_nav_list_label)),
                BottomBarItem.RandomFood(stringResource(id = R.string.bottom_nav_draw_label)),
                BottomBarItem.TemplateFood(stringResource(id = R.string.bottom_nav_templates_label)),
            )
            val showBottomBar = remember { mutableStateOf(false) }
            val showToolbar = remember { mutableStateOf(false) }
            val toolbarTitle = remember { mutableStateOf("") }

            val (activeIndex, setActiveIndex) = remember {
                mutableStateOf(1)
            }

            FoodDeciderTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.background),
                    bottomBar = {
                        if (showBottomBar.value) {
                            BottomBar(
                                navigationList = bottomNavigationList,
                                activeIndex = activeIndex,
                                setActiveIndex = setActiveIndex,
                                onItemCLick = {
                                    navigateTo(navController, it)
                                }
                            )
                        }
                    },
                    topBar = {
                        if (showToolbar.value) {
                            Toolbar(
                                title = toolbarTitle.value,
                                icon = additionalIcon(),
                                onIconClick = { additionalIconAction() },
                                onLogout = {
                                    viewModel.logout {
                                        finish()
                                        val intent = Intent(this, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                    }
                                }
                            )
                        }
                    }
                ) {
                    navController?.let { nav ->
                        NavHost(
                            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.height_bottom_bar)),
                            navController = nav,
                            startDestination = NavigationItem.Login.route
                        ) {
                            composable(route = NavigationItem.Login.route) {
                                showBottomBar.value = false
                                showToolbar.value = false
                                toolbarTitle.value = ""

                                Login(
                                    navToHome = action.navToHome,
                                    navToRegistration = action.navToRegistration,
                                    navToRememberPassword = action.navToRememberPassword
                                )
                            }
                            composable(route = NavigationItem.Registration.route) {
                                showBottomBar.value = false
                                showToolbar.value = false
                                toolbarTitle.value = ""

                                Registration(
                                    navBack = action.navBack
                                )
                            }
                            composable(route = NavigationItem.ForgotPassword.route) {
                                showBottomBar.value = false
                                showToolbar.value = false

                                ForgotPassword(
                                    navBack = action.navBack
                                )
                            }
                            composable(route = NavigationItem.Random.route) {
                                showBottomBar.value = true
                                showToolbar.value = true
                                toolbarTitle.value = ""

                                val navItem = bottomNavigationList
                                    .find { it is BottomBarItem.RandomFood }

                                setActiveIndex(bottomNavigationList.indexOf(navItem))
                                RandomizeFood()
                            }
                            composable(route = NavigationItem.RandomList.route) {
                                showBottomBar.value = true
                                showToolbar.value = true
                                toolbarTitle.value = stringResource(id = R.string.templates_title)

                                val navItem = bottomNavigationList
                                    .find { it is BottomBarItem.RandomFoodList }

                                setActiveIndex(bottomNavigationList.indexOf(navItem))
                                SavedFood()
                            }
                            composable(route = NavigationItem.FoodTemplates.route) {
                                showBottomBar.value = true
                                showToolbar.value = true
                                toolbarTitle.value = stringResource(id = R.string.list_foods_title)

                                val navItem = bottomNavigationList
                                    .find { it is BottomBarItem.TemplateFood }

                                setActiveIndex(bottomNavigationList.indexOf(navItem))
                                FoodTemplate(
                                    navToTemplateDetails = action.navToTemplateDetails
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
                                showBottomBar.value = false
                                showToolbar.value = false
                                toolbarTitle.value = ""

                                val id = it.arguments?.getString(KEY_TEMPLATE_ID)
                                id?.let {
                                    FoodTemplateDetails(id)
                                } ?: nav.navigateUp()

                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun additionalIcon(): ImageVector? {
        return when (getActualNavigationItem()) {
            NavigationItem.Random -> Icons.Default.Add
            else -> null
        }
    }

    private fun additionalIconAction(): Unit? {
        return when (getActualNavigationItem()) {
            NavigationItem.Random -> navController?.navigate(NavigationItem.RandomList.route)
            else -> Unit
        }
    }

    private fun navigateTo(navController: NavHostController?, item: BottomBarItem) {
        navController?.navigate(item.navItem.route)
    }

    private fun getActualNavigationItem(): NavigationItem {
        return when (navController?.currentDestination?.route) {
            NavigationItem.Login.route -> NavigationItem.Login
            NavigationItem.Random.route -> NavigationItem.Random
            NavigationItem.RandomList.route -> NavigationItem.RandomList
            NavigationItem.FoodTemplates.route -> NavigationItem.FoodTemplates
            NavigationItem.FoodTemplateDetails.route -> NavigationItem.FoodTemplateDetails
            else -> NavigationItem.Random
        }
    }

    override fun onBackPressed() {
        if (navController?.currentDestination?.route != NavigationItem.Random.route) {
            super.onBackPressed()
        } else {
            finish()
        }

    }
}