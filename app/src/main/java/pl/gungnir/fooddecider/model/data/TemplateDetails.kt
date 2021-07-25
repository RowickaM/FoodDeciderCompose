package pl.gungnir.fooddecider.model.data

data class TemplateDetails(
    val id: String,
    val categoryFoodName: String,
    val foodCount: Int,
    val foodTags: List<String>,
    val added: List<String>,
    val notAdded: List<String>
)
