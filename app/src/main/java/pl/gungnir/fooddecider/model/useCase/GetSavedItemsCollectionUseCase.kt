package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.model.data.SavedFoodCollection
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo
import pl.gungnir.fooddecider.util.right

class GetSavedItemsCollectionUseCase(
    private val databaseRepo: DatabaseRepo = ServiceDatabaseRepo.getDatabaseRepo()
) : BaseUseCase<Flow<SavedFoodCollection>, String>() {

    override suspend fun run(params: String): Either<Failure, Flow<SavedFoodCollection>> {
        return databaseRepo.getSavedFood(listName = params)
    }
}