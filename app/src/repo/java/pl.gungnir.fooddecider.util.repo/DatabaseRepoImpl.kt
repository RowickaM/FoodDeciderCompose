package pl.gungnir.fooddecider.util.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pl.gungnir.fooddecider.model.data.SavedFoodCollection
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.util.*
import pl.gungnir.fooddecider.util.config.Config
import pl.gungnir.fooddecider.util.firebase.FirebaseAuthHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseHelper

class DatabaseRepoImpl(
    private val firebaseHelper: FirebaseHelper,
    private val firebaseAuthHelper: FirebaseAuthHelper,
    private val config: Config
) : DatabaseRepo {

    override suspend fun isUserDatabaseVersionActual(): Either<Failure, Boolean> {
        return firebaseHelper.getActualDatabaseVersion().first()
            .fold(
                { it.left() },
                {
                    (it == config.databaseVersion).right()
                }
            )
    }

    override fun getSavedFood(listName: String): Either<Failure, Flow<SavedFoodCollection>> {
        val userUUID = firebaseAuthHelper.getUID()
        if (userUUID.isEmpty())
            return Failure.Unauthorized.left()
        return firebaseHelper.getSavedFoodConnection(userUID = userUUID, listName = listName)
            .right()
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
        val allAddedFoods = firebaseHelper.getSavedFood(config.listName).first()
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
        return firebaseHelper.setSavedFood(config.listName, foods).first()
    }

    override suspend fun saveNewFoodToList(item: String): Either<Failure, None> {
        val allSaved = firebaseHelper.getSavedFood(config.listName).first()
        val newList = ArrayList(allSaved)

        newList.add(item)

        return firebaseHelper.setSavedFood(config.listName, newList).first()
    }

    override suspend fun changeStructure(): Either<Failure, None> {
        return firebaseHelper.updateStructure(
            firebaseAuthHelper.getUID()
        ).first()
    }

    override suspend fun addNewList(listName: String): Either<Failure, None> {
        val uid = firebaseAuthHelper.getUID()
        return firebaseHelper.getListsName(uid)
            .map {
                (it as? Either.Right)?.b?.let { listsName ->
                    if (!listsName.contains(listName)) {
                        firebaseHelper.addNewList(
                            uid = uid,
                            listName = listName
                        ).first()
                    } else Failure.ListCollision.left()
                } ?: Failure.Unknown.left()
            }
            .first()
    }

    private suspend fun getTemplateById(id: String): Template? {
        return firebaseHelper.getTemplate(id).first()
    }
}
