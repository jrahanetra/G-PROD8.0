@file:OptIn(ExperimentalMaterial3Api::class)

package mg.geit.jason

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import mg.geit.jason.ui.theme.GPROD80Theme

class MainActivity : ComponentActivity() {
    private lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        dataManager = DataManagerSingleton.getInstance(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //HERE TO VERIFY IF THE DATAMANAGER CONTAINS DATA
        if (dataManager.isCategoryTableEmpty()) {
            insertionData(dataManager)
        }
        setContent {
            GPROD80Theme {
                MainScreen1(
                    dataManager,
                    seeListActivity =  { categorie ->
                        val intent = Intent(this, ListProductActivity::class.java)
                                        .apply { putExtra("idCategory", categorie.id)}
                        Log.i("clickCard","CardClické: ${categorie.name}")
                        startActivity(intent)
                    },
                    produit = Produit(null,"",null,null,"null","",null),
                    doModification = {Log.i("Debug", "doModification INVOKED")},
                    goToRegistrationCategory = {
                        val intent = Intent(this, CategoryRegistrationActivity::class.java)
                        startActivity(intent)
                    },
                    goToPreviousActivity = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    },
                )
            }
        }
    }
}

/**
 * FUNCTION TO ADD ALL DEFAULTS DATA IN THE DATABASE
 * @param dataManager: DataManager the singleton instance of the database
 */
