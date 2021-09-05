package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.right

@ExperimentalCoroutinesApi
class SendRemindPasswordLinkUseCaseTest : BaseTest() {

    private lateinit var useCase: SendRemindPasswordLinkUseCase

    @Mock
    private lateinit var databaseRepo: DatabaseRepo

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val testCoroutineRule = TestCoroutineRule()

    override fun setup() {
        super.setup()

        useCase = SendRemindPasswordLinkUseCase(databaseRepo)
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(databaseRepo)
    }

    @Test
    fun sendRemindPasswordLink_Failure() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.sendResetPasswordMail(anyString())).thenReturn(Failure.UserNotExist.left())

        val result = useCase.run(MOCK_STRING)

        kotlin.test.assertEquals(Failure.UserNotExist.left(), result)
        verify(databaseRepo, times(1)).sendResetPasswordMail(MOCK_STRING)
    }

    @Test
    fun sendRemindPasswordLink_None() = testCoroutineRule.runBlockingTest {
        whenever(databaseRepo.sendResetPasswordMail(anyString())).thenReturn(None.right())

        val result = useCase.run(MOCK_STRING)

        kotlin.test.assertEquals(None.right(), result)
        verify(databaseRepo, times(1)).sendResetPasswordMail(MOCK_STRING)
    }
}