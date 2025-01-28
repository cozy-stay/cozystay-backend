package rw.cozy.cozybackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rw.cozy.cozybackend.dtos.response.ErrorResponse;
import rw.cozy.cozybackend.dtos.response.Response;
import rw.cozy.cozybackend.enums.ResponseType;

import java.util.ArrayList;
import java.util.List;

public class TokenException extends Exception{
    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public TokenException(String message){
        super(message);
    }

    public ResponseEntity<Response> getResponseEntity() {
        List<String> details = new ArrayList<>();
        details.add(super.getMessage());
        ErrorResponse errorResponse = new ErrorResponse().setMessage("You do not have authority to access this resources").setDetails(details);
        Response<ErrorResponse> response = new Response<>();
        response.setPayload(errorResponse);
        response.setType(ResponseType.UNAUTHORIZED);
        return new ResponseEntity<Response>(response , httpStatus);
    }
}
