package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ChangeStructureUseCaseTest : BaseTest() {

    private lateinit var useCase: ChangeStructureUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    private val testCoroutineRule = TestCoroutineRule()

    override fun setup() {
        super.setup()

        useCase = ChangeStructureUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun changeStructure_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.changeStructure()).thenReturn(Failure.Unknown.left())

        val result = useCase.run(None)

        assertEquals(Failure.Unknown.left(), result)
        verify(databaseRepo, times(1)).changeStructure()
    }

    @Test
    fun changeStructure_None() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.changeStructure()).thenReturn(None.right())

        val result = useCase.run(None)

        assertEquals(None.right(), result)
        verify(databaseRepo, times(1)).changeStructure()
    }
}