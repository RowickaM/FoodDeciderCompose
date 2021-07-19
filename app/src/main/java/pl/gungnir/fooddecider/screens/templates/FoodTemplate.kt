package pl.gungnir.fooddecider.screens.templates

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.gungnir.fooddecider.data.Template
import pl.gungnir.fooddecider.mics.Toolbar
import pl.gungnir.fooddecider.R
import pl.gungnir.fooddecider.ui.theme.DarkGrey

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
    val height = 180.dp
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(height),
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.background_template_default),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .aspectRatio(2f),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(color = Color(0x80313131)),
                verticalArrangement = Arrangement.SpaceBetween
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
                        Text(
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    shape = MaterialTheme.shapes.medium,
                                    color = MaterialTheme.colors.primary
                                )
                                .background(color = DarkGrey, shape = MaterialTheme.shapes.medium)
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                            text = tag,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onPrimary
                        )
                        if (index != template.foodTags.size - 1) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }
    }
}