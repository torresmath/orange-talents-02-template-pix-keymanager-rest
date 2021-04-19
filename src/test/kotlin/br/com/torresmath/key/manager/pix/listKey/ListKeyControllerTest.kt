package br.com.torresmath.key.manager.pix.listKey

import br.com.torresmath.key.manager.*
import br.com.torresmath.key.manager.pix.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.given
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListKeyControllerTest {

    @field:Inject
    lateinit var blockingStub: ListKeyGrpcServiceGrpc.ListKeyGrpcServiceBlockingStub

    @field:Inject
    @field:Client("local-test")
    lateinit var client: HttpClient

    @field:Value("\${api.version}")
    lateinit var version: String

    @Test
    fun `should return 200 OK with empty keys list`() {
        val clientId = UUID.randomUUID().toString()

        val url = "/api/${version}/clients/$clientId/pix"

        val grpcRequest = ListKeyRequest.newBuilder()
            .setClientId(clientId)
            .build()

        val grpcResponse = br.com.torresmath.key.manager.ListKeyResponse.newBuilder()
            .setClientId(clientId)
            .clearKeys()
            .build()

        given(blockingStub.listKeyByClientId(grpcRequest))
            .willReturn(grpcResponse)

        val request = HttpRequest.GET<ListKeyResponse>(url)
        val response = client.toBlocking().exchange(request, ListKeyResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertTrue(response.body()!!.keys.isEmpty())
    }

    @Test
    fun `should return 200 OK with keys`() {

        val clientId = UUID.randomUUID().toString()

        val url = "/api/${version}/clients/$clientId/pix"

        val grpcRequest = ListKeyRequest.newBuilder()
            .setClientId(clientId)
            .build()

        given(blockingStub.listKeyByClientId(grpcRequest))
            .willReturn(buildResponse(clientId))

        val request = HttpRequest.GET<ListKeyResponse>(url)
        val response = client.toBlocking().exchange(request, ListKeyResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(1, response.body()!!.keys.size)

    }

    private fun buildResponse(clientId: String) : br.com.torresmath.key.manager.ListKeyResponse {

        val key = br.com.torresmath.key.manager.ListKeyResponse.PixKey.newBuilder()
            .setStatus(KeyStatus.ACTIVE)
            .setAccountType(AccountType.CHECKING_ACCOUNT)
            .setKey("42549789873")
            .setKeyType(KeyType.CPF)
            .build()

        return br.com.torresmath.key.manager.ListKeyResponse.newBuilder()
            .setClientId(clientId)
            .addAllKeys(mutableListOf(key))
            .build()
    }
}

@Factory
@Replaces(factory = KeyManagerGrpcFactory::class)
internal class MockingStubFactory {
    @Singleton
    fun mockStub() = Mockito.mock(ListKeyGrpcServiceGrpc.ListKeyGrpcServiceBlockingStub::class.java)
}