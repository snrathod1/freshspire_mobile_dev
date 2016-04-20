package com.freshspire.api.model.response;

/**
 * A Response Message is an object with a status field and message field, used as a response
 * to some API calls. Endpoints will convert instances of this object to json format and put it
 * in the response body.
 *
 * @created 3/2/16.
 */
public class ResponseMessage {

    /** The status of the response ("ok" or "error") */
    private String status;

    /** The message included in the response (such as "Successfully created user") */
    private String message;

    public ResponseMessage(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
