package pl.gungnir.fooddecider.ui.screens.randomizeFood

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.model.useCase.GetAllSavedFoodUseCase
import pl.gungnir.fooddecider.model.useCase.SetFoodListUseCase
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertContains

@ExperimentalCoroutinesApi
class SaveFoodShareViewModelTest : BaseTest() {

    private lateinit var viewModel: SaveFoodShareViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getAllSavedFoodUseCase: GetAllSavedFoodUseCase

    @Mock
    private lateinit var setFoodListUseCase: SetFoodListUseCase

    private val mockFoodList = listOf(
        "food 1",
        "food 2",
        "food 3"
    )

    override fun setup() {
        super.setup()

        viewModel = SaveFoodShareViewModel(
            getAllSavedFoodUseCase,
            setFoodListUseCase
        )
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(
            getAllSavedFoodUseCase,
            setFoodListUseCase
        )
    }

    @Test
    fun onFoodNameChange() {
        viewModel.onFoodNameChange(MOCK_STRING)

        assertEquals(MOCK_STRING, viewModel.newFood.value)
    }

    @Test
    fun onAddFoodClick_onFailure() = testCoroutineRule.runBlockingTest {
        addToList()

        whenever(setFoodListUseCase.run(any())).thenReturn(Failure.Unknown.left())
        viewModel.onFoodNameChange(MOCK_STRING)

        viewModel.onAddFoodClick {}

        verify(setFoodListUseCase, times(1)).run(any())
        assertEquals(MOCK_STRING, viewModel.newFood.value)
        assertEquals(
            mockFoodList,
            (viewModel.listOfSavedFood.value as Result.SuccessFetch).result
        )
    }

    @Test
    fun onDrawFood() = testCoroutineRule.runBlockingTest {
        addToList()

        viewModel.drawFood(0L)
        val result = viewModel.randomFood.value

        assertEquals(true, result is Result.Success)
        assertContains(mockFoodList, (result as Result.Success).result)
    }

    private fun addToList() = testCoroutineRule.runBlockingTest {
        whenever(getAllSavedFoodUseCase.run(None)).thenReturn(flowOf(mockFoodList).right())

        viewModel.onInitialize()

        verify(getAllSavedFoodUseCase, times(1)).run(any())
        assertEquals(mockFoodList, (viewModel.listOfSavedFood.value as Result.SuccessFetch).result)
    }
}