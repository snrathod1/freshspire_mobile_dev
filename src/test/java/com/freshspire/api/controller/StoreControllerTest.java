package com.freshspire.api.controller;

import com.freshspire.api.TestConstants;
import com.freshspire.api.model.Store;
import com.freshspire.api.model.User;
import com.freshspire.api.service.DiscountService;
import com.freshspire.api.service.StoreService;
import com.freshspire.api.service.UserService;
import com.freshspire.api.utils.ResponseUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.omg.CORBA.TCKind;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StoreControllerTest {

    private StoreController storeController;

    private Gson gson = new Gson();

    @Mock private UserService mockUserService;

    @Mock private DiscountService mockDiscountService;

    @Mock private StoreService mockStoreService;

    @Before
    public void setUp() {
        storeController = new StoreController();

        storeController.setUserService(mockUserService);
        storeController.setDiscountService(mockDiscountService);
        storeController.setStoreService(mockStoreService);
    }

    /**
     * Tests GET /stores
     * with invalid authentication parameters (invalid API key)
     * @throws Exception
     */
    @Test
    public void invalidAuthShouldNotReturnAllStores() throws Exception {
        // Set up
        when(mockUserService.getUserByApiKey("invalid key")).thenReturn(null);

        // Expected
        JsonObject body = new JsonObject();
        body.addProperty("status", "error");
        body.addProperty("message", "Unauthenticated");
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body.toString());
        
        // Actual
        ResponseEntity actual = storeController.getStores("invalid key");

        // Verify user service called, store service not touched, HTTP response is correct
        verify(mockUserService).getUserByApiKey("invalid key");
        verifyZeroInteractions(mockStoreService);
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /stores
     * with valid authentication parameters
     * @throws Exception
     */
    @Test
    public void validAuthShouldReturnStoreList() throws Exception {
        // Set up
        User mockUser = mock(User.class);
        List<Store> storeList = new ArrayList<Store>(5);
        storeList.add(new Store("Main Street Harris Teeter", 12.3, 45.6, "12345"));
        storeList.add(new Store("Second Street Harris Teeter", 12.4, 45.2, "12345"));
        storeList.add(new Store("Capital Square Food Lion", -4.567, 32.123, "55555"));
        storeList.add(new Store("Abbey Road Corner Store", 45.678, 89.87654, "333666"));
        storeList.add(new Store("Bay Avenue", -35.5555, 77.88888, "02468"));

        when(mockUserService.getUserByApiKey(TestConstants.VALID_API_KEY)).thenReturn(mockUser);
        when(mockStoreService.getStores()).thenReturn(storeList);

        // Expected
        JsonArray storeJson = new JsonArray();
        JsonObject body = new JsonObject();
        storeJson.add(gson.toJsonTree(storeList.get(0)));
        storeJson.add(gson.toJsonTree(storeList.get(1)));
        storeJson.add(gson.toJsonTree(storeList.get(2)));
        storeJson.add(gson.toJsonTree(storeList.get(3)));
        storeJson.add(gson.toJsonTree(storeList.get(4)));
        body.addProperty("count", 5);
        body.add("stores", storeJson);
        ResponseEntity<String> expected = ResponseEntity.ok(body.toString());

        // Actual
        ResponseEntity actual = storeController.getStores(TestConstants.VALID_API_KEY);

        // Verify user service & store service called, HTTP response is correct
        verify(mockUserService).getUserByApiKey(TestConstants.VALID_API_KEY);
        verify(mockStoreService).getStores();
        verifyZeroInteractions(mockDiscountService);
        assertEquals("HTTP status code should be 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /stores
     * with no stores in persistence layer
     * @throws Exception
     */
    @Test
    public void noStoresInServiceLayerShouldReturnEmptyStoreList() throws Exception {
        // Set up
        User mockUser = mock(User.class);
        List<Store> storeList = new ArrayList<Store>(0);

        when(mockUserService.getUserByApiKey(TestConstants.VALID_API_KEY)).thenReturn(mockUser);
        when(mockStoreService.getStores()).thenReturn(storeList);

        // Expected
        JsonArray storeJson = new JsonArray();
        JsonObject body = new JsonObject();
        body.addProperty("count", 0);
        body.add("stores", storeJson);
        ResponseEntity<String> expected = ResponseEntity.ok(body.toString());

        // Actual
        ResponseEntity actual = storeController.getStores(TestConstants.VALID_API_KEY);

        // Verify user service & store service called, HTTP response is correct
        verify(mockUserService).getUserByApiKey(TestConstants.VALID_API_KEY);
        verify(mockStoreService).getStores();
        verifyZeroInteractions(mockDiscountService);
        assertEquals("HTTP status code should be 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /stores/{storeId}
     * @throws Exception
     */
    @Test
    public void invalidAuthShouldNotReturnStore() throws Exception {
        // Set up
        Store mockStore = mock(Store.class);
        when(mockUserService.getUserByApiKey(TestConstants.VALID_API_KEY)).thenReturn(null);
        when(mockStoreService.getStoreById(TestConstants.VALID_STORE_ID)).thenReturn(mockStore);

        // Expected
        JsonObject body = new JsonObject();
        body.addProperty("status", "error");
        body.addProperty("message", "Unauthenticated");
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body.toString());

        // Actual
        ResponseEntity<String> actual = storeController.getStoreById(TestConstants.VALID_STORE_ID, TestConstants.VALID_API_KEY);

        // Verify user service & store service correctly called, HTTP response is correct
        verify(mockUserService).getUserByApiKey(TestConstants.VALID_API_KEY);
        verifyZeroInteractions(mockStoreService);
        verifyZeroInteractions(mockDiscountService);
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }
}