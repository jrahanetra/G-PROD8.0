package mg.geit.jason

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mg.geit.jason.R.drawable

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val dataManager = DataManagerSingleton.getInstance(this)
        super.onCreate(savedInstanceState)
        //VERIFY IF THE BASE IS NOT ALREADY CREATED
        if (dataManager.isCategoryTableEmpty()) {
            insertionData(dataManager)
        }
        enableEdgeToEdge()
        setContent {
            MainLoginView(
                this,
                dataManager,
                goToNextActivity = {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}

/**
 * COMPONENT PRINCIPAL OF THIS ACTIVITY
 * @param activity: Activity, this context
 * @param dataManager: DataManager, the singleton of the dataBase
 * @param goToNextActivity: Lambda function
 */
@Composable
fun MainLoginView(
    activity: Activity,
    dataManager: DataManager,
    goToNextActivity: () -> Unit
)
{
    val image = painterResource(drawable.logbackground)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ContainerFieldsRegisCategorie(
                activity,
                dataManager,
                goToNextActivity
            )
        }
    }
}

/**
 * CONTAINER OF ALL FIELDS IN THIS ACTIVITY
 * @param activity: Activity, this context
 * @param dataManager: DataManager, the singleton of the dataBase
 * @param goToNextActivity: Lambda function
 */
@Composable
fun ContainerFieldsRegisCategorie(
    activity: Activity,
    dataManager: DataManager,
    goToNextActivity: () -> Unit,
)
{
    //Déclarer les états pour chaque champ de text
    var nameUser by remember { mutableStateOf("") }
    var  passwordUser by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .size(width = 350.dp, height = 350.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxHeight(),
            ) {
                item {
                    DisplayFields2(
                        onNameChange = {nameUser = it},
                        onPasswordChange = {passwordUser = it},
                        verify = {
                            if(dataManager.isAdmin(nameUser, passwordUser)){
                                goToNextActivity()
                            }else{
                                Toast.makeText(activity, "Votre identité ne correspond pas", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * CONTAINER OF THE TEXTFIELDS AND BUTTON
 * @param onNameChange: lambda function to listen to the change of the name
 * @param onPasswordChange: lambda function to listen to the change of the password
 * @param verify: lambda function to verify if user or not
 */
@Composable
fun DisplayFields2(
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    verify: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Login",
                color = colorResource(R.color.aqua),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        TextFieldWithIconsEditNameUser("Nom", "", Icons.Default.AccountCircle, onNameChange)
        TextFieldWithIconsPassword("Password", "", Icons.Default.Lock, onPasswordChange)

        Button(
            onClick = { verify()  },
            colors = ButtonDefaults.buttonColors(colorResource(R.color.aqua)),// Couleur de fond personnalis
            shape = FloatingActionButtonDefaults.largeShape,
            modifier = Modifier
                .padding(0.dp, 50.dp, 0.dp, 0.dp)
                .width(300.dp)
                .height(50.dp)
        ) {
            Text(
                text = "LOGIN",
                fontSize = 20.sp
            )
        }
    }
}

/**
 * THE TEXTFIELD WITH ICONS, WHERE THE USER SEND THE PSEUDO
 * @param name : String, the name of one text field
 * @param data : String, the default data of the textField
 * @param onValueChange : Lambda function to listen and update to the change of the data
 */
@Composable
fun TextFieldWithIconsEditNameUser(
    name: String,
    data: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue(data)) }
    OutlinedTextField(
        value = text,
        textStyle = LocalTextStyle.current,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "EditIcon",
                tint = Color.Black
            )
        },
        onValueChange = {
            text = it
            onValueChange(it.text)
        },
        modifier = Modifier.width(320.dp),
        shape = RoundedCornerShape(20.dp), // Définir les coins arrondis
        label = {
            Text(
                text = name,
                fontWeight = FontWeight.Bold
            )
        },
        colors = OutlinedTextFieldDefaults.colors(Color.Black)
    )
}

/**
 * THE TEXTFIELD WITH ICONS, WHERE THE USER SEND HER PASSWORD
 * @param name : String, the name of one text field
 * @param data : String, the default data of the textField
 * @param onValueChange : Lambda function to listen and update to the change of the data
 */
@Composable
fun TextFieldWithIconsPassword(
    name: String,
    data: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue(data)) }
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = text,
        textStyle = LocalTextStyle.current,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "EditIcon",
                tint = Color.Black
            )
        },
        onValueChange = {
            text = it
            onValueChange(it.text)
        },
        modifier = Modifier.width(320.dp),
        shape = RoundedCornerShape(20.dp), // Définir les coins arrondis
        label = {
            Text(
                text = name,
                fontWeight = FontWeight.Bold
            )
        },
        colors = OutlinedTextFieldDefaults.colors(Color.Black),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(
                onClick = { passwordVisible = !passwordVisible },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(imageVector = if (passwordVisible) Icons.Default.Search else Icons.Default.Lock, contentDescription = if (passwordVisible) "Hide password" else "Show Passwor")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ShowPreview() {
}
