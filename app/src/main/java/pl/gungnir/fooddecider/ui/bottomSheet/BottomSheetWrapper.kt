package pl.gungnir.fooddecider.ui.bottomSheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.ui.bottomSheet.addElementToList.ShowLists

@ExperimentalMaterialApi
@Composable
fun BottomSheetWrapper(
    state: ModalBottomSheetState,
    sheetState: BottomSheetType?,
    addElementToList: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetState = state,
        sheetContent = {
            EmptyBottomSheet()

            SheetLayout(
                sheetState,
                addElementToList
            )
        },
    ) {
        content()
    }
}

@ExperimentalMaterialApi
@Composable
private fun SheetLayout(
    type: BottomSheetType?,
    AddElementToList: @Composable () -> Unit
) {
    when (type) {
        BottomSheetType.AddElementToList -> AddElementToList()
        is BottomSheetType.ShowLists -> ShowLists(list = type.list, selectedList = type.selected)
    }
}

@Composable
private fun EmptyBottomSheet() {
    Box(modifier = Modifier.height(2.dp)) { }
}
