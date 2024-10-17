package com.example.authtutorial.screens.welcomeUser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.authtutorial.ui.theme.AuthTutorialTheme


@Composable
fun WelcomeUser(modifier: Modifier = Modifier){
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Welcome!")

    }
}

@Preview(showBackground = true)
@Composable
fun welcomeUser(){
    AuthTutorialTheme {
        WelcomeUser()
    }
}
