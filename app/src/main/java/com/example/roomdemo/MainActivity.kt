package com.example.roomdemo

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roomdemo.ui.theme.RoomDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val owner = LocalViewModelStoreOwner.current
                    owner?.let {
                        val viewModel: MainViewModel = viewModel(
                            it,
                            "MainViewModel",
                            MainViewModelFactory(
                                LocalContext.current.applicationContext as Application
                            )
                        )
                        ScreenSetup(viewModel = viewModel)
                    }

                }
            }
        }
    }
}

@Composable
fun ScreenSetup(viewModel: MainViewModel) {
    val allResult by viewModel.allGrocery.observeAsState(listOf())
    val searchResult by viewModel.searchResult.observeAsState(listOf())


    MainScreen(
        viewModel = viewModel,
        allResult = allResult,
        searchResult = searchResult
    )


}

@Composable
fun MainScreen(viewModel: MainViewModel, allResult: List<Grocery>, searchResult: List<Grocery>) {
    var gName by remember {
        mutableStateOf("")
    }
    var searching by remember { mutableStateOf(false) }
    val onchange={t:Boolean->searching=t}
    var wt by remember {
        mutableStateOf("")
    }
    val ongNameChange = { txt1: String -> gName = txt1 }
    val onWtChange = { txt2: String -> wt = txt2 }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(
            title = "Grocery Name",
            textState = gName,
            onTextChange = ongNameChange,
            keyboardType = KeyboardType.Text
        )
        CustomTextField(
            title = "Weight in Kg",
            textState = wt,
            onTextChange = onWtChange,
            keyboardType = KeyboardType.Number
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(onClick = {
                if (wt.isNotEmpty()) {
                    viewModel.addGrocery(
                        Grocery(gName = gName, wt = wt.toInt())
                    )
                    searching = false
                }
            }) {
                Text("Add")
            }
            Button(onClick = {
                searching = true
                viewModel.findGrocery(gName)
            }) {
                Text("Search")
            }

            Button(onClick = {
                searching = false
                gName = ""
                wt = ""
            }) {
                Text("Clear")
            }
        }
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            val list = if (searching) searchResult else allResult
            item {
                TitleRow(
                   head2 = "Name", head3 = "Weight(Kg)"
                )
            }
            items(list) { grocery ->
                ProductRow(
                    name = grocery.gName,
                    wt = grocery.wt,
                    onChange = onchange,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun CustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = textState,
        onValueChange = { onTextChange(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = true,
        label = { Text(title) },
        modifier = Modifier.padding(10.dp),
        textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
    )
}

@Composable
fun TitleRow(head2: String, head3: String) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(
            head2, color = Color.White, modifier = Modifier.weight(0.5f).padding(start = 5.dp)
        )
        Text(head3, color = Color.White, modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun ProductRow(
    name: String,
    wt: Int,
    onChange: (t: Boolean) -> Unit,
    viewModel: MainViewModel
) {
    Box(modifier = Modifier.fillMaxWidth().background(color=Color.Blue)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(text = name, modifier = Modifier.weight(0.63f).padding(top=13.dp).padding(start = 18.dp))
            Text(text = wt.toString()+"Kg", modifier = Modifier.weight(0.37f).padding(top=13.dp))
            IconButton(onClick = {
                onChange(false)
                viewModel.deleteGrocery(name)
            }, content = {
                Icon(modifier=Modifier.size(22.dp).padding(3.dp),painter = painterResource(id = R.drawable.dele), contentDescription ="delete" )
            })

        }
    }
}


@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}

