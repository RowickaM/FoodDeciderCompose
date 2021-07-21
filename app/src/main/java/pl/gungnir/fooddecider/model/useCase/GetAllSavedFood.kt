package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class GetAllSavedFood(
    private val databaseRepo: DatabaseRepo
) : BaseFlowUseCase<List<String>, None>() {

    override fun run(params: None): Flow<List<String>> {
        TODO("Not yet implemented")
    }
}