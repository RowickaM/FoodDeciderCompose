package pl.gungnir.fooddecider.ui.screens.templatesDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.ui.mics.ImageBackgroundColumn
import pl.gungnir.fooddecider.ui.mics.Tag
import pl.gungnir.fooddecider.ui.screens.templates.FoodTemplatesSharedViewModel

@Composable
fun FoodTemplateDetails(
    templateId: String
) {
    val viewModel by inject<FoodTemplatesSharedViewModel>(FoodTemplatesSharedViewModel::class.java)
    viewModel.getTemplateById(templateId)
    val template = remember { viewModel.templateDetails }
    val isRefreshing = remember { viewModel.isRefreshing }

    template.value?.let {
        Column {
            HeaderFoodTemplateDetails(template = template.value)
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
                onRefresh = { viewModel.onRefreshDetails() }
            ) {
                FoodTemplateDetailsLists(template = it)
            }
        }
    } ?: HeaderFoodTemplateDetails(template = null)
}

@Composable
private fun HeaderFoodTemplateDetails(
    template: TemplateDetails?
) {
    val height = 250.dp
    Surface(
        modifier = Modifier.height(height),
        elevation = 4.dp
    ) {
        Box {
            template?.let {
                ImageBackgroundColumn(
                    modifier = Modifier.height(height),
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = template.categoryFoodName.uppercase(),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onPrimary
                    )

                    LazyRow(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    ) {
                        itemsIndexed(template.foodTags) { index, tag ->
                            Tag(tagValue = tag)
                            if (index != template.foodTags.size - 1) {
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }
            } ?: ImageBackgroundColumn(
                modifier = Modifier.height(height),
                verticalAlignment = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.templates_template_no_exist),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Composable
private fun FoodTemplateDetailsLists(
    template: TemplateDetails,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .verticalScroll(scrollState),
    ) {
        if (template.notAdded.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.templates_to_add_list),
                style = MaterialTheme.typography.h6
            )
        }
        Column {
            template.notAdded.forEach { food ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = food)
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(
                                id = R.string.templates_add_food_template,
                                food
                            ),
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (template.added.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.templates_added_list),
                style = MaterialTheme.typography.h6
            )
        }
        Column {
            template.added.forEach { food ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp),
                        text = food
                    )
                }
            }
        }
    }
}