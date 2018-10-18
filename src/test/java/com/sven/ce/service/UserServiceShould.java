package com.sven.ce.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sven.ce.exception.ExclusionUserException;
import com.sven.ce.exception.UserNotFoundException;
import com.sven.ce.model.User;
import com.sven.ce.repository.UserRepository;

public class UserServiceShould
{
    @InjectMocks
    private UserService underTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ExclusionService exclusionService;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ExclusionUserException.class)
    public void should_create_throw_exclusionUserException_for_blocked_user()
    {
        when(exclusionService.validate(anyString(), anyString())).thenReturn(false);

        User uesr = User.builder().dateOfBirth(new Date()).build();
        underTest.create(uesr);

        Fail.fail("should throw ExclusionUserException");
    }

    @Test
    public void should_create_return_created_user_entity_for_nonblocked_user()
    {
        when(exclusionService.validate(anyString(), anyString())).thenReturn(true);

        User request = User.builder().ssn("111111111").dateOfBirth(new Date()).build();

        User expected = User.builder().userId(1l).ssn("111111111").build();
        when(userRepository.save(any(User.class))).thenReturn(expected);

        User actual = underTest.create(request);

        verify(userRepository).save(any(User.class));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void should_findUserByName_return_empty_if_none_was_found()
    {

        Optional<User> expected = Optional.empty();
        when(userRepository.findByUsername(anyString())).thenReturn(expected);

        Optional<User> actual = underTest.findUserByName("username_value");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void should_findUserByName_return_found_user()
    {

        User existingUser = User.builder().userId(1).build();
        Optional<User> expected = Optional.of(existingUser);
        when(userRepository.findByUsername(anyString())).thenReturn(expected);

        Optional<User> actual = underTest.findUserByName("username_value");

        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = UserNotFoundException.class)
    public void should_getUserById_throws_userNotFoundException()
    {
        when(userRepository.findById(any(Long.class))).thenThrow(new UserNotFoundException());

        underTest.getUserById(1l);

        Fail.fail("should throw UserNotFoundException");
    }

    @Test
    public void should_getUserById_return_found_user()
    {

        User actual = User.builder().userId(1l).build();

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(actual));

        User expected = underTest.getUserById(1l);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void should_getAllUsers_return_list_of_found_user()
    {

        User user1 = User.builder().userId(1l).build();
        List<User> expected = Arrays.asList(user1);
        when(userRepository.findAll()).thenReturn(expected);

        List<User> actual = underTest.getAllUsers();
        assertThat(actual).isEqualTo(expected);

    }

}
