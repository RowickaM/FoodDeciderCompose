package pl.gungnir.fooddecider.screens.templates

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.data.Template
import pl.gungnir.fooddecider.mics.Toolbar

@Composable
fun FoodTemplate(
    listTemplates: List<Template>
) {
    val templates = remember { listTemplates }

    Column {

        Toolbar(title = "LIST TEMPLATES")
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(templates) { template ->
                FoodTemplateItem(template = template)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FoodTemplateItem(
    template: Template,
) {
    Surface(
        modifier = Modifier.padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 180.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, top = 8.dp),
                textAlign = TextAlign.End,
                text = "Count: ${template.foodCount}"
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = template.categoryFoodName.uppercase(),
                style = MaterialTheme.typography.h5
            )
            LazyRow(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                itemsIndexed(template.foodTags) { index, tag ->
                    Text(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colors.primary
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        text = tag,
                        textAlign = TextAlign.Center
                    )
                    if (index != template.foodTags.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}