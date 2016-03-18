package com.freshspire.api.controller;

import com.freshspire.api.model.User;
import com.freshspire.api.model.params.ApiKeyLoginParams;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * com.freshspire.api.controller
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

    private User makeDummyUser() {
        User user = new User("Name",
                "1234567890",
                "6CKyrO2AIVxFBFOLX4At3g==",
                "RWOYO5KaKB02QMKjLGABxK8vWQzPN7wwAX+KGYgkdSA=",
                "sJ6/Kiw3qs3Z5rkc9Sns4w==",
                new Date(),
                false,
                false);

        return user;
    }

    private UserService makeDummyUserService(User user) {
        UserService userService = mock(UserService.class);

        when(userService.getUserByApiKey(user.getApiKey())).thenReturn(user);

        return userService;
    }

    @Before
    public void setUp() {
        mockUserService = mock(UserService.class);
        keyLoginController = new KeyLoginController();
        keyLoginController.setUserService(mockUserService);
    }

    @Test
    public void validApiKeyShouldLoginUser() {
        // Setup
        ApiKeyLoginParams params = new ApiKeyLoginParams(VALID_API_KEY);
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

    @Test
    public void invalidApiKeyShouldNotLoginUser() {
        // Setup
        ApiKeyLoginParams params = new ApiKeyLoginParams("invalid API key");
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
