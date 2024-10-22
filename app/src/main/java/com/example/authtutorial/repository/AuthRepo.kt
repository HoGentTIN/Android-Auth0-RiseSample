package com.example.authtutorial.repository

import android.util.Log
import com.example.authtutorial.network.auth0.Auth0Api
import com.example.authtutorial.network.auth0.AuthorizeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


interface IAuthRepo {
    suspend fun getToken(userName: String, password: String): Flow<APIResource<AuthorizeResponse>>

}


class AuthRepo(val authApi: Auth0Api): IAuthRepo{

    /*
    * getToken performs the api call asynchronously using a Call and Response system
    * Don't confuse the response with an APIResource.
    *
    * When the response contains 'successfull' an APIResource object is created to hold the data
    * the APIResource is then emitted on a flow, ready to be collected.
    * */
    override suspend fun getToken(userName: String, password: String) = flow<APIResource<AuthorizeResponse>> {
        val loadingResource = APIResource.Loading<AuthorizeResponse>()
        emit(loadingResource)

        val call = authApi.getAccessToken(userName = userName, pwd = password)

        //converting the call to a flow by executing as a synchronized function
        val response =
            withContext(Dispatchers.IO) {
                call.execute()
            }

        if (response.isSuccessful) {
            val token = response.body()

            // Check if the student object is null due to parsing issues
            if (token != null) {
                Log.d("API", "Token: ${token.access_token}")
                emit(APIResource.Success<AuthorizeResponse>(token))
            } else {
                Log.e("API", "Failed to parse token object, response ok")
                // Fallback: Handle the case when parsing fails
                emit(APIResource.Error<AuthorizeResponse>("Issue when loggin in." + response.errorBody()?.string()))

            }
        } else {
            Log.e("Error", "API call unsuccessful: ${response.errorBody()?.string()}")
            emit(APIResource.Error<AuthorizeResponse>("Issue with API call" + response.errorBody()?.string()))
        }




    }.flowOn(Dispatchers.IO)




}