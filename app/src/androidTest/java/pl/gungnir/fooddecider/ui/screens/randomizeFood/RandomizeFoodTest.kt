package pl.gungnir.fooddecider.ui.screens.randomizeFood

import BaseTest
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.MainActivity
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme
import pl.gungnir.fooddecider.util.test.TestTags

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
class RandomizeFoodTest : BaseTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun displayedAllElements() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                RandomizeFood()
            }
        }

        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.randomize_food_click))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.randomize_food_image_click))
            .assertIsDisplayed()
    }

    @Test
    fun randomAfterClick() {

        composeTestRule.setContent {
            FoodDeciderTheme {
                RandomizeFood()
            }
        }

        val image = composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.randomize_food_click))
        image.assertIsDisplayed()

        val result = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.randomize_food_image_click))
        result.assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.BOX_DRAW_ANIMATION_TAG)
            .performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.randomize_food_image_click))
            .assertExists()
    }
}