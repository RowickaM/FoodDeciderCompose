package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

class GetTemplateDetailsUseCase(
    private val databaseRepo: DatabaseRepo = ServiceDatabaseRepo.getDatabaseRepo()
) : BaseUseCase<TemplateDetails, String>() {

    override suspend fun run(params: String): Either<Failure, TemplateDetails> {
        return databaseRepo.splitFoodsInTemplates(templateId = params)
    }

}