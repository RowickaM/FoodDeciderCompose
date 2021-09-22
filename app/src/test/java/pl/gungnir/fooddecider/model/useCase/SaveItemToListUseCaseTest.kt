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
class SaveItemToListUseCaseTest : BaseTest() {

    private lateinit var useCase: SaveItemToListUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    override fun setup() {
        super.setup()

        useCase = SaveItemToListUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun saveItemUseCase_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.saveNewFoodToList(anyString())).thenReturn(Failure.Unknown.left())

        val result = useCase.run(MOCK_STRING)

        assertEquals(Failure.Unknown.left(), result)
        verify(databaseRepo, times(1)).saveNewFoodToList(MOCK_STRING)
    }

    @Test
    fun saveItemUseCase_None() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.saveNewFoodToList(anyString()))
            .thenReturn(None.right())

        val result = useCase.run(MOCK_STRING)

        assertEquals(None.right(), result)
        verify(databaseRepo, times(1)).saveNewFoodToList(MOCK_STRING)
    }
}