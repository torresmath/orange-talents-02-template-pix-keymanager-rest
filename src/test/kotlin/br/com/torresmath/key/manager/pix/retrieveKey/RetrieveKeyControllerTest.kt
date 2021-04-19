package br.com.torresmath.key.manager.pix.retrieveKey

import br.com.torresmath.key.manager.*
import br.com.torresmath.key.manager.KeyAccountInstitutionResponse
import br.com.torresmath.key.manager.KeyAccountResponse
import br.com.torresmath.key.manager.KeyDetailResponse
import br.com.torresmath.key.manager.KeyOwnerResponse
import br.com.torresmath.key.manager.pix.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
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
internal class RetrieveKeyControllerTest {

    @field:Inject
    lateinit var blockingStub: RetrieveKeyGrpcServiceGrpc.RetrieveKeyGrpcServiceBlockingStub

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
        val request = HttpRequest.GET<HttpResponse<Any>>(url)

        val exc =
            assertThrows<HttpClientResponseException> { client.toBlocking().retrieve(request) }

        assertEquals(HttpStatus.BAD_REQUEST, exc.status)
        assertEquals(expectedMessage, exc.message)
    }

    @Test
    fun `should return 404 NOT_FOUND`() {
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val url = "/api/${version}/clients/$clientId/pix/$pixId"

        given(
            blockingStub.retrieveKey(
                RetrieveKeyRequest.newBuilder()
                    .setClientId(clientId)
                    .setPixId(pixId)
                    .build()
            )
        ).willThrow(StatusRuntimeException(Status.NOT_FOUND))

        val exc = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(
                HttpRequest.GET<HttpResponse<Any>>(url),
                br.com.torresmath.key.manager.pix.retrieveKey.KeyDetailResponse::class.java
            )
        }

        assertEquals(HttpStatus.NOT_FOUND, exc.status)
    }

    @Test
    fun `should return 200 OK`() {
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val url = "/api/${version}/clients/$clientId/pix/$pixId"

        given(
            blockingStub.retrieveKey(
                RetrieveKeyRequest.newBuilder()
                    .setClientId(clientId)
                    .setPixId(pixId)
                    .build()
            )
        ).willReturn(buildProtoResponse(clientId, pixId))

        val response = client.toBlocking().exchange(
            HttpRequest.GET<HttpResponse<Any>>(url),
            br.com.torresmath.key.manager.pix.retrieveKey.KeyDetailResponse::class.java
        )
        assertEquals(HttpStatus.OK, response.status)
    }

    private fun buildProtoResponse(clientId: String, pixId: String): KeyDetailResponse {
        val cpfKey = "42549789873"
        val owner = KeyOwnerResponse.newBuilder()
            .setCpf(cpfKey)
            .setName("Test user")
            .build()

        val account = KeyAccountResponse.newBuilder()
            .setNumber("123456")
            .setBranch("0001")
            .setInstitution(
                KeyAccountInstitutionResponse.newBuilder()
                    .setName("ITAÚ UNIBANCO S.A.")
                    .setIsbn("60701190")
            )
            .build()

        return KeyDetailResponse.newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .setKey(cpfKey)
            .setKeyType("CPF")
            .setOwner(owner)
            .setAccount(account)
            .setCreatedAt(Timestamp.newBuilder().setSeconds(1234).setNanos(1234))
            .build()
    }

}

@Factory
@Replaces(factory = KeyManagerGrpcFactory::class)
internal class MockingStubFactory {

    @Singleton
    fun stubMock() = Mockito.mock(RetrieveKeyGrpcServiceGrpc.RetrieveKeyGrpcServiceBlockingStub::class.java)
}