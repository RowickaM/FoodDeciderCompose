package pl.gungnir.fooddecider.ui.mics

import androidx.compose.animation.*
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.data.NavigationItem

@ExperimentalAnimationApi
@Composable
fun BottomBar(
    navigationList: List<BottomBarItem>,
    activeIndex: Int,
    setActiveIndex: (Int) -> Unit,
    onItemCLick: (BottomBarItem) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .shadow(elevation = dimensionResource(id = R.dimen.elevation_default))
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.height_bottom_bar))
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(navigationList) { index, item ->
            BottomBarItem(
                isActive = activeIndex,
                setActiveItem = setActiveIndex,
                index = index,
                item = item,
                onCLick = onItemCLick
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun BottomBarItem(
    isActive: Int,
    item: BottomBarItem,
    index: Int,
    onCLick: (BottomBarItem) -> Unit,
    setActiveItem: (Int) -> Unit,
) {
    AnimatedContent(
        targetState = isActive,
        transitionSpec = {
            fadeIn(animationSpec = tween(400)) with
                    fadeOut(animationSpec = tween(200)) using
                    SizeTransform { initialSize, targetSize ->
                        if (isActive == index) {
                            keyframes {
                                IntSize(targetSize.width, initialSize.height) at 150
                                durationMillis = 1000
                            }
                        } else {
                            keyframes {
                                IntSize(initialSize.width, targetSize.height) at 150
                                durationMillis = 1000
                            }
                        }
                    }
        }
    ) { targetExpanded ->
        if (targetExpanded == index) {
            ExpandedBottomBarItem(
                item = item,
                color = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                onCLick(it)
            }
        } else {
            CollapsedBottomBarItem(
                item = item,
                color = MaterialTheme.colors.onSurface,
            ) {
                setActiveItem(index)
                onCLick(it)
            }
        }
    }
}

@Composable
private fun ExpandedBottomBarItem(
    item: BottomBarItem,
    color: Color,
    backgroundColor: Color,
    onItemCLick: (BottomBarItem) -> Unit
) {
    Row(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.height_bottom_bar_item))
            .nonRippleClickable(onClick = {
                onItemCLick(item)
            })
            .background(
                shape = CircleShape,
                color = backgroundColor.copy(alpha = 0.3f)
            )
            .padding(horizontal = dimensionResource(id = R.dimen.space_large)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.label,
            tint = color,
            modifier = Modifier.size(
                dimensionResource(id = R.dimen.height_bottom_bar_icon_selected)
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_default)))
        Text(
            text = item.label,
            color = color
        )
    }
}

@Composable
private fun CollapsedBottomBarItem(
    item: BottomBarItem,
    color: Color,
    onItemCLick: (BottomBarItem) -> Unit
) {
    Row(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.height_bottom_bar_item))
            .nonRippleClickable(onClick = {
                onItemCLick(item)
            })
            .background(
                shape = CircleShape,
                color = Color.Transparent
            )
            .padding(horizontal = dimensionResource(id = R.dimen.space_large)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.label,
            tint = color,
            modifier = Modifier.size(
                dimensionResource(id = R.dimen.height_bottom_bar_icon)
            )
        )
    }
}

sealed class BottomBarItem(
    val iconRes: Int,
    val label: String,
    val navItem: NavigationItem
) {

    class RandomFood(label: String) : BottomBarItem(
        R.drawable.ic_cubes,
        label,
        NavigationItem.Random
    )

    class RandomFoodList(label: String) : BottomBarItem(
        R.drawable.ic_foods,
        label,
        NavigationItem.RandomList
    )

    class TemplateFood(label: String) : BottomBarItem(
        R.drawable.ic_templates,
        label,
        NavigationItem.FoodTemplates
    )
}