package rw.cozy.cozybackend.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateCompanyAdminDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private UUID tenantId;
    private UUID memberId;
}
