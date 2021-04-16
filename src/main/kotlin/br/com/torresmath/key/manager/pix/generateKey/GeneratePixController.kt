package br.com.torresmath.key.manager.pix.generateKey

import br.com.torresmath.key.manager.GenerateKeyGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/clients")
class GeneratePixController(
    @Inject private val generatePixStub: GenerateKeyGrpcServiceGrpc.GenerateKeyGrpcServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)

    @Post
    fun generatePixKey(@Body @Valid request: GeneratePixKeyRequest): HttpResponse<Any> {
        val generateKey = generatePixStub.generateKey(request.toGrpcRequest())

        LOGGER.info("response ${generateKey}")
        return HttpResponse.ok()
    }
}