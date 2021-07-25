package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.util.*
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class GetAllSavedFoodUseCase(
    private val databaseRepo: DatabaseRepo
) : BaseUseCase<Flow<List<String>>, None>() {

    override suspend fun run(params: None): Either<Failure, Flow<List<String>>> {
        return databaseRepo.getSavedFood()?.right() ?: Failure.UserNotExist.left()
    }
}