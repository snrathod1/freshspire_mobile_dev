package com.freshspire.api.controller;

import com.freshspire.api.model.ResponseMessage;
import com.freshspire.api.model.User;
import com.freshspire.api.model.params.ApiKeyLoginParams;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * com.freshspire.api.controller
 *
 * @created 2/29/16.
 */
public class KeyLoginControllerTest {

    private User user;

    private UserService mockUserService;

    private KeyLoginController keyLoginController;

    private Gson gson = new Gson();

    private User makeDummyUser() {
        User user = new User();
        user.setUserId("1");
        user.setPhoneNumber("1234567890");
        user.setCreated(new Date(0));
        user.setFirstName("Name");
        user.setSalt("sJ6/Kiw3qs3Z5rkc9Sns4w==");
        user.setPassword("RWOYO5KaKB02QMKjLGABxK8vWQzPN7wwAX+KGYgkdSA=");
        user.setAdmin(false);
        user.setApiKey("6CKyrO2AIVxFBFOLX4At3g==");
        user.setBanned(false);
        user.setRestricted(false);

        return user;
    }

    private UserService makeDummyUserService(User user) {
        UserService userService = mock(UserService.class);

        when(userService.getUserByApiKey(user.getApiKey())).thenReturn(user);

        return userService;
    }

    @Before
    public void setUp() {
        user = makeDummyUser();
        mockUserService = mock(UserService.class);
        keyLoginController = new KeyLoginController();
    }

    @Test
    public void testLoginWithApiKey() {
        // Setup
        when(mockUserService.getUserByApiKey(user.getApiKey())).thenReturn(user);
        keyLoginController.setUserService(mockUserService);
        ResponseEntity<String> expectedUser = ResponseEntity.ok().body(gson.toJson(user, user.getClass()));
        ApiKeyLoginParams params = new ApiKeyLoginParams();
        params.setApiKey(user.getApiKey());

        // Exercise
        ResponseEntity<String> actualUser = keyLoginController.loginWithApiKey(params);

        // Verify
        assertEquals(expectedUser, actualUser);
    }
}
