package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import pl.gungnir.fooddecider.BaseTest
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class SignUpUserUseCaseTest : BaseTest() {

    private lateinit var useCase: SignUpUserUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    private val params = SignUpUserUseCase.Params(
        "email",
        "password"
    )

    override fun setup() {
        super.setup()

        useCase = SignUpUserUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun SignUpUserUseCase_Failure() = testCoroutineRule.runBlockingTest {
        whenever(
            databaseRepo.signUpUser(
                anyString(),
                anyString()
            )
        ).thenReturn(Failure.UserNotExist.left())

        val result = useCase.run(params)

        assertEquals(Failure.UserNotExist.left(), result)
        verify(databaseRepo, times(1)).signUpUser("email", "password")
    }

    @Test
    fun SignUpUserUseCase_None() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.signUpUser(anyString(), anyString())).thenReturn(MOCK_STRING.right())

        val result = useCase.run(params)

        assertEquals(MOCK_STRING.right(), result)
        verify(databaseRepo, times(1)).signUpUser("email", "password")
    }
}