package br.com.torresmath.key.manager.pix.model

import br.com.torresmath.key.manager.AccountType as GrpcAccountType

enum class AccountType(val grpcValue: GrpcAccountType) {
    CHECKING_ACCOUNT(GrpcAccountType.CHECKING_ACCOUNT),
    SAVINGS_ACCOUNT(GrpcAccountType.SAVINGS_ACCOUNT);

    companion object {
        fun getByProtoValue(protoValue: GrpcAccountType) : AccountType {
            return when(protoValue) {
                GrpcAccountType.CHECKING_ACCOUNT -> CHECKING_ACCOUNT
                GrpcAccountType.SAVINGS_ACCOUNT -> SAVINGS_ACCOUNT
                else -> throw IllegalArgumentException("Couldn't translate Proto value: ${protoValue.name} to Account Type")
            }
        }
    }

}