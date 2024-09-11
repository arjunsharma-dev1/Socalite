package com.socalite.scoalite.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class RegisterUserResponseDTO {
    String message = "";
    long userId = -1;
}
