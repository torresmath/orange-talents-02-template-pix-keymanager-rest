package br.com.torresmath.key.manager.pix.listKey

import br.com.torresmath.key.manager.ListKeyGrpcServiceGrpc
import br.com.torresmath.key.manager.ListKeyRequest
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
class ListKeyController(@Inject val listKeyStub: ListKeyGrpcServiceGrpc.ListKeyGrpcServiceBlockingStub) {

    val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Get("/pix")
    fun listKeys(@PathVariable @NotNull @ValidUUID clientId: String): HttpResponse<ListKeyResponse> {

        val keys = listKeyStub.listKeyByClientId(ListKeyRequest.newBuilder().setClientId(clientId).build())

        return HttpResponse.ok(ListKeyResponse.fromGrpc(keys))
    }
}