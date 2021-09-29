package pl.gungnir.fooddecider.ui.screens.savedFood

import BaseTest
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

    @Test
    fun displayedEmptyList() {
        composeTestRule.setContent {
            viewModel = getViewModel()

            FoodDeciderTheme {
                SavedFood(viewModel)
            }
        }

        composeTestRule
            .onNode(hasText(composeTestRule.activity.getString(R.string.list_foods_no_foods)))
            .assertExists()
            .assertIsDisplayed()

    }

    @Test
    fun displayedListItems() {
        val foods = listOf(
            "Food 1",
            "Food 2",
        )
        composeTestRule.setContent {
            viewModel = getViewModel()

            FoodDeciderTheme {
                SavedFood(viewModel)
            }

            viewModel.changeList(foods)
        }

        composeTestRule
            .onNode(hasText(composeTestRule.activity.getString(R.string.list_foods_no_foods)))
            .assertDoesNotExist()

        foods.forEach {
            composeTestRule.onNodeWithText(it)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun removeItem() {
        val foods = listOf(
            "Food 1",
        )
        composeTestRule.setContent {
            viewModel = getViewModel()

            FoodDeciderTheme {
                SavedFood(viewModel)
            }

            viewModel.changeList(foods)
        }

        composeTestRule.onNodeWithText("Food 1")
            .assertExists()
            .assertIsDisplayed()


        composeTestRule.onNodeWithText("Food 1")
            .performGesture {
                swipeLeft()
            }

        viewModel.changeList()

        composeTestRule
            .onNode(hasText(composeTestRule.activity.getString(R.string.list_foods_no_foods)))
            .assertExists()
            .assertIsDisplayed()
    }
}