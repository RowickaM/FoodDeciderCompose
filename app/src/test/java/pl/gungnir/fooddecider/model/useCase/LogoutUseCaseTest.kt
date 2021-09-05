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
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LogoutUseCaseTest : BaseTest() {

    private lateinit var useCase: LogoutUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    override fun setup() {
        super.setup()

        useCase = LogoutUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun logoutUser_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.logoutUser()).thenReturn(Failure.UserNotExist.left())

        val result = useCase.run(None)

        assertEquals(Failure.UserNotExist.left(), result)
        verify(databaseRepo, times(1)).logoutUser()
    }

    @Test
    fun logoutUser_None() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.logoutUser()).thenReturn(None.right())

        val result = useCase.run(None)

        assertEquals(None.right(), result)
        verify(databaseRepo, times(1)).logoutUser()
    }
}