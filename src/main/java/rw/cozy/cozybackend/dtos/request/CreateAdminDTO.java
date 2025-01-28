package rw.cozy.cozybackend.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAdminDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String regCode;
}
