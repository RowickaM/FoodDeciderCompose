package pl.gungnir.fooddecider.ui.screens.login

import BaseTest
import FakeDatabaseRepoImpl
import TestCoroutineRule
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
class LoginTest : BaseTest() {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val testCoroutineRule = TestCoroutineRule()

    @Test
    fun displayedAllElement() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                Login(
                    navToHome = {},
                    navToRegistration = {},
                    navToRememberPassword = {}
                )
            }
        }

        val emailInput =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
        val passwordInput =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
        val toRegister =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.no_account))
        val toResetPassword =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.reset_password))
        val loginButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.sing_in))

        emailInput.assertExists().assertIsDisplayed()
        passwordInput.assertExists().assertIsDisplayed()
        toRegister.assertExists().assertIsDisplayed()
        toResetPassword.assertExists().assertIsDisplayed()
        loginButton.assertExists().assertIsDisplayed().assertIsNotEnabled()
    }

    @Test
    fun emailInvalid() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                Login(
                    navToHome = {},
                    navToRegistration = {},
                    navToRememberPassword = {}
                )
            }
        }

        val emailInput =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
        val passwordInput =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
        val loginButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.sing_in))

        emailInput.performTextInput("email@")
        emailInput.performImeAction()

        emailInput.assertIsNotFocused()
        passwordInput.assertIsFocused()
        loginButton.assertExists().assertIsDisplayed().assertIsNotEnabled()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.invalid_format))
            .assertExists()
    }

    @Test
    fun passwordInvalid() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                Login(
                    navToHome = {},
                    navToRegistration = {},
                    navToRememberPassword = {}
                )
            }
        }

        val emailInput =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
        val passwordInput =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
        val loginButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.sing_in))

        passwordInput.performTextInput("pass")
        passwordInput.performImeAction()

        emailInput.assertIsNotFocused()
        passwordInput.assertIsFocused()

        emailInput.performClick()

        loginButton.assertExists().assertIsDisplayed().assertIsNotEnabled()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.too_short))
            .assertExists()
    }

    @Test
    fun emailAndPasswordValid() {
        composeTestRule.setContent {
            FoodDeciderTheme {
                Login(
                    navToHome = {},
                    navToRegistration = {},
                    navToRememberPassword = {}
                )
            }
        }

        val emailInput =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
        val passwordInput =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
        val loginButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.sing_in))

        emailInput.performTextInput(FakeDatabaseRepoImpl.userMail)
        passwordInput.performTextInput(FakeDatabaseRepoImpl.userPassword)
        loginButton.assertExists().assertIsDisplayed().assertIsEnabled()
    }

    @Test
    fun afterClickInvalidCredentials() = testCoroutineRule.runBlockingTest {
        composeTestRule.setContent {
            FoodDeciderTheme {
                Login(
                    navToHome = {},
                    navToRegistration = {},
                    navToRememberPassword = {}
                )
            }
        }

        val emailInput =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.email))
        val passwordInput =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.password))
        val loginButton =
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.sing_in))

        emailInput.performTextInput(FakeDatabaseRepoImpl.userMail)
        passwordInput.performTextInput(FakeDatabaseRepoImpl.userPassword + "pass")
        loginButton.assertExists().assertIsDisplayed().assertIsEnabled()
        loginButton.performClick()


        composeTestRule.onNodeWithTag(TestTags.DIALOG_TAG).assertExists()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.ok))
            .performClick()
    }
}