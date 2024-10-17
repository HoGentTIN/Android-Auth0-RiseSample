package com.example.authtutorial

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.authtutorial.ui.theme.AuthTutorialTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthTutorialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {  innerPadding ->
                    App();
                }
            }
        }
    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AuthTutorialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            App()
        }
    }
}