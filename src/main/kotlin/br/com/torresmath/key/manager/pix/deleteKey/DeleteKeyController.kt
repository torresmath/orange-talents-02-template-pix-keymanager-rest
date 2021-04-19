package br.com.torresmath.key.manager.pix.deleteKey

import br.com.torresmath.key.manager.DeleteKeyGrpcServiceGrpc
import br.com.torresmath.key.manager.DeleteKeyRequest
import br.com.torresmath.key.manager.annotations.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.validation.constraints.NotNull

@Validated
@Controller("/api/\${api.version}/clients/{clientId}")
class DeleteKeyController(
    @Inject val deletePixStub: DeleteKeyGrpcServiceGrpc.DeleteKeyGrpcServiceBlockingStub
) {

    val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Delete("/pix/{pixId}")
    fun deleteKey(
        @PathVariable @ValidUUID @NotNull clientId: String,
        @PathVariable @ValidUUID @NotNull pixId: String
    ) : HttpResponse<Any> {

        val deleteKeyRequest = DeleteKeyRequest.newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .build()

        deletePixStub.deleteKey(deleteKeyRequest)

        return HttpResponse.noContent()
    }

}