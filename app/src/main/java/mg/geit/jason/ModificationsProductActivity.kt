package mg.geit.jason

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import mg.geit.jason.ui.theme.GPROD80Theme

class ModificationsProductActivity : ComponentActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val dataManager = DataManagerSingleton.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        val idProduct = intent.getIntExtra("idProduit", 0)
        val product = dataManager.readProduct(idProduct)
        enableEdgeToEdge()

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        val composeView: ComposeView = findViewById(R.id.composeView)

        swipeRefreshLayout.setOnRefreshListener(this)

        composeView.setContent {
            GPROD80Theme {
                MainScreen4(
                    product,
                    doModification = {refreshData()},
                    dataManager,
                    goToPreviousActivity = {
                        val intent = Intent(this, ListProductActivity::class.java)
                            .apply { putExtra("idCategory", product.idCategory) }
                        startActivity(intent)
                    }
                )
            }
        }
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun onRefresh()
    {
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show()
            refreshData()
            swipeRefreshLayout.isRefreshing = false
        }, 300)
    }

    private fun refreshData()
    {
        val idProduct = intent.getIntExtra("idProduit", 0)
        val product = dataManager.readProduct(idProduct)
        setContent {
            GPROD80Theme {
                MainScreen4(
                    product,
                    doModification = {
                        refreshData()
                    },
                    dataManager,
                    goToPreviousActivity = {
                        val intent = Intent(this, ListProductActivity::class.java)
                            .apply { putExtra("idCategory", product.idCategory) }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

/**
 * THE COMPONENT MAIN OF THIS ACTIVITY
 * @param produit: Produit, the produit to modify
 * @param doModification: Function lambda to do modification
 * @param dataManager: the object dataManager
 * @param goToPreviousActivity: Function lambda to go to the previous activity
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen4(
    produit: Produit,
    doModification:(Produit)->Unit,
    dataManager: DataManager,
    goToPreviousActivity: ()-> Unit
)
{
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val showDialog = remember { mutableStateOf(false) }

    //Déclarer les états pour chaque champ de text
    var name by remember { mutableStateOf(produit.name) }
    var prix by remember { mutableStateOf(produit.prix.toString()) }
    var quantite by remember { mutableStateOf(produit.quantite.toString()) }
    var imageUrl by remember { mutableStateOf(produit.imageUrl) }
    var description by remember { mutableStateOf(produit.description) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(
                title = "Edit",
                false,
                produit,
                doModification,
                goToPreviousActivity,
                onInfoClick = { showDialog.value = true }
            )
        },
        floatingActionButton = {
            CustomExtendedFloatingActionButton1("SOUMETTRE"){
                // Traitements des données de champs
                produit.id?.let { dataManager.updateProduct(it, name, prix.toInt(), quantite.toInt(), imageUrl, description ) }
                // Vous pouvez également mettre à jour les données du produit et appeler doModification
                val modifiedProduit = produit.copy(
                    name = name,
                    prix = prix.toDoubleOrNull() ?: produit.prix,
                    quantite = quantite.toIntOrNull() ?: produit.quantite,
                    imageUrl = imageUrl,
                    description = description
                )
                doModification(modifiedProduit)
                //Retourner un résultat à l'activité précédente
                goToPreviousActivity()
            }
        },
    )
    { innerPadding ->
        ContainerFields(
            innerPadding,
            name = name,
            onNameChange = { name = it },
            prix = prix,
            onPrixChange = { prix = it },
            quantite = quantite,
            onQuantiteChange = { quantite = it },
            description = description,
            imageUrl = imageUrl,
            onImageUrlChange = {imageUrl = it},
            onDescriptionChange = { description = it }
        )
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
 * THE CONTAINER OF ALL FIELDS OF THIS COMPONENT
 * @param innerPadding: PaddingValues, The default values of this component
 * @param name: String, The name of the product
 * @param onNameChange: Function to listen and update to the change of the name
 * @param prix: String, The price of the product
 * @param onPrixChange: Function to listen and update to the change of the price
 * @param quantite: String, The quantity of the product
 * @param onQuantiteChange: Function to listen and update to the change of the quantity
 * @param imageUrl: String, The imageUrl of the product
 * @param onImageUrlChange: Function to listen and update to the change of the imageUrl
 * @param description: String, The description of the product
 * @param onDescriptionChange: Function to listen and update to the change of the description
 */
@Composable
fun ContainerFields(
    innerPadding: PaddingValues,
    name: String,
    onNameChange: (String) -> Unit,
    prix: String,
    onPrixChange: (String) -> Unit,
    quantite: String,
    onQuantiteChange: (String) -> Unit,
    imageUrl: String,
    onImageUrlChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
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
                    DisplayFields(
                        name = name,
                        onNameChange = onNameChange,
                        prix = prix,
                        onPrixChange = onPrixChange,
                        quantite = quantite,
                        onQuantiteChange = onQuantiteChange,
                        imageUrl = imageUrl,
                        onImageUrlChange = onImageUrlChange,
                        description = description,
                        onDescriptionChange = onDescriptionChange
                    )
                }
            }
        }
    }
}

/**
 * ALL FIELDS
 * @param name: String, The name of the product
 * @param onNameChange: Function to listen and update to the change of the name
 * @param prix: String, The price of the product
 * @param onPrixChange: Function to listen and update to the change of the price
 * @param quantite: String, The quantity of the product
 * @param onQuantiteChange: Function to listen and update to the change of the quantity
 * @param imageUrl: String, The imageUrl of the product
 * @param onImageUrlChange: Function to listen and update to the change of the imageUrl
 * @param description: String, The description of the product
 * @param onDescriptionChange: Function to listen and update to the change of the description
 */
@Composable
fun DisplayFields(
    name: String,
    onNameChange: (String) -> Unit,
    prix: String,
    onPrixChange: (String) -> Unit,
    quantite: String,
    onQuantiteChange: (String) -> Unit,
    imageUrl: String,
    onImageUrlChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit

)
{
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        DisplayTextField("NomProduit", name, onNameChange)
        DisplayNumericField("Prix", prix, onPrixChange)
        DisplayNumericField("Quantité", quantite, onQuantiteChange)
        DisplayTextField("ImageUrl", imageUrl, onImageUrlChange)
        DisplayTextField("Description", description, onDescriptionChange)
    }
}

