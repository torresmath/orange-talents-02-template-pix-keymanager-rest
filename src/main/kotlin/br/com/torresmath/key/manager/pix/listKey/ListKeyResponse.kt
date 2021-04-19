package br.com.torresmath.key.manager.pix.listKey


import br.com.torresmath.key.manager.pix.model.AccountType
import br.com.torresmath.key.manager.pix.model.KeyType
import br.com.torresmath.key.manager.shared.toLocalDateTime
import java.time.LocalDateTime

data class ListKeyResponse(
    val clientId: String,
    val keys: List<KeyItemResponse>
) {
    companion object {
        fun fromGrpc(grpcKeyList: br.com.torresmath.key.manager.ListKeyResponse): ListKeyResponse {

            val keys = grpcKeyList.keysList.map {
                KeyItemResponse(
                    pixId = it.pixId,
                    key = it.key,
                    keyType = KeyType.valueOf(it.keyType.name),
                    accountType = AccountType.getByProtoValue(it.accountType),
                    createdAt = it.createdAt.toLocalDateTime()
                )
            }

            return ListKeyResponse(
                clientId = grpcKeyList.clientId,
                keys = keys
            )
        }
    }
}

data class KeyItemResponse(
    val pixId: String,
    val key: String,
    val keyType: KeyType,
    val accountType: AccountType,
    val createdAt: LocalDateTime
)