package mg.geit.jason

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import mg.geit.jason.ui.theme.GPROD80Theme

class DetailsProductActivity : ComponentActivity(), SwipeRefreshLayout.OnRefreshListener{
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var dataManager = DataManagerSingleton.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)
        enableEdgeToEdge()

        val idProduct = intent.getIntExtra("idProduit",0)
        val product: Produit = dataManager.readProduct(idProduct)
        val idCategory = product.idCategory
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        val composeView : ComposeView = findViewById(R.id.composeView)

        swipeRefreshLayout.setOnRefreshListener(this)

        composeView.setContent {
            GPROD80Theme {
                Log.i("ProduitData", "$product")
                MainScreen3(
                    dataManager,
                    product,
                    doModification = {product ->
                       doModification(product.id)
                    },
                    goToPreviousActivity = {
                        goToListProductActivity(idCategory)
                    },
                    onSuppressProduct = {
                        dataManager.deleteProduct(product.id)
                        goToListProductActivity(idCategory)
                    }
                )
            }
        }
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun onRefresh()
    {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "refresh", Toast.LENGTH_LONG).show()
            refreshData()
            swipeRefreshLayout.isRefreshing = false
        }, 500)
    }

    private fun refreshData(){

        val idProduct = intent.getIntExtra("idProduit",0)
        val product = dataManager.readProduct(idProduct)
        val idCategory = product.idCategory
        setContent{
            GPROD80Theme {
                Log.i("ProduitData", "$product")
                MainScreen3(
                    dataManager,
                    product,
                    doModification = {product ->
                        doModification(product.id)
                    },
                    goToPreviousActivity = {
                        goToListProductActivity(idCategory)
                    },
                    onSuppressProduct = {
                        dataManager.deleteProduct(product.id)
                        goToListProductActivity(idCategory)
                    }
                )
            }
        }
    }

    /**
     * FUNCTION DO MODIFICATION AND GO TO THE MODIFICATIONSPRODUCTACTIVITY
     * @param idProduct : Product the product to modify
     */
    private fun doModification(idProduct: Int?){
        val intent = Intent(this, ModificationsProductActivity::class.java).apply {
            putExtra("idProduit", idProduct)
        }
        startActivity(intent)
    }

    /**
     * FUNCTION TO GO TO THE LISTPRODUCTACTIVITY
     * @param idCategory : Int, the id of the category
     */
    private fun goToListProductActivity(idCategory : Int?){
        val intent = Intent(this, ListProductActivity::class.java)
            .apply { putExtra("idCategory", idCategory) }
        startActivity(intent)
    }
}

/**
 * COMPONENT PRINCIPAL OF THIS ACTIVITY
 * @param produit : Produit
 * @param doModification : Lambda function to go to the ModificationsActivity
 * @param goToPreviousActivity : Lambda function to go to the previous activity
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen3(
    dataManager: DataManager,
    produit: Produit,
    doModification:(Produit)-> Unit,
    goToPreviousActivity: () -> Unit,
    onSuppressProduct: () -> Unit
)
{
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val showDialog = remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(
                title = "Détails",
                true,
                produit,
                doModification,
                goToPreviousActivity,
                onInfoClick = { showDialog.value = true }
            )
        },
        floatingActionButton = {
            CustomExtendedFloatingActionButton(onSuppressProduct)
        },
    ) { innerPadding ->
        SeeDetailsProduit(produit, innerPadding)
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

/**
 * CONTAINER OF ALL ELEMENTS IN THIS ACTIVITY
 * @param produit : Produit, the product that we see the details
 * @param innerPadding : PaddingValues, the default values of the elements in the container Scaffold
 */
@Composable
fun SeeDetailsProduit(
    produit: Produit,
    innerPadding: PaddingValues
)
{
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            border = BorderStroke(1.dp, Color.Black), // Bordure transparente
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .size(width = 350.dp, height = 734.dp)
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxHeight(),
            ){
                item { run {DisplayDetails(produit) } }
            }
        }
    }
}

/**
 * COMPONENT OF THE SEEDETAILSPRODUITS
 * @param produit : Produit
 */
@Composable
fun DisplayDetails(
    produit: Produit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        Column(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            Text(text = produit.name,
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = produit.imageUrl)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                    }).build()
            )
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(328.dp)
                    .align(Alignment.CenterHorizontally)
            )

        }
        DetailsText("Prix", produit.prix.toString()+" Ar")
        DetailsText("Quantité", produit.quantite.toString())
        DetailsText("Description", produit.description)
    }
}

/**
 * THE DETAILS OF THE PRODUCT
 * @param title : String, the title of an data
 * @param data : String, the data
 */
@Composable
fun DetailsText(
    title: String,
    data: String
){
    Column(
        modifier = Modifier
            .padding(16.dp, 5.dp, 0.dp, 0.dp)
            .fillMaxSize()
    ){
        Text(
            text = "$title : ",
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 20.sp
        )
        Column(
            modifier = Modifier
                .padding(16.dp, 5.dp, 0.dp, 0.dp)
                .fillMaxSize(),
        ){
            Text(
                text = data,
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }
}

/**
 * AN CUSTOMIZIED FLOATING ACTION BUTTON TO SUPPRESS THE PRODUCT
 * @param onSuppressProduct : lambda function, the action to do after onclick
 */
@Composable
fun CustomExtendedFloatingActionButton(
    onSuppressProduct: () -> Unit
)
{
    ExtendedFloatingActionButton(
        onClick = { onSuppressProduct() },
        containerColor = colorResource(id = R.color.color5), // Couleur de fond personnalisée
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .padding(35.dp, 0.dp, 0.dp, 16.dp) // Ajoute une marge autour du bouton
            .height(56.dp) // Hauteur du bouton
            .fillMaxWidth() // Remplit toute la largeur disponible
    ) {
        Text("SUPPRIMER",
            fontSize = 20.sp) // Texte du bouton
    }
}