package pl.gungnir.fooddecider.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.data.NavigationItem
import pl.gungnir.fooddecider.model.useCase.AddNewListUseCase
import pl.gungnir.fooddecider.model.useCase.LogoutUseCase
import pl.gungnir.fooddecider.ui.mics.BottomBarItem
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.config.Config
import pl.gungnir.fooddecider.util.helper.ResourceProvider
import pl.gungnir.fooddecider.util.onFailure
import pl.gungnir.fooddecider.util.onSuccess

class MainViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val config: Config,
    private val addNewListUseCase: AddNewListUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val savedList = mutableListOf<String>()
    val selectedList = mutableStateOf<String>("")

    val title = mutableStateOf("")
    val showToolbar = mutableStateOf(false)
    val showBottomBar = mutableStateOf(false)

    val showFab = mutableStateOf(false)

    val selectedBottomItem = mutableStateOf(1)

    private val _message = mutableStateOf<String?>(null)
    val message get() = _message

    val bottomNavigationList = arrayListOf(
        BottomBarItem.RandomFoodList(resourceProvider.getString(R.string.bottom_nav_list_label)),
        BottomBarItem.RandomFood(resourceProvider.getString(R.string.bottom_nav_draw_label)),
        BottomBarItem.TemplateFood(resourceProvider.getString(R.string.bottom_nav_templates_label)),
    )

    fun logout(onSuccessLogout: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase.run(None)
                .onSuccess {
                    onSuccessLogout.invoke()
                }
        }
    }

    fun setTitle(title: String) {
        this.title.value = title
    }

    fun showToolbar(show: Boolean) {
        this.showToolbar.value = show
    }

    fun showBottomBar(show: Boolean) {
        this.showBottomBar.value = show
    }

    fun showFAB(show: Boolean) {
        this.showFab.value = show
    }

    fun setIndex(navigationItem: NavigationItem): Int {
        val bottomBarItem = bottomNavigationList.find { it.navItem.route == navigationItem.route }
        val index = bottomNavigationList.indexOf(bottomBarItem)

        setSelectedBottomNavItem(index)

        return index
    }

    fun setSelectedBottomNavItem(index: Int) {
        this.selectedBottomItem.value = index
    }

    fun setSavedList(list: List<String>, selectedList: String) {
        this.savedList.clear()
        this.savedList.addAll(list)

        this.selectedList.value = selectedList
    }

    fun changeSelectedList(listName: String) {
        config.listName = listName
        this.selectedList.value = listName
    }

    fun addNewList(listName: String) {
        viewModelScope.launch {
            addNewListUseCase.run(listName)
                .onSuccess {
                    config.listName = listName
                    selectedList.value = listName
                }
                .onFailure {
                    when (it) {
                        Failure.ListCollision -> {
                            _message.value =
                                resourceProvider.getString(R.string.list_name_collision)
                        }
                        else -> {
                            _message.value = resourceProvider.getString(R.string.error_unknown)
                        }
                    }
                }
        }
    }

    fun messageDisplayed() {
        _message.value = null
    }
}