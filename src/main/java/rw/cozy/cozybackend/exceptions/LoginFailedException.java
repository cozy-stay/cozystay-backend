package rw.cozy.cozybackend.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rw.cozy.cozybackend.dtos.response.ErrorResponse;
import rw.cozy.cozybackend.dtos.response.Response;
import rw.cozy.cozybackend.enums.ResponseType;


import java.util.ArrayList;
import java.util.List;

public class LoginFailedException extends Exception{
    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public LoginFailedException(String message){
        super(message);
    }

    ResponseEntity<Response> getResponseEntity(){
        List<String> details = new ArrayList<>();
        details.add(super.getMessage());
        ErrorResponse errorResponse = new ErrorResponse().setMessage("Login Failed").setDetails(details);
        Response<ErrorResponse> response = new Response<>();
        response.setType(ResponseType.LOGIN_FAILED);
        response.setPayload(errorResponse);
        return new ResponseEntity<Response>(response , HttpStatus.UNAUTHORIZED);
    }
}
