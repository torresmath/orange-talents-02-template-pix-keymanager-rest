package br.com.torresmath.key.manager.pix.generateKey

import br.com.torresmath.key.manager.*
import br.com.torresmath.key.manager.pix.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
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
internal class GeneratePixControllerTest {

    @field:Inject
    lateinit var blockingStub: GenerateKeyGrpcServiceGrpc.GenerateKeyGrpcServiceBlockingStub

    @field:Inject
    @field:Client("local-test")
    lateinit var client: HttpClient

    @Test
    internal fun `should generate pix key`() {

        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        val keyIdentifier = "42549789873"

        val body =
            GeneratePixKeyRequest(keyIdentifier, br.com.torresmath.key.manager.pix.model.KeyType.CPF, br.com.torresmath.key.manager.pix.model.AccountType.CHECKING_ACCOUNT)

        val grpcRequest = KeyRequest.newBuilder()
            .setClientId(clientId)
            .setKeyType(KeyType.CPF)
            .setKeyIdentifier(keyIdentifier)
            .setAccountType(AccountType.CHECKING_ACCOUNT)
            .build()

        val grpcResponse = KeyResponse.newBuilder()
            .setPixId(pixId)
            .setStatus(KeyStatus.INACTIVE)
            .build()

        given(blockingStub.generateKey(grpcRequest)).willReturn(grpcResponse)

        val request = HttpRequest.POST("/api/v1/clients/$clientId/pix", body)
        println(request.toString())
        val response = client.toBlocking().exchange(request, GeneratePixKeyRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(GenerateKeyGrpcServiceGrpc.GenerateKeyGrpcServiceBlockingStub::class.java)
    }
}