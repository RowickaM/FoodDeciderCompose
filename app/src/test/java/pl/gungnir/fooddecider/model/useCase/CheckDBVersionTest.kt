package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.right

@ExperimentalCoroutinesApi
class CheckDBVersionTest : BaseTest() {

    private lateinit var useCase: CheckDBVersion

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    private val testCoroutineRule = TestCoroutineRule()

    override fun setup() {
        super.setup()

        useCase = CheckDBVersion(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        Mockito.verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun isUserDatabaseVersionActual_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.isUserDatabaseVersionActual()).thenReturn(Failure.Unknown.left())

        val result = useCase.run(None)

        assertEquals(Failure.Unknown.left(), result)
        Mockito.verify(databaseRepo, Mockito.times(1)).isUserDatabaseVersionActual()
    }

    @Test
    fun isUserDatabaseVersionActual_Boolean() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.isUserDatabaseVersionActual()).thenReturn(true.right())

        val result = useCase.run(None)

        assertEquals(true.right(), result)
        Mockito.verify(databaseRepo, Mockito.times(1)).isUserDatabaseVersionActual()
    }
}