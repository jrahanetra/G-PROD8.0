package mg.geit.jason

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mg.geit.jason.ui.theme.GPROD80Theme

class ListProductActivity : ComponentActivity() {
    private var dataManager = DataManager(this)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idCategory = intent.getStringExtra("IdCategory")
        Log.i("Debug", "$idCategory")
        val id = idCategory?.toInt()
        val products = dataManager.readProduits(id)
        enableEdgeToEdge()
        setContent {
            GPROD80Theme {
                Log.i("ListData", "$products")
                MainScreen2(products)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen2(list: List<Produit>){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(title = "Lists")
        } ,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = colorResource(id = R.color.color1),// Couleur de fond personnalis
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
    ){  innerPadding ->
        ScrollDataProduct(list, innerPadding)
    }
}

@Composable
fun ScrollDataProduct(list: List<Produit>, innerPadding : PaddingValues){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(0.dp)) // Ajoute un espace de 16dp entre la topBar et le TextFieldWithIcons
        TextFieldWithIcons()
        Spacer(modifier = Modifier.height(15.dp)) // Ajoute un espace de 16dp entre la topBar et le TextFieldWithIcons
        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            border = BorderStroke(1.dp, Color.Black), // Bordure transparente
            modifier = Modifier
                .size(width = 350.dp, height = 600.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxHeight(),
            ) {
                items(list) { product ->
                    run {
                        ShowProduct(product)
                    }
                }
                items(list) { product ->
                    run {
                        ShowProduct(product)
                    }
                }
                items(list) { product ->
                    run {
                        ShowProduct(product)
                    }
                }
                items(list) { product ->
                    run {
                        ShowProduct(product)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowProduct(product: Produit){
    var checked by remember { mutableStateOf(true) }
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(0.dp, Color.Transparent), // Bordure transparente
        modifier = Modifier
            .size(width = 600.dp, height = 50.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            verticalAlignment = Alignment.CenterVertically

        )  {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    modifier = Modifier
                        .padding(10.dp, 26.dp, 0.dp, 26.dp)
                        .size(30.dp)
                )
                Text(
                    text = product.name,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.Black,
                    style = typography.titleLarge,
                    fontSize = 20.sp
                )
        }
    }
}

@Composable
fun TextFieldWithIcons() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        value = text,
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "SearchIcon") },
        //trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
        onValueChange = {
            text = it
        },
        modifier = Modifier.width(350.dp),
        label = { Text(text = "NomProduit") },
        placeholder = { Text(text = "Entrer le nom du produit") },
    )
}
@Preview (showBackground = true)
@Composable
fun Preview2() {
    GPROD80Theme {
        ShowProduct(product = Produit("mon produit", 100, 200, "Rien à montrer pour l'instant"))
    }
}
