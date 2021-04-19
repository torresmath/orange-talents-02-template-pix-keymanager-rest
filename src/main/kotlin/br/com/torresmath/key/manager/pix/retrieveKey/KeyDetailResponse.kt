package br.com.torresmath.key.manager.pix.retrieveKey

import br.com.torresmath.key.manager.pix.model.AccountType
import br.com.torresmath.key.manager.pix.model.KeyType


data class KeyDetailResponse(
    val clientId: String,
    val pixId: String,
    val key: String,
    val keyType: KeyType,
    val owner: KeyOwnerResponse,
    val account: KeyAccountResponse
) {
    companion object {
        fun fromProto(protoKeyDetail: br.com.torresmath.key.manager.KeyDetailResponse): KeyDetailResponse {

            val protoOwner = protoKeyDetail.owner
            val protoAccount = protoKeyDetail.account
            val protoInst = protoAccount.institution

            val institutionResponse = KeyAccountInstitutionResponse(protoInst.name, protoInst.isbn)

            return KeyDetailResponse(
                clientId = protoKeyDetail.clientId,
                pixId = protoKeyDetail.pixId,
                key = protoKeyDetail.key,
                keyType = KeyType.valueOf(protoKeyDetail.keyType),
                owner = KeyOwnerResponse(protoOwner.name, protoOwner.cpf),
                account = KeyAccountResponse(
                    protoAccount.number,
                    protoAccount.branch,
                    AccountType.getByProtoValue(protoAccount.type),
                    institutionResponse
                )
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