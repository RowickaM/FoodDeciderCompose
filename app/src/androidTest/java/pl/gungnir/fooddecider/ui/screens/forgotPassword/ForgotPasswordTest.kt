package pl.gungnir.fooddecider.ui.screens.forgotPassword

import BaseTest
import FakeDatabaseRepoImpl
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.compose.getViewModel
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.MainActivity
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class ForgotPasswordTest : BaseTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: ForgotPasswordViewModel

    @Before
    fun setupView() {
        composeTestRule.setContent {
            viewModel = getViewModel()

            FoodDeciderTheme {
                ForgotPassword(
                    navBack = {},
                    viewModel = viewModel
                )
            }
        }
    }

    @Test
    fun forgotPasswordDisplayedAllElements() {
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
        val emailInput = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.email))
        emailInput.assertIsDisplayed()

        val actionButton = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.send_link))
        actionButton.assertIsDisplayed()
            .assertHasClickAction()


        emailInput.performClick()
            .performTextInput(FakeDatabaseRepoImpl.userMail)

        actionButton.performClick()
    }

    @Test
    fun forgotPasswordSendLinkFailed() {

        val emailInput = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.email))
        emailInput.assertIsDisplayed()

        val actionButton = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.send_link))
        actionButton.assertIsDisplayed()
            .assertHasClickAction()


        emailInput.performClick()
            .performTextInput("email@example.com")

        actionButton.performClick()

        //todo couldn't check if dialog displayed!
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.cannot_sing_in))

    }

    @Test
    fun forgotPasswordEmailInvalidFormat() {
        val emailInput = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.email))
        emailInput.assertIsDisplayed()

        val actionButton = composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.send_link))
        actionButton.assertIsDisplayed()
            .assertHasClickAction()


        emailInput.performClick()
            .performTextInput("email")

        emailInput.performImeAction()
        actionButton.assertIsNotEnabled()

        emailInput.performClick()
            .performTextInput("@email.com")
        actionButton.assertIsEnabled()
    }
}
