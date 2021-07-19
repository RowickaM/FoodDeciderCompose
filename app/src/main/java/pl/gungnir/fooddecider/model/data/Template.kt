package pl.gungnir.fooddecider.model.data

data class Template(
    val categoryFoodName: String,
    val foodCount: Int,
    val foodTags: List<String>,
    val foodList: List<String>
)
