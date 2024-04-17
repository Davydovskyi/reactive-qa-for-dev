package edu.jcourse.qa.rest.handler;

import edu.jcourse.qa.exception.ApiException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.*;

@Component
public class AppErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        Throwable error = getError(request);

        List<Map<String, Object>> errorList = new ArrayList<>();
        HttpStatus status = error instanceof ApiException apiException ?
                apiException.getStatus() :
                HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = error.getMessage();
        if (errorMessage == null) {
            errorMessage = error.getClass().getName();
        }

        errorList.add(createErrorMap(status, errorMessage));

        errorAttributes.put("status", status.value());
        errorAttributes.put("errors", Collections.singletonMap("errors", errorList));

        return errorAttributes;
    }

    private Map<String, Object> createErrorMap(HttpStatus status, String message) {
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("status", status.value());
        errorMap.put("message", message);
        return errorMap;
    }
}