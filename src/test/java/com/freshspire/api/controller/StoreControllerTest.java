package com.freshspire.api.controller;

import com.freshspire.api.TestConstants;
import com.freshspire.api.model.Discount;
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
        when(mockUserService.getUserByApiKey(TestConstants.INVALID_API_KEY)).thenReturn(null);

        // Expected
        JsonObject body = new JsonObject();
        body.addProperty("status", "error");
        body.addProperty("message", "Unauthenticated");
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body.toString());
        
        // Actual
        ResponseEntity actual = storeController.getStores(TestConstants.INVALID_API_KEY);

        // Verify user service called, store service not touched, HTTP response is correct
        verify(mockUserService).getUserByApiKey(TestConstants.INVALID_API_KEY);
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
        storeList.add(new Store(1, "Main Street HT", "123 Main St", "Boring", "OR", "33333", 12.3, 45.6));
        storeList.add(new Store(1, "Second Street HT", "12 Second St", "Boring", "OR", "33334", 12.4, 45.2));
        storeList.add(new Store(4, "Capital Square Food Lion", "18 Capital Square", "Raleigh", "NC", "55555", -4.567, 32.123));
        storeList.add(new Store(5, "Abbey Road Corner Store", "225 Abbey Road", "Las Vegas", "NV", "44444", 45.678, 89.87654));
        storeList.add(new Store(2, "Bay Ave Whole Foods", "007 Bay Avenue", "Miami", "FL", "123123", -35.5555, 77.88888));

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
     * with invalid API key
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

    /**
     * Tests GET /stores/{storeId}
     * with an invalid storeId (storeId doesn't exist in the persistence layer)
     * @throws Exception
     */
    @Test
    public void invalidStoreIdShouldNotReturnStore() throws Exception {
        // Set up
        User mockUser = mock(User.class);
        when(mockUserService.getUserByApiKey(TestConstants.VALID_API_KEY)).thenReturn(mockUser);
        when(mockStoreService.getStoreById(TestConstants.INVALID_STORE_ID)).thenReturn(null);

        // Expected
        JsonObject body = new JsonObject();
        body.addProperty("status", "error");
        body.addProperty("message", "Store with ID " + TestConstants.INVALID_STORE_ID + " not found");
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body.toString());

        // Actual
        ResponseEntity<String> actual = storeController.getStoreById(TestConstants.INVALID_STORE_ID, TestConstants.VALID_API_KEY);

        // Verify user service and store service called, HTTP response correct
        verify(mockUserService).getUserByApiKey(TestConstants.VALID_API_KEY);
        verify(mockStoreService).getStoreById(TestConstants.INVALID_STORE_ID);
        assertEquals("HTTP status code should be 404 Not Found",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /stores/{storeId}
     * with valid storeId and apiKey
     * @throws Exception
     */
    @Test
    public void validStoreIdShouldReturnStoreObject() throws Exception {
        // Set up
        User mockUser = mock(User.class);
        Store store = new Store(TestConstants.VALID_CHAIN_ID,
                TestConstants.VALID_DISPLAY_NAME, TestConstants.VALID_STREET, TestConstants.VALID_CITY, TestConstants.VALID_STATE,
                TestConstants.VALID_ZIP_CODE, TestConstants.VALID_LATITUDE, TestConstants.VALID_LONGITUDE);
        store.setStoreId(TestConstants.VALID_STORE_ID);
        when(mockUserService.getUserByApiKey(TestConstants.VALID_API_KEY)).thenReturn(mockUser);
        when(mockStoreService.getStoreById(TestConstants.VALID_STORE_ID)).thenReturn(store);

        // Expected
        ResponseEntity<String> expected = ResponseEntity.ok(gson.toJson(store));

        // Actual
        ResponseEntity<String> actual = storeController.getStoreById(TestConstants.VALID_STORE_ID, TestConstants.VALID_API_KEY);

        // Verify user and store service called, HTTP response is correct
        verify(mockUserService).getUserByApiKey(TestConstants.VALID_API_KEY);
        verify(mockStoreService).getStoreById(TestConstants.VALID_STORE_ID);
        assertEquals("HTTP status code should be 200 OK",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /stores/{storeId}/discounts
     * with invalid authentication
     * @throws Exception
     */
    @Test
    public void invalidAuthShouldNotReturnStoreDiscounts() throws Exception {
        // Set up
        when(mockUserService.getUserByApiKey(TestConstants.INVALID_API_KEY)).thenReturn(null);

        // Expected
        JsonObject body = new JsonObject();
        body.addProperty("status", "error");
        body.addProperty("message", "Unauthenticated");
        ResponseEntity expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body.toString());

        // Actual
        ResponseEntity actual = storeController.getDiscountsForStore(TestConstants.VALID_STORE_ID, TestConstants.INVALID_API_KEY, null, null);

        // Verify user service called, store service not touched, HTTP response is correct
        verify(mockUserService).getUserByApiKey(TestConstants.INVALID_API_KEY);
        verifyZeroInteractions(mockStoreService);
        assertEquals("HTTP status code should be 401 Unauthorized",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /stores/{storeId}/discounts
     * with invalid storeId
     * @throws Exception
     */
    @Test
    public void invalidStoreIdShouldNotReturnStoreDiscounts() throws Exception {
        // Set up
        User mockUser = mock(User.class);
        when(mockUserService.getUserByApiKey(TestConstants.VALID_API_KEY)).thenReturn(mockUser);
        when(mockStoreService.getStoreById(TestConstants.INVALID_STORE_ID)).thenReturn(null);

        // Expected
        JsonObject body = new JsonObject();
        body.addProperty("status", "error");
        body.addProperty("message", "Store with ID " + TestConstants.INVALID_STORE_ID + " not found");
        ResponseEntity<String> expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body.toString());

        // Actual
        ResponseEntity<String> actual = storeController.getDiscountsForStore(TestConstants.INVALID_STORE_ID, TestConstants.VALID_API_KEY, null, null);

        // Verify user service and store service called, HTTP response correct
        verify(mockUserService).getUserByApiKey(TestConstants.VALID_API_KEY);
        verify(mockStoreService).getStoreById(TestConstants.INVALID_STORE_ID);
        assertEquals("HTTP status code should be 404 Not Found",
                expected.getStatusCode(), actual.getStatusCode());
        assertEquals("Response body is incorrect",
                expected.getBody(), actual.getBody());
    }

    /**
     * Tests GET /stores/{storeId}/discounts
     * with default parameters
     * @throws Exception
     */
    @Test
    public void defaultParamsShouldReturnAllStoreDiscounts() throws Exception {
        // Set up
        User mockUser = mock(User.class);
        List<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount(1, 1, 1456932600, 1457191800));
        when(mockUserService.getUserByApiKey(TestConstants.VALID_API_KEY)).thenReturn(mockUser);
        when(mockStoreService.getDiscounts(1)).thenReturn(discounts);


    }

    /**
     * Tests GET /stores/{storeId}/discounts
     * with foodType=meat
     * @throws Exception
     */
    @Test
    public void meatFoodTypeShouldReturnMeatDiscounts() throws Exception {
        fail("This test not implemented");
    }

    /**
     * Tests GET /stores/{storeId}/discounts
     * with foodType=meat&foodType=dairy
     * @throws Exception
     */
    @Test
    public void meatAndDairyFoodTypesShouldReturnMeatAndDairyDiscounts() throws Exception {
        fail("This test not implemented");
    }

    /**
     * Tests GET /stores/{storeId}/discounts
     * with query of "avocado"
     * @throws Exception
     */
    @Test
    public void avocadoQueryShouldReturnAvocadoDiscounts() throws Exception {
        fail("This test not implemented");
    }

    /**
     * Tests GET /stores/{storeId}/discounts
     * with query of "avocado" and foodType=produce
     * @throws Exception
     */
    @Test
    public void avocadoAndProduceShouldReturnAvocadoDiscounts() throws Exception {

    }
}