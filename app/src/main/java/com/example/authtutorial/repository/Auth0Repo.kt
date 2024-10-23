package com.example.authtutorial.repository

import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.example.authtutorial.network.auth0.Auth0Api
import com.example.authtutorial.network.auth0.AuthorizeResponse
import kotlinx.coroutines.flow.Flow


class Auth0Repo(val authentication: AuthenticationAPIClient): IAuthRepo{
    override suspend fun getToken(
        userName: String,
        password: String
    ): Flow<APIResource<AuthorizeResponse>> {
        authentication
            .login(userName, password, "Username-Password-Authentication")
            .start(Callback<Credentials, AuthenticationException>{
                @Override
                public void onSuccess(credentials: Credentials) {
                    // Handle success: credentials.getAccessToken(), credentials.getIdToken(), etc.
                }

                @Override
                public void onFailure(AuthenticationException error) {
                    // Handle failure (e.g., wrong username/password)
                }
            });
    }

}
