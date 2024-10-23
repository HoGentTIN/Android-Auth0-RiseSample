package com.example.authtutorial.repository

import android.util.Log
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.example.authtutorial.network.auth0.Auth0Api
import com.example.authtutorial.network.auth0.AuthorizeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


class Auth0Repo(val authentication: AuthenticationAPIClient): IAuthRepo{
    override suspend fun getToken(
        userName: String,
        password: String
    ): Flow<APIResource<AuthorizeResponse>> =
        flow<APIResource<AuthorizeResponse>> {
            val loadingResource = APIResource.Loading<AuthorizeResponse>()
            emit(loadingResource)

            val call = authentication.login(userName, password)
            val response =
                withContext(Dispatchers.IO) {
                    try{
                        call.execute()
                    }
                    catch(e: Exception){
                        //...
                        null
                    }
                }

            //note: to be refactored so the APIResoucre can contain a Credentials object
            if(response?.accessToken != null){
                emit(APIResource.Success<AuthorizeResponse>(AuthorizeResponse(access_token = response.accessToken
                    , expires_in = 5000, refresh_token = response.refreshToken,
                    token_type = response.type)))
            }
            else{
                emit(APIResource.Error("Issue with Auth API"))
            }



        }.flowOn(Dispatchers.IO)

    override suspend fun getCredentials(
        userName: String,
        password: String
    ): Flow<APIResource<Credentials>> = flow<APIResource<Credentials>> {
        val loadingResource = APIResource.Loading<Credentials>()
        emit(loadingResource)


        val call = authentication.login(userName, password)
        val response: Credentials? =
            withContext(Dispatchers.IO) {
                try{
                    call.execute()
                }
                catch(e: Exception){

                   null
                }
            }


        //note: to be refactored so the APIResoucre can contain a Credentials object
        if(response != null){
            emit(APIResource.Success<Credentials>(response))
        }
        else{
            emit(APIResource.Error("Issue with Auth API"))
        }



    }.flowOn(Dispatchers.IO)


}
