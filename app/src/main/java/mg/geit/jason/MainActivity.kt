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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.unit.dp
import mg.geit.jason.ui.theme.GPROD80Theme

class MainActivity : ComponentActivity() {
    private var dataManager = DataManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        insertionData(dataManager);
        setContent {
            GPROD80Theme {
                MainScreen1(
                    dataManager,
                    seeListActivity =  { categorie ->
                        val intent = Intent(this, ListProductActivity::class.java)
                                        .apply { putExtra("IdCategory", categorie.id)}
                        Log.i("clickCard","CardClické: ${categorie.name}")
                        startActivity(intent)
                    },
                    produit = Produit(null,"",null,null,"null"),
                    doModification = {Log.i("Debug", "doModification INVOKED")},
                    goToRegistrationCategory = {
                        val intent = Intent(this, CategoryRegistrationActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

fun insertionData(dataManager: DataManager)
{
    Log.i("insertions", "Insertion data invoked")
    dataManager.insertCatProduct("NOURRITURE", R.drawable.vector)
    dataManager.insertCatProduct("FOURNITURE", R.drawable.fourniture)
    dataManager.insertCatProduct("ELECTRONIQUE", R.drawable.electronique)
    dataManager.insertCatProduct("MENAGER", R.drawable.menager)
    dataManager.insertCatProduct("SOIN", R.drawable.ic_launcher_foreground)
    // Produits pour la catégorie "NOURRITURE" (catégorie ID = 1)
    dataManager.insertProduct("Burger", 400, 20, "C est un fast food", 1)
    dataManager.insertProduct("Pizza", 500, 30, "Délicieux et gourmand", 1)
    dataManager.insertProduct("Gâteau", 1000, 10, "Sucré et délicieux, parfait pour un anniversaire", 1)
    dataManager.insertProduct("Sandwich", 250, 50, "Rapide et pratique pour le déjeuner", 1)

    // Produits pour la catégorie "FOURNITURE" (catégorie ID = 2)
    dataManager.insertProduct("Cahier", 20, 500, "Solide et durable, idéal pour les étudiants", 2)
    dataManager.insertProduct("Stylo", 5, 1000, "Écriture fluide, disponible en plusieurs couleurs", 2)
    dataManager.insertProduct("Sac à dos", 1500, 100, "Confortable et spacieux pour transporter vos affaires", 2)

    // Produits pour la catégorie "ELECTRONIQUE" (catégorie ID = 3)
    dataManager.insertProduct("Téléphone", 30000, 15, "Smartphone dernière génération avec écran OLED", 3)
    dataManager.insertProduct("Ordinateur portable", 70000, 10, "Puissant et léger, parfait pour le travail et les loisirs", 3)
    dataManager.insertProduct("Casque audio", 2000, 50, "Son de haute qualité avec réduction de bruit", 3)

    // Produits pour la catégorie "MENAGER" (catégorie ID = 4)
    dataManager.insertProduct("Aspirateur", 5000, 20, "Efficace pour nettoyer tous types de sols", 4)
    dataManager.insertProduct("Machine à laver", 25000, 5, "Capacité de 7 kg avec plusieurs modes de lavage", 4)
    dataManager.insertProduct("Réfrigérateur", 40000, 8, "Grand espace de rangement avec congélateur intégré", 4)

    // Produits pour la catégorie "SOIN" (catégorie ID = 5)
    dataManager.insertProduct("Shampooing", 150, 100, "Pour des cheveux propres et soyeux", 5)
    dataManager.insertProduct("Crème hydratante", 300, 50, "Hydrate et nourrit la peau en profondeur", 5)
    dataManager.insertProduct("Dentifrice", 50, 200, "Protection complète pour des dents saines et blanches", 5)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen1(
    dataManager: DataManager,
    seeListActivity: (Category) -> Unit,
    produit: Produit,
    doModification: (Produit) -> Unit,
    goToRegistrationCategory: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(
                title = "Catégories",
                false,
                produit,
                doModification
            )
        },
        floatingActionButton = {
            ShowFloatingActionButton(goToRegistrationCategory)
        },
    ) { innerPadding ->
        ScrollContent(dataManager, innerPadding, seeListActivity)
    }
}

@Composable
fun ShowFloatingActionButton(
    goToRegistrationCategory:()->Unit
){
    FloatingActionButton(
        onClick = { goToRegistrationCategory() },
        containerColor = colorResource(id = R.color.color1),// Couleur de fond personnalis
        shape = FloatingActionButtonDefaults.largeShape,
        modifier = Modifier.padding(0.dp,0.dp,8.dp,0.dp)
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Ajouter",
            modifier = Modifier.size(50.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    title: String,
    editable: Boolean,
    produit: Produit,
    doModification:(Produit)->Unit
){
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
            content = {
                TitleSection(
                    title = title,
                    modifier = Modifier
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                    editable,
                    produit,
                    doModification
                )
            }
        )
    }
}

@Composable
fun TitleSection(
    title: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign,
    editable: Boolean,
    produit: Produit,
    doModification: (Produit) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Row {
            Text(
                text = title,
                modifier = modifier
                    .width(320.dp)
                    .height(25.dp),
                textAlign = textAlign
            )
            if (editable){
                IconButton(
                    onClick = { doModification(produit)},
                    modifier = Modifier.height(30.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editer Produit",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ScrollContent(
    dataManager: DataManager,
    innerPadding: PaddingValues,
    seeListActivity: (Category) -> Unit
) {
    val categories = dataManager.readCategory()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = innerPadding,
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxHeight()
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        items(categories){ category ->
            run {
                CategoryCard(category, seeListActivity)
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    seeListActivity: (Category) -> Unit
) {
    val idColor = listOf(R.color.color1, R.color.color2, R.color.color3, R.color.color4)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()
            .fillMaxWidth()
            .clickable { seeListActivity(category) },
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
            Text(text = category.name.toString())
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun Preview(){
//    GPROD80Theme {
//    }
//}