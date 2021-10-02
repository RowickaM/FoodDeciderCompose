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

@ExperimentalCoroutinesApi
class GetSavedItemsCollectionUseCaseTest : BaseTest() {

    private lateinit var useCase: GetSavedItemsCollectionUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

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
        whenever(databaseRepo.getSavedFood(anyString())).thenReturn(null)

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
                        selectedList = "",
                        savedList = listOf()
                    )
                )
            )

        val result = useCase.run("")

        result.onSuccess {
            testCoroutineRule.runBlockingTest {
                assertEquals(emptyList<String>(), it.first())
            }
        }
        verify(databaseRepo, times(1)).getSavedFood(anyString())
    }
}