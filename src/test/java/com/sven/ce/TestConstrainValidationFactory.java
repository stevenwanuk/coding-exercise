package com.sven.ce;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintValidator;

import org.springframework.web.bind.support.SpringWebConstraintValidatorFactory;
import org.springframework.web.context.WebApplicationContext;

/**
 * to allow to test with mocked {@code ConstrainValidation}
 */
public class TestConstrainValidationFactory extends SpringWebConstraintValidatorFactory
{

    private final WebApplicationContext ctx;
    private final Map<Class<?>, ConstraintValidator<?, ?>> mockedConstrainValidation = new HashMap<>();

    public TestConstrainValidationFactory(WebApplicationContext ctx)
    {
        this.ctx = ctx;
    }

    /**
     * register a mock instance of {@code ConstrainValidation}
     * 
     * @param constraintValidatorClass must be not null
     * @param mockInstance must be not null
     */
    public void register(Class<?> constraintValidatorClass, ConstraintValidator<?, ?> mockInstance)
    {

        mockedConstrainValidation.put(constraintValidatorClass, mockInstance);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key)
    {
        ConstraintValidator<?, ?> instance = null;
        if (mockedConstrainValidation.containsKey(key))
        {
            instance = mockedConstrainValidation.get(key);
        }
        else
        {
            instance = super.getInstance(key);
        }
        return (T) instance;
    }

    @Override
    protected WebApplicationContext getWebApplicationContext()
    {
        return ctx;
    }

}
