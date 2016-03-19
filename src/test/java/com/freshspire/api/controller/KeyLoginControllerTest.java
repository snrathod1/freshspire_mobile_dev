package com.freshspire.api.controller;

import com.freshspire.api.model.ResponseMessage;
import com.freshspire.api.model.User;
import com.freshspire.api.model.params.ApiKeyParams;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests the login with API key endpoint and methods.
 *
 * @created 2/29/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class KeyLoginControllerTest {

    private static final String VALID_API_KEY = "a valid API key";
    private static final String VALID_FIRST_NAME = "FirstName";
    private static final String VALID_PHONE_NUMBER = "1234567890";
    private static final String VALID_PASSWORD = "password";
    private static final String VALID_SALT = "salt";

    private UserService mockUserService;

    private KeyLoginController keyLoginController;

    private Gson gson = new Gson();

    /**
     * Sets up key login controller with mocked dependencies
     */
    @Before
    public void setUp() {
        mockUserService = mock(UserService.class);
        keyLoginController = new KeyLoginController();
        keyLoginController.setUserService(mockUserService);
    }

    /**
     * Tests POST /users/key-login
     * with valid API key parameter
     */
    @Test
    public void validApiKeyShouldLoginUser() {
        // Setup
        ApiKeyParams params = new ApiKeyParams(VALID_API_KEY);
        User user = new User(VALID_FIRST_NAME, VALID_PHONE_NUMBER, VALID_API_KEY,
                VALID_PASSWORD, VALID_SALT, new Date(0), false, false);
        when(mockUserService.getUserByApiKey(VALID_API_KEY)).thenReturn(user);

        // Expected
        ResponseEntity expected = ResponseUtil.makeUserObjectResponse(user, HttpStatus.OK); // TODO lose the ResponseUtil dependency

        // Actual
        ResponseEntity actual = keyLoginController.loginWithApiKey(params);

        // Verify that user service was called, HTTP response is correct
        verify(mockUserService).getUserByApiKey(VALID_API_KEY);
        assertEquals("HTTP status code should be 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());

    }

    /**
     * Tests POST /users/key-login
     * with empty API key param
     */
    @Test
    public void emptyApiKeyShouldNotLoginUser() {
        // Setup
        ApiKeyParams params = new ApiKeyParams("");

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "API key cannot be empty"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = keyLoginController.loginWithApiKey(params);

        // Verify user service not called, HTTP response correct
        verifyZeroInteractions(mockUserService);
        assertEquals("HTTP status code is incorrect",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests POST /users/key-login
     * with invalid API key param
     */
    @Test
    public void invalidApiKeyShouldNotLoginUser() {
        // Setup
        ApiKeyParams params = new ApiKeyParams("invalid API key");
        User user = new User(VALID_FIRST_NAME, VALID_PHONE_NUMBER, VALID_API_KEY,
                VALID_PASSWORD, VALID_SALT, new Date(0), false, false);
        when(mockUserService.getUserByApiKey("invalid API key")).thenReturn(null);

        // Expected
        ResponseEntity expected = ResponseUtil.unauthorized("Invalid API key"); // TODO lose the ResponseUtil dependency

        // Actual
        ResponseEntity actual = keyLoginController.loginWithApiKey(params);

        // Verify user service called, HTTP response is correct
        verify(mockUserService).getUserByApiKey("invalid API key");
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }
}
