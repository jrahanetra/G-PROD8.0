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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mg.geit.jason.ui.theme.GPROD80Theme

class ModificationsProductActivity : ComponentActivity() {
    private val dataManager = DataManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idProduct = intent.getIntExtra("idProduit", 0)
        val product = dataManager.readProduct(idProduct)
        enableEdgeToEdge()
        setContent{
            GPROD80Theme {
                MainScreen4(
                    product,
                    doModification = {}
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen4(produit: Produit, doModification:(Produit)->Unit){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(title = "Edit", false, produit, doModification)
        },
        floatingActionButton = {
            CustomExtendedFloatingActionButton1()
        },
    ) { innerPadding ->
        ContainerFields(produit, innerPadding)
    }
}


@Composable
fun ContainerFields(produit: Produit, innerPadding: PaddingValues){
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
                item { run {DisplayFields(produit) } }
            }
        }
    }
}

@Composable
fun DisplayFields(produit: Produit){
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
                text = produit.name,
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        DisplayField("Name", produit.name )
        DisplayField("Prix", produit.prix.toString())
        DisplayField("Quantité", produit.quantite.toString())
        DisplayField("Description", produit.description)
    }
}

@Composable
fun DisplayField(name: String, data: String){
    Column(
        modifier = Modifier
            .padding(16.dp, 5.dp, 0.dp, 0.dp)
            .fillMaxSize()
    ){
        TextFieldWithIconsEdit(name, data)
    }
}
@Composable
fun CustomExtendedFloatingActionButton1() {
    ExtendedFloatingActionButton(
        onClick = { /* TODO */ },
        containerColor = colorResource(id = R.color.green), // Couleur de fond personnalisée
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .padding(35.dp, 0.dp, 0.dp, 16.dp) // Ajoute une marge autour du bouton
            .height(56.dp) // Hauteur du bouton
            .fillMaxWidth() // Remplit toute la largeur disponible
    ) {
        Text("SOUMETTRE",
            fontSize = 20.sp) // Texte du bouton
    }
}

@Composable
fun TextFieldWithIconsEdit(name: String, data: String) {
    var text by remember { mutableStateOf(TextFieldValue(data)) }
    OutlinedTextField(
        value = text,
        textStyle = LocalTextStyle.current,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "EditIcon",
                tint = Color.Black
            )
                      },
        onValueChange = {
            text = it
        },
        modifier = Modifier.width(320.dp),
        label = { Text(
                        text = name,
                        fontWeight = FontWeight.Bold
                    )
                },
        colors = OutlinedTextFieldDefaults.colors(Color.Black)
    )
}