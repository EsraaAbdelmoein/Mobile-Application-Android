package com.codelab.basics
import androidx.compose.foundation.border
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codelab.basics.ui.theme.BasicsCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DBClass(this@MainActivity)

        setContent {
            BasicsCodelabTheme {
                MyApp(
                    modifier = Modifier.fillMaxSize(),
                    names = db.findAll(),
                    DBtest = db
                )
            }
        }
    }
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    names: List<DataModel>,
    DBtest: DBClass
) {
    val windowInfo = rememberWindowInfo()
    var index by remember { mutableIntStateOf(-1) }
    var showMaster = (index == -1)

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            if (showMaster || index !in names.indices) {
                showMaster = false
                ShowPageMaster(
                    names = names,
                    updateIndex = { index = it },
                    DBtest = DBtest
                )
            } else {
                ShowPageDetails(
                    name = names[index],
                    index = index,
                    updateIndex = { index = it }
                )
            }
        } else {
            if (index < 0 && names.isNotEmpty()) index = 0
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.Transparent)
                ) {
                    ShowPageMaster(
                        names = names,
                        updateIndex = { index = it },
                        DBtest = DBtest
                    )
                }
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.Transparent)
                ) {
                    if (index in names.indices) {
                        ShowPageDetails(
                            name = names[index],
                            index = index,
                            updateIndex = { index = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowPageMaster(
    modifier: Modifier = Modifier,
    names: List<DataModel>,
    updateIndex: (index: Int) -> Unit,
    DBtest: DBClass
) {
    val fav = DBtest.getMax()

    Column(modifier = modifier.fillMaxSize()) {
        if (fav != null) {
            Text(
                text = "Favorite Pokémon: ${fav.name} (Accessed ${fav.accessCount} times)",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .background(Color(0xFFFFE082))
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            itemsIndexed(items = names) { pos, item ->
                ShowEachListItem(
                    name = item,
                    pos = pos,
                    updateIndex = updateIndex,
                    DBtest = DBtest
                )
            }
        }
    }
}

private fun cardColorFor(pokemonName: String): Color = when (pokemonName) {
    "Bulbasaur"  -> Color(0xFFA5D6A7) // Green 200
    "Charmander" -> Color(0xFFFFE082) // Amber 300
    "Squirtle"   -> Color(0xFF90CAF9) // Light Blue 300
    "Pikachu"    -> Color(0xFFFFF59D) // Yellow 200
    "Jigglypuff" -> Color(0xFFF8BBD0) // Pink 200
    "Meowth"     -> Color(0xFFCFD8DC) // BlueGrey 200
    else         -> Color(0xFFE0E0E0) // Grey 300
}

@Composable
private fun ShowEachListItem(
    name: DataModel,
    pos: Int,
    updateIndex: (index: Int) -> Unit,
    DBtest: DBClass
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = cardColorFor(name.name)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .border(1.dp, Color.Black, CardDefaults.shape)
    ) {
        CardContent(name, pos, updateIndex, DBtest)
    }
}

@Composable
private fun CardContent(
    name: DataModel,
    pos: Int,
    updateIndex: (index: Int) -> Unit,
    DBtest: DBClass
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                onClick = {
                    updateIndex(pos)
                    DBtest.incAccessCount(name.id)
                    Log.d("CodeLab_DB", "Clicked ${name.name}")
                }
            ) {
                Text(text = "Details $pos")
            }

            Text(
                text = name.name,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            if (expanded) {
                Text(
                    text = name.toString(),
                    color = Color(0xFF1E88E5),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ShowPageDetails(
    name: DataModel,
    updateIndex: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    index: Int
) {
    val windowInfo = rememberWindowInfo()
    Column(
        modifier = modifier.fillMaxWidth(0.5f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(name.toString())

        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            Button(onClick = { updateIndex(-1) }) { Text("Master") }
        }
        Button(onClick = { updateIndex(index + 1) }) { Text("Next") }
        if (index > 0) {
            Button(onClick = { updateIndex(index - 1) }) { Text("Prev") }
        }
    }
}
