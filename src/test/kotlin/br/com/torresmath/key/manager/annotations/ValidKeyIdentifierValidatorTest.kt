package br.com.torresmath.key.manager.annotations

import br.com.torresmath.key.manager.pix.generateKey.GeneratePixKeyRequest
import br.com.torresmath.key.manager.pix.model.AccountType
import br.com.torresmath.key.manager.pix.model.KeyType
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import javax.inject.Inject

@MicronautTest
internal class ValidKeyIdentifierValidatorTest(@Inject val validator: ValidKeyIdentifierValidator) {

    companion object {
        @JvmStatic
        fun mobileParams() = listOf(
            Arguments.of("+55972651418", true),
            Arguments.of("+11000000000", true),
            Arguments.of("+111111111111", true),
            Arguments.of("+1234567891234", true),
            Arguments.of("+12345678", true),
            Arguments.of("+01000000000", false),
            Arguments.of("+01000000000", false),
            Arguments.of("55972651418", false),
        )

        @JvmStatic
        fun randomParams() = listOf(
            Arguments.of("22ccc326-995f-11eb-a8b3-0242ac130003", false),
            Arguments.of("value", false),
            Arguments.of("", true),
        )

        @JvmStatic
        fun cpfParams() = listOf(
            Arguments.of("42549789873", true),
            Arguments.of("value", false),
        )

        @JvmStatic
        fun emailParams() = listOf(
            Arguments.of("email@test.com", true),
            Arguments.of("email@test.com.br", true),
            Arguments.of("email@test", true),
            Arguments.of("email", false),
            Arguments.of("email@", false)
        )
    }

    @Test
    fun `should return false with null value`() {
        assertFalse(validator.isValid(null, null))
    }

    @ParameterizedTest
    @MethodSource("mobileParams")
    fun `test validator MOBILE_NUMBER`(keyIdentifier: String, expected: Boolean) {

        val request = GeneratePixKeyRequest(keyIdentifier, KeyType.MOBILE_NUMBER, AccountType.CHECKING_ACCOUNT)

        assertEquals(expected, validator.isValid(request, null))

    }

    @ParameterizedTest
    @MethodSource("cpfParams")
    fun `test validator CPF`(keyIdentifier: String, expected: Boolean) {

        val request = GeneratePixKeyRequest(keyIdentifier, KeyType.CPF, AccountType.CHECKING_ACCOUNT)

        assertEquals(expected, validator.isValid(request, null))

    }

    @ParameterizedTest
    @MethodSource("emailParams")
    fun `test validator EMAIL`(keyIdentifier: String, expected: Boolean) {

        val request = GeneratePixKeyRequest(keyIdentifier, KeyType.EMAIL, AccountType.CHECKING_ACCOUNT)

        assertEquals(expected, validator.isValid(request, null))

    }

    @ParameterizedTest
    @MethodSource("randomParams")
    fun `test validator RANDOM`(keyIdentifier: String, expected: Boolean) {

        val request = GeneratePixKeyRequest(keyIdentifier, KeyType.RANDOM, AccountType.CHECKING_ACCOUNT)

        assertEquals(expected, validator.isValid(request, null))

    }
}