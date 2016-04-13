package com.freshspire.api.service;

import com.freshspire.api.model.User;

public interface UserService {

    /**
     * Adds a user to the persistence layer.
     * @param user The user to add.
     */
    void addUser(User user);

    /**
     * Updates an existing user on the persistence layer.
     * @param user The user to update
     */
    void updateUser(User user);

    /**
     * Returns the user with ID 'userId', or null if the user
     * doesn't exist with that ID.
     * @param userId The ID of the user
     * @return The User object associated with the given ID, or null if no user for given ID
     */
    User getUserById(int userId);

    /**
     * Deletes a user with given user ID.
     * @param userId
     */
    void deleteUser(int userId);

    /**
     * Returns whether or not the given API key matches the API key of the user with
     * the given user ID. Returns false if no user exists for given userId.
     *
     * @param userId The ID of the user
     * @param apiKey The API key to check
     * @return True if the API key matches the API key of the given user, or false if
     * no user exists for the given user ID
     */
    boolean authenticateUser(int userId, String apiKey);

    /**
     * Returns the user with given phone number, or null if the user doesn't
     * exist with that phone number.
     *
     * @param phoneNumber The phone number of the user
     * @return The User object associated with the given phone number,
     * or null if no user for given phone number
     */
    User getUserByPhoneNumber(String phoneNumber);

    /**
     * Returns the user with given API key, or null if the user doesn't
     * exist with that phone number.
     *
     * @param apiKey The API key of the user
     * @return The User object associated with the given API key,
     * or null if no user exists for the given API key.
     */
    User getUserByApiKey(String apiKey); // TODO API key isn't necessarily unique. How do we handle that?
}
