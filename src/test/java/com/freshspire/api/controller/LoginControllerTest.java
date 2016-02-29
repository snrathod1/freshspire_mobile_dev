package com.freshspire.api.controller;

import com.freshspire.api.model.ApiKeyLoginParams;
import com.freshspire.api.model.User;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * com.freshspire.api.controller
 *
 * @created 2/29/16.
 */
public class LoginControllerTest {

    private User user;

    private UserService mockUserService;

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
        mockUserService = makeDummyUserService(user);

        when(mockUserService.getUserByApiKey(user.getApiKey())).thenReturn(user);
    }

    @Test
    public void testLoginWithApiKey() {
        LoginController lc = new LoginController();
        // TODO inject UserService dependency

        String correct = ResponseUtil.asJsonString(user, User.class);
        ApiKeyLoginParams params = new ApiKeyLoginParams();
        params.setApiKey(user.getApiKey());

        // This fails at the moment since UserService dependency is not injected
        assertEquals(correct, lc.loginWithApiKey(params));
    }
}
