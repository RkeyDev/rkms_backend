package com.rkey.rkms_backend.modules.auth.validation;

import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    private String passwordField;
    private String confirmPasswordField;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.passwordField = constraintAnnotation.passwordField();
        this.confirmPasswordField = constraintAnnotation.confirmPasswordField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(passwordField);
            Object confirmPasswordValue = new BeanWrapperImpl(value).getPropertyValue(confirmPasswordField);

            boolean isValid = passwordValue != null && passwordValue.equals(confirmPasswordValue);

            if (!isValid) {

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(confirmPasswordField)
                        .addConstraintViolation();
            }

            return isValid;
        } catch (Exception e) {
            // Log error: Reflection failed to find fields
            return false;
        }
    }
}