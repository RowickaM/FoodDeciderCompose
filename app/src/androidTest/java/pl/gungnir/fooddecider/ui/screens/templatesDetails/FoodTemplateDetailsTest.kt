package pl.gungnir.fooddecider.ui.screens.templatesDetails

import BaseTest
import FakeDatabaseRepoImpl
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.compose.getViewModel
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.MainActivity
import pl.gungnir.fooddecider.ui.screens.templates.FoodTemplatesSharedViewModel
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class FoodTemplateDetailsTest : BaseTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val template = FakeDatabaseRepoImpl
        .getTemplateDetails(FakeDatabaseRepoImpl.templstes[0])

    @Test
    fun templateNotExist() {
        composeTestRule.setContent {
            val viewModel: FoodTemplatesSharedViewModel = getViewModel()
            viewModel.changeList(FakeDatabaseRepoImpl.templstes)

            FoodDeciderTheme {
                FoodTemplateDetails(templateId = "-1")
            }
        }

        composeTestRule.onNodeWithText(
            composeTestRule
                .activity
                .getString(R.string.templates_template_no_exist)
        )
            .assertExists()
            .assertIsDisplayed()
    }
}