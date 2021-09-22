package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
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
class SplitDishesTemplateUseCaseTest : BaseTest() {

    private lateinit var useCase: SplitDishesTemplateUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    private val mockTemplate = Template(
        id = "id_food_category1",
        imageUrl = null,
        categoryFoodName = "name 1",
        foodCount = 2,
        foodTags = listOf("tag 1, tag 2"),
        foodList = listOf("food 1", "food 4")
    )

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

        useCase = SplitDishesTemplateUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun splitDishesTemplateUseCase_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.splitFoodsInTemplates(template = any())).thenReturn(Failure.UserNotExist.left())

        val result = useCase.run(mockTemplate)

        assertEquals(Failure.UserNotExist.left(), result)
        verify(databaseRepo, times(1)).splitFoodsInTemplates(mockTemplate)
    }

    @Test
    fun splitDishesTemplateUseCase_None() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.splitFoodsInTemplates(template = any()))
            .thenReturn(Pair(mockTemplateDetails, emptyList<String>()).right())

        val result = useCase.run(mockTemplate)

        assertEquals(Pair(mockTemplateDetails, emptyList<String>()).right(), result)
        verify(databaseRepo, times(1)).splitFoodsInTemplates(mockTemplate)
    }
}