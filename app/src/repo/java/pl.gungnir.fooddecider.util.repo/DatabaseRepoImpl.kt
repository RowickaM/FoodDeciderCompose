package pl.gungnir.fooddecider.util.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.util.*
import pl.gungnir.fooddecider.util.firebase.FirebaseAuthHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseHelper

class DatabaseRepoImpl(
    private val firebaseHelper: FirebaseHelper,
    private val firebaseAuthHelper: FirebaseAuthHelper
) : DatabaseRepo {

    override fun getSavedFood(): Flow<List<String>>? {
        val userUUID = firebaseAuthHelper.getUID()
        if (userUUID.isEmpty())
            return null
        return firebaseHelper.getSavedFoodConnection(userUID = userUUID)
    }

    override suspend fun loginUser(email: String, password: String): Either<Failure, String> {
        return firebaseAuthHelper
            .login(email, password)
            .map {
                it.fold(
                    {
                        it.left()
                    },
                    {
                        if (it.isNotBlank()) {
                            it.right()
                        } else {
                            Failure.Unknown.left()
                        }
                    }
                )
            }
            .first()
    }

    override suspend fun sendResetPasswordMail(email: String): Either<Failure, None> {
        return firebaseAuthHelper.resetPasswordLink(email)
            .map {
                it.fold(
                    {
                        it.left()
                    },
                    {
                        None.right()
                    }
                )
            }
            .first()
    }

    override suspend fun signUpUser(
        email: String,
        password: String
    ): Either<Failure, String> {
        return firebaseAuthHelper
            .signUpUser(email, password)
            .first()
    }

    override suspend fun createUseCollection(userUID: String): Either<Failure, None> {
        return firebaseHelper
            .createCollectionForUser(userUID)
            .first()
    }

    override suspend fun sendVerificationEmail(userUID: String): Either<Failure, None> {
        return firebaseAuthHelper
            .sendVerificationEmail()
            .first()
    }

    override suspend fun logoutUser(): Either<Failure, None> {
        return firebaseAuthHelper.logoutUser()
    }

    override fun isUserLogged(): Either<Failure, Boolean> {
        return firebaseAuthHelper.userIsLogged().right()
    }

    override suspend fun getTemplates(): Either<Failure, List<Template>> {
        return firebaseHelper
            .getTemplates()
            .first()
            .right()
    }

    override suspend fun splitFoodsInTemplates(templateId: String): Either<Failure, TemplateDetails> {
        val allAddedFoods = firebaseHelper.getSavedFood().first()
        val template = getTemplateById(templateId) ?: return Failure.Unknown.left()

        val addedFood = arrayListOf<String>()
        val noAddedFood = arrayListOf<String>()

        template.foodList.forEach {
            if (allAddedFoods.contains(it)) {
                addedFood.add(it)
            } else {
                noAddedFood.add(it)
            }
        }

        return TemplateDetails(
            id = template.id,
            imageUrl = template.imageUrl,
            categoryFoodName = template.categoryFoodName,
            foodCount = template.foodCount,
            foodTags = template.foodTags,
            added = addedFood,
            notAdded = noAddedFood
        ).right()
    }

    override suspend fun setNewFoodList(foods: List<String>): Either<Failure, None> {
        return firebaseHelper.setSavedFood(foods).first()
    }

    override suspend fun saveNewFoodToList(item: String): Either<Failure, None> {
        val allSaved = firebaseHelper.getSavedFood().first()
        val newList = ArrayList(allSaved)

        newList.add(item)

        return firebaseHelper.setSavedFood(newList).first()
    }

    private suspend fun getTemplateById(id: String): Template? {
        return firebaseHelper.getTemplatesById(id).first()
    }

    override suspend fun addNewFood(food: String): Either<Failure, None>? {
        val allSavedFoodForList = getSavedFood()?.first()
        allSavedFoodForList ?: return null

        val newList = ArrayList(allSavedFoodForList)
        newList.add(food)

        return setNewFoodList(newList)
    }
}
