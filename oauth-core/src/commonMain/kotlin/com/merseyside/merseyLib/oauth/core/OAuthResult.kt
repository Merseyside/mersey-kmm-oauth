package com.merseyside.merseyLib.oauth.core

import kotlinx.serialization.Serializable

@Serializable
sealed class OAuthResult {

    @Serializable
    data class Success(
        val query: String?,
        val queryParamPairs: OAuthParams
    ): OAuthResult()

    @Serializable
    object Fail : OAuthResult()
}