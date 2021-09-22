package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

class SplitDishesTemplateByIdUseCase(
    private val databaseRepo: DatabaseRepo = ServiceDatabaseRepo.getDatabaseRepo(),
) : BaseUseCase<Pair<TemplateDetails, List<String>>, String>() {

    override suspend fun run(params: String): Either<Failure, Pair<TemplateDetails, List<String>>> {
        return databaseRepo.splitFoodsInTemplates(id = params)
    }

}