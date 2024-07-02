package mg.geit.jason

import android.os.Bundle
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mg.geit.jason.ui.theme.GPROD80Theme

class CategoryRegistrationActivity : ComponentActivity() {
    private val dataManager = DataManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GPROD80Theme {
                MainScreen5 (
                    dataManager,
                    doModification = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen5(
    dataManager: DataManager,
    doModification:(Produit)->Unit
){
    val category = Category(null,null,null)
    val produit = Produit(null, "", null, null, "")
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    //Déclarer les états pour chaque champ de text
    var name by remember { mutableStateOf(produit.name) }
    var prix by remember { mutableStateOf(produit.prix.toString()) }
    var quantite by remember { mutableStateOf(produit.quantite.toString()) }
    var description by remember { mutableStateOf(produit.description) }

    var nameCategory by remember { mutableStateOf(category.name) }
    var  image by remember { mutableStateOf(category.image) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(title = "Registration", false, produit, doModification)
        },
        floatingActionButton = {
            CustomExtendedFloatingActionButton1("AJOUTER"){
                // Traitez les données des champs de texte ici
                //Ajout de nouvelle catégories


            }
        },
    ) { innerPadding ->
        ContainerFieldsRegisCategorie(
            innerPadding,
            onNameCategorieChange = { name = it },
            onImageChange = { prix = it },
        )
    }
}

@Composable
fun ContainerFieldsRegisCategorie(
    innerPadding: PaddingValues,
    onNameCategorieChange: (String) -> Unit,
    onImageChange : (String) -> Unit,
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

@Composable
fun DisplayFields1(
    onNameCategorieChange: (String) -> Unit,
    onImageChange: (String) -> Unit,
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
            Text(
                text = "Nouveau Catégorie",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        DisplayField("NomCatégorie", "", onNameCategorieChange)
        DisplayField("IconCatégorie", "", onImageChange)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview3(){
    GPROD80Theme {
        val produit = Produit(null,"",null,null,"")
    }
}