package rw.cozy.cozybackend.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UpdateUserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private UUID roleId;
}
