package br.com.torresmath.key.manager.annotations

import br.com.torresmath.key.manager.pix.generateKey.GeneratePixKeyRequest
import br.com.torresmath.key.manager.pix.generateKey.KeyTypeRequest
import io.micronaut.validation.validator.constraints.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidKeyIdentifierValidator::class])
annotation class ValidKeyIdentifier(
    val message: String = "Invalid pix key identifier for provided pix key type",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ValidKeyIdentifierValidator : ConstraintValidator<ValidKeyIdentifier, GeneratePixKeyRequest> {


    override fun isValid(value: GeneratePixKeyRequest?, context: ConstraintValidatorContext?): Boolean {
        if (value == null)
            return false

        return when (value.keyType) {
            KeyTypeRequest.RANDOM -> value.keyIdentifier.isBlank()
            KeyTypeRequest.MOBILE_NUMBER -> "^\\+[1-9][0-9]\\d{1,14}\$".toRegex().matches(value.keyIdentifier)
            KeyTypeRequest.CPF -> CPFValidator().run {
                initialize(null)
                isValid(value.keyIdentifier, null)
            }
            KeyTypeRequest.EMAIL -> EmailValidator().run {
                initialize(null)
                isValid(value.keyIdentifier, null)
            }
        }
    }


}
