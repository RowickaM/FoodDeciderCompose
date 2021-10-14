package pl.gungnir.fooddecider.ui.screens.registration

import BaseTest
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
class RegistrationTest : BaseTest() {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun displayedAllElement() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                Registration(navBack = {})
            }
        }

        composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.email)
            )
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.password)
            )
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.repeat_password)
            )
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.sing_up)
            )
            .assertExists()
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun emailInvalid() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                Registration(navBack = {})
            }
        }

        val emailInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.email)
            )
        val passwordInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.password)
            )
        val loginButton = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.sing_up)
            )

        emailInput.performTextInput("email@")
        passwordInput.performClick()

        loginButton.assertExists().assertIsDisplayed().assertIsNotEnabled()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.invalid_format))
            .assertExists()
    }

    @Test
    fun emailValidPasswordInvalid() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                Registration(navBack = {})
            }
        }

        val emailInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.email)
            )
        val passwordInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.password)
            )
        val passwordRepeatInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.repeat_password)
            )
        val loginButton = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.sing_up)
            )

        emailInput.performTextInput("email@email.pl")

        passwordInput.performTextInput("123")

        passwordRepeatInput.performClick()


        loginButton.assertExists().assertIsDisplayed().assertIsNotEnabled()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.too_short))
            .assertExists()
    }

    @Test
    fun repeatPasswordInvalid() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                Registration(navBack = {})
            }
        }

        val emailInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.email)
            )
        val passwordInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.password)
            )
        val passwordRepeatInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.repeat_password)
            )
        val loginButton = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.sing_up)
            )

        emailInput.performTextInput("email@email.pl")

        passwordInput.performTextInput("123456")

        passwordRepeatInput.performTextInput("123")

        emailInput.performClick()


        loginButton.assertExists().assertIsDisplayed().assertIsNotEnabled()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.too_short))
            .assertExists()
    }

    @Test
    fun allInputsValid() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                Registration(navBack = {})
            }
        }

        val emailInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.email)
            )
        val passwordInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.password)
            )
        val passwordRepeatInput = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.repeat_password)
            )
        val loginButton = composeTestRule
            .onNodeWithText(
                composeTestRule
                    .activity
                    .getString(R.string.sing_up)
            )

        emailInput.performTextInput("email@email.pl")

        passwordInput.performTextInput("123456")

        passwordRepeatInput.performTextInput("123456")

        emailInput.performClick()


        loginButton.assertExists().assertIsDisplayed().assertIsEnabled()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.too_short))
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.invalid_format))
            .assertDoesNotExist()
    }
}