package com.freshspire.api.controller;

import com.freshspire.api.TestConstants;
import com.freshspire.api.model.response.ResponseMessage;
import com.freshspire.api.model.User;
import com.freshspire.api.model.param.LoginParams;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.PasswordUtil;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    @Mock private UserService mockUserService;

    private LoginController loginController;



    @Before
    public void setUp() {
        loginController = new LoginController();
        loginController.setUserService(mockUserService);
    }

    @Test
    public void invalidPhoneShouldNotLogin() throws Exception {
        // Set up parameters and mock behavior
        LoginParams params = new LoginParams("invalid phone", TestConstants.VALID_PASSWORD);
        when(mockUserService.getUserByPhoneNumber("invalid phone")).thenReturn(null);

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "Phone number/password pair is invalid"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = loginController.loginWithPhoneAndPassword(params);

        // Verify user service called, HTTP response is correct
        verify(mockUserService).getUserByPhoneNumber("invalid phone");
        verifyNoMoreInteractions(mockUserService);
        assertEquals("HTTP status code is incorrect",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    @Test
    public void invalidPasswordShouldNotLogin() throws Exception {
        // Set up parameters and mock behavior
        LoginParams params = new LoginParams(
                TestConstants.VALID_PHONE_NUMBER,
                "invalid password");
        User user = new User(TestConstants.VALID_FIRST_NAME,
                TestConstants.VALID_PHONE_NUMBER,
                TestConstants.VALID_API_KEY,
                TestConstants.VALID_PASSWORD,
                TestConstants.VALID_SALT,
                TestConstants.VALID_DATE,
                TestConstants.VALID_ADMIN,
                TestConstants.VALID_BANNED,
                TestConstants.VALID_ENABLED_LOCATION);
        when(mockUserService.getUserByPhoneNumber(TestConstants.VALID_PHONE_NUMBER)).thenReturn(user);

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "Phone number/password pair is invalid"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = loginController.loginWithPhoneAndPassword(params);

        // Verify user service called, HTTP response is correct
        verify(mockUserService).getUserByPhoneNumber(TestConstants.VALID_PHONE_NUMBER);
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    @Test
    public void emptyPhoneOrPasswordShouldNotLogin() throws Exception {
        // Set up parameters and mock behavior
        LoginParams emptyPassword = new LoginParams(TestConstants.VALID_PHONE_NUMBER, "");
        LoginParams emptyPhone = new LoginParams("", TestConstants.VALID_PASSWORD);
        User user = new User(TestConstants.VALID_FIRST_NAME,
                TestConstants.VALID_PHONE_NUMBER,
                TestConstants.VALID_API_KEY,
                TestConstants.VALID_PASSWORD,
                TestConstants.VALID_SALT,
                TestConstants.VALID_DATE,
                TestConstants.VALID_ADMIN,
                TestConstants.VALID_BANNED,
                TestConstants.VALID_ENABLED_LOCATION);
        when(mockUserService.getUserByPhoneNumber(TestConstants.VALID_PHONE_NUMBER)).thenReturn(user);
        when(mockUserService.getUserByPhoneNumber("")).thenReturn(null);

        // Expected
        ResponseEntity emptyPasswordExpected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "Password cannot be empty"), ResponseMessage.class));
        ResponseEntity emptyPhoneExpected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "Phone cannot be empty"), ResponseMessage.class));

        // Actual
        ResponseEntity emptyPasswordActual = loginController.loginWithPhoneAndPassword(emptyPassword);
        ResponseEntity emptyPhoneActual = loginController.loginWithPhoneAndPassword(emptyPhone);

        // Verify user service never called, HTTP response correct
        verifyZeroInteractions(mockUserService);
        assertEquals("HTTP status code should be 401 Unauthorized",
                emptyPasswordExpected.getStatusCode(), emptyPasswordActual.getStatusCode());
        assertEquals("HTTP status code should be 401 Unauthorized",
                emptyPhoneExpected.getStatusCode(), emptyPhoneActual.getStatusCode());
        assertEquals("Response body is incorrect",
                emptyPasswordExpected.getBody(), emptyPasswordActual.getBody());
        assertEquals("Response body is incorrect",
                emptyPhoneExpected.getBody(), emptyPhoneActual.getBody());
    }

    @Test
    public void validParamsShouldLogin() throws Exception {
        // Set up parameters and mock behavior
        LoginParams params = new LoginParams(TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_PASSWORD);
        User user = new User(TestConstants.VALID_FIRST_NAME,
                TestConstants.VALID_PHONE_NUMBER,
                TestConstants.VALID_API_KEY,
                TestConstants.VALID_PASSWORD,
                TestConstants.VALID_SALT,
                TestConstants.VALID_DATE,
                TestConstants.VALID_ADMIN,
                TestConstants.VALID_BANNED,
                TestConstants.VALID_ENABLED_LOCATION);
        user.setPassword(PasswordUtil.encryptString(TestConstants.VALID_PASSWORD, TestConstants.VALID_SALT));
        user.setUserId(TestConstants.VALID_USER_ID);
        when(mockUserService.getUserByPhoneNumber(TestConstants.VALID_PHONE_NUMBER)).thenReturn(user);

        // Expected
        JsonObject userJson = new JsonObject();
        userJson.addProperty("apiKey", TestConstants.VALID_API_KEY);
        userJson.addProperty("enabledLocation", TestConstants.VALID_ENABLED_LOCATION);
        userJson.addProperty("firstName", TestConstants.VALID_FIRST_NAME);
        userJson.addProperty("phoneNumber", TestConstants.VALID_PHONE_NUMBER);
        userJson.addProperty("userId", TestConstants.VALID_USER_ID);

        ResponseEntity expected = ResponseEntity.status(HttpStatus.OK).body(userJson.toString());

        // Actual
        ResponseEntity actual = loginController.loginWithPhoneAndPassword(params);

        // Verify user service called, HTTP response is correct
        verify(mockUserService).getUserByPhoneNumber(TestConstants.VALID_PHONE_NUMBER);
        verifyNoMoreInteractions(mockUserService);
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }
}