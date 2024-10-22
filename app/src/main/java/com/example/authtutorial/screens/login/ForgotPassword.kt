package com.example.authtutorial.screens.login

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import kotlinx.coroutines.Dispatchers


/*
* This composable uses the auth0 webview.
* It's much less customizable, but faster in setup.
*
* dev-387gw3fkqtq50fsu.us.auth0.com
* WQXurnHYdulEysdkLtCp4B5EX7U5znSr
*
*
* Callback URLs
* demo://dev-387gw3fkqtq50fsu.us.auth0.com/android/com.example.authtutorial/callback
*
*
*
* */
@Composable
fun ForgotPassword() {
    val context = LocalContext.current
    TextButton(
        onClick = {
            var account = Auth0(
                "WQXurnHYdulEysdkLtCp4B5EX7U5znSr",
                "dev-387gw3fkqtq50fsu.us.auth0.com"
            )

            WebAuthProvider.login(account)
                .withScheme("demo")
                .withScope("openid profile email")
                // Launch the authentication passing the callback where the results will be received
                .start(context, object : Callback<Credentials, AuthenticationException> {
                    // Called when there is an authentication failure
                    override fun onFailure(exception: AuthenticationException) {
                        // Something went wrong!
                    }

                    // Called when authentication completed successfully
                    override fun onSuccess(credentials: Credentials) {
                        // Get the access token from the credentials object.
                        // This can be used to call APIs
                        val accessToken = credentials.accessToken

                    }
                })
        }
    ) {
        Text("forgot password")
    }
}



