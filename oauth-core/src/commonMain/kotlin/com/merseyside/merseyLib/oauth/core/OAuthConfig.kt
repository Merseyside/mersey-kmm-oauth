package com.merseyside.merseyLib.oauth.core

import com.merseyside.merseyLib.kotlin.network.URLBuilder
import kotlinx.serialization.Serializable

@Serializable
data class OAuthConfig(
    val oauthUri: String,
    val redirectHost: String
) {

    constructor(url: String) : this(url, getRedirectUrlFromFullUrl(url))

    class Builder private constructor(
        private val urlBuilder: URLBuilder,
        private val redirectUri: String
    ) {
        constructor(
            protocol: String,
            host: String,
            authMethod: String,
            clientId: String,
            redirectUri: String,
            responseType: String,
            scope: String
        ) : this(
            buildUrl(protocol, host, authMethod, clientId, redirectUri, responseType, scope),
            redirectUri
        )

        fun addParams(key: String, value: String): Builder {
            urlBuilder.queryParam(key, value)
            return this
        }

        fun build(): OAuthConfig {
            val url = urlBuilder.build()
            return OAuthConfig(url, redirectUri)
        }

        companion object {
            private fun buildUrl(
                protocol: String,
                host: String,
                authMethod: String,
                clientId: String,
                redirectUri: String,
                responseType: String,
                scope: String
            ): URLBuilder {
                return URLBuilder().apply {
                    this.protocol = protocol
                    this.host = host
                    this.method = authMethod
                    queryParam("client_id", clientId)
                    queryParam("redirect_uri", redirectUri)
                    queryParam("response_type", responseType)
                    queryParam("scope", scope)
                }
            }
        }
    }

    companion object {
        private fun getRedirectUrlFromFullUrl(url: String): String {
            val regex = "(?<=redirect_uri=)(.*)(?=&)".toRegex()
            val matches = regex.find(url)
            return matches?.value
                ?: throw IllegalArgumentException("'redirect_uri' param not found in passed url!")
        }
    }
}