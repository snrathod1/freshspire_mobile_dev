package com.freshspire.api.controller;

import com.authy.api.Verification;
import com.freshspire.api.TestConstants;
import com.freshspire.api.client.AuthyClient;
import com.freshspire.api.model.ResponseMessage;
import com.freshspire.api.model.User;
import com.freshspire.api.model.params.*;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.PasswordUtil;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for UserController
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock private UserService mockUserService;

    @Mock private AuthyClient mockAuthyClient;

    private Gson gson;

    private UserController userController;

    /**
     * Sets up UserController with mocked dependencies.
     */
    @Before
    public void setUp() {
        gson = new Gson();

        userController = new UserController();
        userController.setUserService(mockUserService);
        userController.setAuthyClient(mockAuthyClient);
    }

    /**
     * Tests GET /users/create
     * with a valid phone number
     * @throws Exception
     */
    @Test
    public void validPhoneShouldSendNewUserAuthCode() throws Exception {
        // Test: valid phone number supplied
        // First create the expected ResponseEntity
        String expectedBody = ResponseUtil.asJsonString(
                new ResponseMessage("ok", "Authentication code sent to " + TestConstants.VALID_PHONE_NUMBER),
                ResponseMessage.class);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.OK)
                .body(expectedBody);

        // Set up mock responses for authy client
        Verification mockVerification = mock(Verification.class);
        // startAuthentication returns mockVerification
        when(mockAuthyClient.startAuthentication(TestConstants.VALID_PHONE_NUMBER)).thenReturn(mockVerification);
        // mockVerification is valid because we're using VALID_PHONE_NUMBER
        when(mockVerification.isOk()).thenReturn(true);

        // Exercise method with valid phone number
        ResponseEntity actual = userController.getNewUserAuthCode(TestConstants.VALID_PHONE_NUMBER);

        // Verify that authy API was correctly called, HTTP status is correct, and body is correct
        verify(mockAuthyClient, times(1)).startAuthentication(TestConstants.VALID_PHONE_NUMBER);
        verify(mockVerification, times(1)).isOk();

        assertEquals("valid phone number should return 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response message is incorrect",
                expectedBody, actual.getBody());

        // Check that user service wasn't touched, and authy client wasn't called
        // other than startAuthentication
        verify(mockUserService).getUserByPhoneNumber(TestConstants.VALID_PHONE_NUMBER);
        verifyNoMoreInteractions(mockUserService);
        verifyNoMoreInteractions(mockAuthyClient);
    }

    /**
     * Tests GET /users/create
     * with an empty phone number
     * @throws Exception
     */
    @Test
    public void emptyPhoneShouldNotSendNewUserAuthCode() throws Exception {
        // Test: empty phone number supplied
        final String EMPTY_PHONE_NUMBER = "";
        // First create the expected ResponseEntity
        String expectedBody = ResponseUtil.asJsonString(
                new ResponseMessage("error", "Phone number is empty"),
                ResponseMessage.class);
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(expectedBody);

        // Exercise method
        ResponseEntity actual = userController.getNewUserAuthCode(EMPTY_PHONE_NUMBER);

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
     * Tests GET /users/create
     * with invalid phone number
     * @throws Exception
     */
    @Test
    public void invalidPhoneShouldNotSendNewUserAuthCode() throws Exception {
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
        ResponseEntity actual = userController.getNewUserAuthCode(INVALID_PHONE_NUMBER);

        // Make sure authy API called, HTTP status is correct, and body is correct
        verify(mockAuthyClient).startAuthentication(INVALID_PHONE_NUMBER);
        assertEquals("HTTP status code should be 400",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expectedBody, actual.getBody());

        // Check that user service was called for validating the phone number, and authy
        // client wasn't called other than startAuthentication
        verify(mockUserService).getUserByPhoneNumber(INVALID_PHONE_NUMBER);
        verifyNoMoreInteractions(mockUserService);
        verifyNoMoreInteractions(mockAuthyClient);
    }

    /**
     * Tests GET /users/create
     * with a phone number that already exists for another user. API does not allow multiple accounts per phone number.
     * @throws Exception
     */
    @Test
    public void duplicatePhoneNumberShouldNotSendNewUserAuthCode() throws Exception {
        // Set up parameters and mock verification
        final String DUPLICATE_PHONE = "duplicate phone number";
        NewUserParams params = new NewUserParams(TestConstants.VALID_FIRST_NAME, DUPLICATE_PHONE, TestConstants.VALID_PASSWORD, TestConstants.VALID_AUTHENTICATION_CODE);

        Verification mockVerification = mock(Verification.class);
        User mockUser = mock(User.class);

        when(mockAuthyClient.checkAuthentication(DUPLICATE_PHONE, TestConstants.VALID_AUTHENTICATION_CODE)).thenReturn(mockVerification);
        when(mockVerification.isOk()).thenReturn(true);
        when(mockUserService.getUserByPhoneNumber(DUPLICATE_PHONE)).thenReturn(mockUser);

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "User already exists for phone number " + DUPLICATE_PHONE),
                ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.getNewUserAuthCode(DUPLICATE_PHONE);

        // Verify authy not called, HTTP response correct
        verifyZeroInteractions(mockAuthyClient);
        assertEquals("HTTP response status should be 400",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests POST /users/create
     * with an empty first name and an empty password parameter (tests both situations)
     * @throws Exception
     */
    @Test
    public void emptyParametersShouldNotCreateNewUser() throws Exception {
        // Set up empty params to send to method
        NewUserParams emptyFirstNameParams = new NewUserParams(
                "", // This is invalid
                TestConstants.VALID_PHONE_NUMBER,
                TestConstants.VALID_PASSWORD,
                TestConstants.VALID_AUTHENTICATION_CODE);
        NewUserParams emptyPasswordParams = new NewUserParams(
                TestConstants.VALID_FIRST_NAME,
                TestConstants.VALID_PHONE_NUMBER,
                "", // This is invalid (empty password)
                TestConstants.VALID_AUTHENTICATION_CODE);

        // Set up mock authy client behavior (should never be called since there are empty params)
        Verification mockAuthentication = mock(Verification.class);
        when(mockAuthyClient.checkAuthentication(TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_AUTHENTICATION_CODE))
                .thenReturn(mockAuthentication);
        when(mockAuthentication.isOk()).thenReturn(true);

        // Expected responses
        String emptyFirstNameBody = ResponseUtil.asJsonString(
                new ResponseMessage("error", "First name parameter cannot be empty"),
                ResponseMessage.class);
        ResponseEntity<String> emptyFirstNameExpected = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(emptyFirstNameBody);
        String emptyPasswordBody = ResponseUtil.asJsonString(
                new ResponseMessage("error", "Password parameter cannot be empty"),
                ResponseMessage.class);
        ResponseEntity<String> emptyPasswordExpected = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(emptyPasswordBody);


        // Actual response
        ResponseEntity emptyFirstNameActual = userController.createUser(emptyFirstNameParams);
        ResponseEntity emptyPasswordActual = userController.createUser(emptyPasswordParams);

        // Verify authy was not called (should avoid calling authy until all params are good)
        verifyZeroInteractions(mockAuthyClient);
        assertEquals("HTTP response status should be 400",
                emptyFirstNameExpected.getStatusCode(), emptyFirstNameActual.getStatusCode());
        assertEquals("Response body incorrect",
                emptyFirstNameExpected.getBody(), emptyFirstNameActual.getBody());
        assertEquals("HTTP response status should be 400",
                emptyPasswordExpected.getStatusCode(), emptyPasswordActual.getStatusCode());
        assertEquals("Response body incorrect",
                emptyPasswordExpected.getBody(), emptyPasswordActual.getBody());
    }

    /**
     * Tests POST /users/create
     * with invalid authentication parameters (phone & authy code pair is incorrect)
     * @throws Exception
     */
    @Test
    public void invalidAuthShouldNotCreateNewUser() throws Exception {
        // Set up test params and mock authy/user service objects
        NewUserParams params = new NewUserParams(TestConstants.VALID_FIRST_NAME, TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_PASSWORD, "invalid code");
        Verification mockVerification = mock(Verification.class);
        when(mockAuthyClient.checkAuthentication(TestConstants.VALID_PHONE_NUMBER, "invalid code")).thenReturn(mockVerification);
        when(mockVerification.isOk()).thenReturn(false);

        // Expected
        String expectedBody = ResponseUtil.asJsonString(
                new ResponseMessage("error", "Invalid phone number/authentication code pair"),
                ResponseMessage.class);
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(expectedBody);

        // Actual
        ResponseEntity actual = userController.createUser(params);

        // Verify auth was checked, user service is called, HTTP response is correct
        verify(mockAuthyClient).checkAuthentication(TestConstants.VALID_PHONE_NUMBER, "invalid code");
        verify(mockUserService).getUserByPhoneNumber(TestConstants.VALID_PHONE_NUMBER);
        verifyNoMoreInteractions(mockUserService);
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expectedBody, actual.getBody());
    }

    /**
     * Tests POST /users/create
     * with valid parameters
     * @throws Exception
     */
    @Test
    @Ignore
    public void validParametersShouldCreateNewUser() throws Exception {
        // TODO this test doesn't pass because generated user has random API key and null userId
        // Set up test params and mock authy/user service objects
        NewUserParams params = new NewUserParams(TestConstants.VALID_FIRST_NAME, TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_PASSWORD, TestConstants.VALID_AUTHENTICATION_CODE);
        Verification mockVerification = mock(Verification.class);
        when(mockAuthyClient.checkAuthentication(TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_AUTHENTICATION_CODE)).thenReturn(mockVerification);
        when(mockVerification.isOk()).thenReturn(true);

        // Expected
        JsonObject newUser = new JsonObject();
        newUser.addProperty("apiKey", TestConstants.VALID_API_KEY);
        newUser.addProperty("enabledLocation", TestConstants.VALID_ENABLED_LOCATION);
        newUser.addProperty("firstName", TestConstants.VALID_FIRST_NAME);
        newUser.addProperty("phoneNumber", TestConstants.VALID_PHONE_NUMBER);
        newUser.addProperty("userId", TestConstants.VALID_USER_ID);

        ResponseEntity expected = ResponseEntity.status(HttpStatus.CREATED).body(newUser);

        // Actual
        ResponseEntity actual = userController.createUser(params);

        // Verify that authy was called, user service was called, and HTTP response is correct
        verify(mockAuthyClient).checkAuthentication(TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_AUTHENTICATION_CODE);

        // TODO this doesn't check that user is CORRECTLY added to DB. Look into spy objects for sln.
        verify(mockUserService).addUser(any(User.class));
        assertEquals("HTTP status code should be 201 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
        assertEquals("Content type should be application/json",
                MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
    }

    /**
     * Tests GET /users/forgot-password/{phoneNumber}
     * with invalid phone number
     * @throws Exception
     */
    @Test
    public void invalidPhoneShouldNotSendForgotPasswordCode() throws Exception {
        // Explanation of what is being tested here:
        // Forgot password shouldn't return whether or not there's an account for a given phone number.
        // Otherwise, it'd basically be doesAccountExistForPhoneNum(xxxx) which is insecure.
        // So, this test checks that the 200 response is sent and that authy isn't called to send a text.

        final String INVALID_PHONE = "000foo000";

        // Set up mock behavior
        when(mockUserService.getUserByPhoneNumber(INVALID_PHONE)).thenReturn(null);

        // Expected response
        ResponseEntity expected = ResponseUtil.ok("Authentication code sent to "
                + INVALID_PHONE
                + " if account exists with that number");

        // Actual response
        ResponseEntity actual = userController.sendCodeForForgotPassword(INVALID_PHONE);

        // Verify that userService was called, authy wasn't called, and HTTP response is correct
        verify(mockUserService).getUserByPhoneNumber(INVALID_PHONE);
        verifyZeroInteractions(mockAuthyClient);
        assertEquals("HTTP status code should be 200",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /users/forgot-password/{phoneNumber}
     * with valid phone number
     * @throws Exception
     */
    @Test
    public void validPhoneShouldSendForgotPasswordCode() throws Exception {
        // Set up mock behavior
        User mockUser = mock(User.class);
        when(mockUserService.getUserByPhoneNumber(TestConstants.VALID_PHONE_NUMBER)).thenReturn(mockUser);

        // Expected response
        ResponseEntity expected = ResponseUtil.ok("Authentication code sent to "
                + TestConstants.VALID_PHONE_NUMBER
                + " if account exists with that number");

        // Actual
        ResponseEntity actual = userController.sendCodeForForgotPassword(TestConstants.VALID_PHONE_NUMBER);

        // Verify user service and authy called appropriately, and HTTP response is correct
        verify(mockUserService).getUserByPhoneNumber(TestConstants.VALID_PHONE_NUMBER);
        verify(mockAuthyClient).startAuthentication(TestConstants.VALID_PHONE_NUMBER);
        assertEquals("Status code should be 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests POST /users/forgot-password/{phoneNumber}
     * with valid auth code, phone number, and password.
     * @throws Exception
     */
    @Test
    public void correctAuthShouldUpdateForgottenPassword() throws Exception {
        // Set up parameters
        PhoneNumberAuthenticationParams validParams = new PhoneNumberAuthenticationParams(
                TestConstants.VALID_PHONE_NUMBER,
                TestConstants.VALID_AUTHENTICATION_CODE,
                "newPassword"
        );
        // Set up mock responses
        Verification mockVerification = mock(Verification.class);
        User mockUserFromDatabase = mock(User.class);

        when(mockUserService.getUserByPhoneNumber(TestConstants.VALID_PHONE_NUMBER)).thenReturn(mockUserFromDatabase);
        when(mockAuthyClient.checkAuthentication(TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_AUTHENTICATION_CODE))
                .thenReturn(mockVerification);
        when(mockVerification.isOk()).thenReturn(true);
        // Mock user responses
        when(mockUserFromDatabase.getUserId()).thenReturn(TestConstants.VALID_USER_ID);
        when(mockUserFromDatabase.getFirstName()).thenReturn(TestConstants.VALID_FIRST_NAME);
        when(mockUserFromDatabase.getPhoneNumber()).thenReturn(TestConstants.VALID_PHONE_NUMBER);
        when(mockUserFromDatabase.getApiKey()).thenReturn(TestConstants.VALID_API_KEY);
        when(mockUserFromDatabase.getPassword()).thenReturn(TestConstants.VALID_PASSWORD);
        when(mockUserFromDatabase.getSalt()).thenReturn(TestConstants.VALID_SALT);
        when(mockUserFromDatabase.getCreated()).thenReturn(TestConstants.VALID_DATE);
        when(mockUserFromDatabase.isAdmin()).thenReturn(false);
        when(mockUserFromDatabase.isBanned()).thenReturn(false);

        // Expected response body is the updated user object
        JsonObject body = new JsonObject();
        body.addProperty("apiKey", TestConstants.VALID_API_KEY);
        body.addProperty("enabledLocation", TestConstants.VALID_ENABLED_LOCATION);
        body.addProperty("firstName", TestConstants.VALID_FIRST_NAME);
        body.addProperty("phoneNumber", TestConstants.VALID_PHONE_NUMBER);
        body.addProperty("userId", TestConstants.VALID_USER_ID);

        // Expected
        ResponseEntity expected = ResponseEntity.ok(gson.toJson(body));

        // Actual
        ResponseEntity actual = userController.verifyCodeForForgotPassword(validParams);

        // Verify authy is called once, userService is called, mock user is updated with
        // new password, and HTTP response is correct
        verify(mockAuthyClient).checkAuthentication(TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_AUTHENTICATION_CODE);
        verify(mockUserFromDatabase).setPassword(
                PasswordUtil.encryptString("newPassword", TestConstants.VALID_SALT));
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

    /**
     * Tests POST /users/forgot-password/{phoneNumber}
     * with incorrect authentication parameters (phone & authy code pair is incorrect)
     * @throws Exception
     */
    @Test
    public void incorrectAuthCodeShouldNotUpdateForgottenPassword() throws Exception {
        PhoneNumberAuthenticationParams params = new PhoneNumberAuthenticationParams(TestConstants.VALID_PHONE_NUMBER, "invalid code", "newPassword");
        Verification mockVerification = mock(Verification.class);
        when(mockAuthyClient.checkAuthentication(TestConstants.VALID_PHONE_NUMBER, "invalid code")).thenReturn(mockVerification);
        when(mockVerification.isOk()).thenReturn(false);

        // Expected
        JsonObject expectedBody = new JsonObject();
        expectedBody.addProperty("status", "error");
        expectedBody.addProperty("message", "Phone/authentication code pair is incorrect");
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(gson.toJson(expectedBody));

        // Actual
        ResponseEntity actual = userController.verifyCodeForForgotPassword(params);

        // Verify authy called, user service not touched, HTTP response is correct
        verify(mockAuthyClient).checkAuthentication(TestConstants.VALID_PHONE_NUMBER, "invalid code");
        verifyZeroInteractions(mockUserService);
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests POST /users/forgot-password/{phoneNumber}
     * with empty newPassword field
     * @throws Exception
     */
    @Test
    public void emptyNewPasswordShouldNotUpdateForgottenPassword() throws Exception {
        PhoneNumberAuthenticationParams params = new PhoneNumberAuthenticationParams(
                TestConstants.VALID_PHONE_NUMBER,
                TestConstants.VALID_AUTHENTICATION_CODE,
                "" // This is invalid
        );

        // Expected
        ResponseEntity expected = ResponseUtil.badRequest("New password cannot be empty");

        // Actual
        ResponseEntity actual = userController.verifyCodeForForgotPassword(params);

        // Verify authy not called, user service not touched, HTTP response is correct
        verifyZeroInteractions(mockAuthyClient);
        verifyZeroInteractions(mockUserService);
        assertEquals("HTTP status code should be 400 Bad Request",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests PUT /users/reset-password
     * with API key that isn't associated with any user
     * @throws Exception
     */
    @Test
    public void incorrectApiKeyShouldNotResetPassword() throws Exception {
        ResetPasswordParams params = new ResetPasswordParams("invalid API key", TestConstants.VALID_PASSWORD, "newPassword");
        when(mockUserService.getUserByApiKey("invalid API key")).thenReturn(null);
        when(mockUserService.getUserByApiKey("invalid API key")).thenReturn(null);

        // Expected
        ResponseEntity expected = ResponseUtil.unauthorized("Invalid authentication credentials");

        // Actual
        ResponseEntity actual = userController.resetPassword(params);

        // Verify authy not called, user service called correctly, HTTP response is correct
        verifyZeroInteractions(mockAuthyClient);
        verify(mockUserService).getUserByApiKey("invalid API key");
        verify(mockUserService, never()).updateUser(any(User.class));
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests PUT /users/reset-password
     * with incorrect oldPassword field
     * @throws Exception
     */
    @Test
    public void incorrectOldPasswordShouldNotResetPassword() throws Exception {
        ResetPasswordParams params = new ResetPasswordParams(TestConstants.VALID_API_KEY, "differentPassword", "newPassword");
        User mockUserFromDatabase = mock(User.class);
        when(mockUserService.getUserByApiKey(TestConstants.VALID_API_KEY)).thenReturn(mockUserFromDatabase);

        when(mockUserFromDatabase.getUserId()).thenReturn(TestConstants.VALID_USER_ID);
        when(mockUserFromDatabase.getFirstName()).thenReturn(TestConstants.VALID_FIRST_NAME);
        when(mockUserFromDatabase.getPhoneNumber()).thenReturn(TestConstants.VALID_PHONE_NUMBER);
        when(mockUserFromDatabase.getApiKey()).thenReturn(TestConstants.VALID_API_KEY);
        when(mockUserFromDatabase.getPassword()).thenReturn(PasswordUtil.encryptString(TestConstants.VALID_PASSWORD, TestConstants.VALID_SALT));
        when(mockUserFromDatabase.getSalt()).thenReturn(TestConstants.VALID_SALT);
        when(mockUserFromDatabase.getCreated()).thenReturn(TestConstants.VALID_DATE);
        when(mockUserFromDatabase.isAdmin()).thenReturn(false);
        when(mockUserFromDatabase.isBanned()).thenReturn(false);

        // Expected
        JsonObject expectedBody = new JsonObject();
        expectedBody.addProperty("status", "error");
        expectedBody.addProperty("message", "Invalid authentication credentials");
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(gson.toJson(expectedBody));

        // Actual
        ResponseEntity actual = userController.resetPassword(params);

        // Verify authy not called, user service called correctly, HTTP response is correct
        verifyZeroInteractions(mockAuthyClient);
        verify(mockUserService).getUserByApiKey(TestConstants.VALID_API_KEY);
        verify(mockUserService, never()).updateUser(any(User.class));
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests PUT /users/reset-password
     * with empty newPassword field
     * @throws Exception
     */
    @Test
    public void emptyNewPasswordShouldNotResetPassword() throws Exception {
        ResetPasswordParams params = new ResetPasswordParams(TestConstants.VALID_API_KEY, TestConstants.VALID_PASSWORD, "");

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                gson.toJson(new ResponseMessage("error", "New password cannot be empty")));

        // Actual
        ResponseEntity actual = userController.resetPassword(params);

        // Verify authy and user service untouched, HTTP response is correct
        verifyZeroInteractions(mockAuthyClient, mockUserService);
        assertEquals("HTTP status code should be 400 Bad Request",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests PUT /users/reset-password
     * with correct parameters
     * @throws Exception
     */
    @Test
    public void validAuthAndNewPasswordShouldResetPassword() throws Exception {
        // Request parameters
        ResetPasswordParams params = new ResetPasswordParams(TestConstants.VALID_API_KEY, TestConstants.VALID_PASSWORD, "newPassword");
        // Mock user to be updated
        User mockUser = mock(User.class);
        when(mockUser.getUserId()).thenReturn(TestConstants.VALID_USER_ID);
        when(mockUser.getFirstName()).thenReturn(TestConstants.VALID_FIRST_NAME);
        when(mockUser.getPhoneNumber()).thenReturn(TestConstants.VALID_PHONE_NUMBER);
        when(mockUser.getApiKey()).thenReturn(TestConstants.VALID_API_KEY);
        when(mockUser.getPassword()).thenReturn(PasswordUtil.encryptString(TestConstants.VALID_PASSWORD, TestConstants.VALID_SALT));
        when(mockUser.getSalt()).thenReturn(TestConstants.VALID_SALT);
        when(mockUser.getCreated()).thenReturn(TestConstants.VALID_DATE);
        when(mockUser.isAdmin()).thenReturn(false);
        when(mockUser.isBanned()).thenReturn(false);
        // Mock user service behavior
        when(mockUserService.getUserByApiKey(TestConstants.VALID_API_KEY)).thenReturn(mockUser);

        // Expected
        ResponseEntity expected = ResponseUtil.ok("Successfully updated password");

        // Actual
        ResponseEntity actual = userController.resetPassword(params);

        // Verify authy not touched, user service correctly called, and HTTP response is correct
        verifyZeroInteractions(mockAuthyClient);
        //verify(mockUserService).updateUser(mockUser);
        //verify(mockUser).setPassword(PasswordUtil.encryptString("newPassword", TestConstants.VALID_SALT));
        verify(mockUser, never()).setPhoneNumber(anyString());
        verify(mockUser, never()).setBanned(anyBoolean());
        verify(mockUser, never()).setSalt(anyString());
        verify(mockUser, never()).setAdmin(anyBoolean());
        verify(mockUser, never()).setCreated(any(Date.class));
        verify(mockUser, never()).setApiKey(anyString());
        verify(mockUser, never()).setUserId(anyString());
        verify(mockUser, never()).setFirstName(anyString());
        assertEquals("HTTP status code should be 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests DELETE /users/{userId} method
     * with invalid user ID
     * @throws Exception
     */
    @Test
    public void unknownUserIdShouldDeleteUser() throws Exception{
        // Set up mocks and parameters
        ApiKeyParams params = new ApiKeyParams(TestConstants.VALID_API_KEY);
        when(mockUserService.getUserById("invalid user ID")).thenReturn(null);

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "User ID/API key pair incorrect"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.deleteUser("invalid user ID", params);

        // Verify mock user service called, HTTP response is correct
        verify(mockUserService).getUserById("invalid user ID");
        assertEquals("HTTP response should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests DELETE /users/{userId} method
     * with invalid API key
     * @throws Exception
     */
    @Test
    public void invalidApiKeyShouldNotDeleteUser() throws Exception {
        ApiKeyParams params = new ApiKeyParams("invalid API key");
        User validUser = new User(TestConstants.VALID_FIRST_NAME, TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_API_KEY,
                TestConstants.VALID_PASSWORD, TestConstants.VALID_SALT, TestConstants.VALID_DATE, TestConstants.VALID_ADMIN, TestConstants.VALID_BANNED, TestConstants.VALID_ENABLED_LOCATION);
        when(mockUserService.getUserById(TestConstants.VALID_USER_ID)).thenReturn(validUser);

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "User ID/API key pair incorrect"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.deleteUser(TestConstants.VALID_USER_ID, params);

        // Verify user service called and HTTP response is correct
        verify(mockUserService).getUserById(TestConstants.VALID_USER_ID);
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests DELETE /users/{userId} method
     * with empty API key
     * @throws Exception
     */
    @Test
    public void emptyApiKeyShouldNotDeleteUser() throws Exception {
        ApiKeyParams params = new ApiKeyParams("");

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "User ID/API key pair incorrect"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.deleteUser(TestConstants.VALID_USER_ID, params);

        // Verify user service not called, HTTP response correct
        verifyZeroInteractions(mockUserService);
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests DELETE /users/{userId} method
     * with valid parameters
     * @throws Exception
     */
    @Test
    public void validParametersShouldDeleteUser() throws Exception {
        ApiKeyParams params = new ApiKeyParams(TestConstants.VALID_API_KEY);
        User validUser = new User(TestConstants.VALID_FIRST_NAME, TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_API_KEY,
                TestConstants.VALID_PASSWORD, TestConstants.VALID_SALT, TestConstants.VALID_DATE, TestConstants.VALID_ADMIN, TestConstants.VALID_BANNED, TestConstants.VALID_ENABLED_LOCATION);
        when(mockUserService.getUserById(TestConstants.VALID_USER_ID)).thenReturn(validUser);

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.OK).body(ResponseUtil.asJsonString(
                new ResponseMessage("ok", "Successfully deleted user"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.deleteUser(TestConstants.VALID_USER_ID, params);

        // Verify user service called, HTTP response is correct
        verify(mockUserService).getUserById(TestConstants.VALID_USER_ID);
        verify(mockUserService).deleteUser(TestConstants.VALID_USER_ID);
        verifyNoMoreInteractions(mockUserService);
        assertEquals("HTTP status code should be 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /users/{userId}/enabledLocation
     * with invalid user ID (userId for user that doesn't exist)
     * @throws Exception
     */
    @Test
    public void invalidUserIdShouldNotReturnEnabledLocation() throws Exception {
        when(mockUserService.getUserById("invalidUserId")).thenReturn(null);

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "User not found"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.getEnabledLocation("invalidUserId", TestConstants.VALID_API_KEY);

        // Verify user service called, HTTP response correct
        verify(mockUserService).getUserById("invalidUserId");
        assertEquals("HTTP status code should be 404 Not Found",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /users/{userId}/enabledLocation
     * with invalid API key parameter, valid user ID
     * @throws Exception
     */
    @Test
    public void invalidApiKeyShouldNotReturnEnabledLocation() throws Exception {
        User user = new User(TestConstants.VALID_FIRST_NAME, TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_API_KEY,
                TestConstants.VALID_PASSWORD, TestConstants.VALID_SALT, TestConstants.VALID_DATE, TestConstants.VALID_ADMIN, TestConstants.VALID_BANNED, TestConstants.VALID_ENABLED_LOCATION);
        when(mockUserService.getUserById(TestConstants.VALID_USER_ID)).thenReturn(user);

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "User ID/API key pair incorrect"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.getEnabledLocation(TestConstants.VALID_USER_ID, "invalid API key");

        // Verify user service called, HTTP response correct
        verify(mockUserService).getUserById(TestConstants.VALID_USER_ID);
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /users/{userId}/enabledLocation
     * with empty API key parameter
     * @throws Exception
     */
    @Test
    public void emptyApiKeyShouldNotReturnEnabledLocation() throws Exception {
        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "API key cannot be empty"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.getEnabledLocation(TestConstants.VALID_USER_ID, "");

        // Verify user service not called, HTTP response is correct
        verifyZeroInteractions(mockUserService);
        assertEquals("HTTP status code should be 400 Bad Request",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /users/{userId}/enabledLocation
     * with valid user ID and API key parameter
     * @throws Exception
     */
    @Test
    public void validParamsShouldReturnEnabledLocation() throws Exception {
        User user = new User(TestConstants.VALID_FIRST_NAME, TestConstants.VALID_PHONE_NUMBER, TestConstants.VALID_API_KEY, TestConstants.VALID_PASSWORD,
                TestConstants.VALID_SALT, TestConstants.VALID_DATE, TestConstants.VALID_ADMIN, TestConstants.VALID_BANNED, TestConstants.VALID_ENABLED_LOCATION);
        when(mockUserService.getUserById(TestConstants.VALID_USER_ID)).thenReturn(user);

        // Expected
        JsonObject json = new JsonObject();
        json.addProperty("enabledLocation", false);
        ResponseEntity expected = ResponseEntity.status(HttpStatus.OK).body(gson.toJson(json));

        // Actual
        ResponseEntity actual = userController.getEnabledLocation(TestConstants.VALID_USER_ID, TestConstants.VALID_API_KEY);

        // Verify user service called, HTTP response is correct
        verify(mockUserService).getUserById(TestConstants.VALID_USER_ID);
        assertEquals("HTTP status code should be 400 Bad Request",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests PUT /users/{userId}/enabledLocation
     * with invalid user ID (userId for user that doesn't exist)
     * @throws Exception
     */
    @Test
    public void invalidUserIdShouldNotUpdateEnabledLocation() throws Exception {
        // Setup
        SetEnabledLocationParams params = new SetEnabledLocationParams(true);
        when(mockUserService.getUserById("invalid user ID")).thenReturn(null);

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "User not found"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.updateEnabledLocation("invalid user ID", TestConstants.VALID_API_KEY, params);

        // Verify user service called, HTTP response correct
        verify(mockUserService).getUserById("invalid user ID");
        assertEquals("HTTP status code should be 404 Not Found",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());

    }

    /**
     * Tests PUT /users/{userId}/enabledLocation
     * with empty API key parameter
     * @throws Exception
     */
    @Test
    public void emptyApiKeyShouldNotUpdateEnabledLocation() throws Exception {
        // Setup
        SetEnabledLocationParams params = new SetEnabledLocationParams(false);

        // Expected
        ResponseEntity expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.asJsonString(
                new ResponseMessage("error", "API key cannot be empty"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.updateEnabledLocation(TestConstants.VALID_USER_ID, "", params);

        // Verify user service not called, HTTP response is correct
        verifyZeroInteractions(mockUserService);
        assertEquals("HTTP status code should be 400 Bad Request",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests PUT /users/{userId}/enabledLocation
     * with valid user ID and API key parameter
     * @throws Exception
     */
    @Test
    public void validParamsShouldUpdateEnabledLocation() throws Exception {
        // Setup
        SetEnabledLocationParams params = new SetEnabledLocationParams(true);
        User mockUser = mock(User.class);

        when(mockUser.getApiKey()).thenReturn(TestConstants.VALID_API_KEY);
        when(mockUserService.getUserById(TestConstants.VALID_USER_ID)).thenReturn(mockUser);

        // Expected
        ResponseEntity expected = ResponseEntity.ok(ResponseUtil.asJsonString(
                new ResponseMessage("ok", "Successfully updated enabledLocation"), ResponseMessage.class));

        // Actual
        ResponseEntity actual = userController.updateEnabledLocation(TestConstants.VALID_USER_ID, TestConstants.VALID_API_KEY, params);

        // Verify user service called, HTTP response is correct
        verify(mockUserService).getUserById(TestConstants.VALID_USER_ID);
        verify(mockUserService).updateUser(mockUser);
        verify(mockUser).setEnabledLocation(params.getEnabledLocation());
        assertEquals("HTTP status code should be 400 Bad Request",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }
}
