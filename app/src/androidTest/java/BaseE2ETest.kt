import android.app.Activity
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.koin.androidx.compose.getViewModel
import pl.gungnir.fooddecider.ui.MainActivity
import pl.gungnir.fooddecider.ui.MainScreen
import pl.gungnir.fooddecider.ui.MainViewModel
import pl.gungnir.fooddecider.ui.bottomSheet.BottomSheetType
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme
import pl.gungnir.fooddecider.util.navigation.Actions
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
open class BaseE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val databaseRepo = FakeDatabaseRepoImpl()

    @Before
    open fun setup() {
        ServiceDatabaseRepo.changeDatabaseRepo(databaseRepo)
    }

    @Before
    open fun setupMainScreen() {
        composeTestRule.setContent {
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()

            val viewModel: MainViewModel = getViewModel()
            val actions = remember(navController) { Actions(navController) }
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

            FoodDeciderTheme {
                MainScreen(
                    bottomSheetState = bottomSheetState,
                    bottomSheetType = bottomSheetType,
                    hideSheet = hideSheet,
                    openSheet = openSheet,
                    bottomNavigationList = bottomNavigationList,
                    actions = actions,
                    navController = navController,
                    clearHistoryStack = { composeTestRule.activity.clearHistoryStack() }
                )
            }
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    fun Activity.clearHistoryStack(activity: Class<*> = MainActivity::class.java) {
        finish()
        val intent = Intent(this, activity)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun getString(resId: Int): String = composeTestRule
        .activity
        .getString(resId)
}