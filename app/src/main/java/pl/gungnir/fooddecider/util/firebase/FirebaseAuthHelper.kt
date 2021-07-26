package pl.gungnir.fooddecider.util.firebase

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None

interface FirebaseAuthHelper {

    fun login(email: String, password: String): Flow<Either<Failure, String>>

    fun userIsLogged(): Boolean

    fun getUID(): String

    fun logoutUser(): Either<Failure, None>
}