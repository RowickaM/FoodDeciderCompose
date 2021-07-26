package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class SetFoodListUseCase(
    private val databaseRepo: DatabaseRepo
) : BaseUseCase<None, List<String>>() {

    override suspend fun run(params: List<String>): Either<Failure, None> {
        return databaseRepo.setNewFoodList(params)
    }
}