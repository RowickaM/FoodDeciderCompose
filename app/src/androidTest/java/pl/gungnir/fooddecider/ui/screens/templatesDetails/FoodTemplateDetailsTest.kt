package pl.gungnir.fooddecider.ui.screens.templatesDetails

import BaseTest
import FakeDatabaseRepoImpl
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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

    //todo order of test has template for now. Problem with pass testable template

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
    fun templateProperlyDisplayed() {
        val template = FakeDatabaseRepoImpl.getTemplateDetails(
            FakeDatabaseRepoImpl.templstes[0]
        )

        composeTestRule.setContent {
            val viewModel: FoodTemplateDetailsViewModel = getViewModel()

            FoodDeciderTheme {
                FoodTemplateDetails(templateId = template.id, viewModel = viewModel)
            }

            viewModel.setTemplateDetails(template)
        }

        composeTestRule.onNodeWithText(template.categoryFoodName.uppercase())
            .assertExists()
            .assertIsDisplayed()

        //check not added element
        if (template.notAdded.isNotEmpty()) {
            composeTestRule.onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.templates_to_add_list)
            )
                .assertExists()
                .assertIsDisplayed()

        }
        template.notAdded.forEach {
            composeTestRule.onNodeWithText(it)
                .assertExists()
                .assertIsDisplayed()

            composeTestRule.onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.templates_add_food_template,
                    it
                )
            )
                .assertExists()
                .assertIsDisplayed()
        }

        //check added element
        if (template.added.isNotEmpty()) {
            composeTestRule.onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.templates_added_list)
            )
                .assertExists()
                .assertIsDisplayed()
        }

        composeTestRule.onNodeWithTag("addedList")
            .onChildren()
            .assertCountEquals(template.added.size)
    }
}