package com.rkey.rkms_backend.modules.auth.validation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy=PasswordMatchValidator.class)
@Documented
public @interface PasswordMatch {
    String message() default "Passwords do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payloads() default {};
    
    String passwordField();
    String confirmPasswordField();

    @Target({TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        PasswordMatch[] value();
    }
}
