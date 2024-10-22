package com.example.authtutorial.screens.login

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.authtutorial.repository.APIResource
import com.example.authtutorial.network.auth0.AuthorizeResponse
import com.example.authtutorial.ui.theme.AuthTutorialTheme

@Composable
fun Login(login: (AuthorizeResponse) -> Unit, modifier: Modifier = Modifier){
    //passing the login functionality to the VM
    val extras = MutableCreationExtras().apply {
        set(LoginViewModel.LOGIN_KEY, login)
        set(LoginViewModel.APPLICATION_KEY, LocalContext.current.applicationContext as Application)
    }
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.Factory,
        extras = extras,
    )

    val loginState by viewModel.uiState.collectAsState()
    val apiResponseState by viewModel.authResponse.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = loginState.username,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.updateUserName(it) },
            label = { Text(text = "Username") },
            isError = loginState.loginError
        )

        var passwordVisible by rememberSaveable {mutableStateOf(false)}
        PasswordField(
            value = loginState.password, isError = loginState.loginError,
            onValueChange = { x: String -> viewModel.updatePwd(x)},
            passwordVisible = passwordVisible,
            toggleVisible = {passwordVisible = !passwordVisible}
        )

        if(loginState.loginError && apiResponseState is APIResource.Error){
            ErrorMessage(apiResponseState)
        }
        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            ForgotPassword {}
            Button(onClick = { viewModel.onSubmit() }) {
                if(apiResponseState is APIResource.Loading)
                    Text("loading...")
                else{
                    Text("submit")
                }
            }

        }

    }
}

@Composable
fun ErrorMessage(apiResponse: APIResource<AuthorizeResponse>) {
    Text("The combination of username and password is not found." + apiResponse.message)
}

@Composable
fun ForgotPassword(onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() }
    ) {
        Text("forgot password")
    }
}

@Composable
fun PasswordField(value: String, isError: Boolean, onValueChange: (String) -> Unit, passwordVisible: Boolean, toggleVisible: () -> Unit){
    TextField(value = value,
        isError = isError,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = {onValueChange(it)},
        label = { Text(text = "Password")} ,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = {toggleVisible()}){
                Icon(imageVector  = image, description)
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    AuthTutorialTheme {
        Login(login = {})
    }
}
