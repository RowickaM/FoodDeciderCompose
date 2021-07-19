package pl.gungnir.fooddecider.mics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.NavigationItem
import pl.gungnir.fooddecider.R

@Composable
fun BottomBar(
    currentDestination: String?,
    onItemCLick: (BottomBarItem) -> Unit
) {
    val list = arrayListOf(
        BottomBarItem.RandomFood,
        BottomBarItem.RandomFoodList,
        BottomBarItem.TemplateFood,
    )
    LazyRow(
        modifier = Modifier
            .shadow(elevation = 8.dp)
            .fillMaxWidth()
            .height(60.dp)
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        currentDestination?.let {
            itemsIndexed(list) { _, item ->
                BottomBarItem(
                    item = item,
                    onCLick = onItemCLick
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    item: BottomBarItem,
    onCLick: (BottomBarItem) -> Unit,
) {

    Row(
        modifier = Modifier
            .nonRippleClickable(onClick = {
                onCLick(item)
            })
            .background(
                shape = CircleShape,
                color = MaterialTheme.colors.surface
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.label,
            tint = MaterialTheme.colors.secondaryVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.label,
            color = MaterialTheme.colors.secondaryVariant
        )
    }
}

sealed class BottomBarItem(
    val iconRes: Int,
    val label: String,
    val navItem: NavigationItem
) {

    object RandomFood : BottomBarItem(
        R.drawable.ic_list,
        "Draw",
        NavigationItem.Random
    )

    object RandomFoodList : BottomBarItem(
        R.drawable.ic_list,
        "Foods",
        NavigationItem.RandomList
    )

    object TemplateFood : BottomBarItem(
        R.drawable.ic_list,
        "Templates",
        NavigationItem.FoodTemplates
    )
}