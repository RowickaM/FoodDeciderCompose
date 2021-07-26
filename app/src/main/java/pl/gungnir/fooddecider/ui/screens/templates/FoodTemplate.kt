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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.ui.NavigationItem
import pl.gungnir.fooddecider.ui.mics.*

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
            Toolbar(title = "LIST TEMPLATES")
            Spacer(modifier = Modifier.height(16.dp))

            when (templates.value) {
                Result.Loading -> Loading()
                Result.Empty -> EmptyInfo(text = "No templates to show")
                is Result.Success -> LazyColumn {
                    (templates.value as? Result.Success)?.result?.let {
                        items(it) { template ->
                            FoodTemplateItem(
                                template = template,
                                onClick = { navigateToDetails(navController, template.id) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
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
    val height = 180.dp
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(height)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp
    ) {
        Box {
            ImageBackgroundColumn(
                modifier = Modifier.height(height)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp, top = 8.dp),
                    textAlign = TextAlign.End,
                    text = "Count: ${template.foodCount}",
                    color = MaterialTheme.colors.onPrimary
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = template.categoryFoodName.uppercase(),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onPrimary
                )
                LazyRow(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    itemsIndexed(template.foodTags) { index, tag ->
                        Tag(tagValue = tag)
                        if (index != template.foodTags.size - 1) {
                            Spacer(modifier = Modifier.width(8.dp))
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
    navController.navigate(NavigationItem.FoodTemplateDetails.route.replace("{id}", id))
}