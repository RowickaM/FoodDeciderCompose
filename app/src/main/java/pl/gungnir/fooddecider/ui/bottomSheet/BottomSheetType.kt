package pl.gungnir.fooddecider.ui.bottomSheet

sealed class BottomSheetType {
    object AddElementToList : BottomSheetType()
    class ShowLists(val list: List<String>, val selected: String) : BottomSheetType()
}
