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
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.model.data.NavigationItem
import pl.gungnir.fooddecider.ui.bottomSheet.BottomSheetType
import pl.gungnir.fooddecider.ui.bottomSheet.BottomSheetWrapper
import pl.gungnir.fooddecider.ui.mics.BottomBar
import pl.gungnir.fooddecider.ui.mics.FloatingActionButton
import pl.gungnir.fooddecider.ui.mics.Toolbar
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme
import pl.gungnir.fooddecider.util.navigation.Actions
import pl.gungnir.fooddecider.util.navigation.NavHostImpl

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    companion object {

        const val SHOW_COMPONENT = "SHOW_COMPONENT"
    }

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
    }

    @Composable
    private fun additionalIcon(actualNav: NavigationItem): ImageVector? {
        return when (actualNav) {
            NavigationItem.Random -> Icons.Default.Add
            NavigationItem.RandomList -> Icons.Default.Add
            else -> null
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