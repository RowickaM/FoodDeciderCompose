package pl.gungnir.fooddecider.ui.bottomSheet.showLists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.mics.ButtonWithGradient

@Composable
fun ShowLists(
    list: List<String>,
    selectedList: String,
    onElementClick: (String) -> Unit,
    onAddButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.space_large),
                vertical = dimensionResource(id = R.dimen.space_default)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonWithGradient(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddButtonClick,
            shape = RoundedCornerShape(50.dp),
            gradient = Brush.linearGradient(
                0f to MaterialTheme.colors.primaryVariant,
                0.4f to MaterialTheme.colors.primary,
                0.6f to MaterialTheme.colors.primary,
                1f to MaterialTheme.colors.primaryVariant,
            )
        ) {
            Text(
                text = stringResource(id = R.string.add_new_list).uppercase(),
                color = MaterialTheme.colors.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.space_default)
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
}

@Preview(showBackground = true)
@Composable
fun ShowListsPreview() {
    ShowLists(
        list = listOf("List 1", "List 2"),
        selectedList = "List 1",
        onElementClick = {},
        onAddButtonClick = {}
    )
}