fun insertionData(dataManager: DataManager)
{
    Log.i("insertions", "Insertion data invoked")
    dataManager.insertCatProduct("NOURRITURE", "https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
    dataManager.insertCatProduct("FOURNITURE", "https://images.pexels.com/photos/159538/school-book-binder-folder-notebook-159538.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
    dataManager.insertCatProduct("ELECTRONIQUE", "https://images.pexels.com/photos/6446709/pexels-photo-6446709.jpeg?auto=compress&cs=tinysrgb&w=600")
    dataManager.insertCatProduct("MENAGER", "https://images.pexels.com/photos/276583/pexels-photo-276583.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
    dataManager.insertCatProduct("SOIN", "https://images.pexels.com/photos/8154399/pexels-photo-8154399.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")

// Produits pour la catégorie "NOURRITURE" (catégorie ID = 1)
    dataManager.insertProduct("Burger", 400.0, 20.0, "C'est un fast food", "https://www.shutterstock.com/shutterstock/photos/2282033179/display_1500/stock-photo-classic-hamburger-stock-photo-isolated-in-white-2282033179.jpg", 1)
    dataManager.insertProduct("Pizza", 500.0, 30.0, "Délicieux et gourmand", "https://www.shutterstock.com/shutterstock/photos/1829205563/display_1500/stock-photo-fresh-homemade-italian-pizza-margherita-with-buffalo-mozzarella-and-basil-1829205563.jpg", 1)
    dataManager.insertProduct("Gâteau", 1000.0, 10.0, "Sucré et délicieux, parfait pour un anniversaire", "https://www.shutterstock.com/shutterstock/photos/2465720135/display_1500/stock-photo-caramel-bundt-cake-in-a-dark-setting-with-florals-2465720135.jpg", 1)
    dataManager.insertProduct("Sandwich", 250.0, 50.0, "Rapide et pratique pour le déjeuner", "https://www.shutterstock.com/shutterstock/photos/2452698355/display_1500/stock-photo-sandwich-one-fresh-big-submarine-sandwich-with-ham-cheese-lettuce-tomatoes-and-microgreens-on-2452698355.jpg", 1)

// Produits pour la catégorie "FOURNITURE" (catégorie ID = 2)
    dataManager.insertProduct("Cahier", 20.0, 500.0, "Solide et durable, idéal pour les étudiants", "https://www.shutterstock.com/shutterstock/photos/2407978697/display_1500/stock-photo-different-notebooks-on-light-pink-background-top-view-space-for-text-2407978697.jpg", 2)
    dataManager.insertProduct("Stylo", 5.0, 1000.0, "Écriture fluide, disponible en plusieurs couleurs", "https://www.shutterstock.com/shutterstock/photos/2477506159/display_1500/stock-photo-a-black-pen-isolated-on-the-white-paper-background-2477506159.jpg", 2)
    dataManager.insertProduct("Sac à dos", 1500.0, 100.0, "Confortable et spacieux pour transporter vos affaires", "https://www.shutterstock.com/shutterstock/photos/2450122393/display_1500/stock-photo-black-backpack-isolated-on-a-white-background-2450122393.jpg", 2)

// Produits pour la catégorie "ELECTRONIQUE" (catégorie ID = 3)
    dataManager.insertProduct("Téléphone", 30000.0, 15.0, "Smartphone dernière génération avec écran OLED", "https://www.shutterstock.com/shutterstock/photos/2456481051/display_1500/stock-photo-mobile-or-smartphone-new-model-photo-isolated-on-transparent-background-clipping-path-2456481051.jpg", 3)
    dataManager.insertProduct("Ordinateur portable", 70000.0, 10.0, "Puissant et léger, parfait pour le travail et les loisirs", "https://www.shutterstock.com/shutterstock/photos/683063848/display_1500/stock-photo-modern-laptop-isolated-on-the-white-background-683063848.jpg", 3)
    dataManager.insertProduct("Casque audio", 2000.0, 50.0, "Son de haute qualité avec réduction de bruit", "https://www.shutterstock.com/shutterstock/photos/2426416757/display_1500/stock-photo-miami-usa-august-headphones-for-sale-at-a-best-buy-display-featuring-various-sony-2426416757.jpg", 3)

// Produits pour la catégorie "MENAGER" (catégorie ID = 4)
    dataManager.insertProduct("Aspirateur", 5000.0, 20.0, "Efficace pour nettoyer tous types de sols", "https://www.shutterstock.com/shutterstock/photos/2452799471/display_1500/stock-photo-a-cordless-vacuum-cleaner-cleans-the-carpet-on-the-floor-cleaning-and-cleaning-close-up-2452799471.jpg", 4)
    dataManager.insertProduct("Machine à laver", 25000.0, 5.0, "Capacité de 7 kg avec plusieurs modes de lavage", "https://www.shutterstock.com/shutterstock/photos/2364564075/display_1500/stock-photo-laundry-room-interior-with-washing-machine-near-gray-grunge-wall-2364564075.jpg", 4)
    dataManager.insertProduct("Réfrigérateur",
        40000.0,
        8.0, "Grand espace de rangement avec congélateur intégré", "https://www.shutterstock.com/shutterstock/photos/2407423627/display_1500/stock-photo-detail-in-kitchen-interior-blue-refrigerator-with-stainless-steel-handles-in-retro-style-near-2407423627.jpg", 4)

// Produits pour la catégorie "SOIN" (catégorie ID = 5)
    dataManager.insertProduct("Shampooing",
        150.0,
        100.0, "Pour des cheveux propres et soyeux", "https://www.shutterstock.com/shutterstock/photos/2459746831/display_1500/stock-photo-rice-shampoo-and-conditioner-organic-rice-water-hair-care-natural-beauty-organic-fermented-2459746831.jpg", 5)
    dataManager.insertProduct("Crème hydratante",
        300.0,
        50.0, "Hydrate et nourrit la peau en profondeur", "https://www.shutterstock.com/shutterstock/photos/2467066395/display_1500/stock-photo-hands-middle-age-woman-hold-an-open-jar-of-white-hand-or-body-cream-next-to-pink-tea-roses-on-white-2467066395.jpg", 5)
    dataManager.insertProduct("Dentifrice",
        50.0,
        200.0, "Protection complète pour des dents saines et blanches", "https://www.shutterstock.com/shutterstock/photos/2473121543/display_1500/stock-photo-pouring-a-whitening-purple-toothpaste-on-a-toothbrush-isolated-on-white-background-texture-of-2473121543.jpg", 5)
}

/**
 * THE PRINCIPAL COMPONENT OF THIS ACTIVITY
 * @param dataManager: DataManager the singleton instance of the database
 * @param seeListActivity: Function lambda to redirect to the ListProductActivity
 * @param produit: Produit, the object Produit
 * @param doModification: Function lambda to redirect to the ModificationsProductActivity
 * @param goToRegistrationCategory: Function lambda to redirect to the CategoryRegistrationActivity
 * @param goToPreviousActivity: Function lambda to return to the previous activity
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen1(
    dataManager: DataManager,
    seeListActivity: (Category) -> Unit,
    produit: Produit,
    doModification: (Produit) -> Unit,
    goToRegistrationCategory: () -> Unit,
    goToPreviousActivity: () -> Unit,
) 
{
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val showDialog = remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(
                title = "Catégories",
                false,
                produit,
                doModification,
                goToPreviousActivity,
                onInfoClick = { showDialog.value = true }
            )
        },
        floatingActionButton = {
            ShowFloatingActionButton(goToRegistrationCategory)
        },
    ) { innerPadding ->
        ScrollContent(dataManager, innerPadding, seeListActivity)
        if (showDialog.value) {
            InfoDialog(
                onDismiss = { showDialog.value = false },
                nbTotalProduit = dataManager.readAllProduct().size,
                prixTotal = dataManager.readAllProduct().sumOf { it.prix!!},
                mostExpensive = dataManager.readAllProduct().maxBy { it.prix!! },
                lessExpensive = dataManager.readAllProduct().minBy { it.prix!! },
            )
        }
    }
}

@Composable
fun InfoDialog(
    onDismiss: () -> Unit,
    nbTotalProduit: Int,
    prixTotal: Double,
    mostExpensive: Produit,
    lessExpensive: Produit
)
{
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Informations")
        },
        text = {
            Column {
                Text(text = "Nombre total de produit $nbTotalProduit")
                Text(text = "\uD83D\uDCB2La valeur de l'inventaire est de $prixTotal Ariary")
                Text(text = "\uD83C\uDFAFLe produit le plus cher est :  ${mostExpensive.name} avec une prix de ${mostExpensive.prix}")
                Text(text = "\uD83C\uDFAFLe produit le moins cher est :  ${lessExpensive.name} avec une prix de ${lessExpensive.prix}")
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("OK")
            }
        }
    )
}

/**
 * THE COMPONENT FLOATINGACTIONBUTTON
 * @param goToRegistrationCategory: Function lambda to redirect to the CategoryRegistrationActivity
 */
@Composable
fun ShowFloatingActionButton(
    goToRegistrationCategory:()->Unit
)
{
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

/**
 * THE HEADER COMPONENT
 * @param title: String, the title of the section
 * @param editable: Boolean, to know if the section is editable or no
 * @param produit: Produit
 * @param doModification: Function lambda to redirect to the ModificationsProductActivity
 * @param goToPreviousActivity: Function lambda to return to the previous activity
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    title: String,
    editable: Boolean,
    produit: Produit,
    doModification:(Produit)-> Unit,
    goToPreviousActivity: ()-> Unit,
    onInfoClick: () -> Unit
)
{
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
                IconButton(onClick = { goToPreviousActivity() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        modifier = Modifier.size(40.dp),
                        tint = Color.Black,
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = {
                IconButton(onClick = { onInfoClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Localized description",
                        tint = Color.Black,
                        modifier = Modifier.size(60.dp)
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

/**
 * THE COMPONENT TITLE OF THE SECTION
 * @param title: String, the title of the section
 * @param modifier: Modifier
 * @param textAlign: TextAlign
 * @param editable: Boolean, to know if the section is editable or no
 * @param produit: Produit
 * @param doModification: Function lambda to redirect to the ModificationsProductActivity
 */
@Composable
fun TitleSection(
    title: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign,
    editable: Boolean,
    produit: Produit,
    doModification: (Produit) -> Unit
)
{
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

/**
 * THE PRINCIPAL COMPONENT OF THE SCAFFOLD
 * @param dataManager: DataManager the singleton instance of the database
 * @param innerPadding: PaddingValues, The default values of this component
 * @param seeListActivity: Function lambda to redirect to the ListProductActivity
 */
@Composable
fun ScrollContent(
    dataManager: DataManager,
    innerPadding: PaddingValues,
    seeListActivity: (Category) -> Unit
)
{
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
        items(categories) { category ->
            run {
                CategoryCard(category, seeListActivity)
            }
        }
    }
}

/**
 * THE CONTAINER CARD CATEGORY PRODUCT
 * @param category: Category, the instance object category
 * @param seeListActivity: Function lambda to redirect to the ListProductActivity
 */
@Composable
fun CategoryCard(
    category: Category,
    seeListActivity: (Category) -> Unit
)
{
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
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = category.imageUrl)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(8.dp)) // Adjust the corner radius as needed
                    .border(1.dp, Color.White, RoundedCornerShape(8.dp)) // Adjust the border width and color as needed
            )
            Text(
                text = category.name.toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
