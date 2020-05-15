package edu.upc.mishuserver.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private String matchingPassword;
    private String role;
}
