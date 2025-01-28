package rw.cozy.cozybackend.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiSecondResponse {
    private Boolean success;
    private String message;
    private Object data;

    private List<?> dataArray;

    public ApiSecondResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiSecondResponse(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public ApiSecondResponse(boolean success, List<?> data, String message) {
        this.success = success;
        this.dataArray = data;
    }
    public ApiSecondResponse(boolean success, String message, Object object ){
        this.success = success;
        this.message = message;
        this.data = object;
    }



    public static ApiSecondResponse success(Object data) {
        return new ApiSecondResponse(true, data);
    }
    public static ApiSecondResponse success(List<?> data, String message) {
        return new ApiSecondResponse(true, data, message);
    }

    public static ApiSecondResponse success(String message, Object object) {
        return new ApiSecondResponse(true, message, object);
    }

    public static ApiSecondResponse fail(Object data) {
        return new ApiSecondResponse(false, data);
    }

    public static ApiSecondResponse success(String message) {
        return new ApiSecondResponse(true, message);
    }

    public static ApiSecondResponse fail(String message) {
        return new ApiSecondResponse(false, message);
    }
}