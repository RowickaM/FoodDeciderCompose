package pl.gungnir.fooddecider.ui.screens.forgotPassword

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.MainActivity
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class ForgotPasswordTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun forgotPasswordDisplayedAllElements() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                ForgotPassword(navBack = {})
            }
        }

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.reset_password_title))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.reset_password_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.email))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.send_link))
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun forgotPasswordSendLinkSuccessful() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                ForgotPassword(navBack = {})
            }
        }

        val emailInput = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.email))
        emailInput.assertIsDisplayed()

        val actionButton = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.send_link))
        actionButton.assertIsDisplayed()
            .assertHasClickAction()


        emailInput.performClick()
            .performTextInput("fistasze.23.21@gmail.com")

        actionButton.performClick()
    }

    @Test
    fun forgotPasswordEmailInvalidFormat() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                ForgotPassword(navBack = {})
            }
        }

        val emailInput = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.email))
        emailInput.assertIsDisplayed()

        val actionButton = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.send_link))
        actionButton.assertIsDisplayed()
            .assertHasClickAction()


        emailInput.performClick()
            .performTextInput("fistasze.23.21")

        emailInput.performImeAction()
        actionButton.assertIsNotEnabled()

        emailInput.performClick()
            .performTextInput("@gmail.com")
        actionButton.assertIsEnabled()
    }
}
