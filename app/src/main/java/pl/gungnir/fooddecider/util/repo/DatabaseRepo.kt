package pl.gungnir.fooddecider.util.repo

import kotlinx.coroutines.flow.Flow

interface DatabaseRepo {

    fun getSavedFood(userId: String): Flow<List<String>>
}