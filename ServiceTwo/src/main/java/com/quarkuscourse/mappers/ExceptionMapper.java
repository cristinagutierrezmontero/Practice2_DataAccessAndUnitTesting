package com.quarkuscourse.mappers;


import com.quarkuscourse.exceptions.CustomException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

@Provider
public class ExceptionMapper  implements ResponseExceptionMapper<CustomException> {
    @Override
    public CustomException toThrowable(Response response) {
        return new CustomException(response.getStatus(), response.getStatusInfo().getReasonPhrase());
    }

}