package pl.gungnir.fooddecider.util.helper

import org.junit.Test
import pl.gungnir.fooddecider.BaseTest
import kotlin.test.assertEquals

class ValidationTest : BaseTest() {

    @Test
    fun testEmail_notMatch() {
        val invalidMail = "invalid!"

        val result = isEmailValid(invalidMail)

        assertEquals(false, result)
    }

    @Test
    fun testEmail_match() {
        val validMail = "valid@exaple.com"

        val result = isEmailValid(validMail)

        assertEquals(true, result)
    }

    @Test
    fun password_invalid() {
        val invalidPassword = "short"

        val result = isPasswordValid(invalidPassword)

        assertEquals(false, result)
    }

    @Test
    fun password_valid() {
        val validPassword = "Password"

        val result = isPasswordValid(validPassword)

        assertEquals(true, result)
    }

}