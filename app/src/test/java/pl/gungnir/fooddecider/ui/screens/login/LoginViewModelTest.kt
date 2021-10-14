package pl.gungnir.fooddecider.ui.screens.login

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.model.useCase.*
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.config.Config
import pl.gungnir.fooddecider.util.helper.ResourceProvider
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.right
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LoginViewModelTest : BaseTest() {

    private lateinit var viewModel: LoginViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var resourceProvider: ResourceProvider

    @Mock
    private lateinit var loginUseCase: LoginUseCase

    @Mock
    private lateinit var isUserLoggedUseCase: IsUserLoggedUseCase

    @Mock
    private lateinit var sendEmailVerificationUseCase: SendEmailVerificationUseCase

    @Mock
    private lateinit var config: Config

    @Mock
    private lateinit var checkDBVersion: CheckDBVersion

    @Mock
    private lateinit var changeStructureUseCase: ChangeStructureUseCase

    private val emailMock = "email@example.com"

    private val passwordMock = "examplePassword"

    private val onSuccess: () -> Unit = { }

    private val onSuccessString: (String) -> Unit = { }

    private val onFailure: (Boolean, String) -> Unit = { _, _ -> }

    override fun setup() {
        super.setup()

        viewModel = LoginViewModel(
            resourceProvider = resourceProvider,
            config = config,
            loginUseCase = loginUseCase,
            isUserLoggedUseCase = isUserLoggedUseCase,
            sendEmailVerificationUseCase = sendEmailVerificationUseCase,
            checkDBVersion = checkDBVersion,
            changeStructureUseCase = changeStructureUseCase
        )
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(
            resourceProvider,
            config,
            loginUseCase,
            isUserLoggedUseCase,
            sendEmailVerificationUseCase,
            checkDBVersion,
            changeStructureUseCase
        )
    }

    @Test
    fun onInitialize_userNotAuthorized_setUserAuthorization() = testCoroutineRule.runBlockingTest {
        whenever(isUserLoggedUseCase.run(any())).thenReturn(true.right())

        viewModel.onInitialize()

        verify(isUserLoggedUseCase, times(1)).run(any())
    }

    @Test
    fun onInitialize_userAuthorized_nothingHappened() {
        //prepare set authorization
        testCoroutineRule.runBlockingTest {
            viewModel.isUserLogged.value = true

            viewModel.onInitialize()
        }
    }

    @Test
    fun onLoginClick_onFailure_UserNotExist() = testCoroutineRule.runBlockingTest {
        whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
        whenever(loginUseCase.run(any())).thenReturn(Failure.UserNotExist.left())

        viewModel.onLoginClick(
            emailMock,
            passwordMock,
            onSuccess,
            onFailure
        )

        verify(loginUseCase, times(1)).run(any())
        verify(resourceProvider, times(1)).getString(R.string.firebase_user_not_exist)
    }

    @Test
    fun onLoginClick_onFailure_InvalidCredentials() = testCoroutineRule.runBlockingTest {
        whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
        whenever(loginUseCase.run(any())).thenReturn(Failure.InvalidCredentials.left())

        viewModel.onLoginClick(
            emailMock,
            passwordMock,
            onSuccess,
            onFailure
        )

        verify(loginUseCase, times(1)).run(any())
        verify(resourceProvider, times(1)).getString(R.string.firebase_invalid_credentials)
    }

    @Test
    fun onLoginClick_onFailure_UserNotVerify() = testCoroutineRule.runBlockingTest {
        whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
        whenever(loginUseCase.run(any())).thenReturn(Failure.UserNotVerify(MOCK_STRING).left())

        viewModel.onLoginClick(
            emailMock,
            passwordMock,
            onSuccess,
            onFailure
        )

        verify(loginUseCase, times(1)).run(any())
        verify(resourceProvider, times(1)).getString(R.string.firebase_user_not_verify)
    }

    @Test
    fun onLoginClick_onFailure() = testCoroutineRule.runBlockingTest {
        whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
        whenever(loginUseCase.run(any())).thenReturn(Failure.Unknown.left())

        viewModel.onLoginClick(
            emailMock,
            passwordMock,
            onSuccess,
            onFailure
        )

        verify(loginUseCase, times(1)).run(any())
        verify(resourceProvider, times(1)).getString(R.string.firebase_unknown)
    }

    @Test
    fun onSendEmailVerification_onSuccess_logout() = testCoroutineRule.runBlockingTest {
        whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
        whenever(sendEmailVerificationUseCase.run(anyString())).thenReturn(None.right())

        viewModel.onSendEmailVerification(onSuccessString)

        assertEquals(false, viewModel.isUserLogged.value)
        verify(sendEmailVerificationUseCase, times(1)).run(anyString())
        verify(resourceProvider, times(1)).getString(R.string.send_email_verification)
    }
}