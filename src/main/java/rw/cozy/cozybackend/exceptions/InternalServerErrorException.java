package rw.cozy.cozybackend.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rw.cozy.cozybackend.dtos.response.ErrorResponse;
import rw.cozy.cozybackend.dtos.response.Response;
import rw.cozy.cozybackend.enums.ResponseType;

import java.util.ArrayList;
import java.util.List;

public class InternalServerErrorException  extends RuntimeException{
    public InternalServerErrorException(String message){
        super(message);
    }

    public ResponseEntity<?> getResponse(){
        List<String> details = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse().setMessage(getMessage()).setDetails(details);
        Response<ErrorResponse> response = new Response<>();
        response.setType(ResponseType.LOGIN_FAILED);
        response.setPayload(errorResponse);
        return new ResponseEntity<Response>(response , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}