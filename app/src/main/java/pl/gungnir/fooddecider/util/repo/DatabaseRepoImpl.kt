package pl.gungnir.fooddecider.util.repo

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.util.firebase.FirebaseHelper

class DatabaseRepoImpl(
    private val firebaseHelper: FirebaseHelper
) : DatabaseRepo {

    override fun getSavedFood(userId: String): Flow<List<String>> {
        return firebaseHelper.getSavedFoodConnection(userUID = userId)
    }
}