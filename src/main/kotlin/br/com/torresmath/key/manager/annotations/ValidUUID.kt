package br.com.torresmath.key.manager.annotations

import java.util.*
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Constraint(validatedBy = [ValidUUIDValidator::class])
annotation class ValidUUID(
    val message: String = "Value must be a valid UUID",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ValidUUIDValidator : ConstraintValidator<ValidUUID, Any> {
    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        return kotlin.runCatching { UUID.fromString(value.toString()) }.isSuccess
    }

}
