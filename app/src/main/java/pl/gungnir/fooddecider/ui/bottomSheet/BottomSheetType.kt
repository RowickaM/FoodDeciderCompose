package pl.gungnir.fooddecider.ui.bottomSheet

sealed class BottomSheetType {
    object AddElementToList : BottomSheetType()
    class AddList(val onDone: (String) -> Unit) : BottomSheetType()
    class ShowLists(
        val list: List<String>,
        val selected: String,
        val onItemClick: (String) -> Unit,
        val onAddButtonClick: () -> Unit
    ) : BottomSheetType()
}
