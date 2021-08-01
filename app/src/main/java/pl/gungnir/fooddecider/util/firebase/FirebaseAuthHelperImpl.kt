package pl.gungnir.fooddecider.util.firebase

import android.util.Log
import com.google.firebase.auth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import pl.gungnir.fooddecider.util.*

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

    @ExperimentalCoroutinesApi
    override fun resetPasswordLink(email: String): Flow<Either<Failure, None>> {
        return channelFlow {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    try {
                        if (!task.isSuccessful) {
                            task.exception?.let { throw it }
                        }

                        if (task.isSuccessful) {
                            trySendBlocking(None.right())
                            close()
                        }
                    } catch (e: FirebaseAuthInvalidUserException) {
                        trySendBlocking(Failure.UserNotExist.left())
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

    override fun logoutUser(): Either<Failure, None> {
        auth.signOut()
        return None.right()
    }

    @ExperimentalCoroutinesApi
    override fun signUpUser(email: String, password: String): Flow<Either<Failure, String>> {
        return channelFlow {
            auth.createUserWithEmailAndPassword(email, password)
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
                    } catch (e: FirebaseAuthUserCollisionException) {
                        trySendBlocking(Failure.UserCollision.left())
                    } catch (e: FirebaseAuthException) {
                        trySendBlocking(Failure.FirebaseAuthUnknown.left())
                    }
                }
            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    override fun sendVerificationEmail(): Flow<Either<Failure, None>> {
        return channelFlow {
            auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    trySendBlocking(None.right())
                }
            }
            awaitClose()
        }.flowOn(Dispatchers.IO)
    }
}