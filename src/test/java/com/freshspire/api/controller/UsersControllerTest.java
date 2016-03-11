package com.freshspire.api.controller;

import com.authy.api.Verification;
import com.freshspire.api.client.AuthyClient;
import com.freshspire.api.model.ResponseMessage;
import com.freshspire.api.model.params.NewUserParams;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for UsersController
 */
public class UsersControllerTest {

    private static final String VALID_PASSWORD = "this is a valid password 123";

    private UserService mockUserService;

    private AuthyClient mockAuthyClient;

    private UsersController usersController;

    @Before
    public void setUp() {
        // Mocks
        mockUserService = mock(UserService.class);
        mockAuthyClient = mock(AuthyClient.class);

        // Real UsersController
        usersController = new UsersController();
        usersController.setUserService(mockUserService);
        usersController.setAuthyClient(mockAuthyClient);
    }

    /**
     * Tests step 1 of the create user process (GET /users/create with phone number param).
     * Tests valid phone input.
     * @throws Exception
     */
    @Test
    public void testVerificationWithValidPhone() throws Exception {
        // Test: valid phone number supplied
        final String VALID_PHONE_NUMBER = "1234567890";
        // First create the expected ResponseEntity
        String expectedBody = ResponseUtil.asJsonString(
                new ResponseMessage("ok", "Verification code sent to " + VALID_PHONE_NUMBER),
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
    }

    /**
     * Tests step 1 of the create user process (GET /users/create with phone number param).
     * Tests empty phone input.
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

    }

    /**
     * Tests step 1 of the create user process (GET /users/create with phone number param).
     * Tests invalid phone input.
     * @throws Exception
     */
    @Test
    public void testVerificationCodeWithInvalidPhone() throws Exception {
        // Test: invalid phone number
        final String INVALID_PHONE_NUMBER = "this is an invalid phone number";
        // First, set up expected ResponseEntity
        String expectedBody = ResponseUtil.asJsonString(
                new ResponseMessage("error", "Could not send verification code. Is phone number formatted correctly?"),
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
    }

    @Test
    public void testCreateUser() throws Exception {
        NewUserParams params = new NewUserParams();
        params.setPassword(VALID_PASSWORD);

    }

    @Test
    public void testSendCodeForPasswordReset() throws Exception {

    }

    @Test
    public void testVerifyCodeForPasswordReset() throws Exception {

    }

    @Test
    public void testResetPassword() throws Exception {

    }
}
