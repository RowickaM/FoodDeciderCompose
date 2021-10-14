package pl.gungnir.fooddecider.ui.screens.navigation

import BaseE2ETest
import FakeDatabaseRepoImpl
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import org.junit.Before
import org.junit.Test
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.util.test.TestTags

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class NavigationTest : BaseE2ETest() {

    lateinit var emailInputLogin: SemanticsNodeInteraction
    lateinit var passwordInputLogin: SemanticsNodeInteraction
    lateinit var loginButtonLogin: SemanticsNodeInteraction

    @Before
    fun setupLoginElements() {
        emailInputLogin = composeTestRule
            .onNodeWithText(
                getString(R.string.email)
            )
        passwordInputLogin = composeTestRule
            .onNodeWithText(
                getString(R.string.password)
            )
        loginButtonLogin = composeTestRule
            .onNodeWithText(
                getString(R.string.sing_in)
            )
    }

    @Test
    fun login_homeScreenDisplayed() {

        emailInputLogin.assertExists().assertIsDisplayed()
        passwordInputLogin.assertExists().assertIsDisplayed()
        loginButtonLogin.assertExists().assertIsDisplayed()

        emailInputLogin.performTextInput(FakeDatabaseRepoImpl.userMail)
        passwordInputLogin.performTextInput(FakeDatabaseRepoImpl.userPassword)
        loginButtonLogin.performClick()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.randomize_food_click))
            .assertIsDisplayed()
    }

    @Test
    fun login_listItem_allList_Displayed() {
        emailInputLogin.assertExists().assertIsDisplayed()
        passwordInputLogin.assertExists().assertIsDisplayed()
        loginButtonLogin.assertExists().assertIsDisplayed()

        emailInputLogin.performTextInput(FakeDatabaseRepoImpl.userMail)
        passwordInputLogin.performTextInput(FakeDatabaseRepoImpl.userPassword)
        loginButtonLogin.performClick()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.randomize_food_click))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(getString(R.string.bottom_nav_list_label))
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithTag(TestTags.TOOLBAR_TITLE_TAG)
            .assertTextEquals(getString(R.string.list_foods_title))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("food 1")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun login_listItem_allList_Displayed_switchList() {
        emailInputLogin.assertExists().assertIsDisplayed()
        passwordInputLogin.assertExists().assertIsDisplayed()
        loginButtonLogin.assertExists().assertIsDisplayed()

        emailInputLogin.performTextInput(FakeDatabaseRepoImpl.userMail)
        passwordInputLogin.performTextInput(FakeDatabaseRepoImpl.userPassword)
        loginButtonLogin.performClick()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.randomize_food_click))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(getString(R.string.bottom_nav_list_label))
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithTag(TestTags.TOOLBAR_TITLE_TAG)
            .assertTextEquals(getString(R.string.list_foods_title))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.FAB_TAG)
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        //todo not display bottom sheet with list of list name
    }

    @Test
    fun login_templates() {
        emailInputLogin.assertExists().assertIsDisplayed()
        passwordInputLogin.assertExists().assertIsDisplayed()
        loginButtonLogin.assertExists().assertIsDisplayed()

        emailInputLogin.performTextInput(FakeDatabaseRepoImpl.userMail)
        passwordInputLogin.performTextInput(FakeDatabaseRepoImpl.userPassword)
        loginButtonLogin.performClick()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.randomize_food_click))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(getString(R.string.bottom_nav_templates_label))
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithText(FakeDatabaseRepoImpl.templstes[0].categoryFoodName.toUpperCase(Locale.current))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun login_templates_templateDetails() {
        val template = FakeDatabaseRepoImpl.templstes[0]
        emailInputLogin.assertExists().assertIsDisplayed()
        passwordInputLogin.assertExists().assertIsDisplayed()
        loginButtonLogin.assertExists().assertIsDisplayed()

        emailInputLogin.performTextInput(FakeDatabaseRepoImpl.userMail)
        passwordInputLogin.performTextInput(FakeDatabaseRepoImpl.userPassword)
        loginButtonLogin.performClick()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.randomize_food_click))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(getString(R.string.bottom_nav_templates_label))
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithText(template.categoryFoodName.toUpperCase(Locale.current))
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithText(template.categoryFoodName.uppercase())
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun login_credits() {
        emailInputLogin.assertExists().assertIsDisplayed()
        passwordInputLogin.assertExists().assertIsDisplayed()
        loginButtonLogin.assertExists().assertIsDisplayed()

        emailInputLogin.performTextInput(FakeDatabaseRepoImpl.userMail)
        passwordInputLogin.performTextInput(FakeDatabaseRepoImpl.userPassword)
        loginButtonLogin.performClick()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.randomize_food_click))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(getString(R.string.credits))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.show_credits))
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.credits))
            .assertIsDisplayed()
    }

    @Test
    fun login_logout() {
        emailInputLogin.assertExists().assertIsDisplayed()
        passwordInputLogin.assertExists().assertIsDisplayed()
        loginButtonLogin.assertExists().assertIsDisplayed()

        emailInputLogin.performTextInput(FakeDatabaseRepoImpl.userMail)
        passwordInputLogin.performTextInput(FakeDatabaseRepoImpl.userPassword)
        loginButtonLogin.performClick()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.randomize_food_click))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.logout))
            .assertExists()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.email))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun registration_login() {
        composeTestRule
            .onNodeWithText(getString(R.string.no_account))
            .assertExists()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.email))
            .performTextInput("new${FakeDatabaseRepoImpl.userMail}")

        composeTestRule
            .onNodeWithText(getString(R.string.password))
            .performTextInput(FakeDatabaseRepoImpl.userPassword)

        composeTestRule
            .onNodeWithText(getString(R.string.repeat_password))
            .performTextInput(FakeDatabaseRepoImpl.userPassword)

        composeTestRule
            .onNodeWithText(getString(R.string.sing_up))
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.singup_user_success))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(getString(R.string.ok))
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.sing_in))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun registration_error() {
        composeTestRule
            .onNodeWithText(getString(R.string.no_account))
            .assertExists()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.email))
            .performTextInput(FakeDatabaseRepoImpl.userMail)

        composeTestRule
            .onNodeWithText(getString(R.string.password))
            .performTextInput(FakeDatabaseRepoImpl.userPassword)

        composeTestRule
            .onNodeWithText(getString(R.string.repeat_password))
            .performTextInput(FakeDatabaseRepoImpl.userPassword)

        composeTestRule
            .onNodeWithText(getString(R.string.sing_up))
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.cannot_singup_user))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(getString(R.string.ok))
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.sing_in))
            .assertDoesNotExist()
    }

    @Test
    fun forgotPassword_error() {
        composeTestRule
            .onNodeWithText(getString(R.string.reset_password))
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.reset_password_title))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(getString(R.string.email))
            .assertExists()
            .performTextInput("new${FakeDatabaseRepoImpl.userMail}")

        composeTestRule
            .onNodeWithText(getString(R.string.send_link))
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.cannot_sing_in))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(getString(R.string.ok))
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.cannot_sing_in))
            .assertDoesNotExist()
    }

    @Test
    fun forgotPassword_login() {
        composeTestRule
            .onNodeWithText(getString(R.string.reset_password))
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.reset_password_title))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(getString(R.string.email))
            .assertExists()
            .performTextInput(FakeDatabaseRepoImpl.userMail)

        composeTestRule
            .onNodeWithText(getString(R.string.send_link))
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithText(getString(R.string.sing_in))
            .assertExists()
            .assertIsDisplayed()
    }
}