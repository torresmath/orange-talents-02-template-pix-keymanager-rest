package br.com.torresmath.key.manager.shared

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class GlobalExceptionHandlerTest {

    private val request = HttpRequest.GET<Any>("/")

    companion object {
        private val defaultGrpcMessage = "Message that should be ignored"

        @JvmStatic
        fun params() = listOf(
            Arguments.of(Status.NOT_FOUND, HttpStatus.NOT_FOUND, "Not found", "Not found"),
            Arguments.of(Status.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST, defaultGrpcMessage, "Invalid request body"),
            Arguments.of(Status.ALREADY_EXISTS, HttpStatus.UNPROCESSABLE_ENTITY, "Already exists", "Already exists"),
            Arguments.of(
                Status.ABORTED,
                HttpStatus.INTERNAL_SERVER_ERROR,
                defaultGrpcMessage,
                "Couldn't solve request due to error: $defaultGrpcMessage"
            ),
            Arguments.of(
                Status.INTERNAL,
                HttpStatus.INTERNAL_SERVER_ERROR,
                defaultGrpcMessage,
                "Couldn't solve request due to error: $defaultGrpcMessage"
            ),
            Arguments.of(
                Status.DATA_LOSS,
                HttpStatus.INTERNAL_SERVER_ERROR,
                defaultGrpcMessage,
                "Couldn't solve request due to error: $defaultGrpcMessage"
            ),
            Arguments.of(
                Status.CANCELLED,
                HttpStatus.INTERNAL_SERVER_ERROR,
                defaultGrpcMessage,
                "Couldn't solve request due to error: $defaultGrpcMessage"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("params")
    internal fun `should return 404 when Status == NOT_FOUND`(
        status: Status,
        expectedHttpStatus: HttpStatus,
        grpcMessage: String,
        expectedDescription: String
    ) {

        val exc = StatusRuntimeException(status.withDescription(grpcMessage))

        val response = GlobalExceptionHandler().handle(request, exc)
        assertEquals(expectedHttpStatus, response.status)
        assertNotNull(response.body())
        assertEquals(expectedDescription, (response.body() as JsonError).message)
    }
}