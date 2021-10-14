package pl.gungnir.fooddecider.ui.screens.savedFood

import BaseTest
import FakeDatabaseRepoImpl
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.compose.getViewModel
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.MainActivity
import pl.gungnir.fooddecider.ui.screens.randomizeFood.SaveFoodShareViewModel
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme

@ExperimentalTestApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
class SavedFoodTest : BaseTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: SaveFoodShareViewModel

    private val foodList = FakeDatabaseRepoImpl.savedFoodCollection1.savedList

    @Test
    fun displayedListItems() {
        composeTestRule.setContent {
            viewModel = getViewModel()

            FoodDeciderTheme {
                SavedFood(FakeDatabaseRepoImpl.listName1, viewModel)
            }
        }

        composeTestRule
            .onNode(hasText(composeTestRule.activity.getString(R.string.list_foods_no_foods)))
            .assertDoesNotExist()

        composeTestRule.onNodeWithText(foodList[0])
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun emptyList_infoDisplayed() {
        composeTestRule.setContent {
            viewModel = getViewModel()

            FoodDeciderTheme {
                SavedFood(FakeDatabaseRepoImpl.listName1, viewModel)
            }
        }
        viewModel.onChangeList(emptyList())

        composeTestRule
            .onNode(hasText(composeTestRule.activity.getString(R.string.list_foods_no_foods)))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun removeItem() {
        composeTestRule.setContent {
            viewModel = getViewModel()

            FoodDeciderTheme {
                SavedFood(FakeDatabaseRepoImpl.listName1, viewModel)
            }
        }

        composeTestRule.onNodeWithText(foodList[0])
            .assertExists()
            .assertIsDisplayed()


        composeTestRule.onNodeWithText(foodList[0])
            .performGesture {
                swipeLeft()
            }
    }
}