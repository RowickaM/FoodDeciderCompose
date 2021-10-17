package pl.gungnir.fooddecider.ui.bottomSheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.ui.bottomSheet.addElementToList.AddElementToListBottomSheet
import pl.gungnir.fooddecider.ui.bottomSheet.showLists.ShowLists

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun BottomSheetWrapper(
    state: ModalBottomSheetState,
    sheetState: BottomSheetType?,
    closeSheet: () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetState = state,
        sheetContent = {
            EmptyBottomSheet()

            SheetLayout(
                sheetState,
                closeSheet = closeSheet
            )
        },
    ) {
        content()
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
private fun SheetLayout(
    type: BottomSheetType?,
    closeSheet: () -> Unit,
) {
    when (type) {
        BottomSheetType.AddElementToList -> AddElementToListBottomSheet(closeSheet)
        is BottomSheetType.ShowLists -> ShowLists(
            list = type.list,
            selectedList = type.selected,
            onElementClick = {
                closeSheet()
                type.onItemClick(it)
            },
            onAddButtonClick = {
                //todo
            }
        )
    }
}

@Composable
private fun EmptyBottomSheet() {
    Box(modifier = Modifier.height(2.dp)) { }
}
