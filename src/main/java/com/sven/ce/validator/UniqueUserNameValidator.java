package com.sven.ce.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.sven.ce.annotation.UniqueUserName;
import com.sven.ce.service.UserService;

/**
 * Validate given UserName if it has been used
 * 
 * @see UniqueUserName
 */

@Component
public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String>
{

    private UserService userService;

    public UniqueUserNameValidator(UserService userService)
    {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context)
    {
        return !userService.findUserByName(userName).isPresent();
    }

}
