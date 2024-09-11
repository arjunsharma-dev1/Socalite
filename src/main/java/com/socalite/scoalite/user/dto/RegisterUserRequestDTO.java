package com.socalite.scoalite.user.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserRequestDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private String username;
    private String emailAddress;
    private String phoneNumber;
}
