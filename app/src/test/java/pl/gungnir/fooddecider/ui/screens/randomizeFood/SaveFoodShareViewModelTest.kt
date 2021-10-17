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
import pl.gungnir.fooddecider.model.data.SavedFoodCollection
import pl.gungnir.fooddecider.model.useCase.ChangeStructureUseCase
import pl.gungnir.fooddecider.model.useCase.CheckDBVersion
import pl.gungnir.fooddecider.model.useCase.GetSavedItemsCollectionUseCase
import pl.gungnir.fooddecider.model.useCase.SetFoodListUseCase
import pl.gungnir.fooddecider.util.config.Config
import pl.gungnir.fooddecider.util.right

@ExperimentalCoroutinesApi
class SaveFoodShareViewModelTest : BaseTest() {

    private lateinit var viewModel: SaveFoodShareViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var setFoodListUseCase: SetFoodListUseCase

    @Mock
    private lateinit var checkDBVersion: CheckDBVersion

    @Mock
    private lateinit var getSavedItemsCollectionUseCase: GetSavedItemsCollectionUseCase

    @Mock
    private lateinit var changeStructureUseCase: ChangeStructureUseCase

    @Mock
    private lateinit var config: Config

    private val savedCollection = SavedFoodCollection(
        allListName = listOf(),
        selectedListName = "",
        savedList = listOf(
            "food 1",
            "food 2",
            "food 3"
        )
    )

    override fun setup() {
        super.setup()

        viewModel = SaveFoodShareViewModel(
            setFoodListUseCase = setFoodListUseCase,
            checkDBVersion = checkDBVersion,
            getSavedItemsCollectionUseCase = getSavedItemsCollectionUseCase,
            changeStructureUseCase = changeStructureUseCase,
            config = config
        )
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(
            setFoodListUseCase,
            checkDBVersion,
            getSavedItemsCollectionUseCase,
            changeStructureUseCase,
            config
        )
    }

    @Test
    fun onFoodNameChange() {
        viewModel.onFoodNameChange(MOCK_STRING)

        assertEquals(MOCK_STRING, viewModel.newFood.value)
    }

    private fun addToList() = testCoroutineRule.runBlockingTest {
        whenever(getSavedItemsCollectionUseCase.run(anyString()))
            .thenReturn(flowOf(savedCollection).right())

        viewModel.onInitialize { _, _ -> Unit }

        verify(getSavedItemsCollectionUseCase, times(1)).run(anyString())
        assertEquals(
            savedCollection.savedList,
            (viewModel.listOfSavedFood.value as Result.SuccessFetch).result
        )
    }
}