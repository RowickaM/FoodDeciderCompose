package pl.gungnir.fooddecider.util.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.firebase.FirebaseAuthHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseHelper
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.right

class DatabaseRepoImpl(
    private val firebaseHelper: FirebaseHelper,
    private val firebaseAuthHelper: FirebaseAuthHelper
) : DatabaseRepo {

    override fun getSavedFood(): Flow<List<String>>? {
        val userUUID = firebaseAuthHelper.getUID()
        if (userUUID.isEmpty())
            return null
        return firebaseHelper.getSavedFoodConnection(userUID = userUUID)
    }

    override suspend fun loginUser(email: String, password: String): Either<Failure, String> {
        return firebaseAuthHelper
            .login(email, password)
            .map {
                it.fold(
                    {
                        it.left()
                    },
                    {
                        if (it.isNotBlank()) {
                            it.right()
                        } else {
                            Failure.Unknown.left()
                        }
                    }
                )
            }
            .first()
    }

    override fun isUserLogged(): Either<Failure, Boolean> {
        return firebaseAuthHelper.userIsLogged().right()
    }
}