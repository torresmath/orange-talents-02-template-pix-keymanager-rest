package br.com.torresmath.key.manager.pix.generateKey

import br.com.torresmath.key.manager.KeyRequest
import br.com.torresmath.key.manager.annotations.ValidKeyIdentifier
import br.com.torresmath.key.manager.annotations.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidKeyIdentifier
data class GeneratePixKeyRequest(
    @ValidUUID @NotBlank val clientId: String,
    @NotBlank @Size(max = 77) val keyIdentifier: String,
    @NotNull val keyType: KeyTypeRequest,
    @NotNull val accountType: AccountTypeRequest
) {
    fun toGrpcRequest(): KeyRequest {
        return KeyRequest.newBuilder()
            .setClientId(clientId)
            .setKeyIdentifier(keyIdentifier)
            .setKeyType(keyType.grpcValue)
            .setAccountType(accountType.grpcValue)
            .build()
    }
}

enum class KeyTypeRequest(val grpcValue: br.com.torresmath.key.manager.KeyType) {
    CPF(br.com.torresmath.key.manager.KeyType.CPF),
    MOBILE_NUMBER(br.com.torresmath.key.manager.KeyType.MOBILE_NUMBER),
    EMAIL(br.com.torresmath.key.manager.KeyType.EMAIL),
    RANDOM(br.com.torresmath.key.manager.KeyType.RANDOM);
}

enum class AccountTypeRequest(val grpcValue: br.com.torresmath.key.manager.AccountType) {
    CHECKING_ACCOUNT(br.com.torresmath.key.manager.AccountType.CHECKING_ACCOUNT),
    SAVINGS_ACCOUNT(br.com.torresmath.key.manager.AccountType.SAVINGS_ACCOUNT)
}