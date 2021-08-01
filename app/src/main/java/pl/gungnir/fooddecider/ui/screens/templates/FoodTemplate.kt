package pl.gungnir.fooddecider.ui.screens.templates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.data.NavigationItem
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.ui.mics.*
import pl.gungnir.fooddecider.util.KEY_TEMPLATE_ID

@Composable
fun FoodTemplate(
    navController: NavController
) {
    val viewModel by inject<FoodTemplatesSharedViewModel>(FoodTemplatesSharedViewModel::class.java)
    viewModel.onInitialize()
    val templates = remember { viewModel.templates }
    val isRefreshing = remember { viewModel.isRefreshing }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = { viewModel.onRefresh() }
    ) {
        Column {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_large)))

            when (templates.value) {
                Result.Loading -> Loading()
                Result.Empty -> EmptyInfo(text = stringResource(id = R.string.templates_no_templates))
                is Result.Success -> LazyColumn {
                    (templates.value as? Result.Success)?.result?.let {
                        items(it) { template ->
                            FoodTemplateItem(
                                template = template,
                                onClick = { navigateToDetails(navController, template.id) }
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_large)))
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun FoodTemplateItem(
    template: Template,
    onClick: () -> Unit
) {
    val height = dimensionResource(id = R.dimen.height_food_template_item)
    Surface(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.space_large))
            .height(height)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = dimensionResource(id = R.dimen.elevation_small)
    ) {
        Box {
            ImageBackgroundColumn(
                modifier = Modifier.height(height)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            end = dimensionResource(id = R.dimen.space_large),
                            top = dimensionResource(id = R.dimen.space_default)
                        ),
                    textAlign = TextAlign.End,
                    text = stringResource(id = R.string.count_template, template.foodCount),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onPrimary
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = template.categoryFoodName.uppercase(),
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onPrimary
                )
                LazyRow(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.space_xMedium),
                            vertical = dimensionResource(id = R.dimen.space_default)
                        )
                ) {
                    itemsIndexed(template.foodTags) { index, tag ->
                        Tag(tagValue = tag)
                        if (index != template.foodTags.size - 1) {
                            Spacer(
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.space_default))
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun navigateToDetails(
    navController: NavController,
    id: String
) {
    navController.navigate(
        NavigationItem.FoodTemplateDetails.route.replace(
            "{$KEY_TEMPLATE_ID}",
            id
        )
    )
}