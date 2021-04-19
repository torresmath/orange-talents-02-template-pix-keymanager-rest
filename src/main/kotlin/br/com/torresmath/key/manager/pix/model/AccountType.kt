package br.com.torresmath.key.manager.pix.model

enum class AccountType(val grpcValue: br.com.torresmath.key.manager.AccountType) {
    CHECKING_ACCOUNT(br.com.torresmath.key.manager.AccountType.CHECKING_ACCOUNT),
    SAVINGS_ACCOUNT(br.com.torresmath.key.manager.AccountType.SAVINGS_ACCOUNT);

    companion object {
        fun getByProtoValue(protoValue: br.com.torresmath.key.manager.AccountType) : AccountType {
            return when(protoValue) {
                br.com.torresmath.key.manager.AccountType.CHECKING_ACCOUNT -> CHECKING_ACCOUNT
                br.com.torresmath.key.manager.AccountType.SAVINGS_ACCOUNT -> SAVINGS_ACCOUNT
                else -> throw IllegalArgumentException("Couldn't translate Proto value: ${protoValue.name} to Account Type")
            }
        }
    }

}