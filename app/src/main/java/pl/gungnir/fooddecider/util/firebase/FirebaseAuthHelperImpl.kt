package pl.gungnir.fooddecider.util.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.right

class FirebaseAuthHelperImpl : FirebaseAuthHelper {

    private val auth by lazy { FirebaseAuth.getInstance() }

    @ExperimentalCoroutinesApi
    override fun login(email: String, password: String): Flow<Either<Failure, String>> {
        return channelFlow {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    try {
                        if (!task.isSuccessful) {
                            task.exception?.let { throw it }
                        }

                        if (task.isSuccessful) {
                            task.result?.user?.let {
                                trySendBlocking(it.uid.right())
                            } ?: trySendBlocking(Failure.Unknown.left())
                            close()
                        }
                    } catch (e: FirebaseAuthInvalidUserException) {
                        trySendBlocking(Failure.UserNotExist.left())
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        trySendBlocking(Failure.InvalidCredentials.left())
                    } catch (e: FirebaseAuthException) {
                        trySendBlocking(Failure.FirebaseAuthUnknown.left())
                    }

                }
            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    override fun userIsLogged(): Boolean {
        return auth.currentUser != null
    }

    override fun getUID(): String {
        return auth.currentUser?.uid ?: ""
    }
}