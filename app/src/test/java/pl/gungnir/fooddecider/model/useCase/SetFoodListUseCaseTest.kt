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
class SetFoodListUseCaseTest : BaseTest() {

    private lateinit var useCase: SetFoodListUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    private val mockList = listOf(
        "food 1",
        "food 2"
    )

    override fun setup() {
        super.setup()

        useCase = SetFoodListUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun setFoodListUseCaseFailure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.setNewFoodList(anyList())).thenReturn(Failure.Unknown.left())

        val result = useCase.run(mockList)

        assertEquals(Failure.Unknown.left(), result)
        verify(databaseRepo, times(1)).setNewFoodList(mockList)
    }

    @Test
    fun setFoodListUseCaseNone() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.setNewFoodList(anyList())).thenReturn(None.right())

        val result = useCase.run(mockList)

        assertEquals(None.right(), result)
        verify(databaseRepo, times(1)).setNewFoodList(mockList)
    }
}