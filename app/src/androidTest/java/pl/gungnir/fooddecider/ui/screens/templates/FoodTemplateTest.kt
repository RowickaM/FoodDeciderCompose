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
import pl.gungnir.fooddecider.util.test.TestTags

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalTestApi
class FoodTemplateTest : BaseTest() {

   @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val templates = FakeDatabaseRepoImpl.templstes

    @Before
    override fun setup() {
        super.setup()
        composeTestRule.setContent {
            val viewModel: TemplatesViewModel = getViewModel()
            viewModel.changeLists(templates)

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
            .onNodeWithTag(TestTags.TEMPLATE_LIST_TAG)
            .performScrollToIndex(templates.size - 1)

        composeTestRule
            .onNodeWithText(templates.last().categoryFoodName.toUpperCase(Locale.current))
            .assertExists()
            .assertIsDisplayed()
    }
}