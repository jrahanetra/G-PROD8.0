package mg.geit.jason

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import mg.geit.jason.ui.theme.GPROD80Theme

class CategoryRegistrationActivity : ComponentActivity(), SwipeRefreshLayout.OnRefreshListener{
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val dataManager = DataManagerSingleton.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        enableEdgeToEdge()

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        val composeView: ComposeView = findViewById(R.id.composeView)

        swipeRefreshLayout.setOnRefreshListener(this)

        composeView.setContent {
            GPROD80Theme {
                MainScreen5 (
                    dataManager,
                    //here just to satisfy to the parameter
                    doModification = {},
                    this,
                    goToPreviousActivity = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    override fun onRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show()
            refreshData()
            swipeRefreshLayout.isRefreshing = false
        }, 300)
    }
    private fun refreshData()
    {
        setContent {
            GPROD80Theme {
                MainScreen5(
                    dataManager,
                    doModification = {
                        refreshData()
                    },
                    this,
                    goToPreviousActivity = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

/**
 * PRINCIPAL COMPONENT OF THIS ACTIVITY
 * @param dataManager : DataManager, the only instance of the classe dataManager
 * @param doModification
 * @param thisActivity : Activity, the activity
 * @param goToPreviousActivity : To go to the previous activity
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen5(
    dataManager: DataManager,
    doModification:(Produit)->Unit,
    thisActivity : Activity,
    goToPreviousActivity: ()-> Unit
)
{
    val category = Category(null,null,null)
    val produit = Produit(null, "", null, null, "","", null)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    //Déclarer les états pour chaque champ de text
    var nameCategory by remember { mutableStateOf(category.name) }
    var  imageUrl by remember { mutableStateOf(category.imageUrl) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(
                title = "Registration",
                false,
                produit,
                doModification,
                goToPreviousActivity
            )
        },
        floatingActionButton = {
            CustomExtendedFloatingActionButton1("AJOUTER"){
                // Traitez les données des champs de texte ici
                //Ajout de nouvelle catégories
                dataManager.insertCatProduct(nameCategory, imageUrl)
                Toast.makeText(thisActivity,"Catégorie Ajouté",Toast.LENGTH_LONG).show()
                goToPreviousActivity()
            }
        },
    ) { innerPadding ->
        ContainerFieldsRegisCategorie(
            innerPadding,
            onNameCategorieChange = { nameCategory = it },
            onImageChange = { imageUrl = it },
        )
    }
}

/**
 * CONTAINER OF ALL FIELDS IN THIS ACTIVITY
 * @param innerPadding: PaddingValues, The default values of this component
 * @param onNameCategorieChange: To update and listen of the change of the name Of Category
 * @param onImageChange: To update and listen of the change of the imageUrl Of Category
 */
@Composable
fun ContainerFieldsRegisCategorie(
    innerPadding: PaddingValues,
    onNameCategorieChange: (String) -> Unit,
    onImageChange : (String) -> Unit,
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
                item {
                    DisplayFields1(
                        onNameCategorieChange ,
                        onImageChange,
                    )
                }
            }
        }
    }
}

/**
 * CONTAINER OF AN FIELDS
 * @param onNameCategoryChange: To update and listen of the change of the name Of Category
 * @param onImageChange: To update and listen of the change of the imageUrl Of Category
 */
@Composable
fun DisplayFields1(
    onNameCategoryChange: (String) -> Unit,
    onImageChange: (String) -> Unit,
)
{
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
            Text(
                text = "Nouveau Catégorie",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        DisplayTextField("NomCatégorie", "", onNameCategoryChange)
        DisplayTextField("UrlImage", "", onImageChange)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview3(){
    GPROD80Theme {
    }
}