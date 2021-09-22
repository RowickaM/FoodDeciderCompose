package pl.gungnir.fooddecider.ui.screens.templates

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.useCase.GetTemplatesUseCase
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FoodTemplatesSharedViewModelTest : BaseTest() {

    private lateinit var viewModel: FoodTemplatesSharedViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getTemplatesUseCase: GetTemplatesUseCase

//    @Mock
//    private lateinit var splitDishesTemplateUseCase: SplitDishesTemplateUseCase
//
//    @Mock
//    private lateinit var setFoodListUseCase: SetFoodListUseCase

//    private val mockTemplates = listOf(
//        Template(
//            id = "id_food_category1",
//            imageUrl = null,
//            categoryFoodName = "name 1",
//            foodCount = 2,
//            foodTags = listOf("tag 1, tag 2"),
//            foodList = listOf("food 1", "food 4")
//        ),
//        Template(
//            id = "id_food_category2",
//            imageUrl = "",
//            categoryFoodName = "name 2",
//            foodCount = 2,
//            foodTags = listOf("tag 2, tag 4"),
//            foodList = listOf("food 2", "food 3")
//        )
//    )

//    private val mockTemplateDetails = TemplateDetails(
//        id = "id_food_category1",
//        imageUrl = null,
//        categoryFoodName = "name 1",
//        foodCount = 2,
//        foodTags = listOf("tag 1, tag 2"),
//        added = listOf(),
//        notAdded = listOf("food 1", "food 4")
//    )
//
//    private val mockTemplateDetailsAfterAdded = TemplateDetails(
//        id = "id_food_category1",
//        imageUrl = null,
//        categoryFoodName = "name 1",
//        foodCount = 2,
//        foodTags = listOf("tag 1, tag 2"),
//        added = listOf("food 4"),
//        notAdded = listOf("food 1")
//    )

    override fun setup() {
        super.setup()

        viewModel = FoodTemplatesSharedViewModel(
            getTemplatesUseCase,
        )
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(
            getTemplatesUseCase,
        )
    }

    @Test
    fun fetchData_emptyResponse() {
        fetchData(emptyList())
    }

//    @Test
//    fun getTemplateById() = testCoroutineRule.runBlockingTest {
//        getTemplateDetails(mockTemplates[0].id)
//    }

    @Test
    fun onRefresh() = testCoroutineRule.runBlockingTest {
        whenever(getTemplatesUseCase.run(None)).thenReturn(emptyList<Template>().right())

        viewModel.onRefresh(0)

        assertEquals(false, viewModel.isRefreshing.value)
        verify(getTemplatesUseCase, times(1)).run(None)
        verifyFetchData(emptyList())
    }

//    @Test
//    fun onRefreshDetails() = testCoroutineRule.runBlockingTest {
//        fetchData(mockTemplates)
//        whenever(splitDishesTemplateUseCase.run(any())).thenReturn(
//            Pair(
//                mockTemplateDetails,
//                mockTemplateDetails.added
//            ).right()
//        )
//        viewModel.getTemplateById(mockTemplates[0].id)
//
//        viewModel.onRefreshDetails(0)
//
//        verify(splitDishesTemplateUseCase, times(2)).run(any())
//        assertEquals(mockTemplateDetails, viewModel.templateDetails.value)
//    }

//    @Test
//    fun onAddButtonClick() = testCoroutineRule.runBlockingTest {
//        fetchData(mockTemplates)
//        getTemplateDetails(mockTemplates[0].id)
//        whenever(setFoodListUseCase.run(anyList())).thenReturn(None.right())
//
//        viewModel.onAddButtonClick(mockTemplates[0].foodList[1])
//
//        verify(setFoodListUseCase, times(1)).run(anyList())
//        assertEquals(mockTemplateDetailsAfterAdded, viewModel.templateDetails.value)
//    }

    private fun fetchData(list: List<Template>) = testCoroutineRule.runBlockingTest {
        whenever(getTemplatesUseCase.run(None)).thenReturn(list.right())

        viewModel.onInitialize()

        verify(getTemplatesUseCase, times(1)).run(None)
        verifyFetchData(list)
    }

    private fun verifyFetchData(list: List<Template>) = testCoroutineRule.runBlockingTest {
        if (list.isEmpty()) {
            assertEquals(true, viewModel.templates.value is Result.Empty)
        } else {
            assertEquals(true, viewModel.templates.value is Result.Success)
            assertEquals(list, (viewModel.templates.value as Result.Success).result)
        }
    }

//    private fun getTemplateDetails(id: String) = testCoroutineRule.runBlockingTest {
//        fetchData(mockTemplates)
//        whenever(splitDishesTemplateUseCase.run(any())).thenReturn(
//            Pair(
//                mockTemplateDetails,
//                mockTemplateDetails.added
//            ).right()
//        )
//
//        viewModel.getTemplateById(id)
//
//        verify(splitDishesTemplateUseCase, times(1)).run(any())
//        assertEquals(mockTemplateDetails, viewModel.templateDetails.value)
//    }
}