package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

class SplitDishesTemplateUseCase(
    private val databaseRepo: DatabaseRepo = ServiceDatabaseRepo.getDatabaseRepo()
) : BaseUseCase<Pair<TemplateDetails, List<String>>, Template>() {

    override suspend fun run(params: Template): Either<Failure, Pair<TemplateDetails, List<String>>> {
        return databaseRepo.splitFoodsInTemplates(template = params)
    }

}