package pl.gungnir.fooddecider.ui.screens.registration

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import pl.gungnir.fooddecider.BaseTest
import pl.gungnir.fooddecider.MainCoroutineRule
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.TestCoroutineRule
import pl.gungnir.fooddecider.model.useCase.CreateUserCollectionUseCase
import pl.gungnir.fooddecider.model.useCase.LogoutUseCase
import pl.gungnir.fooddecider.model.useCase.SendEmailVerificationUseCase
import pl.gungnir.fooddecider.model.useCase.SignUpUserUseCase
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.helper.ResourceProvider
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.right

@ExperimentalCoroutinesApi
class RegistrationViewModelTest : BaseTest() {

    private lateinit var viewModel: RegistrationViewModel

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var resourceProvider: ResourceProvider

    @Mock
    private lateinit var signUpUserUseCase: SignUpUserUseCase

    @Mock
    private lateinit var createUserCollectionUseCase: CreateUserCollectionUseCase

    @Mock
    private lateinit var logoutUseCase: LogoutUseCase

    @Mock
    private lateinit var sendEmailVerificationUseCase: SendEmailVerificationUseCase

    private val emailMock = "email@example.com"

    private val passwordMock = "examplePassword"

    private val uidMock = "UIDFirebaseMock"

    private val onSuccess: (String) -> Unit = {}

    private val onFailure: (String) -> Unit = {}

    override fun setup() {
        super.setup()

        viewModel = RegistrationViewModel(
            resourceProvider,
            signUpUserUseCase,
            createUserCollectionUseCase,
            logoutUseCase,
            sendEmailVerificationUseCase,
        )
    }

    override fun tearDown() {
        super.tearDown()

        verifyNoMoreInteractions(
            resourceProvider,
            signUpUserUseCase,
            createUserCollectionUseCase,
            logoutUseCase,
            sendEmailVerificationUseCase,
        )
    }

    @Test
    fun onRegistrationClick_onFailureRegistration_UserCollision() =
        testCoroutineRule.runBlockingTest {
            whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
            whenever(signUpUserUseCase.run(any())).thenReturn(Failure.UserCollision.left())

            viewModel.onRegistrationClick(
                emailMock,
                passwordMock,
                onSuccess,
                onFailure
            )

            verify(resourceProvider, times(1)).getString(R.string.firebase_user_collision)
            verify(signUpUserUseCase, times(1)).run(any())

        }

    @Test
    fun onRegistrationClick_onFailureRegistration_FirebaseAuthUnknown() =
        testCoroutineRule.runBlockingTest {
            whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
            whenever(signUpUserUseCase.run(any())).thenReturn(Failure.FirebaseAuthUnknown.left())

            viewModel.onRegistrationClick(
                emailMock,
                passwordMock,
                onSuccess,
                onFailure
            )

            verify(resourceProvider, times(1)).getString(R.string.firebase_unknown)
            verify(signUpUserUseCase, times(1)).run(any())

        }

    @Test
    fun onRegistrationClick_onFailureRegistration_else() =
        testCoroutineRule.runBlockingTest {
            whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
            whenever(signUpUserUseCase.run(any())).thenReturn(Failure.Unknown.left())

            viewModel.onRegistrationClick(
                emailMock,
                passwordMock,
                onSuccess,
                onFailure
            )

            verify(resourceProvider, times(1)).getString(R.string.error_unknown)
            verify(signUpUserUseCase, times(1)).run(any())

        }

    @Test
    fun onRegistrationClick_onFailureCreateUser() =
        testCoroutineRule.runBlockingTest {
            whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
            whenever(signUpUserUseCase.run(any())).thenReturn(uidMock.right())
            whenever(createUserCollectionUseCase.run(anyString())).thenReturn(Failure.Unauthorized.left())

            viewModel.onRegistrationClick(
                emailMock,
                passwordMock,
                onSuccess,
                onFailure
            )

            verify(resourceProvider, times(1)).getString(R.string.error_unknown)
            verify(signUpUserUseCase, times(1)).run(any())
            verify(createUserCollectionUseCase, times(1)).run(uidMock)

        }

    @Test
    fun onRegistrationClick_onFailureSendEmailVerification_nothingHappened() =
        testCoroutineRule.runBlockingTest {
            whenever(signUpUserUseCase.run(any())).thenReturn(uidMock.right())
            whenever(createUserCollectionUseCase.run(anyString())).thenReturn(None.right())
            whenever(sendEmailVerificationUseCase.run(anyString())).thenReturn(Failure.Unknown.left())

            viewModel.onRegistrationClick(
                emailMock,
                passwordMock,
                onSuccess,
                onFailure
            )

            verify(signUpUserUseCase, times(1)).run(any())
            verify(createUserCollectionUseCase, times(1)).run(uidMock)
            verify(sendEmailVerificationUseCase, times(1)).run(uidMock)

        }

    @Test
    fun onRegistrationClick_allSuccess_logoutUser() =
        testCoroutineRule.runBlockingTest {
            whenever(resourceProvider.getString(anyInt())).thenReturn(MOCK_STRING)
            whenever(signUpUserUseCase.run(any())).thenReturn(uidMock.right())
            whenever(createUserCollectionUseCase.run(anyString())).thenReturn(None.right())
            whenever(sendEmailVerificationUseCase.run(anyString())).thenReturn(None.right())
            whenever(logoutUseCase.run(any())).thenReturn(None.right())

            viewModel.onRegistrationClick(
                emailMock,
                passwordMock,
                onSuccess,
                onFailure
            )

            verify(resourceProvider, times(1)).getString(R.string.send_email_verification)
            verify(signUpUserUseCase, times(1)).run(any())
            verify(createUserCollectionUseCase, times(1)).run(uidMock)
            verify(sendEmailVerificationUseCase, times(1)).run(uidMock)
            verify(logoutUseCase, times(1)).run(any())

        }
}