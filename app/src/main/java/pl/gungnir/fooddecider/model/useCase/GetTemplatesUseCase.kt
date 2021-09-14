package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

class GetTemplatesUseCase : BaseUseCase<List<Template>, None>() {

    override suspend fun run(params: None): Either<Failure, List<Template>> {
        return ServiceDatabaseRepo.getDatabaseRepo().getTemplates()
    }
}