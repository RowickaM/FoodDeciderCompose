package pl.gungnir.fooddecider.ui.screens.forgotPassword

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.MainActivity
import pl.gungnir.fooddecider.ui.theme.FoodDeciderTheme
import pl.gungnir.fooddecider.util.repo.DatabaseRepoImpl
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class ForgotPasswordTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        ServiceDatabaseRepo.changeDatabaseRepo(DatabaseRepoImpl())
    }

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
            .performTextInput(DatabaseRepoImpl.userMail)

        actionButton.performClick()
    }

    @Test
    fun forgotPasswordSendLinkFailed() {
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
            .performTextInput("email@example.com")

        actionButton.performClick()

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.cannot_sing_in))

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
            .performTextInput("email")

        emailInput.performImeAction()
        actionButton.assertIsNotEnabled()

        emailInput.performClick()
            .performTextInput("@email.com")
        actionButton.assertIsEnabled()
    }
}
