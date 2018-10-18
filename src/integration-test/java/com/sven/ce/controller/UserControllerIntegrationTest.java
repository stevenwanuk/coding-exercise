package com.sven.ce.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sven.ce.CodingExerciseApplication;
import com.sven.ce.TestConstants;
import com.sven.ce.controller.model.RegisterUserRequest;
import com.sven.ce.model.User;

/**
 * integration test for {@code UserController}.
 * <p>
 * DB data are loaded from import.xml
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CodingExerciseApplication.class)
@Transactional
public class UserControllerIntegrationTest
{
    private final static User EXISTING_USER = User.builder().username("usera").userId(1l).build();

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new TestRestTemplate().getRestTemplate();

    @Test
    public void should_get_all_users_return_a_list_of_1_valid_user() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(MediaType.ALL));

        String baseUrl = "http://localhost:" + port;

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        URI endpoint = new URI(baseUrl + "/users");
        ParameterizedTypeReference<List<User>> typeReference = new ParameterizedTypeReference<List<User>>()
        {
        };
        ResponseEntity<List<User>> response = restTemplate.exchange(endpoint, HttpMethod.GET, request, typeReference);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0).getUsername()).isEqualTo(EXISTING_USER.getUsername());
    }

    @Test
    public void should_get_user_by_id_return_existing_user() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(MediaType.ALL));

        String baseUrl = "http://localhost:" + port;

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        URI endpoint = new URI(baseUrl + "/users/" + EXISTING_USER.getUserId());
        ParameterizedTypeReference<User> typeReference = new ParameterizedTypeReference<User>()
        {
        };
        ResponseEntity<User> response = restTemplate.exchange(endpoint, HttpMethod.GET, request, typeReference);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUserId()).isEqualTo(EXISTING_USER.getUserId());
        assertThat(response.getBody().getUsername()).isEqualTo(EXISTING_USER.getUsername());
    }

    @Test
    public void should_get_user_by_id_return_not_found_for_non_existing_user_id() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(MediaType.ALL));

        String baseUrl = "http://localhost:" + port;

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(null, headers);
        URI endpoint = new URI(baseUrl + "/users/" + 2);
        ParameterizedTypeReference<String> typeReference = new ParameterizedTypeReference<String>()
        {
        };
        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.GET, request, typeReference);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void should_post_register_should_return_created_user_for_valid_request() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(MediaType.ALL));

        String baseUrl = "http://localhost:" + port;

        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                        .username(TestConstants.USER_NAME_VALID)
                        .password(TestConstants.PASSWORD_VALID)
                        .dateOfBirth(new Date())
                        .ssn(TestConstants.SSN_VALID)
                        .build();

        HttpEntity<RegisterUserRequest> request = new HttpEntity<>(registerUserRequest, headers);
        URI endpoint = new URI(baseUrl + "/users/register");
        ParameterizedTypeReference<User> typeReference = new ParameterizedTypeReference<User>()
        {
        };
        ResponseEntity<User> response = restTemplate.exchange(endpoint, HttpMethod.POST, request, typeReference);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualToIgnoringGivenFields(registerUserRequest, "userId");
    }
    
    @Test
    public void should_post_register_should_return_bad_request_for_empty_username() throws Exception
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(MediaType.ALL));

        String baseUrl = "http://localhost:" + port;

        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                        .username(TestConstants.USER_NAME_EMPTY)
                        .password(TestConstants.PASSWORD_VALID)
                        .dateOfBirth(new Date())
                        .ssn(TestConstants.SSN_VALID)
                        .build();

        HttpEntity<RegisterUserRequest> request = new HttpEntity<>(registerUserRequest, headers);
        URI endpoint = new URI(baseUrl + "/users/register");
        ParameterizedTypeReference<String> typeReference = new ParameterizedTypeReference<String>()
        {
        };
        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, request, typeReference);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    /*
     * TODO more tests of other negative scenarios for password / dob / ssn
     */
}
