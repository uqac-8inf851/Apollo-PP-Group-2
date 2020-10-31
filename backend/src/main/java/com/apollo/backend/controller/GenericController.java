package com.apollo.backend.controller;

import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class GenericController {

    protected String[] validRequest(Object object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Object>> violations = validator.validate(object);

        ArrayList<String> messages = new ArrayList<String>();

        if(!violations.isEmpty()) {
            for (ConstraintViolation<?> violation : violations) {
                messages.add(violation.getMessage());
            }
        }

        return messages.toArray(new String[0]);
    }
}