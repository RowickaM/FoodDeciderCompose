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
class TemplatesViewModelTest : BaseTest() {

    private lateinit var viewModel: TemplatesViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getTemplatesUseCase: GetTemplatesUseCase

    override fun setup() {
        super.setup()

        viewModel = TemplatesViewModel(
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

    @Test
    fun onRefresh() = testCoroutineRule.runBlockingTest {
        whenever(getTemplatesUseCase.run(None)).thenReturn(emptyList<Template>().right())

        viewModel.onRefresh(0)

        assertEquals(false, viewModel.isRefreshing.value)
        verify(getTemplatesUseCase, times(1)).run(None)
        verifyFetchData(emptyList())
    }

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
}