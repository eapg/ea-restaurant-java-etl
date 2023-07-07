package com.ea.restaurant.grpc.exceptionHandler;

import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.proc.BadJWTException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import java.text.ParseException;

@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler
    public Status handleException(Exception e) {
        if(e.getCause() instanceof BadJWTException exception){
            return handleBadJWTException(exception);
        }

        if(e.getCause() instanceof BadJOSEException exception){
            return handleBadJOSEException(exception);
        }

        if(e.getCause() instanceof ParseException exception){
            return handleParseException(exception);
        }

        return internalServerErrorStatus(e);
    }

    @GrpcExceptionHandler
    public Status handleBadJOSEException(BadJOSEException e) {

        return permisionDeniedStatus(e);
    }

    @GrpcExceptionHandler
    public Status handleBadJWTException(BadJWTException e) {
        return permisionDeniedStatus(e);
    }

    @GrpcExceptionHandler
    public Status handleParseException(ParseException e) {
        return permisionDeniedStatus(e);
    }

    private Status internalServerErrorStatus(Exception e){
        return Status.INTERNAL.withDescription(e.getMessage()).withCause(e.getCause());
    }

    private Status permisionDeniedStatus(Exception e){
        return Status.PERMISSION_DENIED.withDescription(e.getMessage()).withCause(e.getCause());
    }


}
