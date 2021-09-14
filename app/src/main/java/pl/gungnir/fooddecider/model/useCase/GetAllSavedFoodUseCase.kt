package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.util.*
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

class GetAllSavedFoodUseCase : BaseUseCase<Flow<List<String>>, None>() {

    override suspend fun run(params: None): Either<Failure, Flow<List<String>>> {
        return ServiceDatabaseRepo.getDatabaseRepo().getSavedFood()?.right()
            ?: Failure.UserNotExist.left()
    }
}