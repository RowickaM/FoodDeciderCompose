package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import pl.gungnir.fooddecider.BaseTest
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetTemplateDetailsUseCaseTest : BaseTest() {

    private lateinit var detailsUseCase: GetTemplateDetailsUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    private val templateId = "id_food_category1"

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

        detailsUseCase = GetTemplateDetailsUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun getTemplateDetailsUseCase_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.splitFoodsInTemplates(any())).thenReturn(Failure.UserNotExist.left())

        val result = detailsUseCase.run(templateId)

        assertEquals(Failure.UserNotExist.left(), result)
        verify(databaseRepo, times(1)).splitFoodsInTemplates(templateId)
    }

    @Test
    fun getTemplateDetailsUseCase_None() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.splitFoodsInTemplates(any()))
            .thenReturn(mockTemplateDetails.right())

        val result = detailsUseCase.run(templateId)

        assertEquals(mockTemplateDetails.right(), result)
        verify(databaseRepo, times(1)).splitFoodsInTemplates(templateId)
    }
}