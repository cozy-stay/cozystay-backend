package rw.cozy.cozybackend.utils;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rw.cozy.cozybackend.exceptions.*;
import rw.cozy.cozybackend.payload.ApiResponse;


public class ExceptionUtils {
    public static  <T> ResponseEntity<ApiResponse> handleControllerExceptions(Exception e) {
        System.out.println("Exception caught in controller:");

        if (e instanceof NotFoundException) {
            return new ResponseEntity<>(new ApiResponse(
                    false,
                    e.getMessage()
            ), HttpStatus.NOT_FOUND);
        } else if(e instanceof InvalidUUIDException){
            return new ResponseEntity<>(new ApiResponse(
                    false,
                    e.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }else if(e instanceof ResourceNotFoundException){
            return new ResponseEntity<>(new ApiResponse(
                    false,
                    e.getMessage()
            ), HttpStatus.NOT_FOUND);
        } else if (e instanceof IllegalArgumentException){
            return new ResponseEntity<>(new ApiResponse(
                    false,
                    e.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }else if (e instanceof InternalServerErrorException) {
            return new ResponseEntity<>(new ApiResponse(
                    false,
                    e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if(e instanceof BadRequestException){
            return new ResponseEntity<>(new ApiResponse(
                    false,
                    e.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }else {
            // Handle other exceptions as needed
            return new ResponseEntity<>(new ApiResponse(
                    false,
                    e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> void handleServiceExceptions(Exception e) {
        System.out.println("Exception caught in service:");
        if (e instanceof NotFoundException) {
            throw new NotFoundException(e.getMessage());
        } else if(e instanceof IllegalArgumentException){
            throw new IllegalArgumentException(e.getMessage());
        }else if( e instanceof ResourceNotFoundException){
            throw new ResourceNotFoundException(e.getMessage());
        }else if (e instanceof InternalServerErrorException) {
            throw new InternalServerErrorException(e.getMessage());
        } else if (e instanceof BadRequestException){
            throw new BadRequestException(e.getMessage());
        }else {
            throw new RuntimeException("Failed!! Something went wrong " + e.getMessage(), e);
        }
    }

}
