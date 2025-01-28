package rw.cozy.cozybackend.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.cozy.cozybackend.model.Privileges;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CreateRoleDTO {
    private String roleName;
    private Privileges privileges;
}
