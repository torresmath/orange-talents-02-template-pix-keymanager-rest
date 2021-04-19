package br.com.torresmath.key.manager.pix.generateKey

import br.com.torresmath.key.manager.GenerateKeyGrpcServiceGrpc
import br.com.torresmath.key.manager.annotations.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@Controller("/api/\${api.version}/clients/{clientId}")
class GeneratePixController(
    @Inject private val generatePixStub: GenerateKeyGrpcServiceGrpc.GenerateKeyGrpcServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Post("/pix")
    fun generatePixKey(
        @ValidUUID @NotNull clientId: UUID,
        @Body @Valid request: GeneratePixKeyRequest
    ): HttpResponse<Any> {
        val pixKey = generatePixStub.generateKey(request.toGrpcRequest(clientId))

        val uri = HttpResponse.uri("/api/v1/clients/${clientId}/pix/${pixKey.pixId}")
        return HttpResponse.created(uri)
    }
}