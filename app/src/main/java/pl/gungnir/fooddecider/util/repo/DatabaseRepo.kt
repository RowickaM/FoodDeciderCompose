package pl.gungnir.fooddecider.util.repo

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None

interface DatabaseRepo {

    suspend fun isUserDatabaseVersionActual(): Either<Failure, Boolean>

    suspend fun changeStructure(): Either<Failure, None>

    fun getSavedFood(): Flow<List<String>>?

    suspend fun loginUser(email: String, password: String): Either<Failure, String>

    fun isUserLogged(): Either<Failure, Boolean>

    suspend fun getTemplates(): Either<Failure, List<Template>>

    suspend fun splitFoodsInTemplates(templateId: String): Either<Failure, TemplateDetails>

    suspend fun setNewFoodList(foods: List<String>): Either<Failure, None>

    suspend fun saveNewFoodToList(item: String): Either<Failure, None>

    suspend fun logoutUser(): Either<Failure, None>

    suspend fun sendResetPasswordMail(email: String): Either<Failure, None>

    suspend fun signUpUser(email: String, password: String): Either<Failure, String>

    suspend fun createUseCollection(userUID: String): Either<Failure, None>

    suspend fun sendVerificationEmail(userUID: String): Either<Failure, None>
}