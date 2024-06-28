@file:OptIn(ExperimentalMaterial3Api::class)

package mg.geit.jason

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mg.geit.jason.ui.theme.GPROD80Theme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GPROD80Theme {
                MainScreen(
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.blue),
                        titleContentColor = Color.White,
                    ),
                    title = {
                        Text(
                            "G-PROD8.0",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    color = colorResource(id = R.color.color1),
                    contentColor = colorResource(id = R.color.white),
                    content = { Greeting(
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center,
                    ) },
               )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = colorResource(id = R.color.color1),// Couleur de fond personnalis
                ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
    ) { innerPadding ->
        ScrollContent(innerPadding)
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, textAlign: TextAlign){
    Text(
        text = "Catégories",
        modifier = modifier,
        textAlign = textAlign
    )
}
@Composable
fun ScrollContent(innerPadding: PaddingValues) {
    val categories = listOf(
        Category("Nourriture", R.drawable.vector),
        Category("Fourniture", R.drawable.fourniture),
        Category("Électronique", R.drawable.electronique),
        Category("Ménager", R.drawable.menager),
        Category("Soin", R.drawable.ic_launcher_foreground),
        Category("Nourriture", R.drawable.vector),
        Category("Fourniture", R.drawable.fourniture),
        Category("Électronique", R.drawable.electronique),
        Category("Ménager", R.drawable.menager),
        Category("Soin", R.drawable.ic_launcher_foreground),
        Category("Nourriture", R.drawable.vector),
        Category("Fourniture", R.drawable.fourniture),
        Category("Électronique", R.drawable.electronique),
        Category("Ménager", R.drawable.menager),
        Category("Soin", R.drawable.ic_launcher_foreground),

    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = innerPadding,
    ) {
        items(categories){ category ->
            run {
                CategoryCard(category, Random.nextInt(1,5))
            }
        }
    }
}

@Composable
fun CategoryCard(category: Category, id : Int) {
    val idColor = listOf(R.color.color1, R.color.color2, R.color.color3, R.color.color4)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(colorResource(idColor.random())),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
                .fillMaxSize()

        ) {
            Icon(painterResource(id = category.iconRes), contentDescription = null, modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(text = category.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GPROD80Theme {
        MainScreen()
    }
}