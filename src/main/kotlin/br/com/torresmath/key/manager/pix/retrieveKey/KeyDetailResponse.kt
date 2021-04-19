package br.com.torresmath.key.manager.pix.retrieveKey

import br.com.torresmath.key.manager.pix.model.AccountType
import br.com.torresmath.key.manager.pix.model.KeyType
import br.com.torresmath.key.manager.shared.toLocalDateTime
import java.time.LocalDateTime


data class KeyDetailResponse(
    val clientId: String,
    val pixId: String,
    val key: String,
    val keyType: KeyType,
    val owner: KeyOwnerResponse,
    val account: KeyAccountResponse,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromGrpc(grpcKeyDetail: br.com.torresmath.key.manager.KeyDetailResponse): KeyDetailResponse {

            val protoOwner = grpcKeyDetail.owner
            val protoAccount = grpcKeyDetail.account
            val protoInst = protoAccount.institution

            val institutionResponse = KeyAccountInstitutionResponse(protoInst.name, protoInst.isbn)

            return KeyDetailResponse(
                clientId = grpcKeyDetail.clientId,
                pixId = grpcKeyDetail.pixId,
                key = grpcKeyDetail.key,
                keyType = KeyType.valueOf(grpcKeyDetail.keyType),
                owner = KeyOwnerResponse(protoOwner.name, protoOwner.cpf),
                account = KeyAccountResponse(
                    protoAccount.number,
                    protoAccount.branch,
                    AccountType.getByProtoValue(protoAccount.type),
                    institutionResponse
                ),
                createdAt = grpcKeyDetail.createdAt.toLocalDateTime()
            )
        }
    }
}

data class KeyOwnerResponse(val name: String, val cpf: String)
data class KeyAccountResponse(
    val number: String,
    val branch: String,
    val type: AccountType,
    val institution: KeyAccountInstitutionResponse
)

data class KeyAccountInstitutionResponse(val name: String, val isbn: String)