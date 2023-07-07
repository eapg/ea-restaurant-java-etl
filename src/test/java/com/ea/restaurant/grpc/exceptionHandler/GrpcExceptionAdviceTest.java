package com.ea.restaurant.grpc.exceptionHandler;

import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.proc.BadJWTException;
import io.grpc.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

public class GrpcExceptionAdviceTest {

    private GrpcExceptionAdvice grpcExceptionAdvice;

    @BeforeEach
    void setUp() throws Exception {
        grpcExceptionAdvice = new GrpcExceptionAdvice();
    }

    @Test
    public void whenBadJwtException_shouldReturnPermissionDeniedStatus() {

        var badJwtException = new BadJWTException("BadJwtException");
        var runtimeException = new RuntimeException();
        runtimeException.initCause(badJwtException);
        var permissionDeniedStatus = grpcExceptionAdvice.handleException(runtimeException);
        System.out.println(permissionDeniedStatus);
        Assertions.assertEquals(permissionDeniedStatus.getCode(), Status.PERMISSION_DENIED.getCode());
    }

    @Test
    public void whenBadJOSEException_shouldReturnPermissionDeniedStatus(){
        var badJOSEException = new BadJOSEException("BadJOSEException");
        var runtimeException = new RuntimeException();
        runtimeException.initCause(badJOSEException);
        var permissionDeniedStatus = grpcExceptionAdvice.handleException(runtimeException);
        Assertions.assertEquals(permissionDeniedStatus.getCode(), Status.PERMISSION_DENIED.getCode());
    }

    @Test
    public void whenParseException_shouldReturnPermissionDeniedStatus(){
        var parseException = new ParseException("BadJOSEException", 1);
        var runtimeException = new RuntimeException();
        runtimeException.initCause(parseException);
        var permissionDeniedStatus = grpcExceptionAdvice.handleException(runtimeException);
        Assertions.assertEquals(permissionDeniedStatus.getCode(), Status.PERMISSION_DENIED.getCode());
    }

    @Test
    public void whenRunTimeException_shouldReturnInternalStatusCode(){
        var otherException = new IllegalStateException();
        var runtimeException = new RuntimeException();
        runtimeException.initCause(otherException);
        var internalStatus = grpcExceptionAdvice.handleException(runtimeException);
        Assertions.assertEquals(internalStatus.getCode(), Status.INTERNAL.getCode());
    }
}
