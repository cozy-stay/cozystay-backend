package rw.cozy.cozybackend.dtos.response;

import lombok.Data;
import rw.cozy.cozybackend.enums.EMemberStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GetGroupMemberProfile {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String identificationCard;

    // work info
    private String staffId;
    private String department;
    private String division;
    private LocalDate joiningDate;

    // financial info
    private String bankName;
    private String bankCode;
    private String accountNumber;
    private String accountName;
    private double salary;
    private double currentLoanBalance;
    private LocalDateTime registrationDate;
    private EMemberStatus status;
    private double netSalary;
    private UUID groupId;
    private UUID userId;
}
