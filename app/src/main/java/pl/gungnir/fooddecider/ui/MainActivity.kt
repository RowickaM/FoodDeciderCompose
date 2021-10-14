package pl.gungnir.fooddecider.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.model.data.NavigationItem
import pl.gungnir.fooddecider.ui.bottomSheet.BottomSheetType
import pl.gungnir.fooddecider.ui.bottomSheet.BottomSheetWrapper
import pl.gungnir.fooddecider.ui.mics.BottomBar
import pl.gungnir.fooddecider.ui.mics.BottomBarItem
import pl.gungnir.fooddecider.ui.mics.FloatingActionButton
import pl.gungnir.fooddecider.ui.mics.Toolbar
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme
import pl.gungnir.fooddecider.util.navigation.Actions
import pl.gungnir.fooddecider.util.navigation.NavHostImpl

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private var actions: Actions? = null
    private val viewModel by inject<MainViewModel>(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()

            actions = remember(navController) { Actions(navController) }
            val bottomNavigationList = viewModel.bottomNavigationList

            val bottomSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden
            )
            val (bottomSheetType, changeBottomSheet) = remember<MutableState<BottomSheetType?>> {
                mutableStateOf(null)
            }
            val openSheet: (BottomSheetType) -> Unit = {
                coroutineScope.launch {
                    changeBottomSheet(it)
                    bottomSheetState.show()
                }
            }
            val hideSheet: () -> Unit = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
            }

            actions?.let { action ->
                FoodDeciderTheme {
                    MainScreen(
                        bottomSheetState = bottomSheetState,
                        bottomSheetType = bottomSheetType,
                        hideSheet = hideSheet,
                        openSheet = openSheet,
                        bottomNavigationList = bottomNavigationList,
                        actions = action,
                        navController = navController,
                        clearHistoryStack = { clearHistoryStack() }
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        if (actions?.getActualNavigationItem()?.route != NavigationItem.Random.route) {
            super.onBackPressed()
            actions?.getActualNavigationItem()?.let { viewModel.setIndex(it) }
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

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(
    viewModel: MainViewModel = getViewModel(),
    bottomSheetState: ModalBottomSheetState,
    bottomSheetType: BottomSheetType?,
    hideSheet: () -> Unit,
    openSheet: (BottomSheetType) -> Unit,
    clearHistoryStack: () -> Unit,
    bottomNavigationList: ArrayList<BottomBarItem>,
    actions: Actions,
    navController: NavHostController
) {
    BottomSheetWrapper(
        state = bottomSheetState,
        sheetState = bottomSheetType,
        closeSheet = hideSheet,
    ) {
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
                        onItemCLick = actions.navigateTo
                    )
                }
            },
            topBar = {
                if (viewModel.showToolbar.value) {
                    Toolbar(
                        title = viewModel.title.value,
                        icon = additionalIcon(actualNav = actions.getActualNavigationItem()),
                        onIconClick = {
                            openSheet(BottomSheetType.AddElementToList)
                        },
                        onLogout = {
                            viewModel.logout { clearHistoryStack() }
                        }
                    )
                }
            },
            floatingActionButton = {
                if (viewModel.showFab.value) {
                    FloatingActionButton(onClick = {
                        openSheet(
                            BottomSheetType.ShowLists(
                                list = viewModel.savedList,
                                selected = viewModel.selectedList.value,
                                onItemClick = viewModel::changeSelectedList
                            )
                        )
                    })
                }
            },
        ) {
            NavHostImpl(
                viewModel = viewModel,
                navController = navController,
                navToHome = actions.navToHome,
                navToRememberPassword = actions.navToRememberPassword,
                navToRegistration = actions.navToRegistration,
                navToTemplateDetails = actions.navToTemplateDetails,
                navBack = actions.navBack,
            )
        }
    }
}

@Composable
private fun additionalIcon(actualNav: NavigationItem): ImageVector? {
    return when (actualNav) {
        NavigationItem.Random -> Icons.Default.Add
        NavigationItem.RandomList -> Icons.Default.Add
        else -> null
    }
}