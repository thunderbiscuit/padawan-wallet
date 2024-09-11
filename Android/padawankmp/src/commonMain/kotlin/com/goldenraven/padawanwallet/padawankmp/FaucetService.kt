/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.padawankmp

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.utils.io.core.use
import kotlinx.coroutines.runBlocking

public class FaucetService {
    public fun callTatooineFaucet(
        address: String,
        faucetUrl: String,
        faucetUsername: String,
        faucetPassword: String
    ): FaucetCall {
        val ktorClient = HttpClient(CIO) {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(
                            username = faucetUsername,
                            password = faucetPassword
                        )
                    }
                }
            }
        }

        return ktorClient.use {
            try {
                val response: HttpResponse = runBlocking {
                    ktorClient.request(faucetUrl) {
                        method = HttpMethod.Post
                        setBody(TextContent(address, ContentType.Text.Plain))
                    }
                }

                when (response.status.value) {
                    200 -> FaucetCall.Success(
                        status = response.status.value,
                        description = response.status.description,
                    )

                    else -> FaucetCall.Error(
                        status = response.status.value,
                        description = response.status.description
                    )
                }
            } catch (exception: Exception) {
                FaucetCall.ExceptionThrown(exception = exception)
            }
        }
    }
}

public sealed class FaucetCall {
    public data class Success(val status: Int, val description: String) : FaucetCall()
    public data class Error(val status: Int, val description: String) : FaucetCall()
    public data class ExceptionThrown(val exception: Exception) : FaucetCall()
}
