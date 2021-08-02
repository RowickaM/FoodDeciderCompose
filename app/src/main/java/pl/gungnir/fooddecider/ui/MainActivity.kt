package pl.gungnir.fooddecider.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.model.data.NavigationItem
import pl.gungnir.fooddecider.ui.mics.BottomBar
import pl.gungnir.fooddecider.ui.mics.Toolbar
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme
import pl.gungnir.fooddecider.util.navigation.Actions
import pl.gungnir.fooddecider.util.navigation.NavHostImpl

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null
    private val viewModel by inject<MainViewModel>(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()

            val action = remember(navController) { Actions(navController) }
            val bottomNavigationList = viewModel.bottomNavigationList

            FoodDeciderTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.background),
                    bottomBar = {
                        if (viewModel.showBottomBar.value) {
                            BottomBar(
                                navigationList = bottomNavigationList,
                                activeIndex = viewModel.selectedBottomItem.value,
                                setActiveIndex = viewModel::setSelectedBottomNavItem,
                                onItemCLick = action.navigateTo
                            )
                        }
                    },
                    topBar = {
                        if (viewModel.showToolbar.value) {
                            Toolbar(
                                title = viewModel.title.value,
                                icon = additionalIcon(actualNav = action.getActualNavigationItem()),
                                onIconClick = {
                                    additionalIconAction(
                                        actualNav = action.getActualNavigationItem(),
                                        navToList = action.navToFoodList
                                    )
                                },
                                onLogout = {
                                    viewModel.logout { clearHistoryStack() }
                                }
                            )
                        }
                    }
                ) {
                    navController?.let { nav ->
                        NavHostImpl(
                            viewModel = viewModel,
                            navController = nav,
                            navToHome = action.navToHome,
                            navToRememberPassword = action.navToRememberPassword,
                            navToRegistration = action.navToRegistration,
                            navToTemplateDetails = action.navToTemplateDetails,
                            navBack = action.navBack,
                        )
                    }
                }

            }
        }
    }

    @Composable
    private fun additionalIcon(actualNav: NavigationItem): ImageVector? {
        return when (actualNav) {
            NavigationItem.Random -> Icons.Default.Add
            else -> null
        }
    }

    private fun additionalIconAction(
        actualNav: NavigationItem,
        navToList: () -> Unit
    ) {
        return when (actualNav) {
            NavigationItem.Random -> navToList()
            else -> Unit
        }
    }

    override fun onBackPressed() {
        if (navController?.currentDestination?.route != NavigationItem.Random.route) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    private fun clearHistoryStack(activity: Class<*> = MainActivity::class.java) {
        finish()
        val intent = Intent(this, activity)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}