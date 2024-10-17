package com.example.authtutorial.network.auth0

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Auth0Api {

    /*
    * {"access_token":"eyJhbGci...","expires_in":86400,"token_type":"Bearer"}
    * */
    @FormUrlEncoded
    @POST("oauth/token")
    fun getAccessToken(@Field("username") userName: String,
                       @Field("audience") audience: String= "https://androidapp.example.hogent.be",
                       @Field("grant_type") grantType: String = "password",
                       @Field("client_id") clientId: String = "WQXurnHYdulEysdkLtCp4B5EX7U5znSr",
                       @Field("password") pwd: String
                       ): Call<AuthorizeResponse>

}