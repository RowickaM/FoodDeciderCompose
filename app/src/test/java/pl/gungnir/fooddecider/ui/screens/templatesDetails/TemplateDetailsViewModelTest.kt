package pl.gungnir.fooddecider.ui.screens.templatesDetails

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.model.useCase.GetTemplateDetailsUseCase
import pl.gungnir.fooddecider.model.useCase.SaveItemToListUseCase
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TemplateDetailsViewModelTest : BaseTest() {

    @Mock
    lateinit var getTemplateDetailsUseCase: GetTemplateDetailsUseCase

    @Mock
    lateinit var saveItemToListUseCase: SaveItemToListUseCase

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: TemplateDetailsViewModel

    private val mockTemplateDetails = TemplateDetails(
        id = "id_food_category1",
        imageUrl = null,
        categoryFoodName = "name 1",
        foodCount = 2,
        foodTags = listOf("tag 1, tag 2"),
        added = listOf(),
        notAdded = listOf("food 1", "food 4")
    )

    override fun setup() {
        super.setup()

        viewModel = TemplateDetailsViewModel(
            getTemplateDetailsUseCase,
            saveItemToListUseCase
        )
    }

    override fun tearDown() {
        super.tearDown()
        verifyNoMoreInteractions(
            getTemplateDetailsUseCase,
            saveItemToListUseCase
        )
    }

    @Test
    fun getTemplateById() = testCoroutineRule.runBlockingTest {
        getTemplateDetails(mockTemplateDetails.id)
    }

    @Test
    fun onRefreshDetails() = testCoroutineRule.runBlockingTest {
        whenever(getTemplateDetailsUseCase.run(any())).thenReturn(mockTemplateDetails.right())
        viewModel.getTemplateById(mockTemplateDetails.id)

        viewModel.onRefreshDetails(0)

        verify(getTemplateDetailsUseCase, times(2)).run(any())
        assertEquals(mockTemplateDetails, viewModel.templateDetails.value)
    }

    private fun getTemplateDetails(id: String) = testCoroutineRule.runBlockingTest {
        whenever(getTemplateDetailsUseCase.run(any())).thenReturn(mockTemplateDetails.right())

        viewModel.getTemplateById(id)

        verify(getTemplateDetailsUseCase, times(1)).run(any())
        assertEquals(mockTemplateDetails, viewModel.templateDetails.value)
    }
}