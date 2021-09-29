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
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class FoodTemplateDetailsTest : BaseTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun templateNotExist() {
        composeTestRule.setContent {
            val viewModel: TemplateDetailsViewModel = getViewModel()

            FoodDeciderTheme {
                FoodTemplateDetails(templateId = "-1", viewModel)
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.templates_template_no_exist))
            .assertExists()
            .assertIsDisplayed()
    }


    @Test
    fun templateDisplayed() {
        val index = 0
        val template = FakeDatabaseRepoImpl.templstes[index]

        composeTestRule.setContent {
            val viewModel: TemplateDetailsViewModel = getViewModel()
            viewModel.templateDetails.value = databaseRepo.splitToTemplateDetails(template)

            FoodDeciderTheme {
                FoodTemplateDetails(templateId = (index + 1).toString(), viewModel)
            }
        }

        composeTestRule.onNodeWithText(
            composeTestRule
                .activity
                .getString(R.string.templates_template_no_exist)
        )
            .assertDoesNotExist()

        composeTestRule.onNodeWithText(
            text = template.categoryFoodName,
            ignoreCase = true
        )
            .assertExists()
            .assertIsDisplayed()

    }
}