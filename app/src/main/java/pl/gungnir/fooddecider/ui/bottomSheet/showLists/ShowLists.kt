package pl.gungnir.fooddecider.ui.bottomSheet.showLists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import pl.gungnir.fooddecider.R

@Composable
fun ShowLists(
    list: List<String>,
    selectedList: String,
    onElementClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.space_large),
                vertical = dimensionResource(id = R.dimen.space_default)
            ),
    ) {
        items(list) { listName ->
            val backgroundColor = if (listName == selectedList) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                Color.Transparent
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .clickable { onElementClick(listName) }
                    .padding(
                        vertical = dimensionResource(id = R.dimen.space_default),
                        horizontal = dimensionResource(id = R.dimen.space_default)
                    ),
                text = listName,
            )
        }
    }
}