package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class SplitDishesTemplateUseCase(
    private val databaseRepo: DatabaseRepo
) : BaseUseCase<TemplateDetails, Template>() {

    override suspend fun run(params: Template): Either<Failure, TemplateDetails> {
        return databaseRepo.splitFoodsInTemplates(template = params)
    }

}