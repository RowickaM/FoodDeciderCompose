package pl.gungnir.fooddecider.ui.screens.templates

import BaseTest
import FakeDatabaseRepoImpl
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import coil.annotation.ExperimentalCoilApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.compose.getViewModel
import pl.gungnir.fooddecider.ui.MainActivity
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalTestApi
class FoodTemplateTest : BaseTest() {

    //todo cannot get data from FakeDatabaseRepo. For now is additional function "changeList" to control list in test

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val templates = FakeDatabaseRepoImpl.templstes

    @Before
    override fun setup() {
        super.setup()
        composeTestRule.setContent {
            val viewModel: FoodTemplatesSharedViewModel = getViewModel()
            viewModel.changeList(templates)

            FoodDeciderTheme {
                FoodTemplate(navToTemplateDetails = {}, viewModel = viewModel)
            }

        }
    }

    @Test
    fun checkAllElementDisplayed() {
        composeTestRule
            .onNodeWithText(templates[0].categoryFoodName.toUpperCase(Locale.current))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("templateList")
            .performScrollToIndex(templates.size - 1)

        composeTestRule
            .onNodeWithText(templates.last().categoryFoodName.toUpperCase(Locale.current))
            .assertExists()
            .assertIsDisplayed()
    }
}