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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent.inject
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.ui.mics.ImageBackgroundColumn
import pl.gungnir.fooddecider.ui.mics.Tag
import pl.gungnir.fooddecider.ui.screens.templates.FoodTemplatesSharedViewModel

@Composable
fun FoodTemplateDetails(
    templateId: String
) {
    val viewModel by inject<FoodTemplatesSharedViewModel>(FoodTemplatesSharedViewModel::class.java)

    val template = viewModel.getTemplateById(templateId)

    Column {
        HeaderFoodTemplateDetails(template = template)
        template?.let { FoodTemplateDetailsLists(template = template) }
    }
}

@Composable
private fun HeaderFoodTemplateDetails(
    template: Template?
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
                    text = "Template no exist",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Composable
private fun FoodTemplateDetailsLists(
    template: Template,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .verticalScroll(scrollState),
    ) {
        Text(text = "Food to add:", style = MaterialTheme.typography.h6)
        Column {
            template.foodList.forEach { food ->
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
                            contentDescription = "Add food $food"
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Food already added:", style = MaterialTheme.typography.h6)
        Column {
            template.foodList.forEach { food ->
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