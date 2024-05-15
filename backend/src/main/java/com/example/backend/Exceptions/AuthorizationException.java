package com.example.backend.Exceptions;

import java.util.List;

public class AuthorizationException extends ErrorListException {

    /**
     * Creates an Object of type {@link ErrorListException}.
     *
     * @param messageSummary a summary of the problem
     * @param errors         a list of detail problem descriptions
     */
    public AuthorizationException(String messageSummary, List<String> errors) {
        super(messageSummary, errors);
    }
}
