package mg.geit.jason

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import mg.geit.jason.ui.theme.GPROD80Theme

class ProductRegistrationActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val idCategory = intent.getIntExtra("idCategory", 0)
        setContent {
            GPROD80Theme {
                MainViewRegistrationProduct(
                    dataManager = DataManagerSingleton.getInstance(this),
                    idCategory,
                    doModification = {},
                    thisActivity = this,
                    goToPreviousActivity = {
                        val intent = Intent(this, ListProductActivity::class.java)
                            .apply { putExtra("idCategory", idCategory) }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

/**
 * MAINVIEW REGISTRATION PRODUCT
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainViewRegistrationProduct(
    dataManager: DataManager,
    idCategory: Int,
    doModification:(Produit)->Unit,
    thisActivity: Activity,
    goToPreviousActivity: ()->Unit
)
{
    val produit = Produit(null, "", null, null, "","", null)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    //Déclarer les états pour chaque champ de text
    var nameProduct by remember { mutableStateOf(produit.name) }
    var priceProduct by remember { mutableStateOf(produit.prix.toString()) }
    var qttProduct by remember { mutableStateOf(produit.quantite.toString()) }
    var imageUrlProduct by remember { mutableStateOf(produit.imageUrl) }
    var descriptionProduct by remember { mutableStateOf(produit.description) }

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
                //Add new product
                dataManager.insertProduct(
                    nameProduct,
                    priceProduct.toDouble(),
                    qttProduct.toDouble(),
                    descriptionProduct,
                    imageUrlProduct,
                    idCategory
                )
                goToPreviousActivity()
                Toast.makeText(thisActivity,"Produit Ajouté", Toast.LENGTH_LONG).show()
            }
        },
    ) { innerPadding ->
        ContainerFields(
            innerPadding,
            nameProduct,
            onNameChange = { nameProduct = it },
            priceProduct,
            onPrixChange = { priceProduct = it },
            qttProduct,
            onQuantiteChange = {qttProduct = it},
            imageUrlProduct,
            onImageUrlChange = {imageUrlProduct = it},
            descriptionProduct,
            onDescriptionChange = {descriptionProduct = it},
        )
    }
}