/**
 * ONE CONTAINER TEXT FIELD
 * @param name : String the name of the Fields
 * @param data : String the default data of the textField
 */
@Composable
fun DisplayTextField(
    name: String,
    data: String,
    onValueChange: (String) -> Unit
)
{
    Column(
        modifier = Modifier
            .padding(16.dp, 5.dp, 0.dp, 0.dp)
            .fillMaxSize()
    ){
        TextFieldWithIconsEdit(name, data, onValueChange)
    }
}

/**
 * ONE CONTAINER NUMERIC FIELD
 * @param name : String the name of the Fields
 * @param data : String the default data of the textField
 */
@Composable
fun DisplayNumericField(
    name: String,
    data: String,
    onValueChange: (String) -> Unit
)
{
    Column(
        modifier = Modifier
            .padding(16.dp, 5.dp, 0.dp, 0.dp)
            .fillMaxSize()
    ){
        NumberFieldWithIconsEdit(name, data, onValueChange)
    }
}

/**
 * AN CUSTOMIZIED FLOATING ACTION BUTTON TO SUBMIT THE CHANGE
 * @param title :  String, the title of the floating action button
 * @param onClick : lambda function, the action to do after onclick
 */
@Composable
fun CustomExtendedFloatingActionButton1(
    title: String,
    onClick:()-> Unit
)
{
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        containerColor = colorResource(id = R.color.green), // Couleur de fond personnalisée
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .padding(35.dp, 0.dp, 0.dp, 16.dp) // Ajoute une marge autour du bouton
            .height(56.dp) // Hauteur du bouton
            .fillMaxWidth() // Remplit toute la largeur disponible
    ) {
        Text(
            title,
            fontSize = 20.sp
        ) // Texte du bouton
    }
}

/**
 * THE TEXTFIELD WITH ICONS
 * @param name : String, the name of one text field
 * @param data : String, the default data of the textField
 * @param onValueChange : Lambda function to listen and update to the change of the data
 */
@Composable
fun TextFieldWithIconsEdit(
    name: String,
    data: String,
    onValueChange: (String) -> Unit
)
{
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
            onValueChange(it.text)
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

/**
 *
 * THE NUMERICFIELD WITH ICONS
 * @param name : String, the name of one text field
 * @param data : String, the default data of the textField
 * @param onValueChange : Lambda function to listen and update to the change of the data
 */
@Composable
fun NumberFieldWithIconsEdit(
    name: String,
    data: String,
    onValueChange: (String) -> Unit
)
{
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
            // Allow only numeric values
            val filteredText = it.text.filter { char -> char.isDigit() || char == '.' }
            text = it.copy(text = filteredText)
            onValueChange(filteredText)
        },
        modifier = Modifier.width(320.dp),
        label = {
            Text(
                text = name,
                fontWeight = FontWeight.Bold
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
        ),
        colors = OutlinedTextFieldDefaults.colors(Color.Black),
    )
}

