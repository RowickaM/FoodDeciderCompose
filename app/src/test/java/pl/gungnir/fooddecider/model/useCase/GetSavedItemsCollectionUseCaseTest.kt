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
import pl.gungnir.fooddecider.model.data.SavedFoodCollection
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.onSuccess
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.right

@ExperimentalCoroutinesApi
class GetSavedItemsCollectionUseCaseTest : BaseTest() {

    private lateinit var useCase: GetSavedItemsCollectionUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    private val savedCollectionEmpty = SavedFoodCollection(
        allListName = listOf(),
        selectedListName = "",
        savedList = listOf()
    )

    override fun setup() {
        super.setup()

        useCase = GetSavedItemsCollectionUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun getAllSavedFood_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.getSavedFood(anyString())).thenReturn(Failure.UserNotExist.left())

        val result = useCase.run("")

        assertEquals(Failure.UserNotExist.left(), result)
        verify(databaseRepo, times(1)).getSavedFood(anyString())
    }

    @Test
    fun getAllSavedFood_None() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.getSavedFood(anyString()))
            .thenReturn(
                flowOf(
                    SavedFoodCollection(
                        allListName = listOf(),
                        selectedListName = "",
                        savedList = listOf()
                    )
                ).right()
            )

        val result = useCase.run("")

        result.onSuccess {
            testCoroutineRule.runBlockingTest {
                assertEquals(savedCollectionEmpty, it.first())
            }
        }
        verify(databaseRepo, times(1)).getSavedFood(anyString())
    }
}