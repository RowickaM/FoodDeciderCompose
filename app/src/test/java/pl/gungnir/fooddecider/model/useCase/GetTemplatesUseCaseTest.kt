package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetTemplatesUseCaseTest : BaseTest() {

    private lateinit var useCase: GetTemplatesUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    override fun setup() {
        super.setup()

        useCase = GetTemplatesUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        Mockito.verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun getTemplates_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.getTemplates()).thenReturn(Failure.UserNotExist.left())

        val result = useCase.run(None)

        assertEquals(Failure.UserNotExist.left(), result)
        Mockito.verify(databaseRepo, Mockito.times(1)).getTemplates()
    }

    @Test
    fun getTemplates_ListOfTemplate() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.getTemplates()).thenReturn(emptyList<Template>().right())

        val result = useCase.run(None)

        assertEquals(emptyList<Template>().right(), result)
        Mockito.verify(databaseRepo, Mockito.times(1)).getTemplates()
    }
}