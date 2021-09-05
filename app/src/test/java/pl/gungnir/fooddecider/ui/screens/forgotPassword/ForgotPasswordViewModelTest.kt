package pl.gungnir.fooddecider.ui.screens.forgotPassword

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.model.useCase.SendRemindPasswordLinkUseCase
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.helper.ResourceProvider
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.right

class ForgotPasswordViewModelTest : BaseTest() {

    private lateinit var viewModel: ForgotPasswordViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var resourceProvider: ResourceProvider

    @Mock
    private lateinit var sendRemindPasswordLinkUseCase: SendRemindPasswordLinkUseCase

    override fun setup() {
        super.setup()

        viewModel = ForgotPasswordViewModel(
            resourceProvider,
            sendRemindPasswordLinkUseCase
        )
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(
            resourceProvider,
            sendRemindPasswordLinkUseCase
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun sendLink_onFailure_UserNotExist() = testCoroutineRule.runBlockingTest {
        whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
        whenever(sendRemindPasswordLinkUseCase.run(anyString())).thenReturn(Failure.UserNotExist.left())

        viewModel.sendLink(MOCK_STRING, {}, {})

        verify(resourceProvider, times(1)).getString(R.string.firebase_user_not_exist)
        verify(sendRemindPasswordLinkUseCase, times(1)).run(anyString())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun sendLink_onFailure_ElseErrors() = testCoroutineRule.runBlockingTest {
        whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
        whenever(sendRemindPasswordLinkUseCase.run(anyString())).thenReturn(Failure.FirebaseAuthUnknown.left())

        viewModel.sendLink(MOCK_STRING, {}, {})

        verify(resourceProvider, times(1)).getString(R.string.firebase_unknown)
        verify(sendRemindPasswordLinkUseCase, times(1)).run(anyString())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun sendLink_onSuccess_nothingHappened() = testCoroutineRule.runBlockingTest {
        whenever(sendRemindPasswordLinkUseCase.run(anyString())).thenReturn(None.right())

        viewModel.sendLink(MOCK_STRING, {}, {})

        verify(sendRemindPasswordLinkUseCase, times(1)).run(anyString())
    }
}