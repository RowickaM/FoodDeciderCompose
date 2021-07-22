package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class GetAllSavedFoodUseCase(
    private val databaseRepo: DatabaseRepo
) : BaseFlowUseCase<List<String>, String>() {

    override fun run(params: String): Flow<List<String>> {
        return databaseRepo.getSavedFood(userId = params)
    }
}