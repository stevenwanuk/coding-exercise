package com.sven.ce.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sven.ce.model.User;
import com.sven.ce.service.UserService;

public class UniqueUserNameValidatorShould
{
    @InjectMocks
    private UniqueUserNameValidator underTest;

    @Mock
    private UserService userService;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_isValid_return_false_for_exsting_user()
    {

        boolean expected = false;

        User existingUser = User.builder().userId(1l).username("username_value").build();
        when(userService.findUserByName(anyString())).thenReturn(Optional.of(existingUser));

        boolean actual = underTest.isValid(existingUser.getUsername(), null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void should_isValid_return_true_for_non_exsting_user()
    {

        boolean expected = true;
        when(userService.findUserByName(anyString())).thenReturn(Optional.empty());

        boolean actual = underTest.isValid("username_value", null);
        assertThat(actual).isEqualTo(expected);
    }

}
