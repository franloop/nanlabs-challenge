package com.nanlab.challenge.spacex.taskmanagementapi.exception;

import org.apache.http.client.HttpResponseException;

public class RemoteServiceException extends HttpResponseException {
    public RemoteServiceException(int statusCode, String reasonPhrase) {
        super(statusCode, reasonPhrase);
    }
}
