package br.com.torresmath.key.manager.pix.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AccountTypeTest {

    @Test
    fun `test getByProtoValue`() {
        val savings = AccountType.getByProtoValue(br.com.torresmath.key.manager.AccountType.SAVINGS_ACCOUNT)
        val checking = AccountType.getByProtoValue(br.com.torresmath.key.manager.AccountType.CHECKING_ACCOUNT)

        assertEquals(AccountType.SAVINGS_ACCOUNT, savings)
        assertEquals(AccountType.CHECKING_ACCOUNT, checking)
        assertThrows<IllegalArgumentException> {
            AccountType.getByProtoValue(br.com.torresmath.key.manager.AccountType.UNRECOGNIZED)
        }
    }
}