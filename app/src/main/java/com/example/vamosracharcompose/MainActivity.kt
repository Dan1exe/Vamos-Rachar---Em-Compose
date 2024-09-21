package com.example.vamosracharcompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vamosracharcompose.ui.theme.VamosRacharComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VamosRacharComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RachadorRun()
                }
            }
        }
    }
}



@Composable
fun Rachador () {
    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("R$0,00") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Rachador",
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp)
                .padding(top = 26.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "RACHADOR",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold

            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            textAlign = TextAlign.Center

        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = text,
            onValueChange = {
                text = it
                calculateResult(text, text2)?.let { res ->
                    result = "R$${"%.2f".format(res)}"
                } ?: run {
                    result = "Erro"
                }
            },
            label = { Text("Valor total") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)

                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = text2,
            onValueChange = {
                text2 = it
                calculateResult(text, text2)?.let { res ->
                    result = "R$${"%.2f".format(res)}"
                } ?: run {
                    result = "Erro"
                }

            },
            label = { Text("Quantidade de Pessoas") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = result,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,


                ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            textAlign = TextAlign.Center

        )
        Spacer(modifier = Modifier.height(30.dp))

        FloatingActionButton(
            onClick = { shareResult(context, result) },
            modifier = Modifier
                .align(Alignment.Start)
                .padding(70.dp)

        ) {
            Icon(Icons.Filled.Share, contentDescription = "Compartilhar resultado")

        }
    }
}

        fun calculateResult(text: String, text2: String): Double? {
            val n1 = text.toDoubleOrNull()
            val n2 = text2.toDoubleOrNull()
            return if (n1 != null && n2 != null && n2 != 0.0) {
                n1 / n2
            } else {
                null
            }
        }

        fun shareResult(context: android.content.Context, result: String) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Cada um deve pagar: $result")
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(shareIntent, null))
        }


@Preview(showBackground = true)

@Composable
fun RachadorRun(){
    Rachador()
}