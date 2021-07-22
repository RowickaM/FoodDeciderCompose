package pl.gungnir.fooddecider.util.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn

class FirebaseAuthHelperImpl : FirebaseAuthHelper {

    private val auth by lazy { FirebaseAuth.getInstance() }

    @ExperimentalCoroutinesApi
    override fun login(email: String, password: String): Flow<String> {
        return channelFlow {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        trySendBlocking(it.uid)
                    } ?: close()
                }
                .addOnFailureListener {
                    close(it)
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