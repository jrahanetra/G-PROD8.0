@file:OptIn(ExperimentalMaterial3Api::class)

package mg.geit.jason

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

class MainActivity : ComponentActivity() {
    private var dataManager = DataManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataManager.insertCatProduit("NOURRITURE", R.drawable.vector)
        dataManager.insertCatProduit("FOURNITURE", R.drawable.fourniture)
        dataManager.insertCatProduit("ELECTRONIQUE", R.drawable.electronique)
        dataManager.insertCatProduit("MENAGER", R.drawable.menager)
        dataManager.insertCatProduit("SOIN", R.drawable.ic_launcher_foreground)
        enableEdgeToEdge()
        setContent {
            GPROD80Theme {
                MainScreen1(dataManager, seeListActivity = {
                    val intent = Intent(this, ListProduitActivity::class.java)
                    Log.i("clickCard","CardClické")
                    startActivity(intent)
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen1(dataManager: DataManager, seeListActivity: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            Header(title = "Catégories")
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
        ScrollContent(dataManager, innerPadding, seeListActivity)
    }
}

@Composable
fun Header(title: String){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
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
            content = { TitleSection(
                title = title,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center,
            ) },
        )
    }
}
@Composable
fun TitleSection(title: String, modifier: Modifier = Modifier, textAlign: TextAlign){
    Text(
        text = title,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
fun ScrollContent(dataManager: DataManager, innerPadding: PaddingValues, seeListActivity: () -> Unit) {
    val categories = dataManager.readCategories()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = innerPadding,
        modifier = Modifier.background(color = Color.White)
    ) {
        items(categories){ category ->
            run {
                CategoryCard(category, seeListActivity)
            }
        }
    }
}

@Composable
fun CategoryCard(category: Category, seeListActivity: () -> Unit) {
    val idColor = listOf(R.color.color1, R.color.color2, R.color.color3, R.color.color4)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()
            .fillMaxWidth()
            .clickable { seeListActivity()},
        colors = CardDefaults.cardColors(colorResource(idColor.random())),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()

        ) {
            Icon(painterResource(id = category.image), contentDescription = null, tint = Color.Black, modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(text = category.name)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Preview(){
    GPROD80Theme {
    }
}