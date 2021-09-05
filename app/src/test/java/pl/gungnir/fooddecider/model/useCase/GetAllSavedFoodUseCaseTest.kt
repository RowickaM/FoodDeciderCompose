package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.util.*
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

@ExperimentalCoroutinesApi
class GetAllSavedFoodUseCaseTest : BaseTest() {

    private lateinit var useCase: GetAllSavedFoodUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    override fun setup() {
        super.setup()

        useCase = GetAllSavedFoodUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun getAllSavedFood_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.getSavedFood()).thenReturn(null)

        val result = useCase.run(None)

        assertEquals(Failure.UserNotExist.left(), result)
        verify(databaseRepo, times(1)).getSavedFood()
    }

    @Test
    fun getAllSavedFood_None() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.getSavedFood()).thenReturn(flowOf(emptyList()))

        val result = useCase.run(None)

        result.onSuccess {
            testCoroutineRule.runBlockingTest {
                assertEquals(emptyList<String>(), it.first())
            }
        }
        verify(databaseRepo, times(1)).getSavedFood()
    }
}