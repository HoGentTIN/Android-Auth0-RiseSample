package com.example.authtutorial.network.auth0

import kotlinx.serialization.Serializable

@Serializable
data class AuthorizeResponse(
    val access_token: String,
    val expires_in: Int,
    val token_type: String

) {

}

