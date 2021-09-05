package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class IsUserLoggedUseCaseTest : BaseTest() {

    private lateinit var useCase: IsUserLoggedUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    override fun setup() {
        super.setup()

        useCase = IsUserLoggedUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        Mockito.verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun isUserLogged_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.isUserLogged()).thenReturn(Failure.UserNotExist.left())

        val result = useCase.run(None)

        assertEquals(Failure.UserNotExist.left(), result)
        Mockito.verify(databaseRepo, Mockito.times(1)).isUserLogged()
    }

    @Test
    fun isUserLogged_false() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.isUserLogged()).thenReturn(false.right())

        val result = useCase.run(None)

        assertEquals(false.right(), result)
        Mockito.verify(databaseRepo, Mockito.times(1)).isUserLogged()
    }
}