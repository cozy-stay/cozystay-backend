package rw.cozy.cozybackend.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import rw.cozy.cozybackend.enums.ResponseType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Response<T> {
    private ResponseType type;
    private T payload;
}
