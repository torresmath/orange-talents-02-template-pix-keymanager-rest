package br.com.torresmath.key.manager.pix.retrieveKey

import br.com.torresmath.key.manager.RetrieveKeyGrpcServiceGrpc
import br.com.torresmath.key.manager.RetrieveKeyRequest
import br.com.torresmath.key.manager.annotations.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.validation.constraints.NotNull

@Validated
@Controller("/api/\${api.version}/clients/{clientId}")
class RetrieveKeyController(@Inject private val retrieveKeyStub: RetrieveKeyGrpcServiceGrpc.RetrieveKeyGrpcServiceBlockingStub) {

    val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Get("/pix/{pixId}")
    fun retrievePixKey(
        @PathVariable @NotNull @ValidUUID clientId: String,
        @PathVariable @NotNull @ValidUUID pixId: String
    ) : HttpResponse<Any> {

        val retrieveKeyRequest = RetrieveKeyRequest.newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .build()

        val grpcKey = retrieveKeyStub.retrieveKey(retrieveKeyRequest)

        return HttpResponse.ok(KeyDetailResponse.fromProto(grpcKey))
    }

}