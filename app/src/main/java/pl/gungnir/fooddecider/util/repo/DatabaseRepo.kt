package pl.gungnir.fooddecider.util.repo

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None

interface DatabaseRepo {

    fun getSavedFood(userId: String): Flow<List<String>>

    suspend fun loginUser(email: String, password: String): Either<Failure, String>
}