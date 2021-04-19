package br.com.torresmath.key.manager.pix.deleteKey

import br.com.torresmath.key.manager.DeleteKeyGrpcServiceGrpc
import br.com.torresmath.key.manager.DeleteKeyRequest
import br.com.torresmath.key.manager.pix.KeyManagerGrpcFactory
import com.google.protobuf.Empty
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito
import org.mockito.kotlin.given
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class DeleteKeyControllerTest {

    @field:Inject
    lateinit var blockingStub: DeleteKeyGrpcServiceGrpc.DeleteKeyGrpcServiceBlockingStub

    @field:Inject
    @field:Client("local-test")
    lateinit var client: HttpClient

    @field:Value("\${api.version}")
    lateinit var version: String

    companion object {
        @JvmStatic
        fun params() = listOf(
            Arguments.of(UUID.randomUUID().toString(), "invalid-uuid", "pixId: Value must be a valid UUID"),
            Arguments.of("invalid-uuid", UUID.randomUUID().toString(), "clientId: Value must be a valid UUID")
        )
    }

    @ParameterizedTest
    @MethodSource("params")
    fun `should return 400 BAD_REQUEST invalid Uuid`(clientId: String, pixId: String, expectedMessage: String) {

        val url = "/api/${version}/clients/$clientId/pix/$pixId"
        val request = HttpRequest.DELETE(url, null)

        val exc =
            assertThrows<HttpClientResponseException> { client.toBlocking().exchange(request, Any::class.java) }
        assertEquals(HttpStatus.BAD_REQUEST, exc.status)
        assertEquals(expectedMessage, exc.message)
    }

    @Test
    fun `should DELETE and return 204 NO_CONTENT`() {

        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val grpcRequest = DeleteKeyRequest.newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .build()

        given(blockingStub.deleteKey(grpcRequest)).willReturn(Empty.newBuilder().build())

        val url = "/api/${version}/clients/$clientId/pix/$pixId"
        val request = HttpRequest.DELETE(url, null)
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.NO_CONTENT, response.status)
    }

    @Test
    fun `should return 404 NOT_FOUND`() {

        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val grpcRequest = DeleteKeyRequest.newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .build()

        given(blockingStub.deleteKey(grpcRequest)).willThrow(StatusRuntimeException(io.grpc.Status.NOT_FOUND))

        val url = "/api/${version}/clients/$clientId/pix/$pixId"
        val request = HttpRequest.DELETE(url, null)
        val exc =
            assertThrows<HttpClientResponseException> { client.toBlocking().exchange(request, Any::class.java) }

        assertEquals(HttpStatus.NOT_FOUND.code, exc.status.code)
    }

}

@Factory
@Replaces(factory = KeyManagerGrpcFactory::class)
internal class MockingStubFactory {
    @Singleton
    fun stubMock() = Mockito.mock(DeleteKeyGrpcServiceGrpc.DeleteKeyGrpcServiceBlockingStub::class.java)
}