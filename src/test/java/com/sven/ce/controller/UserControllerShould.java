package com.sven.ce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;

import javax.validation.ConstraintValidatorContext;

import org.hamcrest.Matchers;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.support.GenericWebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sven.ce.TestConstants;
import com.sven.ce.TestConstrainValidationFactory;
import com.sven.ce.component.ApplicationExceptionResolver;
import com.sven.ce.controller.model.RegisterUserRequest;
import com.sven.ce.exception.UserNotFoundException;
import com.sven.ce.model.User;
import com.sven.ce.service.UserService;
import com.sven.ce.util.Constants;
import com.sven.ce.validator.UniqueUserNameValidator;

public class UserControllerShould
{

    private MockMvc mvc;

    @InjectMocks
    private UserController underTest;

    @Mock
    private UserService userService;

    @Mock
    private UniqueUserNameValidator uniqueUserNameValidator;
    @Mock
    private MockServletContext servletContext;
    @Mock
    private Enumeration<String> mockEnumeration;

    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.DOB_DATE_FORMAT_PATTERN);
    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup()
    {
        // using "utc" timezone as date format
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        MockitoAnnotations.initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(underTest)
                        .setControllerAdvice(new ApplicationExceptionResolver())
                        .setValidator(buildValidator())
                        .build();
    }

    private Validator buildValidator()
    {
        // setup UniqueUserNameValidator
        when(servletContext.getInitParameterNames()).thenReturn(mockEnumeration);
        when(servletContext.getAttributeNames()).thenReturn(mockEnumeration);
        final GenericWebApplicationContext context = new GenericWebApplicationContext(servletContext);
        context.refresh();
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        TestConstrainValidationFactory constraintFactory = new TestConstrainValidationFactory(context);
        constraintFactory.register(UniqueUserNameValidator.class, uniqueUserNameValidator);
        validatorFactoryBean.setConstraintValidatorFactory(constraintFactory);
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        validatorFactoryBean.afterPropertiesSet();

        return validatorFactoryBean;
    }

    @Test
    public void should_register_non_existing_user() throws Exception
    {
        when(uniqueUserNameValidator.isValid(anyString(), any(ConstraintValidatorContext.class))).thenReturn(true);

        RegisterUserRequest request = RegisterUserRequest.builder()
                        .username(TestConstants.USER_NAME_VALID)
                        .password(TestConstants.PASSWORD_VALID)
                        .dateOfBirth(new Date())
                        .ssn(TestConstants.SSN_VALID)
                        .build();

        User expected = User.builder().userId(1).build();
        when(userService.create(any(User.class))).thenReturn(expected);

        mvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(mapper.writeValueAsString(request)))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andDo(print())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$.userId", Matchers.is(((Long) expected.getUserId()).intValue())));

        verify(userService).create(any(User.class));
    }

    @Test
    public void should_return_bad_request_for_empty_username() throws Exception
    {
        when(uniqueUserNameValidator.isValid(anyString(), any(ConstraintValidatorContext.class))).thenReturn(true);

        RegisterUserRequest request = RegisterUserRequest.builder()
                        .username(TestConstants.USER_NAME_EMPTY)
                        .password(TestConstants.PASSWORD_VALID)
                        .dateOfBirth(new Date())
                        .ssn(TestConstants.SSN_VALID)
                        .build();

        User expected = User.builder().userId(1).build();
        when(userService.create(any(User.class))).thenReturn(expected);

        mvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(mapper.writeValueAsString(request)))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(userService, times(0)).create(any(User.class));
    }

    @Test
    public void should_return_bad_request_for_username_with_non_alphanumerical_char() throws Exception
    {
        when(uniqueUserNameValidator.isValid(anyString(), any(ConstraintValidatorContext.class))).thenReturn(true);

        RegisterUserRequest request = RegisterUserRequest.builder()
                        .username(TestConstants.USER_NAME_WITH_NON_AlPHANUMERICAL_CHAR)
                        .password(TestConstants.PASSWORD_VALID)
                        .dateOfBirth(new Date())
                        .ssn(TestConstants.SSN_VALID)
                        .build();

        User expected = User.builder().userId(1).build();
        when(userService.create(any(User.class))).thenReturn(expected);

        mvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(mapper.writeValueAsString(request)))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(userService, times(0)).create(any(User.class));
    }

    @Test
    public void should_return_bad_request_for_username_with_whitespace() throws Exception
    {
        when(uniqueUserNameValidator.isValid(anyString(), any(ConstraintValidatorContext.class))).thenReturn(true);

        RegisterUserRequest request = RegisterUserRequest.builder()
                        .username(TestConstants.USER_NAME_WITH_WHITESPACE)
                        .password(TestConstants.PASSWORD_VALID)
                        .dateOfBirth(new Date())
                        .ssn(TestConstants.SSN_VALID)
                        .build();

        User expected = User.builder().userId(1).build();
        when(userService.create(any(User.class))).thenReturn(expected);

        mvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(mapper.writeValueAsString(request)))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(userService, times(0)).create(any(User.class));
    }

    /*
     * TODO more tests of other negative scenarios for password / dob / ssn
     */

    @Test
    public void should_get_user_by_id_return_found_if_user_exists() throws Exception
    {

        User expected = User.builder().userId(1).build();
        when(userService.getUserById(any(Long.class))).thenReturn(expected);

        mvc.perform(get("/users/1"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(print())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$.userId", Matchers.is(((Long) expected.getUserId()).intValue())));

        verify(userService).getUserById(any(Long.class));
    }

    @Test
    public void should_get_user_by_id_return_not_found_if_user_not_exists() throws Exception
    {

        when(userService.getUserById(any(Long.class))).thenThrow(new UserNotFoundException());

        mvc.perform(get("/users/1")).andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(userService).getUserById(any(Long.class));
    }

    @Test
    public void should_get_all_users_return_users_list_if_one_was_found() throws Exception
    {

        User user1 = User.builder().userId(1).build();
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1));

        mvc.perform(get("/users/"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(print())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$", Matchers.hasSize(1)))
                        .andExpect(jsonPath("$[0].userId", Matchers.is(((Long) user1.getUserId()).intValue())));

        verify(userService).getAllUsers();
    }

    @Test
    public void should_get_all_users_return_empty_list_if_non_was_found() throws Exception
    {

        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mvc.perform(get("/users/"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(print())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$", Matchers.hasSize(0)));

        verify(userService).getAllUsers();
    }
}
