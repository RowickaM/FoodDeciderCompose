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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    val height = dimensionResource(id = R.dimen.height_food_template_details)
    Surface(
        modifier = Modifier.height(height),
        elevation = dimensionResource(id = R.dimen.elevation_small)
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
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.onPrimary
                    )

                    LazyRow(
                        modifier = Modifier
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.space_xMedium),
                                vertical = dimensionResource(id = R.dimen.space_default)
                            ),
                    ) {
                        itemsIndexed(template.foodTags) { index, tag ->
                            Tag(tagValue = tag)
                            if (index != template.foodTags.size - 1) {
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_default)))
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
                    style = MaterialTheme.typography.h1,
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
            .padding(
                start = dimensionResource(id = R.dimen.space_large),
                top = dimensionResource(id = R.dimen.space_large),
                end = dimensionResource(id = R.dimen.space_large)
            )
            .verticalScroll(scrollState),
    ) {
        if (template.notAdded.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.templates_to_add_list),
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )
        }
        Column {
            template.notAdded.forEach { food ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.space_default)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$food $food",
                        modifier = Modifier.fillMaxWidth(0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_xlarge)))
        if (template.added.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.templates_added_list),
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )
        }
        Column {
            template.added.forEach { food ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.space_default)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.space_medium)),
                        text = food
                    )
                }
            }
        }
    }
}