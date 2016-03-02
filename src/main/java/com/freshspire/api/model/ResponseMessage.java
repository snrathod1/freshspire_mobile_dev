package com.freshspire.api.model;

/**
 * com.freshspire.api.model
 *
 * @created 3/2/16.
 */
public class ResponseMessage {

    private String status;

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
