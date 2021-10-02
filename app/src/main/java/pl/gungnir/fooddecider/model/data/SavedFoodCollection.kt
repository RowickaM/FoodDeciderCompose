package pl.gungnir.fooddecider.model.data

data class SavedFoodCollection(
    val allListName: List<String>,
    val selectedList: String,
    val savedList: List<String>,
)
