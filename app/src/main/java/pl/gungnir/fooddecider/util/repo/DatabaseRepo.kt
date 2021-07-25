package pl.gungnir.fooddecider.util.repo

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure

interface DatabaseRepo {

    fun getSavedFood(): Flow<List<String>>?

    suspend fun loginUser(email: String, password: String): Either<Failure, String>

    fun isUserLogged(): Either<Failure, Boolean>
}