package br.com.torresmath.key.manager.pix.generateKey

import br.com.torresmath.key.manager.KeyRequest
import br.com.torresmath.key.manager.annotations.ValidKeyIdentifier
import br.com.torresmath.key.manager.pix.model.AccountType
import br.com.torresmath.key.manager.pix.model.KeyType
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidKeyIdentifier
data class GeneratePixKeyRequest(
//    @ValidUUID @NotBlank val clientId: String,
    @NotBlank @Size(max = 77) val keyIdentifier: String,
    @NotNull val keyType: KeyType,
    @NotNull val accountType: AccountType
) {
    fun toGrpcRequest(clientId: UUID): KeyRequest {
        return KeyRequest.newBuilder()
            .setClientId(clientId.toString())
            .setKeyIdentifier(keyIdentifier)
            .setKeyType(keyType.grpcValue)
            .setAccountType(accountType.grpcValue)
            .build()
    }
}