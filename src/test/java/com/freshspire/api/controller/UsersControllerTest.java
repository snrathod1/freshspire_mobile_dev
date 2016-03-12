package com.freshspire.api.controller;

import com.authy.api.Verification;
import com.freshspire.api.client.AuthyClient;
import com.freshspire.api.model.ResponseMessage;
import com.freshspire.api.model.User;
import com.freshspire.api.model.params.NewUserParams;
import com.freshspire.api.model.params.PhoneNumberVerificationParams;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.PasswordUtil;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for UsersController
 */
@RunWith(MockitoJUnitRunner.class)
public class UsersControllerTest {

    @Mock private UserService mockUserService;

    @Mock private AuthyClient mockAuthyClient;

    private UsersController usersController;

    private static final String VALID_API_KEY = "apiKey";
    private static final String VALID_USER_ID = "1";
    private static final String VALID_FIRST_NAME = "FirstName";
    private static final String VALID_PASSWORD = "A valid password";
    private static final String VALID_SALT = "salt";
    private static final String VALID_PHONE_NUMBER = "1234567890";
    private static final String VALID_VALIDATION_CODE = "0000";

    @Before
    public void setUp() {
        usersController = new UsersController();
        usersController.setUserService(mockUserService);
        usersController.setAuthyClient(mockAuthyClient);
    }

    /**
     * Tests step 1 of the create user process (GET /users/create with phone number param).
     * Tests behavior and response for a valid phone input.
     * @throws Exception
     */
    @Test
    public void testVerificationWithValidPhone() throws Exception {
        // Test: valid phone number supplied
        final String VALID_PHONE_NUMBER = "1234567890";
        // First create the expected ResponseEntity
        String expectedBody = ResponseUtil.asJsonString(
                new ResponseMessage("ok", "Authentication code sent to " + VALID_PHONE_NUMBER),
                ResponseMessage.class);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.OK)
                .body(expectedBody);

        // Set up mock responses for authy client
        Verification mockVerification = mock(Verification.class);
        // startAuthentication returns mockVerification
        when(mockAuthyClient.startAuthentication(VALID_PHONE_NUMBER)).thenReturn(mockVerification);
        // mockVerification is valid because we're using VALID_PHONE_NUMBER
        when(mockVerification.isOk()).thenReturn(true);

        // Exercise method with valid phone number
        ResponseEntity actual = usersController.getNewUserVerificationCode(VALID_PHONE_NUMBER);

        // Verify that authy API was correctly called, HTTP status is correct, and body is correct
        verify(mockAuthyClient, times(1)).startAuthentication(VALID_PHONE_NUMBER);
        verify(mockVerification, times(1)).isOk();

        assertEquals("valid phone number should return 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response message is incorrect",
                expectedBody, actual.getBody());

