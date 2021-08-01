package pl.gungnir.fooddecider.model.data

sealed class NavigationItem(val route: String) {
    object Login : NavigationItem("login")
    object Registration : NavigationItem("registration")
    object ForgotPassword : NavigationItem("forget_password")
    object Random : NavigationItem("random")
    object RandomList : NavigationItem("random_list")
    object FoodTemplates : NavigationItem("food_templates")
    object FoodTemplateDetails : NavigationItem("food_template_details/{id}")
}