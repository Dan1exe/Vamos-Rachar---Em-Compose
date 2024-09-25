package com.example.vamosracharcompose

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale


class MainActivity : ComponentActivity() {
    var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("pt", "BR"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    println("Idioma não suportado ou faltando dados")
                } else {
                    println("TextToSpeech inicializado com sucesso!")
                }
            } else {
                println("Falha na inicialização do TextToSpeech!")
            }
        }
        setContent {
            MyAppTheme {
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
fun Rachador() {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        RachadorLandscape()
    } else {
        RachadorPortrait()
    }
}


@Composable
fun RachadorPortrait() {
    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("R$0,00") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .verticalScroll(scrollState),
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
            label = { Text("Número de Pessoas") },
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

        Row {


            FloatingActionButton(
                onClick = { shareResult(context, result) },
                modifier = Modifier
                    .padding(70.dp),
                containerColor = MaterialTheme.colorScheme.primary,

            ) {
                Icon(Icons.Filled.Share, contentDescription = "Compartilhar resultado")

            }
            FloatingActionButton(
                onClick = {
                    val mainActivity = context as? MainActivity
                    mainActivity?.tts?.let { tts ->
                        if (tts.isLanguageAvailable(Locale("pt", "BR")) >= TextToSpeech.LANG_AVAILABLE) {
                            if (result.isNotEmpty()) {
                                println("Falando o resultado: $result")
                                tts.speak(result, TextToSpeech.QUEUE_FLUSH, null, null)
                            } else {
                                println("Resultado vazio ou não definido")
                            }
                        } else {
                            println("Língua não disponível ou TTS não inicializado corretamente")
                        }
                    } ?: println("TextToSpeech não está inicializado")
                },
                modifier = Modifier
                    .padding(70.dp),
                    containerColor = MaterialTheme.colorScheme.primary,

            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Falar resultado")

            }
        }
    }
}
@Composable
fun RachadorLandscape() {
    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("R$0,00") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Coloque o conteúdo da coluna em modo paisagem aqui

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Rachador",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(top = 10.dp),
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
                    .padding(bottom = 25.dp),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {

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
                    modifier = Modifier
                        .height(30.dp)
                        .width(200.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Right)

                        }
                    )
                )
                Spacer(modifier = Modifier.width(50.dp))
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
                    label = { Text("Número de Pessoas") },
                    modifier = Modifier
                        .height(30.dp)
                        .width(200.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )
            }


            Text(
                text = result,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 40.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                FloatingActionButton(
                    onClick = { shareResult(context, result) },
                    modifier = Modifier
                        .padding(0.dp)
                ) {
                    Icon(Icons.Filled.Share, contentDescription = "Compartilhar resultado")
                }
                Spacer(modifier = Modifier.width(300.dp))
                FloatingActionButton(
                    onClick = {
                        val mainActivity = context as? MainActivity
                        mainActivity?.tts?.let { tts ->
                            if (tts.isLanguageAvailable(Locale("pt", "BR")) >= TextToSpeech.LANG_AVAILABLE) {
                                if (result.isNotEmpty()) {
                                    println("Falando o resultado: $result")
                                    tts.speak(result, TextToSpeech.QUEUE_FLUSH, null, null)
                                } else {
                                    println("Resultado vazio ou não definido")
                                }
                            } else {
                                println("Língua não disponível ou TTS não inicializado corretamente")
                            }
                        } ?: println("TextToSpeech não está inicializado")
                    },
                    modifier = Modifier.padding(0.dp),
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Falar resultado")
                }
            }
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


@Preview(
    showBackground = true,
    /*widthDp = 720, // Largura simulada em dp para landscape
    heightDp = 360, // Altura simulada em dp para landscape
    name = "Landscape Preview"*/
)

@Composable
fun RachadorRun(){
    Rachador()
}