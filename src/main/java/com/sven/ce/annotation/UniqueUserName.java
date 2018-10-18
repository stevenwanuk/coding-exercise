package com.sven.ce.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.sven.ce.model.User;
import com.sven.ce.validator.UniqueUserNameValidator;

/**
 * Indicates that the value of an annotated field haven't been used as {@link User#getUsername()}
 * 
 * @see UniqueUserNameValidator
 */

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserNameValidator.class)
public @interface UniqueUserName
{
    String message() default "{validation.username.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
