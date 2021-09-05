package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LoginUseCaseTest : BaseTest() {

    private lateinit var useCase: LoginUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    private val params = LoginUseCase.Params(
        "email",
        "password"
    )

    override fun setup() {
        super.setup()

        useCase = LoginUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun loginUser_Failure() = testCoroutineRule.runBlockingTest {
        whenever(
            databaseRepo.loginUser(
                anyString(),
                anyString()
            )
        ).thenReturn(Failure.UserNotExist.left())

        val result = useCase.run(params)

        assertEquals(Failure.UserNotExist.left(), result)
        verify(databaseRepo, times(1)).loginUser("email", "password")
    }

    @Test
    fun loginUser_ListOfTemplate() = testCoroutineRule.runBlockingTest {
        whenever(
            databaseRepo.loginUser(
                anyString(),
                anyString()
            )
        ).thenReturn("UID".right())

        val result = useCase.run(params)

        assertEquals("UID".right(), result)
        verify(databaseRepo, times(1)).loginUser("email", "password")
    }
}