package mg.geit.jason

import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import mg.geit.jason.ui.theme.GPROD80Theme

class CategoryRegistrationActivity : ComponentActivity() {
    private val dataManager = DataManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GPROD80Theme {
                MainScreen5 (
                    doModification = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen5(doModification:(Produit)->Unit){
    val produit = Produit(null,"",null,null,"")
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var name by remember { mutableStateOf(produit.name) }
    var prix by remember { mutableStateOf(produit.prix.toString()) }
    var quantite by remember { mutableStateOf(produit.quantite.toString()) }
    var description by remember { mutableStateOf(produit.description) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(title = "Registration", false, produit, doModification)
        },
        floatingActionButton = {
            CustomExtendedFloatingActionButton1("AJOUTER"){
                // Traitez les données des champs de texte ici
                println("NomProduit: $name")
                println("Prix: $prix")
                println("Quantité: $quantite")
                println("Description: $description")

                // Vous pouvez également mettre à jour les données du produit et appeler doModification
                val modifiedProduit = produit.copy(
                    name = name,
                    prix = prix.toDoubleOrNull() ?: produit.prix,
                    quantite = quantite.toIntOrNull() ?: produit.quantite,
                    description = description
                )
                doModification(modifiedProduit)
            }
        },
    ) { innerPadding ->
        ContainerFields(
            produit,
            innerPadding,
            true,
            name = name,
            onNameChange = { name = it },
            prix = prix,
            onPrixChange = { prix = it },
            quantite = quantite,
            onQuantiteChange = { quantite = it },
            description = description,
            onDescriptionChange = { description = it })
    }
}
@Preview(showBackground = true)
@Composable
fun Preview3(){
    GPROD80Theme {
        val produit = Produit(null,"",null,null,"")
        MainScreen5(doModification = {})
    }
}