        // Check that user service wasn't touched, and authy client wasn't called
        // other than startAuthentication
        verifyZeroInteractions(mockUserService);
        verifyNoMoreInteractions(mockAuthyClient);
    }

    /**
     * Tests step 1 of the create user process (GET /users/create with phone number param).
     * Tests behavior and response for an empty phone number input.
     * @throws Exception
     */
    @Test
    public void testVerificationCodeWithEmptyPhone() throws Exception {
        // Test: empty phone number supplied
        final String EMPTY_PHONE_NUMBER = "";
        // First create the expected ResponseEntity
        String expectedBody = ResponseUtil.asJsonString(
                new ResponseMessage("error", "Phone number is empty"),
                ResponseMessage.class);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(expectedBody);

        // Exercise method
        ResponseEntity actual = usersController.getNewUserVerificationCode(EMPTY_PHONE_NUMBER);

        // Make sure authy API not called, HTTP status is correct, and body is correct
        verifyZeroInteractions(mockAuthyClient);
        assertEquals("HTTP status should be 400 Bad Request",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response message is incorrect",
                expectedBody, actual.getBody());

        // Check that user service wasn't touched, and authy client wasn't called
        // other than startAuthentication
        verifyZeroInteractions(mockUserService);
        verifyNoMoreInteractions(mockAuthyClient);
    }

    /**
     * Tests step 1 of the create user process (GET /users/create with phone number param).
     * Tests behavior and response for an invalid phone input.
     * @throws Exception
     */
    @Test
    public void testVerificationCodeWithInvalidPhone() throws Exception {
        // Test: invalid phone number
        final String INVALID_PHONE_NUMBER = "this is an invalid phone number";
        // First, set up expected ResponseEntity
        String expectedBody = ResponseUtil.asJsonString(
                new ResponseMessage("error", "Could not send authentication code. Is phone number formatted correctly?"),
                ResponseMessage.class);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(expectedBody);
        // Set up mock authy responses
        Verification mockVerification = mock(Verification.class);
        when(mockAuthyClient.startAuthentication(INVALID_PHONE_NUMBER)).thenReturn(mockVerification);
        when(mockVerification.isOk()).thenReturn(false);

        // Exercise method
        ResponseEntity actual = usersController.getNewUserVerificationCode(INVALID_PHONE_NUMBER);

        // Make sure authy API called, HTTP status is correct, and body is correct
        verify(mockAuthyClient).startAuthentication(INVALID_PHONE_NUMBER);
        assertEquals("HTTP status code should be 400",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expectedBody, actual.getBody());

        // Check that user service wasn't touched, and authy client wasn't called
        // other than startAuthentication
        verifyZeroInteractions(mockUserService);
        verifyNoMoreInteractions(mockAuthyClient);
    }

    /**
     * Tests step 2 of the create users process (POST /users/create with new user parameters).
     * Tests behavior and response for an empty first name or empty password parameter.
     * @throws Exception
     */
    @Test
    public void testCreateUserEmptyParameters() throws Exception {
        // Set up empty params to send to method
        NewUserParams emptyFirstNameParams = new NewUserParams(
                "", // This is invalid
                VALID_PHONE_NUMBER,
                VALID_PASSWORD,
                VALID_VALIDATION_CODE);
        NewUserParams emptyPasswordParams = new NewUserParams(
                VALID_FIRST_NAME,
                VALID_PHONE_NUMBER,
                "", // This is invalid (empty password)
                VALID_VALIDATION_CODE);

        // Set up mock authy client behavior (should never be called since there are empty params)
        Verification mockAuthentication = mock(Verification.class);
        when(mockAuthyClient.checkAuthentication(VALID_PHONE_NUMBER, VALID_VALIDATION_CODE))
                .thenReturn(mockAuthentication);
        when(mockAuthentication.isOk()).thenReturn(true);

        // Expected response
        String expectedBody = ResponseUtil.asJsonString(
                new ResponseMessage("error", "Invalid request parameters"),
                ResponseMessage.class);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(expectedBody);

        // Actual response
        ResponseEntity emptyFirstNameActual = usersController.createUser(emptyFirstNameParams);
        ResponseEntity emptyPasswordActual = usersController.createUser(emptyPasswordParams);

        // Verify authy was not called (should avoid calling authy until all params are good)
        verifyZeroInteractions(mockAuthyClient);
        assertEquals("HTTP response status should be 400",
                expected.getStatusCode(), emptyFirstNameActual.getStatusCode());
        assertEquals("Response body incorrect",
                expectedBody, emptyFirstNameActual.getBody());
        assertEquals("HTTP response status should be 400",
                expected.getStatusCode(), emptyPasswordActual.getStatusCode());
        assertEquals("Response body incorrect",
                expectedBody, emptyPasswordActual.getBody());

    }

    @Test
    public void testSendCodeForForgotPasswordWithInvalidPhone() throws Exception {
        // Explanation of what is being tested here:
        // Forgot password shouldn't return whether or not there's an account for a given phone number.
        // Otherwise, it'd basically be doesAccountExistForPhoneNum(xxxx)
        // So, this test checks that the 200 response is sent and that authy isn't called to send a text.

        final String INVALID_PHONE = "000foo000";

        // Set up mock behavior
        when(mockUserService.userExistsWithPhoneNumber(INVALID_PHONE)).thenReturn(false);

        // Expected response
        ResponseEntity expected = ResponseUtil.ok("Verification code sent to "
                + INVALID_PHONE
                + " if account exists with that number");

        // Actual response
        ResponseEntity actual = usersController.sendCodeForForgotPassword(INVALID_PHONE);

        // Verify that userService was called, authy wasn't called, and HTTP response is correct
        verify(mockUserService).userExistsWithPhoneNumber(INVALID_PHONE);
        verifyZeroInteractions(mockAuthyClient);
        assertEquals("HTTP status code should be 200",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    @Test
    public void validPhoneShouldSendForgotPasswordAuthCode() throws Exception {
        // Set up mock behavior
        User mockUser = mock(User.class);
        when(mockUserService.userExistsWithPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(true);

        // Expected response
        ResponseEntity expected = ResponseUtil.ok("Verification code sent to "
                + VALID_PHONE_NUMBER
                + " if account exists with that number");

        // Actual
        ResponseEntity actual = usersController.sendCodeForForgotPassword(VALID_PHONE_NUMBER);

        // Verify user service and authy called appropriately, and HTTP response is correct
        verify(mockUserService).userExistsWithPhoneNumber(VALID_PHONE_NUMBER);
        verify(mockAuthyClient).startAuthentication(VALID_PHONE_NUMBER);
        assertEquals("Status code should be 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests POST /users/forgot-password
     * with valid auth code, phone number, and password.
     * @throws Exception
     */
    @Test
    public void correctAuthCodeShouldUpdateForgottenPassword() throws Exception {
        // Set up parameters
        PhoneNumberVerificationParams validParams = new PhoneNumberVerificationParams(
                VALID_PHONE_NUMBER,
                VALID_VALIDATION_CODE,
                "newPassword"
        );
        // Set up mock responses
        Verification mockVerification = mock(Verification.class);
        User mockUserFromDatabase = mock(User.class);

        //(VALID_FIRST_NAME, VALID_PHONE_NUMBER, "apiKey", VALID_PASSWORD, "salt", new Date(), false, false);

        when(mockUserService.getUserByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(mockUserFromDatabase);
        when(mockAuthyClient.checkAuthentication(VALID_PHONE_NUMBER, VALID_VALIDATION_CODE))
                .thenReturn(mockVerification);
        when(mockVerification.isOk()).thenReturn(true);
        // Mock user responses
        when(mockUserFromDatabase.getUserId()).thenReturn(VALID_USER_ID);
        when(mockUserFromDatabase.getFirstName()).thenReturn(VALID_FIRST_NAME);
        when(mockUserFromDatabase.getPhoneNumber()).thenReturn(VALID_PHONE_NUMBER);
        when(mockUserFromDatabase.getApiKey()).thenReturn(VALID_API_KEY);
        when(mockUserFromDatabase.getPassword()).thenReturn(VALID_PASSWORD);
        when(mockUserFromDatabase.getSalt()).thenReturn(VALID_SALT);
        when(mockUserFromDatabase.getCreated()).thenReturn(new Date(0));
        when(mockUserFromDatabase.isAdmin()).thenReturn(false);
        when(mockUserFromDatabase.isBanned()).thenReturn(false);

        // Expected response body is the updated user object
        JsonObject body = new JsonObject();
        body.addProperty("userId", VALID_USER_ID);
        body.addProperty("apiKey", VALID_API_KEY);
        body.addProperty("firstName", VALID_FIRST_NAME);
        body.addProperty("phoneNumber", VALID_PHONE_NUMBER);

        // Expected
        ResponseEntity expected = ResponseUtil.ok(new Gson().toJson(body));

        // Actual
        ResponseEntity actual = usersController.verifyCodeForForgotPassword(validParams);

        // Verify authy is called once, userService is called, mock user is updated with
        // new password, and HTTP response is correct
        verify(mockAuthyClient).checkAuthentication(VALID_PHONE_NUMBER, VALID_VALIDATION_CODE);
        verify(mockUserFromDatabase).setPassword(
                PasswordUtil.encryptString("newPassword", VALID_SALT));
        verify(mockUserService).updateUser(mockUserFromDatabase);
        // Verify no other setters called
        verify(mockUserFromDatabase, never()).setUserId(anyString());
        verify(mockUserFromDatabase, never()).setFirstName(anyString());
        verify(mockUserFromDatabase, never()).setPhoneNumber(anyString());
        verify(mockUserFromDatabase, never()).setApiKey(anyString());
        verify(mockUserFromDatabase, never()).setSalt(anyString());
        verify(mockUserFromDatabase, never()).setCreated(any(Date.class));
        verify(mockUserFromDatabase, never()).setAdmin(anyBoolean());
        verify(mockUserFromDatabase, never()).setBanned(anyBoolean());
        assertEquals("Status code should be 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    @Test
    public void testVerifyCodeForForgotPasswordWithInvalidAuthentication() throws Exception {

    }

    @Test
    public void testVerifyCodeForForgotPasswordWithEmptyPassword() throws Exception {

    }

    @Test
    public void testResetPassword() throws Exception {

    }
}
