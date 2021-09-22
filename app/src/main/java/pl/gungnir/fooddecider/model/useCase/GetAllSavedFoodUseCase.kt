package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.left
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo
import pl.gungnir.fooddecider.util.right

class GetAllSavedFoodUseCase(
    private val databaseRepo: DatabaseRepo = ServiceDatabaseRepo.getDatabaseRepo()
) : BaseUseCase<Flow<List<String>>, String>() {

    override suspend fun run(params: String): Either<Failure, Flow<List<String>>> {
        return databaseRepo.getSavedFood()?.right() ?: Failure.UserNotExist.left()
    }
}