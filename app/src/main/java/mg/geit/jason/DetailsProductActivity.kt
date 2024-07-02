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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import mg.geit.jason.ui.theme.GPROD80Theme

class DetailsProductActivity : ComponentActivity(), SwipeRefreshLayout.OnRefreshListener{
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var dataManager = DataManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        val idProduct = intent.getIntExtra("idProduit",0)
        Log.i("Debug", "$idProduct")
        val product = dataManager.readProduct(idProduct)
        enableEdgeToEdge()

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        val composeView : ComposeView = findViewById(R.id.composeView)

        swipeRefreshLayout.setOnRefreshListener(this)

        composeView.setContent {
            GPROD80Theme {
                Log.i("ProduitData", "$product")
                MainScreen3(
                    product,
                    doModification = {product ->
                        val intent = Intent(this, ModificationsProductActivity::class.java).apply {
                            putExtra("idProduit", product.id)
                        }
                        Log.i("Coucou","Edit CLické")
                        startActivity(intent)
                    }
                )
            }
        }
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "refresh", Toast.LENGTH_LONG).show()
            refreshData()
            swipeRefreshLayout.isRefreshing = false
        }, 500)
    }

    private fun refreshData(){
        val idProduct = intent.getIntExtra("idProduit",0)
        val product = dataManager.readProduct(idProduct)
        setContent{
            GPROD80Theme {
                Log.i("ProduitData", "$product")
                MainScreen3(
                    product,
                    doModification = {product ->
                        val intent = Intent(this, ModificationsProductActivity::class.java).apply {
                            putExtra("idProduit", product.id)
                        }
                        Log.i("Coucou","Edit CLické")
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen3(
    produit: Produit,
    doModification:(Produit)-> Unit
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(title = "Détails", true, produit, doModification)
        },
        floatingActionButton = {
            CustomExtendedFloatingActionButton()
        },
    ) { innerPadding ->
        SeeDetailsProduit(produit, innerPadding)
    }
}

@Composable
fun SeeDetailsProduit(
    produit: Produit,
    innerPadding: PaddingValues
){
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
        }
        DetailsText("Prix", produit.prix.toString()+" Ar")
        DetailsText("Quantité", produit.quantite.toString())
        DetailsText("Description", produit.description)
    }
}

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

@Composable
fun CustomExtendedFloatingActionButton() {
    ExtendedFloatingActionButton(
        onClick = { /* TODO */ },
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